package com.generation.ecommerce.repository;

import com.generation.ecommerce.model.Categoria;
import com.generation.ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {



}
