package app.dto;

import app.entities.Ville;
import app.entities.Departement;

/**
 * Mapper pour convertir entre Ville et VilleDto.
 */
public class VilleMapper {

    /**
     * Transforme un DTO en entité Ville.
     * @param dto DTO de la ville
     * @param dep Département associé
     * @return Ville entity
     */
    public static Ville toEntity(VilleDto dto, Departement dep) {
        Ville v = new Ville();
        v.setId(dto.getId());
        v.setNom(dto.getNom());
        v.setPopulationMunicipale(dto.getPopulationMunicipale());
        v.setPopulationTotale(dto.getPopulationTotale());
        v.setDepartement(dep);
        return v;
    }

    public static VilleDto toDto(Ville v) {
        VilleDto dto = new VilleDto();
        dto.setId(v.getId());
        dto.setNom(v.getNom());
        dto.setPopulationMunicipale(v.getPopulationMunicipale());
        dto.setPopulationTotale(v.getPopulationTotale());
        dto.setCodeDepartement(v.getDepartement().getCode());
        return dto;
    }
}