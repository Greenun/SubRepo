package com.swmaestro.web.presentation.controller;

import com.swmaestro.web.auth.JwtProvider;
import com.swmaestro.web.presentation.dto.PowerpointDTO;
import com.swmaestro.web.presentation.dto.response.PowerpointResponse;
import com.swmaestro.web.presentation.service.PowerpointService;
import com.swmaestro.web.presentation.service.S3Service;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/presentation")
public class PresentationController {
    private final Logger logger = LoggerFactory.getLogger(PresentationController.class);

    private final JwtProvider jwtProvider;

    private final PowerpointService powerpointService;

    @Autowired
    public PresentationController(JwtProvider jwtProvider, PowerpointService pptService) {
        this.jwtProvider = jwtProvider;
        this.powerpointService = pptService;
    }

    @ApiOperation(value = "PPT 업로드", notes = "Presentation용 PPT 업로드")
    @PostMapping("/ppt")
    public PowerpointResponse uploadPowerpoint(@RequestHeader("X-AUTH-TOKEN") String token,
                                               @RequestBody byte[] ppt) {
        String username = jwtProvider.getUsername(token);
        return this.powerpointService.uploadService(username, ppt);
    }

    @ApiOperation(value = "PPT 다운로드", notes = "Presentation PPT 다운로드")
    @GetMapping("/ppt")
    public PowerpointResponse downloadPowerpoint(@RequestHeader("X-AUTH-TOKEN") String token) {
        String username = this.jwtProvider.getUsername(token);
        PowerpointResponse resp = this.powerpointService.downloadService(username);
//        logger.info(new String(resp.getPpt()));
        return resp;
    }
}
