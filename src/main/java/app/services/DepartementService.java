package app.services;

import app.entities.Departement;
import app.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import app.repository.DepartementRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des entités {@link Departement}.
 * <p>
 * Fournit des opérations de consultation et de CRUD
 * en s'appuyant sur {@link DepartementRepository}.
 */
@Service
@Transactional
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    /**
     * Récupère tous les départements.
     *
     * @return liste des départements.
     */
    public List<Departement> getAll() {
        return departementRepository.findAll();
    }

    /**
     * Recherche un département par son code.
     *
     * @param code code du département (ex: "75").
     * @return Optional du département.
     */
    public Optional<Departement> findByCode(String code) {
        return departementRepository.findById(code);
    }

    /**
     * Crée un nouveau département.
     *
     * @param departement entité à persister.
     * @return département persisté.
     */
    public Departement addDepartement(Departement departement) {
        return departementRepository.save(departement);
    }

    /**
     * Met à jour un département existant (par code).
     * Si le département n'existe pas, une NotFoundException est levée.
     *
     * @param code        code du département à mettre à jour.
     * @param departement données à appliquer (le code fourni fait foi).
     * @return département mis à jour.
     */
    public Departement updateDepartement(String code, Departement departement) {
        Departement existDep = departementRepository.findById(code)
                .orElseThrow(() -> new NotFoundException("Département inexistant : " + code));
        existDep.setNom(departement.getNom());
        return departementRepository.save(existDep);
    }

    /**
     * Supprime un département par son code.
     * Si le département n'existe pas, une NotFoundException est levée.
     *
     * @param code code du département.
     */
    public void deleteDepartement(String code) {
        if (!departementRepository.existsById(code)) {
            throw new NotFoundException("Impossible de supprimer : département inexistant " + code);
        }
        departementRepository.deleteById(code);
    }
}