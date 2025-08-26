package app.documentation;

import app.entities.Ville;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface VilleApi {

    @Operation(summary = "Retourne toutes les villes paginées")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Liste des villes au format JSON",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Ville.class))))
    })
    @GetMapping
    Page<Ville> getAllVilles(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "Retourne une ville par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ville trouvée",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ville.class))),
            @ApiResponse(responseCode = "404", description = "Ville introuvable", content = @Content())
    })
    @GetMapping("/{id}")
    Ville getVilleById(@Parameter(description = "Identifiant de la ville", example = "1") @PathVariable Long id);

    @Operation(summary = "Crée une nouvelle ville")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ville créée",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ville.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content())
    })
    @PostMapping
    Ville addVille(@RequestBody Ville ville);

    @Operation(summary = "Met à jour une ville existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ville mise à jour",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ville.class))),
            @ApiResponse(responseCode = "400", description = "Ville introuvable ou données invalides", content = @Content())
    })
    @PutMapping("/{id}")
    Ville updateVille(@PathVariable Long id, @RequestBody Ville ville);

    @Operation(summary = "Supprime une ville")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ville supprimée"),
            @ApiResponse(responseCode = "404", description = "Ville introuvable", content = @Content())
    })
    @DeleteMapping("/{id}")
    void deleteVille(@PathVariable Long id);
}