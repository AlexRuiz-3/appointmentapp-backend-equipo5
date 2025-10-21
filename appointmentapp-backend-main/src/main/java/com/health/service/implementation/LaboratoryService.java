// Gestión de laboratorios
- findAll(): List<Laboratory>
- findById(Integer id): Optional<Laboratory>
- save(Laboratory laboratory): Laboratory
- update(Integer id, Laboratory laboratoryDetails): Laboratory
- deleteById(Integer id): void
- findActiveLaboratories(): List<Laboratory>
- findByNameContaining(String name): List<Laboratory>
