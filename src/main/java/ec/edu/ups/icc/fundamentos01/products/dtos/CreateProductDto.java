package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.math.BigDecimal;

/**
 * DTO utilizado para recibir los datos necesarios
 * para crear un nuevo producto desde una petición HTTP.
 */
public class CreateProductDto {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    public CreateProductDto() {
    }

    public CreateProductDto(String name, String description, BigDecimal price, Integer stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}