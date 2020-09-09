package com.swmaestro.web.presentation.service;

import com.swmaestro.web.ResponseStatus;
import com.swmaestro.web.presentation.domain.PowerpointEntity;
import com.swmaestro.web.presentation.domain.PowerpointRepository;
import com.swmaestro.web.presentation.dto.response.PowerpointResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
public class PowerpointService {

    private final S3Service s3Service;

    private final String pptPath = "UserPowerpoint";

    private final String pptPostfix = ".pdf"; // temp

    private final String videoPath = "UserVideo";

    private PowerpointRepository pptRepository;

    @Autowired
    public PowerpointService(S3Service s3Service, PowerpointRepository repository) {
        this.s3Service = s3Service;
        this.pptRepository = repository;
    }

    /*
        upload 시 발생 문제
        1개의 계정으로 2개 이상의 브라우저에서 올릴 경우 (ppt와 동영상 맵핑 문제)
        1. Spring security 중복 로그인 방지(세션 기반) + 최신 ppt와 최신 동영상 맵핑
        2. Front 측에서 timestamp 기반의 토큰 보냄 -> 토큰 기반으로 유효성 확인 및 ppt, 동영상 db 내 매핑

        현재 최신 ppt와 video를 매핑하도록 구현중..
    */
    public PowerpointResponse uploadService(String username, byte[] ppt) {
        try {
            String filepath = this.s3Service.generateFileKey(this.pptPath + "/" + username);
            this.s3Service.putS3Object(filepath, ppt);
            PowerpointEntity e = PowerpointEntity.builder()
                    .filename(filepath)
                    .username(username)
                    .build();
            this.pptRepository.save(e);
        } catch (Exception e) {
            e.printStackTrace();
            return PowerpointResponse.builder().status(ResponseStatus.ERROR).build();
        }
        return PowerpointResponse.builder().build();
    }

    public PowerpointResponse downloadService(String username) {
        try {
            PowerpointEntity pe = this.pptRepository.findTopByUsernameOrderByIdDesc(username);
            System.out.println(pe.getFilename());
            ByteBuffer buffer = this.s3Service.getS3Object(pe.getFilename());
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            return PowerpointResponse.builder().ppt(data).build();
        } catch (Exception e) {
            e.printStackTrace();
            return PowerpointResponse.builder().status(ResponseStatus.ERROR).build();
        }
    }
}
