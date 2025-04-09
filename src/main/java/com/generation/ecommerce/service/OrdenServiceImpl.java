package com.generation.ecommerce.service;

import com.generation.ecommerce.dto.OrdenDTO;
import com.generation.ecommerce.exception.OrdenException;
import com.generation.ecommerce.exception.StockInsuficienteException;
import com.generation.ecommerce.model.EEstadoOrden;
import com.generation.ecommerce.model.Orden;
import com.generation.ecommerce.model.Producto;
import com.generation.ecommerce.repository.OrdenRepository;
import com.generation.ecommerce.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdenServiceImpl {

    //Inyectamos las dependencias
    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;

    //Creamos la lógica para generar una orden
    public OrdenDTO saveOrden(OrdenDTO solicitudDTO) {

        //Validamos primero, los datos de la solicitud
        //Primero, validamos que la lista de productosIds y cantidadesProductos no sean nulas
        //Segundo, validamos que ambas listas tengan el mismo tamaño
        if (solicitudDTO.getProductosIds() == null
                || solicitudDTO.getCantidadesProductos() == null
                || solicitudDTO.getProductosIds().size() != solicitudDTO.getCantidadesProductos().size()) {
            //Si sucede que alguno de estos casos se cumple, lanzamos una excepción personalizada
            throw new OrdenException("Las cantidades de productos no coinciden");
        }

        //Validamos que tampoco se reciba una lista vacía
        if (solicitudDTO.getProductosIds().isEmpty()) {
            throw new OrdenException("La lista de productos está vacía");
        }

        //Creamos la orden con el patrón Builder, para luego persistirla
        Orden orden = Orden.builder()
                .fechaCreacion(LocalDateTime.now())//Seteamos la fecha de creación a la fecha actual
                .estado(EEstadoOrden.PENDIENTE)
                .build();

        //Datos de los productos
        List<String> nombresProductos = new ArrayList<>();
        List<Double> preciosProductos = new ArrayList<>();
        Double total = 0.00;

        //Iteramos sobre la lista de productos
        for (int i = 0; i < solicitudDTO.getProductosIds().size(); i++) {
            //Obtenemos el Id de producto en cada iteración y lo guardamos en una variable Long
            Long productoId = solicitudDTO.getProductosIds().get(i);
            Integer productoCantidad = solicitudDTO.getCantidadesProductos().get(i);

            //Guardamos el producto seleccionado en cada iteración en una variable producto
            Producto productoSeleccionado = productoRepository.findById(productoId)
                    .orElseThrow(() -> new OrdenException("Producto no existente"));

            //Validamos stock del producto
            if(productoSeleccionado.getStock() < productoCantidad) {
                throw new StockInsuficienteException("Stock insuficiente para el producto " + productoSeleccionado.getNombre());
            }

            //Actualizar el stock del producto
            productoSeleccionado.setStock(productoSeleccionado.getStock() - productoCantidad);
            productoRepository.save(productoSeleccionado);

            //Agregamos los nombres de productos y sus precios para la respuesta de la orden
            nombresProductos.add(productoSeleccionado.getNombre());
            preciosProductos.add(productoSeleccionado.getPrecio());

            //Hacemos el cálculo del total, sumando el precio de cada producto y multiplicándolo por la cantidad
            total += (productoSeleccionado.getPrecio() * productoCantidad);
        }

        //Le seteamos el total a la orden
        orden.setTotal(total);

        //Guardamos la orden en la BDD
        Orden ordenGuardada = ordenRepository.save(orden);

        //Generamos un DTO de respuesta
        OrdenDTO respuestaDTO = OrdenDTO.builder()
                .id(orden.getId())
                .nombresProductos(nombresProductos)
                .estado(ordenGuardada.getEstado().name())
                .preciosProductos(preciosProductos)
                .total(total)
                .build();

         return respuestaDTO;
    }
}
