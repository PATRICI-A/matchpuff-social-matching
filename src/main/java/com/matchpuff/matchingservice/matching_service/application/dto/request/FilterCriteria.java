package com.matchpuff.matchingservice.matching_service.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterCriteria {
    private List<String> careers;      // ["SISTEMAS", "CIVIL"] — null = sin filtro
    private List<Integer> semesters;   // [2, 3, 4] — null = sin filtro
    private List<String> tags;         // ["gaming", "musica"] — null = sin filtro
}