package app.utils;

import app.entities.Departement;
import app.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import app.repository.DepartementRepository;
import app.repository.VilleRepository;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Composant qui charge les données initiales depuis un fichier CSV ou SQL
 * pour les villes et départements.
 */
@Component
public class CsvLoader {

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private DepartementRepository departementRepository;

    private static final String CSV_FILE = "recensement.csv";

    /**
     * Charge les 1000 villes les plus peuplées en base au démarrage
     * si la table est vide.
     */
    @PostConstruct
    public void loadCsv() {
        if (villeRepository.count() > 0) {
            return; // déjà chargé
        }

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            br.readLine(); // sauter l'entête si présent
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length < 4) continue;

                String nomVille = tokens[0].trim();
                String codeDep = tokens[1].trim();
                String nomDep = tokens[2].trim();
                int populationMunicipale = Integer.parseInt(tokens[3].trim());
                int populationTotale = Integer.parseInt(tokens[4].trim());

                // Récupère ou crée le département
                Departement dep = departementRepository.findById(codeDep)
                        .orElseGet(() -> departementRepository.save(new Departement(codeDep, nomDep)));

                // Crée la ville et persiste
                Ville ville = new Ville();
                ville.setNom(nomVille);
                ville.setPopulationMunicipale(Integer.parseInt(tokens[3].trim()));
                ville.setPopulationTotale(Integer.parseInt(tokens[4].trim()));
                ville.setDepartement(dep);

                villeRepository.save(ville);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}