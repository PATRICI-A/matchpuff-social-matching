package com.matchpuff.matchingservice.matching_service.entrypoints.rest.controller;

import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.request.MatchUpdateRequest;
import com.matchpuff.matchingservice.matching_service.application.dto.response.MatchResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.RecommendationResponse;
import com.matchpuff.matchingservice.matching_service.application.dto.response.RecommendationWithScoreResponse;
import com.matchpuff.matchingservice.matching_service.application.mapper.MatchApplicationMapper;
import com.matchpuff.matchingservice.matching_service.domain.model.AffinityScore;
import com.matchpuff.matchingservice.matching_service.domain.model.Match;
import com.matchpuff.matchingservice.matching_service.domain.model.MatchStatus;
import com.matchpuff.matchingservice.matching_service.domain.ports.in.MatchUseCasePort;
import com.matchpuff.matchingservice.matching_service.domain.ports.in.RecommendationsUseCasePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Matches", description = "Match requests and user recommendations")
@RequiredArgsConstructor
public class MatchController {

    private final MatchUseCasePort matchUseCase;
    private final RecommendationsUseCasePort recommendationsUseCase;
    private final MatchApplicationMapper matchRestMapper;

    // ---------------- CREATE MATCH ----------------
    @PostMapping
    @Operation(summary = "Send a match request", description = "Sends a match request from the requester to the target user")
    @ApiResponse(responseCode = "201", description = "Match request sent successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data or match already exists")
    public ResponseEntity<MatchResponse> createMatch(@Valid @RequestBody MatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                matchRestMapper.toResponse(matchUseCase.createMatch(request.getRequesterId(), request.getTargetId())));
    }

    // ---------------- GET MATCH BY ID ----------------
    @GetMapping("/{id}")
    @Operation(summary = "Get a match by ID", description = "Retrieves a match by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Match found")
    @ApiResponse(responseCode = "404", description = "Match not found")
    public ResponseEntity<MatchResponse> getMatch(
            @Parameter(description = "ID of the match") @PathVariable UUID id) {
        return ResponseEntity.ok(matchRestMapper.toResponse(matchUseCase.getMatch(id)));
    }

    // ---------------- GET MATCHES POR USUARIO ----------------
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all matches for a user",
               description = "Returns the matches where the user is the requester or recipient")
    @ApiResponse(responseCode = "200", description = "List of matches for the user")
    public ResponseEntity<List<MatchResponse>> getMatchesByUser(
            @Parameter(description = "ID of the user") @PathVariable UUID userId) {
        List<Match> requesterMatches = matchUseCase.findByRequesterId(userId);
        List<Match> targetMatches = matchUseCase.findByTargetId(userId);

        List<MatchResponse> matches = matchRestMapper.toResponseList(requesterMatches);
        matches.addAll(matchRestMapper.toResponseList(targetMatches));

        return ResponseEntity.ok(matches);
    }

    @GetMapping("/user/{userId}/sent")
    @Operation(summary = "Get match requests sent by a user",
               description = "Returns all match requests where the user is the requester")
    @ApiResponse(responseCode = "200", description = "List of sent match requests")
    public ResponseEntity<List<MatchResponse>> getSentMatchesByUser(
            @Parameter(description = "ID of the user") @PathVariable UUID userId) {
        return ResponseEntity.ok(matchRestMapper.toResponseList(matchUseCase.findByRequesterId(userId)));
    }

    @GetMapping("/user/{userId}/received")
    @Operation(summary = "Get match requests received by a user",
               description = "Returns all match requests where the user is the target")
    @ApiResponse(responseCode = "200", description = "List of received match requests")
    public ResponseEntity<List<MatchResponse>> getReceivedMatchesByUser(
            @Parameter(description = "ID of the user") @PathVariable UUID userId) {
        return ResponseEntity.ok(matchRestMapper.toResponseList(matchUseCase.findByTargetId(userId)));
    }

    // ---------------- UPDATE STATUS ----------------
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update match status",
               description = "Changes the status of the match to ACCEPTED or REJECTED")
    @ApiResponse(responseCode = "200", description = "Status updated")
    @ApiResponse(responseCode = "404", description = "Match not found")
    @ApiResponse(responseCode = "400", description = "Invalid status")
    public ResponseEntity<MatchResponse> updateMatchStatus(
            @Parameter(description = "ID of the match") @PathVariable UUID id,
            @Valid @RequestBody MatchUpdateRequest request) {
        boolean accept = request.getStatus() == MatchStatus.ACCEPTED;
        return ResponseEntity.ok(matchRestMapper.toResponse(matchUseCase.respondToMatchRequest(id, accept)));
    }

    // ---------------- GET RECOMMENDATIONS ----------------
    @GetMapping("/recommendations/{userId}")
    @Operation(summary = "Obtain recommendations for user", description = "Obtain a list of recommended user IDs based on affinity scores")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable UUID userId) {
        return ResponseEntity.ok(matchRestMapper.toRecommendationResponse(userId, recommendationsUseCase.getRecommendedUserIdsForUser(userId)));
    }

    @GetMapping("/recommendations/{userId}/scores")
    @Operation(
        summary = "Get recommendations with affinity scores",
        description = "Returns all recommended users sorted by total affinity score (descending), including the score breakdown per dimension"
    )
    @ApiResponse(responseCode = "200", description = "List of recommendations with affinity scores")
    public ResponseEntity<List<RecommendationWithScoreResponse>> getRecommendationsWithScores(@PathVariable UUID userId) {
        Map<UUID, AffinityScore> scores = recommendationsUseCase.getRecommendationsForUser(userId);
        List<RecommendationWithScoreResponse> response = scores.entrySet().stream()
                .sorted(Map.Entry.<UUID, AffinityScore>comparingByValue(
                        Comparator.comparingDouble(AffinityScore::getTotalScore)).reversed())
                .map(e -> RecommendationWithScoreResponse.builder()
                        .targetUserId(e.getKey())
                        .totalScore(e.getValue().getTotalScore())
                        .interestScore(e.getValue().getInterestScore())
                        .academicScore(e.getValue().getAcademicScore())
                        .scheduleScore(e.getValue().getScheduleScore())
                        .build())
                .toList();
        return ResponseEntity.ok(response);
    }
}