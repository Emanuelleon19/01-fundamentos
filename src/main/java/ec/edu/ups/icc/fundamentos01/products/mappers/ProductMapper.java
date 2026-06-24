package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductModel toModel(CreateProductDto dto) {
        if (dto == null) return null;
        return new ProductModel(dto.getName(), dto.getDescription(), dto.getPrice(), dto.getStock());
    }

    public ProductEntity toEntity(ProductModel model) {
        if (model == null) return null;
        ProductEntity entity = new ProductEntity(model.getName(), model.getDescription(), model.getPrice(), model.getStock());
        if (model.getId() != null) {
            entity.setId(model.getId());
        }
        return entity;
    }

    public ProductModel toModel(ProductEntity entity) {
        if (entity == null) return null;
        ProductModel model = new ProductModel(entity.getName(), entity.getDescription(), entity.getPrice(), entity.getStock());
        model.setId(entity.getId());
        return model;
    }

    public ProductResponseDto toResponseDto(ProductModel model) {
        if (model == null) return null;
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setPrice(model.getPrice());
        dto.setStock(model.getStock());
        // Si tu ProductResponseDto tiene fecha de creación, la asignas aquí:
        // dto.setCreatedAt(model.getCreatedAt());
        return dto;
    }
}