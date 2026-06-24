package ec.edu.ups.icc.fundamentos01.products.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo de dominio del recurso products.
 * * Representa al producto dentro de la lógica de negocio.
 * No es una entidad de base de datos y no debe tener anotaciones JPA.
 */
public class ProductModel {

    /**
     * Identificador del producto.
     */
    private Long id;
    
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private LocalDateTime createdAt;

    // Constructor vacío
    public ProductModel() {
    }

    // Constructor lleno para transformaciones básicas
    public ProductModel(String name, String description, BigDecimal price, Integer stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    // Constructor completo
    public ProductModel(Long id, String name, String description, BigDecimal price, Integer stock, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public BigDecimal getPrice() { 
        return price; 
    }

    public void setPrice(BigDecimal price) { 
        this.price = price; 
    }

    public Integer getStock() { 
        return stock; 
    }

    public void setStock(Integer stock) { 
        this.stock = stock; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }

    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
}