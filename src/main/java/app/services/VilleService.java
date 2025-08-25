package app.services;

import app.entities.Departement;
import app.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import app.repository.VilleRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des entités {@link Ville}.
 * <p>
 * Fournit des opérations de consultation, de recherche et de CRUD
 * en s'appuyant sur {@link VilleRepository}.
 */
@Service
@Transactional
public class VilleService {

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private DepartementService departementService;

    /**
     * Récupère toutes les villes avec pagination.
     *
     * @param page numéro de page (0-indexé).
     * @param size taille de la page.
     * @return une page de villes.
     */
    public Page<Ville> getAll(int page, int size) {
        return villeRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * Récupère une ville par son identifiant.
     *
     * @param id identifiant de la ville.
     * @return un Optional pouvant contenir la ville.
     */
    public Optional<Ville> getById(Long id) {
        return villeRepository.findById(id);
    }

    /**
     * Récupère une ville par son nom
     * @param nom nom de la ville
     * @return la liste des villes correspondantes
     */
    public List<Ville> findByNomExact(String nom) {
        return villeRepository.findByNomIgnoreCase(nom);
    }

    /**
     * Crée une nouvelle ville.
     *
     * @param ville entité à persister.
     * @return la ville persistée.
     */
    public Ville addVille(Ville ville) {
        // Vérifie que le département existe
        Departement dep = ville.getDepartement();
        if (dep == null || dep.getCode() == null) {
            throw new IllegalArgumentException("Département manquant pour la ville.");
        }
        Departement existDep = departementService.findByCode(dep.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Département inexistant : " + dep.getCode()));
        ville.setDepartement(existDep);
        return villeRepository.save(ville);
    }

    /**
     * Met à jour une ville existante (par id).
     * Si la ville n'existe pas, une IllegalArgumentException est levée.
     *
     * @param id    identifiant de la ville à mettre à jour.
     * @param ville données à appliquer (l'id fourni fait foi).
     * @return la ville mise à jour.
     */
    public Ville updateVille(Long id, Ville ville) {
        if (!villeRepository.existsById(id)) {
            throw new IllegalArgumentException("Ville introuvable: id=" + id);
        }
        // Vérifie que le département existe
        Departement dep = ville.getDepartement();
        if (dep == null || dep.getCode() == null) {
            throw new IllegalArgumentException("Département manquant pour la ville.");
        }
        Departement existDep = departementService.findByCode(dep.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Département inexistant : " + dep.getCode()));
        ville.setDepartement(existDep);
        ville.setId(id);
        return villeRepository.save(ville);
    }

    /**
     * Supprime une ville par son identifiant.
     *
     * @param id identifiant de la ville à supprimer.
     */
    public void deleteVille(Long id) {
        villeRepository.deleteById(id);
    }

    // ---- Recherches demandées par le TP 7----

    /**
     * Villes dont le nom commence par un préfixe (insensible à la casse).
     *
     * @param prefix préfixe.
     * @return liste des villes correspondantes.
     */
    public List<Ville> findByNomPrefix(String prefix) {
        return villeRepository.findByNomStartingWithIgnoreCase(prefix);
    }

    /**
     * Villes dont la population est > min (tri décroissant).
     *
     * @param min population minimale.
     * @return liste triée.
     */
    public List<Ville> findByPopulationMin(int min) {
        return villeRepository.findByPopulationTotaleGreaterThanOrderByPopulationTotaleDesc(min);
    }

    /**
     * Villes dont la population est entre min et max (tri décroissant).
     *
     * @param min borne min.
     * @param max borne max.
     * @return liste triée.
     */
    public List<Ville> findByPopulationBetween(int min, int max) {
        return villeRepository.findByPopulationTotaleBetweenOrderByPopulationTotaleDesc(min, max);
    }

    /**
     * Villes d'un département avec population > min (tri décroissant).
     *
     * @param departement département.
     * @param min         population minimale.
     * @return liste triée.
     */
    public List<Ville> findByDepartementAndPopulationMin(Departement departement, int min) {
        return villeRepository.findByDepartementAndPopulationTotaleGreaterThanOrderByPopulationTotaleDesc(departement, min);
    }

    /**
     * Villes d'un département avec population entre min et max (tri décroissant).
     *
     * @param departement département.
     * @param min         borne min.
     * @param max         borne max.
     * @return liste triée.
     */
    public List<Ville> findByDepartementAndPopulationBetween(Departement departement, int min, int max) {
        return villeRepository.findByDepartementAndPopulationTotaleBetweenOrderByPopulationTotaleDesc(departement, min, max);
    }

    /**
     * Top N villes les plus peuplées d'un département (via Pageable).
     *
     * @param departement département.
     * @param n           nombre de villes à retourner.
     * @return liste des N villes les plus peuplées.
     */
    public List<Ville> findTopNByDepartement(Departement departement, int n) {
        return villeRepository.findByDepartementOrderByPopulationTotaleDesc(departement, PageRequest.of(0, n));
    }
}