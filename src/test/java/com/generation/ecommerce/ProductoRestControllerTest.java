package com.generation.ecommerce;

import com.generation.ecommerce.api.ProductoRestController;
import com.generation.ecommerce.model.Producto;
import com.generation.ecommerce.service.ProductoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//Al testear un controlador, estamos haciendo un test de integración porque se integra con la capa web
//En este caso no sólo trabajamos con mockito sino también en el contexto de Spring
@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductoRestController.class)
public class ProductoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;//Esta dependencia nos proporciona métodos par simular peticiones sobre el controlador

    @MockBean//Esta anotación va a crear el mock (imitación) pero lo mantiene en el contexto de Spring
    private ProductoServiceImpl productoService;

    @Test
    public void cuandoSolicitoListaDeProductosDeboObtener200() throws Exception {
        //A través del objeto mockMvc accedemos al método perform que puede hacer peticiones HTTP
        mockMvc.perform(get("/api/productos/lista"))
                .andExpect(status().isOk());
    }

    @Test
    void cuandoSolicitoProductoPorIdDeboObtener200() throws Exception {
        //Arrange
        Long id = 1L;
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Camisa");

        when(productoService.findById(id)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/producto/{id}", id))
                .andExpect(status().isOk());

    }



}
