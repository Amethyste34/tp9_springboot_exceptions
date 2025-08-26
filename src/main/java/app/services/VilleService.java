package app.services;

import app.entities.Departement;
import app.entities.Ville;
import app.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface VilleService {
    Page<Ville> getAll(int page, int size);

    Optional<Ville> getById(Long id);

    List<Ville> findByNomExact(String nom) throws NotFoundException;

    Ville addVille(Ville ville) throws NotFoundException;

    Ville updateVille(Long id, Ville ville) throws NotFoundException;

    void deleteVille(Long id) throws NotFoundException;

    List<Ville> findByNomPrefix(String prefix) throws NotFoundException;

    List<Ville> findByPopulationMin(int min) throws NotFoundException;

    List<Ville> findByPopulationBetween(int min, int max) throws NotFoundException;

    List<Ville> findByDepartementAndPopulationMin(Departement departement, int min) throws NotFoundException;

    List<Ville> findByDepartementAndPopulationBetween(Departement departement, int min, int max) throws NotFoundException;

    List<Ville> findTopNByDepartement(Departement departement, int n) throws NotFoundException;
}