package br.com.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.api.infrastructure.services.EvaluationDashboardService;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {
    @Autowired
    private EvaluationDashboardService _evaluationService;

    @GetMapping("/generate")
    public ResponseEntity<?> generate() {
        return ResponseEntity.ok()
                .body(_evaluationService.getInstance().getResultsJson().toString());
    }
}
