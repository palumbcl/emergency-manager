package com.emergency.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlotteCreationRequestDTO {
    private Long caserneId;
    private int nombrePompiersNecessaires;
    private Long feuId;
}
