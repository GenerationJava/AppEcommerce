package com.generation.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalles_orden")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Esta entidad ya no es necesaria por el DTO de la Orden
public class ProductosOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    private Integer cantidad;

    private Double precio;

    private Double total;
}