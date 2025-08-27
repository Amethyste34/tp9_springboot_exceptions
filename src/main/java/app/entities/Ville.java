package app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Entité représentant une ville.
 * <p>
 * Correspond aux informations d'une commune du fichier recensement.csv.
 */
@Entity
@Table(name = "ville")
public class Ville {

    /** Identifiant auto-généré de la ville */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom de la commune */
    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String code; // ✅ Code INSEE de la ville

    /** Population municipale */
    @Column(nullable = false)
    private int populationMunicipale;

    /** Population totale */
    @Column(nullable = false)
    private int populationTotale;

    /** Département auquel appartient la ville */
    @ManyToOne(optional = false)
    @JoinColumn(name = "departement_id", nullable = false)
    @JsonBackReference
    private Departement departement;

    /**
     * Constructeur vide requis par JPA et Jackson.
     */
    public Ville() { }

    // --- Getters et Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPopulationMunicipale() {
        return populationMunicipale;
    }

    public void setPopulationMunicipale(int populationMunicipale) {
        this.populationMunicipale = populationMunicipale;
    }

    public int getPopulationTotale() {
        return populationTotale;
    }

    public void setPopulationTotale(int populationTotale) {
        this.populationTotale = populationTotale;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
}