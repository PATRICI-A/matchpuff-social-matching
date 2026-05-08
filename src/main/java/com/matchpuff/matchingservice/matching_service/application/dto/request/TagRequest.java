package com.matchpuff.matchingservice.matching_service.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagRequest {

    @NotBlank(message = "El nombre del tag es requerido")
    @Size(max = 50, message = "El nombre del tag no puede superar 50 caracteres")
    private String name;

    @NotBlank(message = "La categoría del tag es requerida")
    @Size(max = 100, message = "La categoría no puede superar 100 caracteres")
    private String category;
}
