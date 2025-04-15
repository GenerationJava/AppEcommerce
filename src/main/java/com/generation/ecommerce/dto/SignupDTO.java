package com.generation.ecommerce.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
