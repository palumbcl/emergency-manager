package com.emergency.controller;

import com.emergency.dto.CamionDTO;
import com.emergency.mapper.CamionMapper;
import com.emergency.model.Camion;
import com.emergency.service.CamionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camions")
public class CamionController {
    private final CamionService camionService;
    private final CamionMapper camionMapper;

    @GetMapping
    public List<CamionDTO> getAllCamions() {
        List<Camion> camions = camionService.getAllCamions();
        return camions.stream()
                .map(camionMapper::toDTO)
                .toList();
    }
}
