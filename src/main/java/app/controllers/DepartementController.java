package app.controllers;

import app.entities.Departement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import app.services.DepartementService;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des départements.
 * <p>
 * Expose des routes pour les opérations CRUD et la consultation.
 */
@RestController
@RequestMapping("/departements")
public class DepartementController {

    @Autowired
    private DepartementService departementService;

    /** Liste de tous les départements. */
    @GetMapping
    public List<Departement> getAllDepartements() {
        return departementService.getAll();
    }

    /** Récupère un département par code. */
    @GetMapping("/{code}")
    public Departement getDepartementByCode(@PathVariable String code) {
        return departementService.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Département introuvable"));
    }

    /** Crée un département. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Departement addDepartement(@RequestBody Departement departement) {
        return departementService.addDepartement(departement);
    }

    /** Met à jour un département par code. */
    @PutMapping("/{code}")
    public Departement update(@PathVariable String code, @RequestBody Departement departement) {
        try {
            return departementService.updateDepartement(code, departement);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    /** Supprime un département par code. */
    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartement(@PathVariable String code) {
        departementService.deleteDepartement(code);
    }
}