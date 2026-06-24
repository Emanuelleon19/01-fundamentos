package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.math.BigDecimal;

/**
 * DTO utilizado para recibir los datos que se desean
 * actualizar parcialmente en un producto existente (PATCH).
 */
public class PartialUpdateProductDto {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    public PartialUpdateProductDto() {
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