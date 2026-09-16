package cl.duoc.digitalfix.catalog.controller;

import cl.duoc.digitalfix.catalog.entity.CatalogItem;
import cl.duoc.digitalfix.catalog.repository.CatalogItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Catalogo tecnico. API interna: no valida JWT (solo el BFF le habla por
 * localhost); la autorizacion por rol vive en ms-digitalfix-bff.
 */
@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogItemRepository repository;

    public CatalogController(CatalogItemRepository repository) {
        this.repository = repository;
    }

    /** ?tipo=SERVICIO|REPUESTO (opcional). */
    @GetMapping("/services")
    public List<CatalogItem> services(@RequestParam(required = false) String tipo) {
        return tipo == null || tipo.isBlank()
            ? repository.findAllByOrderByIdAsc()
            : repository.findByTipoIgnoreCaseOrderByNombre(tipo);
    }

    @GetMapping("/services/{id}")
    public CatalogItem byId(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ítem no encontrado: " + id));
    }
}
