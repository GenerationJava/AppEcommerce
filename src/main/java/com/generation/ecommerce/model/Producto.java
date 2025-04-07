package com.generation.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productos")
@AllArgsConstructor//Anotación para constructor con todos los atributos o lleno
@NoArgsConstructor//Anotación para constructor vacío
@Getter//Anotación que genera getter para todos los atributos
@Setter//Anotación que genera setter para todos los atributos
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Double precio;
    private String nombre;
    private Integer stock;
    /**** OTROS ATRIBUTOS Y RELACIONES ****/


}
