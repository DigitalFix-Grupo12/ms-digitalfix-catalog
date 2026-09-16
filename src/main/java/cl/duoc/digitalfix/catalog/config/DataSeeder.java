package cl.duoc.digitalfix.catalog.config;

import cl.duoc.digitalfix.catalog.entity.CatalogItem;
import cl.duoc.digitalfix.catalog.repository.CatalogItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/** Catalogo inicial (H2 en memoria): se carga solo si la tabla esta vacia. */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedCatalog(CatalogItemRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            repo.saveAll(List.of(
                new CatalogItem("Cambio de tablero eléctrico", "SERVICIO", 12, 45000),
                new CatalogItem("Reparación de cortocircuito", "SERVICIO", 8, 32000),
                new CatalogItem("Inspección de transformador", "SERVICIO", 5, 60000),
                new CatalogItem("Instalación de medidor", "SERVICIO", 20, 28000),
                new CatalogItem("Interruptor automático 20A", "REPUESTO", 150, 8500),
                new CatalogItem("Cable THHN 12 AWG (100 m)", "REPUESTO", 40, 54000),
                new CatalogItem("Fusible de media tensión", "REPUESTO", 25, 19000)
            ));
        };
    }
}
