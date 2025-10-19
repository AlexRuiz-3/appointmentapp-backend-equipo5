package com.health.repository;

import com.health.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    // Buscar productos por nombre 
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Buscar productos por categoría
    List<Product> findByCategoryIdCategory(Integer idCategory);
    
    // Buscar productos por familia
    List<Product> findByFamilyIdFamily(Integer idFamily);
    
    // Buscar productos por laboratorio
    List<Product> findByLaboratoryIdLaboratory(Integer idLaboratory);
    
    // Buscar productos con stock bajo
    @Query("SELECT p FROM Product p WHERE p.stock < :minStock")
    List<Product> findProductsWithLowStock(@Param("minStock") Integer minStock);
    
    // Buscar productos por rango de precio
    @Query("SELECT p FROM Product p WHERE p.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findProductsByPriceRange(@Param("minPrice") Double minPrice, 
                                         @Param("maxPrice") Double maxPrice);
    
    // Verificar si existe un producto con el mismo nombre
    Boolean existsByName(String name);
    
    // Buscar productos activos 
    @Query("SELECT p FROM Product p WHERE p.stock > 0")
    List<Product> findActiveProducts();
}
