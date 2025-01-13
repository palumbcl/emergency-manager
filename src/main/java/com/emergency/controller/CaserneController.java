package com.emergency.controller;

import com.emergency.dto.CaserneDTO;
import com.emergency.mapper.CaserneMapper;
import com.emergency.model.Caserne;
import com.emergency.service.CaserneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/casernes")
public class CaserneController {
    private final CaserneService caserneService;
    private final CaserneMapper caserneMapper;

    @GetMapping
    public List<CaserneDTO> getAllCasernes() {
        List<Caserne> casernes = caserneService.getAllCasernes();
        return casernes.stream()
                .map(caserneMapper::toDTO)
                .toList();
    }
}