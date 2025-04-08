package com.generation.ecommerce.repository;

import com.generation.ecommerce.model.ECategoria;
import com.generation.ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    //Declaramos un método para buscar producto por nombre
    Optional<Producto> findProductoByNombre(String nombre);

    //Usamos JPQL para generar una busqueda por el nombre de la categoria
    @Query("SELECT p FROM Producto p WHERE p.categoria.nombre = :categoria")
    List<Producto> findAllProductoByCategoria(@Param(value = "categoria") ECategoria categoria);

    //Query para buscar por rango de precio entre dos double
    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :min AND :max")
    List<Producto> findAllProductoByRangoPrecio(@Param(value = "min") Double min, @Param(value = "max") Double max);


}
