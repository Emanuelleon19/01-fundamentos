package ec.edu.ups.icc.fundamentos01.categories.controllers;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos01.core.dtos.PaginationDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductFilterByCategoryDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;

/*
 * Controlador REST encargado de exponer consultas relacionadas
 * entre categorías y productos.
 *
 * Ruta: /categories/{id}/products
 * La lógica se delega a ProductService porque el recurso consultado es products.
 */
@RestController
@RequestMapping("/categories")
public class CategoryProductsController {

    private final ProductService productService;

    public CategoryProductsController(ProductService productService) {
        this.productService = productService;
    }

    /*
     * Endpoint normal, sin paginación.
     *
     * GET /api/categories/{id}/products
     */
    @GetMapping("/{id}/products")
    public List<ProductResponseDto> findProductsByCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductFilterByCategoryDto filters
    ) {
        return productService.findByCategoryIdWithFilters(id, filters);
    }

    /*
     * Endpoint paginado usando Page.
     *
     * GET /api/categories/{id}/products/page
     * GET /api/categories/{id}/products/page?page=0&size=5
     * GET /api/categories/{id}/products/page?name=laptop&minPrice=500&page=0&size=5
     */
    @GetMapping("/{id}/products/page")
    public Page<ProductResponseDto> findProductsByCategoryPage(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductFilterByCategoryDto filters,
            @Valid @ModelAttribute PaginationDto pagination
    ) {
        return productService.findByCategoryIdWithFiltersPage(id, filters, pagination);
    }

    /*
     * Endpoint paginado usando Slice.
     *
     * GET /api/categories/{id}/products/slice
     * GET /api/categories/{id}/products/slice?page=0&size=5
     */
    @GetMapping("/{id}/products/slice")
    public Slice<ProductResponseDto> findProductsByCategorySlice(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductFilterByCategoryDto filters,
            @Valid @ModelAttribute PaginationDto pagination
    ) {
        return productService.findByCategoryIdWithFiltersSlice(id, filters, pagination);
    }
}