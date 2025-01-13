package com.emergency.controller;

import com.emergency.dto.FeuDetecteDTO;
import com.emergency.mapper.FeuDetecteMapper;
import com.emergency.model.FeuDetecte;
import com.emergency.service.FeuDetecteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feu-detecte")
public class FeuDetecteController {
    private final FeuDetecteService feuDetecteService;
    private final FeuDetecteMapper feuDetecteMapper;

    @GetMapping
    public List<FeuDetecteDTO> getAllFeuDetectes() {
        List<FeuDetecte> feuDetectes = feuDetecteService.getAllFeuDetectes();
        return feuDetectes.stream()
                .map(feuDetecteMapper::toDTO)
                .toList();
    }

    @PostMapping
    public FeuDetecteDTO addFeuDetecte(FeuDetecteDTO feuDetecteDTO) {
        FeuDetecte feuDetecte = feuDetecteMapper.toEntity(feuDetecteDTO);
        FeuDetecte feuDetecteAdded = feuDetecteService.addFeuDetecte(feuDetecte);
        return feuDetecteMapper.toDTO(feuDetecteAdded);
    }
}
