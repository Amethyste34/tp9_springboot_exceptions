package app.services;

import app.entities.Departement;

import java.util.List;
import java.util.Optional;

public interface DepartementService {
    List<Departement> getAll();

    Optional<Departement> findByCode(String code);

    Departement addDepartement(Departement departement);

    Departement updateDepartement(String code, Departement departement);

    void deleteDepartement(String code);
}