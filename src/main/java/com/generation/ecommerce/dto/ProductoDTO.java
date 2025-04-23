package com.generation.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductoDTO {
    @NotBlank
    private String nombre;

    @Positive
    private Double precio;

    @PositiveOrZero
    private Integer stock;

    @NotNull
    private Long categoriaId;

    private String imagen;
}
