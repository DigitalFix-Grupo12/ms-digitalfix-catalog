package cl.duoc.digitalfix.catalog.entity;

import jakarta.persistence.*;

/** Servicio tecnico o repuesto ofrecido por DigitalFix. */
@Entity
@Table(name = "catalog_items")
public class CatalogItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    /** SERVICIO | REPUESTO */
    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false)
    private int stock;

    /** Tarifa en CLP. */
    @Column(nullable = false)
    private int tarifa;

    protected CatalogItem() {}

    public CatalogItem(String nombre, String tipo, int stock, int tarifa) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.stock = stock;
        this.tarifa = tarifa;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getStock() { return stock; }
    public int getTarifa() { return tarifa; }
}
