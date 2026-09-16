package cl.duoc.digitalfix.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Pruebas de integracion con H2: entidad + repositorio + controller + datos semilla. */
@SpringBootTest
@AutoConfigureMockMvc
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listaCatalogoCompleto() throws Exception {
        mockMvc.perform(get("/api/catalog/services"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(7)))
            .andExpect(jsonPath("$[0].nombre").exists())
            .andExpect(jsonPath("$[0].tarifa").isNumber());
    }

    @Test
    void filtraPorTipo() throws Exception {
        mockMvc.perform(get("/api/catalog/services").param("tipo", "repuesto"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[*].tipo", everyItem(is("REPUESTO"))));
    }

    @Test
    void filtraPorTipoServicio() throws Exception {
        mockMvc.perform(get("/api/catalog/services").param("tipo", "SERVICIO"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[*].tipo", everyItem(is("SERVICIO"))));
    }

    @Test
    void itemInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/api/catalog/services/9999"))
            .andExpect(status().isNotFound());
    }
}
