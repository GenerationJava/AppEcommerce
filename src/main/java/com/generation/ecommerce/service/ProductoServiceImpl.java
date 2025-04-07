package com.generation.ecommerce.service;

import com.generation.ecommerce.model.Producto;
import com.generation.ecommerce.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor//Anotación de lombok que crea un constructor que requiere los campos llenos
public class ProductoServiceImpl implements ProductoService {

    //Inyección de dependencia se realiza gracias a la anotación de Lombok
    private final ProductoRepository productoRepository;


    //Métodos para el CRUD
    @Override
    public Producto findById(Long id) {
        return productoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Producto no encontrado"));
    }

    @Override
    public List<Producto> findAllProducto() {
        return productoRepository.findAll();
    }

    @Override
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteProductoById(Long id) {
        productoRepository.deleteById(id);
    }

    //Métodos más específicos

}
