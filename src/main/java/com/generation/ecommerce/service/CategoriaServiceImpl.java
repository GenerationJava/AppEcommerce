package com.generation.ecommerce.service;

import com.generation.ecommerce.model.Categoria;
import com.generation.ecommerce.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoriaServiceImpl  implements CategoriaService{

    private final CategoriaRepository categoriaRepository;


    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }
}
