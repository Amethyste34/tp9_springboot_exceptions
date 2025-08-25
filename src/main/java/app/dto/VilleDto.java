package app.dto;

/**
 * Data Transfer Object pour la classe Ville.
 * Sert à exposer les données de la ville via l'API sans renvoyer l'entité complète.
 */
public class VilleDto {

    private Long id;
    private String nom;
    private int populationMunicipale;
    private int populationTotale;
    private String codeDepartement;

    /** Constructeur vide requis par Spring et Jackson */
    public VilleDto() {}

    /** Constructeur complet */
    public VilleDto(Long id, String nom, int populationMunicipale, int populationTotale, String codeDepartement) {
        this.id = id;
        this.nom = nom;
        this.populationMunicipale = populationMunicipale;
        this.populationTotale = populationTotale;
        this.codeDepartement = codeDepartement;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getPopulationMunicipale() { return populationMunicipale; }
    public void setPopulationMunicipale(int populationMunicipale) { this.populationMunicipale = populationMunicipale; }

    public int getPopulationTotale() { return populationTotale; }
    public void setPopulationTotale(int populationTotale) { this.populationTotale = populationTotale; }

    public String getCodeDepartement() { return codeDepartement; }
    public void setCodeDepartement(String codeDepartement) { this.codeDepartement = codeDepartement; }
}
