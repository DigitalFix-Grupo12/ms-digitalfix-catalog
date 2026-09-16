package cl.duoc.digitalfix.catalog.repository;

import cl.duoc.digitalfix.catalog.entity.CatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogItemRepository extends JpaRepository<CatalogItem, Long> {
    List<CatalogItem> findByTipoIgnoreCaseOrderByNombre(String tipo);
    List<CatalogItem> findAllByOrderByIdAsc();
}
