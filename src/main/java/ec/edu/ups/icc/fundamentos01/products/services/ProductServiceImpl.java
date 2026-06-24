package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toModel)
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Object findOne(Long id) {
        ProductEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        ProductModel model = mapper.toModel(entity);
        return mapper.toResponseDto(model);
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        ProductModel model = mapper.toModel(dto);
        ProductEntity entity = mapper.toEntity(model);
        ProductEntity savedEntity = repository.save(entity);
        return mapper.toResponseDto(mapper.toModel(savedEntity));
    }

    @Override
    public Object update(Long id, UpdateProductDto dto) {
        ProductEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());

        ProductEntity saved = repository.save(existing);
        return mapper.toResponseDto(mapper.toModel(saved));
    }

    @Override
    public Object partialUpdate(Long id, PartialUpdateProductDto dto) {
        ProductEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if (dto.getStock() != null) existing.setStock(dto.getStock());

        ProductEntity saved = repository.save(existing);
        return mapper.toResponseDto(mapper.toModel(saved));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        repository.deleteById(id);
    }
}