package com.emergency.controller;

import com.emergency.dto.PompierDTO;
import com.emergency.mapper.PompierMapper;
import com.emergency.model.Pompier;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.emergency.service.PompierService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pompiers")
public class PompierController {
    private final PompierService pompierService;
    private final PompierMapper pompierMapper;

    @GetMapping
    public List<PompierDTO> getAllPompiers() {
        List<Pompier> pompiers = pompierService.getAllPompiers();
        return pompiers.stream()
                .map(pompierMapper::toDTO)
                .toList();
    }
}
