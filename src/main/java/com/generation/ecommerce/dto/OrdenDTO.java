package com.generation.ecommerce.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


//Este objeto no persistente, me permite enviar los datos de la orden entre capas de servicio y presentacion
@Data
@Builder
public class OrdenDTO {

    //Datos para la solicitud de compra
    private String email;
    private List<Long> productosIds;//Este campo lleva la lista de ID's de producto en la compra
    private List<Integer> cantidadesProductos;


    //Datos para la respuesta
    private Long id;
    private Double total;
    private String estado;
    private List<String> nombresProductos;
    private List<Double> preciosProductos;


}
