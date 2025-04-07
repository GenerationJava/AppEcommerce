package com.generation.ecommerce.service;

import com.generation.ecommerce.model.Producto;

import java.util.List;

public interface ProductoService {

    //Declaro los métodos o acciones que se pueden hacer sobre el producto (CRUD)
    Producto findById(Long id);
    List<Producto> findAllProducto();
    Producto saveProducto(Producto producto);
    void deleteProductoById(Long id);
}
