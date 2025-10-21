package com.health.controller;

import com.health.model.Product;
import com.health.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET - Todos los productos
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts() {
        List<EntityModel<Product>> products = productService.findAll().stream()
            .map(product -> EntityModel.of(product,
                linkTo(methodOn(ProductController.class).getProductById(product.getIdProduct())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products")))
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Product>> collectionModel = CollectionModel.of(products,
            linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> getProductById(@PathVariable Integer id) {
        return productService.findById(id)
            .map(product -> {
                EntityModel<Product> resource = EntityModel.of(product,
                    linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel(),
                    linkTo(methodOn(ProductController.class).getAllProducts()).withRel("all-products"));
                return ResponseEntity.ok(resource);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // POST - Crear nuevo producto
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
        try {
            Product savedProduct = productService.save(product);
            EntityModel<Product> resource = EntityModel.of(savedProduct,
                linkTo(methodOn(ProductController.class).getProductById(savedProduct.getIdProduct())).withSelfRel());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear producto: " + e.getMessage());
        }
    }

    // PUT - Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @Valid @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.update(id, productDetails);
            EntityModel<Product> resource = EntityModel.of(updatedProduct,
                linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel());
            
            return ResponseEntity.ok(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET - Productos por categoría
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getProductsByCategory(@PathVariable Integer categoryId) {
        List<EntityModel<Product>> products = productService.findByCategory(categoryId).stream()
            .map(product -> EntityModel.of(product,
                linkTo(methodOn(ProductController.class).getProductById(product.getIdProduct())).withSelfRel()))
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Product>> collectionModel = CollectionModel.of(products,
            linkTo(methodOn(ProductController.class).getProductsByCategory(categoryId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // GET - Productos por nombre (búsqueda)
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name) {
        List<Product> products = productService.findByNameContaining(name);
        return ResponseEntity.ok(products);
    }

    // GET - Productos con stock bajo
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getProductsWithLowStock(@RequestParam(defaultValue = "10") Integer minStock) {
        List<Product> products = productService.findProductsWithLowStock(minStock);
        return ResponseEntity.ok(products);
    }
}
