package com.generation.ecommerce;

import com.generation.ecommerce.model.Producto;
import com.generation.ecommerce.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

//Testear un repositorio también es un test de integración porque este se integra con la BDD
@DataJpaTest
//Le indicamos qué perfil va a tomar en cuenta para la creación de la base de datos
@ActiveProfiles("test")
public class ProductoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void cuandoBuscoUnProductoPorNombreDebeRetornarProducto() {
        //Arrange
        Producto producto = new Producto();
        producto.setNombre("Pantalon");


        //Act
        entityManager.persist(producto);
        entityManager.flush();

        Producto productoSeleccionado = productoRepository.findProductoByNombre("Pantalon").get();


        //Assert
        assertThat(productoSeleccionado.getNombre()).isEqualTo(producto.getNombre());

    }

}
