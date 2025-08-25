package app.dto;

import app.entities.Departement;
import app.entities.Ville;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre Departement et DepartementDto.
 */
public class DepartementMapper {

    /** Convertit un Departement en DepartementDto */
    public static DepartementDto toDto(Departement departement) {
        DepartementDto dto = new DepartementDto();
        dto.setId(departement.getId());
        dto.setCode(departement.getCode());
        dto.setNom(departement.getNom());
        return dto;
    }

    /** Convertit un DepartementDto en entité Departement */
    public static Departement toEntity(DepartementDto dto) {
        if (dto == null) return null;
        Departement dep = new Departement();
        dep.setId(dto.getId());
        dep.setCode(dto.getCode());
        dep.setNom(dto.getNom());
        // Les villes sont gérées séparément via VilleMapper
        return dep;
    }
}