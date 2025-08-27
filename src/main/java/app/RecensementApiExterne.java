package app;

import app.dto.DepartementDto;
import app.dto.DepartementMapper;
import app.dto.VilleDto;
import app.dto.VilleMapper;
import app.entities.Departement;
import app.entities.Ville;
import app.repository.DepartementRepository;
import app.repository.VilleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

/**
 * Charge les départements et les communes depuis l'API GeoGouv au démarrage de l'application.
 * <p>
 * Cette classe implémente {@link CommandLineRunner}, ce qui permet d'exécuter le code de la méthode
 * {@link #run(String...)} automatiquement après le lancement de Spring Boot.
 * </p>
 * <p>
 * Les départements sont récupérés depuis :
 * <a href="https://geo.api.gouv.fr/departements">API Départements</a>
 * </p>
 * Les communes sont récupérées depuis :
 * <a href="https://geo.api.gouv.fr/communes?fields=nom,code,codeDepartement,population">
 * API Communes avec population</a>
 * </p>
 */
@Component
public class RecensementApiExterne implements CommandLineRunner {

    private final DepartementRepository departementRepository;
    private final VilleRepository villeRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public RecensementApiExterne(DepartementRepository departementRepository,
                                 VilleRepository villeRepository) {
        this.departementRepository = departementRepository;
        this.villeRepository = villeRepository;
    }

    @Override
    public void run(String... args) {
        loadDepartements();
        loadCommunes();
    }

    /**
     * Charge tous les départements depuis l'API et les sauvegarde en base
     * uniquement s'ils n'existent pas déjà.
     */
    private void loadDepartements() {
        String url = "https://geo.api.gouv.fr/departements";
        DepartementDto[] depDtos = restTemplate.getForObject(url, DepartementDto[].class);

        if (depDtos != null) {
            Arrays.stream(depDtos).forEach(dto -> {
                Optional<Departement> existing = departementRepository.findByCode(dto.getCode());
                if (existing.isEmpty()) {
                    Departement dep = DepartementMapper.toEntity(dto);
                    departementRepository.save(dep);
                }
            });
        }
    }

    /**
     * Charge toutes les communes depuis l'API et les sauvegarde en base.
     * La population retournée par l'API correspond à la population totale,
     * que l'on recopie également dans population municipale pour éviter d'avoir 0.
     */
    private void loadCommunes() {
        // Ajout du limit=50000 pour tout récupérer
        String url = "https://geo.api.gouv.fr/communes?fields=nom,code,codeDepartement,population&limit=50000";
        VilleDto[] villeDtos = restTemplate.getForObject(url, VilleDto[].class);

        if (villeDtos != null) {
            Arrays.stream(villeDtos).forEach(dto -> {
                Optional<Ville> existing = villeRepository.findByCode(dto.getCode());
                if (existing.isEmpty()) {
                    // Cherche le département associé
                    Optional<Departement> depOpt = departementRepository.findByCode(dto.getCodeDepartement());
                    depOpt.ifPresent(dep -> {
                        // Copie population dans les deux champs
                        dto.setPopulationTotale(dto.getPopulationTotale());
                        dto.setPopulationMunicipale(dto.getPopulationTotale());

                        Ville ville = VilleMapper.toEntity(dto, dep);
                        villeRepository.save(ville);
                    });
                }
            });
            System.out.println(villeDtos.length + " communes chargées.");
        }
    }
}