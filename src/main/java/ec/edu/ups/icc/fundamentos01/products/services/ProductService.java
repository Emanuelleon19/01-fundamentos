package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;

public interface ProductService {
    List<ProductResponseDto> findAll();
    Object findOne(Long id); 
    ProductResponseDto create(CreateProductDto dto);
    Object update(Long id, UpdateProductDto dto);
    Object partialUpdate(Long id, PartialUpdateProductDto dto);
    void delete(Long id);
}