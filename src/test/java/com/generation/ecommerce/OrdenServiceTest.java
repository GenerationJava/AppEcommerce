package com.generation.ecommerce;


import com.generation.ecommerce.dto.OrdenDTO;
import com.generation.ecommerce.exception.StockInsuficienteException;
import com.generation.ecommerce.model.Producto;
import com.generation.ecommerce.repository.ProductoRepository;
import com.generation.ecommerce.service.OrdenServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

//Configuramos Mockito para que podamos trabajar con Mocks de las dependencias
@ExtendWith(MockitoExtension.class)
public class OrdenServiceTest {

    @Mock//Anotación que indica que cree un mock del repository
    private ProductoRepository productoRepository;

    @InjectMocks//Inyecta el mock en el OrdenServiceImpl
    private OrdenServiceImpl ordenService;


    @Test
    @DisplayName("Al ingresar una cantidad mayor al stock, debería obtener una excepción de Stock Insuficiente")
    void cuandoIngresoUnaOrdenConCantidadDeProductosMayorAStockDebeLanzarExcepcion() {
        //Arrange:
        Producto producto = new Producto();
        producto.setStock(5);

        //Indicamos a través del método when de Mockito, qué debe hacer el mock de productoRepository cuando se le consulte por el producto con Id 1
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        OrdenDTO ordenDTO = OrdenDTO.builder().build();

        //Act:
        ordenDTO.setProductosIds(List.of(1L));
        ordenDTO.setCantidadesProductos(List.of(10));

        //Assert:
        assertThrows(StockInsuficienteException.class, () -> ordenService.saveOrden(ordenDTO));
    }




}
