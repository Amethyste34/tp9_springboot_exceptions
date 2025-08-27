package app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object pour la classe Ville.
 * Sert à exposer les données de la ville via l'API sans renvoyer l'entité complète.
 */
public class VilleDto {

    /** Identifiant technique (utilisé en base, pas présent dans l'API) */
    private Long id;

    /** Nom de la commune */
    private String nom;

    /** Code INSEE de la commune */
    private String code;

    /** Population municipale : par défaut identique à la population totale */
    private int populationMunicipale;

    /** Population totale de la commune (provenant du champ "population" de l'API) */
    @JsonProperty("population")
    private int populationTotale;

    /** Code du département auquel appartient la commune */
    private String codeDepartement;

    /** Constructeur vide requis par Spring et Jackson */
    public VilleDto() {}

    /**
     * Constructeur complet
     * @param id identifiant
     * @param nom nom de la commune
     * @param code code INSEE
     * @param codeDepartement code du département
     * @param populationTotale population totale de la commune
     */
    public VilleDto(Long id, String nom, String code, String codeDepartement, int populationTotale) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.codeDepartement = codeDepartement;
        this.populationTotale = populationTotale;
        this.populationMunicipale = populationTotale;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() {return code; }
    public void setCode(String code) { this.code = code; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getPopulationMunicipale() { return populationMunicipale; }
    public void setPopulationMunicipale(int populationMunicipale) { this.populationMunicipale = populationMunicipale; }

    public int getPopulationTotale() { return populationTotale; }
    public void setPopulationTotale(int populationTotale) {
        this.populationTotale = populationTotale;
        this.populationMunicipale = populationTotale;
    }

    public String getCodeDepartement() { return codeDepartement; }
    public void setCodeDepartement(String codeDepartement) { this.codeDepartement = codeDepartement; }
}
