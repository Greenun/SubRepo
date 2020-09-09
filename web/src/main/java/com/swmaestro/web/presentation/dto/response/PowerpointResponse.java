package com.swmaestro.web.presentation.dto.response;


import com.swmaestro.web.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PowerpointResponse {

    @Builder.Default
    private ResponseStatus status = ResponseStatus.OK;

    @Builder.Default
    private String message = "";

    @Builder.Default
    private byte[] ppt = null;
}
