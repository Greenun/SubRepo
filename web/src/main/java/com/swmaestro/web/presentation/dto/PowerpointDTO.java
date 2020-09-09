package com.swmaestro.web.presentation.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PowerpointDTO {
    private byte[] data;
}
