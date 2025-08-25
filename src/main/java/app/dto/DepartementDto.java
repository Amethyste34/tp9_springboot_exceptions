package app.dto;

import java.util.List;

/**
 * Data Transfer Object pour la classe Departement.
 * Sert à exposer les données via l'API sans renvoyer l'entité complète.
 */
public class DepartementDto {

    private Long id;
    private String code;
    private String nom;

    /** Constructeur vide requis par Spring et Jackson */
    public DepartementDto() {}

    /** Constructeur complet */
    public DepartementDto(Long id, String code, String nom) {
        this.id = id;
        this.code = code;
        this.nom = nom;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}