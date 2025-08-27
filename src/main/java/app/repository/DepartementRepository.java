package app.repository;

import app.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository Spring Data JPA pour la gestion des entités {@link Departement}.
 * <p>
 * Cette interface fournit automatiquement les opérations CRUD de base
 * (création, lecture, mise à jour, suppression) ainsi que la possibilité
 * de définir des requêtes personnalisées si nécessaire.
 */
public interface DepartementRepository extends JpaRepository<Departement, String> {

    /**
     * Recherche d’un département par son code.
     * <p>
     * Cette méthode est automatiquement implémentée par Spring Data JPA
     * grâce à son nommage. Elle est équivalente à un
     * "SELECT d FROM Departement d WHERE d.code = :code".
     *
     * @param code le code du département.
     * @return le département correspondant, ou {@code null} si aucun trouvé.
     */
    Optional<Departement> findByCode(String code);


    boolean existsByCode(String code);

}