package com.generation.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductosOrden> productos = new ArrayList<>();

    private Double total;

    @Enumerated(EnumType.STRING)
    private EEstadoOrden estado;

    private LocalDateTime fechaCreacion;

    // Otros campos y relaciones


}

