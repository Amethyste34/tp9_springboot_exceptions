package app.repository;

import app.entities.Departement;
import app.entities.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Repository Spring Data JPA pour la gestion des entités {@link Ville}.
 * <p>
 * Cette interface permet d'effectuer des recherches personnalisées
 * sur les villes stockées en base, en complément des opérations CRUD
 * fournies par {@link JpaRepository}.
 */
public interface VilleRepository extends JpaRepository<Ville, Long> {

    /**
     * Vérifie si une ville existe déjà en base avec le code donné.
     * @param code code INSEE de la ville
     * @return true si existe, false sinon
     */
    boolean existsByCode(String code);

    /**
     * Cherche une ville par son code INSEE.
     * @param code code INSEE de la ville
     * @return Ville si trouvée, sinon null
     */
    Optional<Ville> findByCode(String code);

    /**
     * Recherche d'une ville par son nom
     * @param nom nom de la ville
     * @return Liste de villes correspondantes
     */
    List<Ville> findByNomIgnoreCase(String nom);

    /**
     * Recherche toutes les villes dont le nom commence par une chaîne donnée.
     * @param prefix Le préfixe du nom de la ville (insensible à la casse).
     * @return Liste des villes correspondantes.
     */
    List<Ville> findByNomStartingWithIgnoreCase(String prefix);

    /**
     * Recherche toutes les villes dont la population est supérieure à une valeur donnée.
     * Résultat trié par population décroissante.
     * @param min Population minimale.
     * @return Liste des villes correspondantes.
     */
    List<Ville> findByPopulationTotaleGreaterThanOrderByPopulationTotaleDesc(int min);

    /**
     * Recherche toutes les villes dont la population est comprise entre deux bornes.
     * Résultat trié par population décroissante.
     * @param min Population minimale.
     * @param max Population maximale.
     * @return Liste des villes correspondantes.
     */
    List<Ville> findByPopulationTotaleBetweenOrderByPopulationTotaleDesc(int min, int max);

    /**
     * Recherche toutes les villes d’un département dont la population est supérieure à une valeur donnée.
     * Résultat trié par population décroissante.
     * @param departement departement Département concerné.
     * @param min Population minimale.
     * @return Liste des villes correspondantes.
     */
    List<Ville> findByDepartementAndPopulationTotaleGreaterThanOrderByPopulationTotaleDesc(Departement departement, int min);

    /**
     * Recherche toutes les villes d’un département dont la population est comprise entre deux bornes.
     * Résultat trié par population décroissante.
     * @param departement departement Département concerné.
     * @param min Population minimale.
     * @param max Population maximale.
     * @return Liste des villes correspondantes.
     */
    List<Ville> findByDepartementAndPopulationTotaleBetweenOrderByPopulationTotaleDesc(Departement departement, int min, int max);

    /**
     * Recherche les {@code n} villes les plus peuplées d’un département.
     * Utilise {@link Pageable} pour limiter le nombre de résultats.
     * @param departement Département concerné.
     * @param pageable pageable Objet de pagination pour limiter le nombre de résultats.
     * @return Liste des villes correspondantes.
     */
    List<Ville> findByDepartementOrderByPopulationTotaleDesc(Departement departement, Pageable pageable);

    /**
     * Variante avec une requête JPQL : permet de récupérer explicitement les n villes les plus peuplées d’un département.
     * @param codeDep Code du département.
     * @param pageable pageable Objet de pagination pour limiter le nombre de résultats.
     * @return Liste des villes correspondantes.
     */
    @Query("SELECT v FROM Ville v WHERE v.departement.code = :codeDep ORDER BY v.populationTotale DESC")
    List<Ville> findTopByDepartementCodeOrderByPopulationDesc(String codeDep, Pageable pageable);
}