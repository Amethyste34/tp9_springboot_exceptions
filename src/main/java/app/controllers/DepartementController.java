package app.controllers;

import app.entities.Departement;
import app.entities.Ville;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import app.services.impl.DepartementServiceImpl;

import java.io.IOException;
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
    private DepartementServiceImpl departementService;

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

    /**
     * Exporte au format PDF les informations d’un département
     * ainsi que la liste de ses villes.
     * <p>
     * Le fichier généré contient :
     * <ul>
     *     <li>Le code du département</li>
     *     <li>Le nom du département</li>
     *     <li>Un tableau des villes avec leur nom et leur population</li>
     * </ul>
     * <p>
     * Le fichier PDF est renvoyé en pièce jointe dans la réponse HTTP.
     *
     * @param response la réponse HTTP dans laquelle sera écrit le fichier PDF
     * @param code     le code du département à exporter
     * @throws IOException       si une erreur d’écriture du fichier se produit
     * @throws DocumentException si une erreur survient lors de la génération du PDF
     */
    @GetMapping("/export/pdf/{code}")
    public void exportPdf(HttpServletResponse response, @PathVariable String code) throws IOException, DocumentException {
        Departement dep = departementService.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Département introuvable"));

        String filename = "departement_" + code + ".pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Titre
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph(dep.getNom(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("Code : " + dep.getCode()));
        document.add(new Paragraph("Nom : " + dep.getNom()));
        document.add(new Paragraph(" ")); // saut de ligne

        // Liste des villes
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell("Nom de la ville");
        table.addCell("Population");

        for (Ville v : dep.getVilles()) {
            table.addCell(v.getNom());
            table.addCell(String.valueOf(v.getPopulationTotale()));
        }

        document.add(table);
        document.close();
    }

}