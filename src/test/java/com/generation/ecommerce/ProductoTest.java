package com.generation.ecommerce;

import com.generation.ecommerce.model.Categoria;
import com.generation.ecommerce.model.ECategoria;
import com.generation.ecommerce.model.Producto;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class ProductoTest {

    //La anotación @Test indica que el método es una prueba
    @Test
    void cuandoCreoUnProductoConDatosValidosDebeMantenerValoresAsignados() {
        //Los test unitarios siguen un patrón de tres etapas: AAA = Arrange - Act - Assert
        //Arrange: corresponde a la etapa de preparación, donde configuro los datos u objeto con que voy a testear
        Categoria categoriaHombre = new Categoria();
        categoriaHombre.setNombre(ECategoria.HOMBRE);

        //Act: corresponde a la etapa donde se realizan las acciones
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Camisa");
        producto.setPrecio(1000.00);
        producto.setStock(50);
        producto.setCategoria(categoriaHombre);

        //Assert: corresponde a la etapa donde verificamos que la acción haya tenido el efecto esperado
        assertAll("Verificar las distintas propiedades del producto",
                () -> assertEquals(1, producto.getId()),
                () -> assertEquals("Camisa", producto.getNombre()),
                () -> assertEquals(1000.00, producto.getPrecio()),
                () -> assertEquals(50, producto.getStock()),
                () -> assertEquals(ECategoria.HOMBRE, producto.getCategoria().getNombre())
        );

        //Alternativa con AssertJ
        assertThat(producto)
                .hasFieldOrPropertyWithValue("id",1L)
                .hasFieldOrPropertyWithValue("nombre", "Camisa")
                .hasFieldOrPropertyWithValue("precio", 1000.00)
                .hasFieldOrPropertyWithValue("stock", 50)
                .extracting(Producto::getCategoria)
                .isNotNull()
                .extracting(Categoria::getNombre)
                .isEqualTo(ECategoria.HOMBRE);
    }

    //Test unitario de una función del producto: el stock
    @Test
    void cuandoModificoElStockDebeActualizarDisponibilidad() {
        //Arrange: creamos una instancia de Producto
        Producto producto = new Producto();
        producto.setStock(10);

        //Act: acciones a realizarse sobre el stock
        //Assert: verificamos cada acción que se realiza

        //Verificamos si hay stock disponible
        assertTrue(producto.getStock() > 0, "Indica que tiene stock disponible");

        //Verificamos la reducción de stock
        producto.setStock(5);
        assertEquals(5, producto.getStock());

        //Verificamos si es posible agotar el stock
        producto.setStock(0);
        assertEquals(0, producto.getStock());


    }


}
