package com.generation.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad que representa un producto en el sistema")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de producto autogenerado", example = "1")
    private long id;

    @Schema(description = "Precio de producto", example = "10000.00")
    private Double precio;

    @Schema(description = "Nombre de producto", example = "Camiseta")
    private String nombre;

    @Schema(description = "Stock de producto", example = "100")
    private Integer stock;

    @Schema(description = "URL de imágen del producto", example = "https://unawebrandom.com/imagen.png")
    private String imagenUrl;
    /**** OTROS ATRIBUTOS Y RELACIONES ****/

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @Schema(description = "Categoría del producto")
    private Categoria categoria;


}
