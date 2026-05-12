package com.matchpuff.matchingservice.matching_service.entrypoints.rest.controller;

import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchUpdateRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.response.MatchResponse;
import com.matchpuff.matchingservice.matching_service.domain.model.Match;
import com.matchpuff.matchingservice.matching_service.domain.ports.in.MatchUseCasePort;
import com.matchpuff.matchingservice.matching_service.domain.ports.in.RecommendationsUseCasePort;
import com.matchpuff.matchingservice.matching_service.entrypoints.rest.mapper.MatchRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    private final MatchUseCasePort matchUseCase;
    private final RecommendationsUseCasePort recommendationsUseCase;
    private final MatchRestMapper matchRestMapper;

    // ---------------- CREATE MATCH ----------------
    @PostMapping
    @Operation(summary = "Crear un match", description = "Crea un nuevo match en estado PENDING entre dos usuarios")
    @ApiResponse(responseCode = "201", description = "Match creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o match ya existente")
    public ResponseEntity<MatchResponse> createMatch(@Valid @RequestBody MatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                matchRestMapper.toResponse(matchUseCase.createMatch(request.getRequesterId(), request.getTargetId())));
    }

    // ---------------- GET MATCH BY ID ----------------
    @GetMapping("/{id}")
    @Operation(summary = "Obtener match por ID")
    @ApiResponse(responseCode = "200", description = "Match encontrado")
    @ApiResponse(responseCode = "404", description = "Match no encontrado")
    public ResponseEntity<MatchResponse> getMatch(
            @Parameter(description = "ID del match") @PathVariable UUID id) {
        return ResponseEntity.ok(matchRestMapper.toResponse(matchUseCase.getMatch(id)));
    }

    // ---------------- GET MATCHES POR USUARIO ----------------
    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener todos los matches de un usuario",
               description = "Retorna los matches donde el usuario es solicitante o destinatario")
    @ApiResponse(responseCode = "200", description = "Lista de matches del usuario")
    public ResponseEntity<List<MatchResponse>> getMatchesByUser(
            @Parameter(description = "ID del usuario") @PathVariable UUID userId) {
        List<Match> requesterMatches = matchUseCase.findByRequesterId(userId);
        List<Match> targetMatches = matchUseCase.findByTargetId(userId);

        List<MatchResponse> matches = matchRestMapper.toResponseList(requesterMatches);
        matches.addAll(matchRestMapper.toResponseList(targetMatches));

        return ResponseEntity.ok(matches);
    }

    // ---------------- UPDATE STATUS ----------------
    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de un match",
               description = "Cambia el estado del match a ACCEPTED o REJECTED")
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @ApiResponse(responseCode = "404", description = "Match no encontrado")
    @ApiResponse(responseCode = "400", description = "Estado inválido")
    public ResponseEntity<MatchResponse> updateMatchStatus(
            @Parameter(description = "ID del match") @PathVariable UUID id,
            @Valid @RequestBody MatchUpdateRequest request) {
        boolean accept = request.getStatus() == com.matchpuff.matchingservice.matching_service.domain.model.enums.MatchStatus.ACCEPTED;
        return ResponseEntity.ok(matchRestMapper.toResponse(matchUseCase.respondToMatchRequest(id, accept)));
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un match")
    @ApiResponse(responseCode = "204", description = "Match eliminado")
    @ApiResponse(responseCode = "404", description = "Match no encontrado")
    public ResponseEntity<Void> deleteMatch(@Parameter(description = "ID del match") @PathVariable UUID id) {
        matchUseCase.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- GET RECOMMENDATIONS ----------------
    @GetMapping("/recommendations/{userId}")
    @Operation(summary = "Obtener recomendaciones de matches para un usuario")
    public ResponseEntity<List<UUID>> getRecommendations(@PathVariable UUID userId) {
        // puedes cambiar a DTO con score si quieres
        List<UUID> recommendedIds = recommendationsUseCase.getRecommendationsForUser(userId).keySet().stream().toList();
        return ResponseEntity.ok(recommendedIds);
    }
}