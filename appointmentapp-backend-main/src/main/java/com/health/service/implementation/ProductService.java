// Métodos CRUD completos para Product
- findAll(): List<Product>
- findById(Integer id): Optional<Product>
- save(Product product): Product
- update(Integer id, Product productDetails): Product
- deleteById(Integer id): void
- findByNameContaining(String name): List<Product>
- findByCategory(Integer categoryId): List<Product>
- findProductsWithLowStock(Integer minStock): List<Product>
