package app.services.impl;

import app.entities.Departement;
import app.entities.Ville;
import app.exceptions.NotFoundException;
import app.services.VilleService;
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
public class VilleServiceImpl implements VilleService {

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private DepartementServiceImpl departementService;

    // ------------------- CRUD -------------------

    @Override
    public Page<Ville> getAll(int page, int size) {
        return villeRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Optional<Ville> getById(Long id) {
        return villeRepository.findById(id);
    }

    @Override
    public List<Ville> findByNomExact(String nom) throws NotFoundException {
        List<Ville> villes = villeRepository.findByNomIgnoreCase(nom);
        if (villes.isEmpty()) {
            throw new NotFoundException("Ville non trouvée avec le nom : " + nom);
        }
        return villes;
    }

    @Override
    public Ville addVille(Ville ville) throws NotFoundException {
        Departement dep = ville.getDepartement();
        if (dep == null || dep.getCode() == null) {
            throw new NotFoundException("Département manquant pour la ville.");
        }
        Departement existDep = departementService.findByCode(dep.getCode())
                .orElseThrow(() -> new NotFoundException("Département inexistant : " + dep.getCode()));
        ville.setDepartement(existDep);
        return villeRepository.save(ville);
    }

    @Override
    public Ville updateVille(Long id, Ville ville) throws NotFoundException {
        if (!villeRepository.existsById(id)) {
            throw new NotFoundException("Ville introuvable: id=" + id);
        }
        Departement dep = ville.getDepartement();
        if (dep == null || dep.getCode() == null) {
            throw new NotFoundException("Département manquant pour la ville.");
        }
        Departement existDep = departementService.findByCode(dep.getCode())
                .orElseThrow(() -> new NotFoundException("Département inexistant : " + dep.getCode()));
        ville.setDepartement(existDep);
        ville.setId(id);
        return villeRepository.save(ville);
    }

    @Override
    public void deleteVille(Long id) throws NotFoundException {
        if (!villeRepository.existsById(id)) {
            throw new NotFoundException("Impossible de supprimer : ville inexistante avec id=" + id);
        }
        villeRepository.deleteById(id);
    }

    // ------------------- Recherches spécifiques -------------------

    @Override
    public List<Ville> findByNomPrefix(String prefix) throws NotFoundException {
        List<Ville> villes = villeRepository.findByNomStartingWithIgnoreCase(prefix);
        if (villes.isEmpty()) {
            throw new NotFoundException("Aucune ville dont le nom commence par " + prefix + " n’a été trouvée");
        }
        return villes;
    }

    @Override
    public List<Ville> findByPopulationMin(int min) throws NotFoundException {
        List<Ville> villes = villeRepository.findByPopulationTotaleGreaterThanOrderByPopulationTotaleDesc(min);
        if (villes.isEmpty()) {
            throw new NotFoundException("Aucune ville n’a une population supérieure à " + min);
        }
        return villes;
    }

    @Override
    public List<Ville> findByPopulationBetween(int min, int max) throws NotFoundException {
        List<Ville> villes = villeRepository.findByPopulationTotaleBetweenOrderByPopulationTotaleDesc(min, max);
        if (villes.isEmpty()) {
            throw new NotFoundException("Aucune ville n’a une population comprise entre " + min + " et " + max);
        }
        return villes;
    }

    @Override
    public List<Ville> findByDepartementAndPopulationMin(Departement departement, int min) throws NotFoundException {
        List<Ville> villes = villeRepository.findByDepartementAndPopulationTotaleGreaterThanOrderByPopulationTotaleDesc(departement, min);
        if (villes.isEmpty()) {
            throw new NotFoundException("Aucune ville n’a une population supérieure à " + min + " dans le département " + departement.getCode());
        }
        return villes;
    }

    @Override
    public List<Ville> findByDepartementAndPopulationBetween(Departement departement, int min, int max) throws NotFoundException {
        List<Ville> villes = villeRepository.findByDepartementAndPopulationTotaleBetweenOrderByPopulationTotaleDesc(departement, min, max);
        if (villes.isEmpty()) {
            throw new NotFoundException("Aucune ville n’a une population comprise entre " + min + " et " + max +
                    " dans le département " + departement.getCode());
        }
        return villes;
    }

    @Override
    public List<Ville> findTopNByDepartement(Departement departement, int n) throws NotFoundException {
        List<Ville> villes = villeRepository.findByDepartementOrderByPopulationTotaleDesc(departement, PageRequest.of(0, n));
        if (villes.isEmpty()) {
            throw new NotFoundException("Aucune ville trouvée dans le département " + departement.getCode());
        }
        return villes;
    }
}