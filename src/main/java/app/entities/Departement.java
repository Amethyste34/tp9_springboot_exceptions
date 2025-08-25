package app.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

/**
 * Représente un département français.
 * <p>
 * Chaque département possède un identifiant unique auto-généré,
 * un code unique (ex: "01", "75") et un nom.
 * Un département peut posséder plusieurs villes.
 * </p>
 */
@Entity
public class Departement {

    /** Identifiant auto-généré du département */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Code unique du département (ex: "01") */
    @Column(unique = true, nullable = false)
    private String code;

    /** Nom du département */
    private String nom;

    /** Liste des villes appartenant à ce département */
    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Ville> villes;

    /** Constructeur sans paramètre requis par JPA */
    public Departement() {
    }

    /** Constructeur avec paramètres */
    public Departement(String code, String nom) {
        this.code = code;
        this.nom = nom;
    }

    // ===== Getters / Setters =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Ville> getVilles() {
        return villes;
    }

    public void setVilles(List<Ville> villes) {
        this.villes = villes;
    }
}