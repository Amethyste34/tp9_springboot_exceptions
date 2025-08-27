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
        v.setCode(dto.getCode());
        v.setNom(dto.getNom());
        v.setPopulationTotale(dto.getPopulationTotale());
        // par cohérence, populationMunicipale = populationTotale
        v.setPopulationMunicipale(dto.getPopulationTotale());
        v.setDepartement(dep);
        return v;
    }

    public static VilleDto toDto(Ville v) {
        VilleDto dto = new VilleDto();
        dto.setId(v.getId());
        dto.setNom(v.getNom());
        dto.setCode(v.getCode());
        dto.setPopulationTotale(v.getPopulationTotale());
        dto.setPopulationMunicipale(v.getPopulationMunicipale());
        dto.setCodeDepartement(v.getDepartement().getCode());
        return dto;
    }
}