package app.controllers;

import app.documentation.VilleApi;
import app.entities.Departement;
import app.entities.Ville;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import app.services.impl.DepartementServiceImpl;
import app.services.impl.VilleServiceImpl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des villes.
 * <p>
 * Expose des routes pour les opérations CRUD, la pagination et
 * les recherches (nom, population, département).
 */
@RestController
@RequestMapping("/villes")
public class VilleController implements VilleApi {

    @Autowired
    private VilleServiceImpl villeService;

    @Autowired
    private DepartementServiceImpl departementService;

    /** Récupère toutes les villes paginées. */
    @GetMapping
    public Page<Ville> getAllVilles(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return villeService.getAll(page, size);
    }

    /** Récupère une ville par id (uniquement chiffres). */
    @GetMapping("/{id:[0-9]+}")
    public Ville getVilleById(@PathVariable Long id) {
        return villeService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ville introuvable"));
    }

    /**
     * Récupère une ville par son nom */
    @GetMapping("/nomExact/{nom}")
    public List<Ville> getVillesByNomExact(@PathVariable String nom) {
        return villeService.findByNomExact(nom);
    }

    /** Crée une ville. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ville addVille(@RequestBody Ville ville) {
        try {
            return villeService.addVille(ville);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    /** Met à jour une ville par id. */
    @PutMapping("/{id}")
    public Ville updateVille(@PathVariable Long id, @RequestBody Ville ville) {
        try {
            return villeService.updateVille(id, ville);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    /** Supprime une ville par id. */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVille(@PathVariable Long id) {
        villeService.deleteVille(id);
    }

    // ---- Recherches ----

    /** Villes dont le nom commence par un préfixe. */
    @GetMapping("/nom/{prefix}")
    public List<Ville> getVillesByNomPrefix(@PathVariable String prefix) {
        return villeService.findByNomPrefix(prefix);
    }

    /** Villes avec population > min. */
    @GetMapping("/population/min/{min}")
    public List<Ville> getVillesByPopulationMin(@PathVariable int min) {
        return villeService.findByPopulationMin(min);
    }

    /** Villes avec population entre min et max. */
    @GetMapping("/population/{min}/{max}")
    public List<Ville> getVillesByPopulationBetween(@PathVariable int min, @PathVariable int max) {
        return villeService.findByPopulationBetween(min, max);
    }

    /** Villes d'un département avec population > min. */
    @GetMapping("/departement/{code}/population/min/{min}")
    public List<Ville> getVillesByDepartementAndPopulationMin(@PathVariable String code, @PathVariable int min) {
        Departement dep = departementService.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Département introuvable"));
        return villeService.findByDepartementAndPopulationMin(dep, min);
    }

    /** Villes d'un département avec population entre min et max. */
    @GetMapping("/departement/{code}/population/{min}/{max}")
    public List<Ville> getVillesByDepartementAndPopulationBetween(@PathVariable String code,
                                                                  @PathVariable int min,
                                                                  @PathVariable int max) {
        Departement dep = departementService.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Département introuvable"));
        return villeService.findByDepartementAndPopulationBetween(dep, min, max);
    }

    /** Top N villes les plus peuplées d'un département. */
    @GetMapping("/departement/{code}/top/{n}")
    public List<Ville> getTopNVillesByDepartement(@PathVariable String code, @PathVariable int n) {
        Departement dep = departementService.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Département introuvable"));
        return villeService.findTopNByDepartement(dep, n);
    }

    /**
     * Exporte toutes les villes dont la population municipale est supérieure
     * ou égale à la valeur du paramètre {@code min}, au format CSV.
     *
     * <p>Le fichier CSV généré contient les colonnes suivantes&nbsp;:
     * <ul>
     *   <li>Nom de la ville</li>
     *   <li>Population municipale</li>
     *   <li>Code du département</li>
     *   <li>Nom du département</li>
     * </ul>
     *
     * <p>L'encodage utilisé est <b>UTF-8 avec BOM</b> afin d'assurer une
     * compatibilité optimale avec Excel (Windows) et les autres tableurs
     * (LibreOffice, Google Sheets, etc.).</p>
     *
     * @param min seuil minimum de population municipale. Seules les villes ayant
     *            une population supérieure ou égale à ce seuil sont exportées.
     * @param response objet {@link HttpServletResponse} utilisé pour écrire
     *                 le flux CSV directement dans la réponse HTTP.
     * @throws IOException si une erreur survient lors de l'écriture du flux.
     */
    @GetMapping("/export/csv/population/min/{min}")
    public void exportVillesToCsv(@PathVariable int min,
                                  HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=villes.csv");

        // Ajout du BOM pour Excel
        OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
        writer.write('\uFEFF');

        writer.write("Nom de la ville;Population;Code département;Nom département\n");

        List<Ville> villes = villeService.findByPopulationMin(min);
        for (Ville ville : villes) {
            writer.write(ville.getNom() + ";" +
                    ville.getPopulationTotale() + ";" +
                    ville.getDepartement().getCode() + ";" +
                    ville.getDepartement().getNom() + "\n");
        }

        writer.flush();
        writer.close();
    }

}