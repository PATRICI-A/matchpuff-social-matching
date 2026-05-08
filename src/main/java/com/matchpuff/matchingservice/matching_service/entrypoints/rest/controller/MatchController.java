package com.matchpuff.matchingservice.matching_service.entrypoints.rest.controller;

import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchUpdateRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.response.MatchResponse;
import com.matchpuff.matchingservice.matching_service.application.service.MatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Matches", description = "CRUD de matches entre usuarios")
@RequiredArgsConstructor
public class MatchController {

    private final MatchingService matchingService;

    @PostMapping
    @Operation(summary = "Crear un match", description = "Crea un nuevo match en estado PENDING entre dos usuarios")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Match creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos o match ya existente")
    })
    public ResponseEntity<MatchResponse> createMatch(@Valid @RequestBody MatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchingService.createMatch(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener match por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Match encontrado"),
        @ApiResponse(responseCode = "404", description = "Match no encontrado")
    })
    public ResponseEntity<MatchResponse> getMatch(
            @Parameter(description = "ID del match") @PathVariable UUID id) {
        return ResponseEntity.ok(matchingService.getMatch(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener todos los matches de un usuario",
               description = "Retorna los matches donde el usuario es solicitante o destinatario")
    @ApiResponse(responseCode = "200", description = "Lista de matches del usuario")
    public ResponseEntity<List<MatchResponse>> getMatchesByUser(
            @Parameter(description = "ID del usuario") @PathVariable UUID userId) {
        return ResponseEntity.ok(matchingService.getMatchesByUser(userId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de un match",
               description = "Cambia el estado del match a ACCEPTED o REJECTED")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado"),
        @ApiResponse(responseCode = "404", description = "Match no encontrado"),
        @ApiResponse(responseCode = "400", description = "Estado invalido")
    })
    public ResponseEntity<MatchResponse> updateMatchStatus(
            @Parameter(description = "ID del match") @PathVariable UUID id,
            @Valid @RequestBody MatchUpdateRequest request) {
        return ResponseEntity.ok(matchingService.updateMatchStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un match")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Match eliminado"),
        @ApiResponse(responseCode = "404", description = "Match no encontrado")
    })
    public ResponseEntity<Void> deleteMatch(
            @Parameter(description = "ID del match") @PathVariable UUID id) {
        matchingService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}
