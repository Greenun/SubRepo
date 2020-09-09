package com.swmaestro.web.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.*;

@Component
public class AmazonCredentials {

    @Value("${aws.access.key.id:AKIAVO6B3BACCOYDKKGH}")
    private String awsId;

    @Value("${aws.access.key.secret:IbpkLMlLh7DX2sQ4Bwn6uNBQIWkppZdl/Xu/ljdV}")
    private String awsSecret;

    public AwsBasicCredentials getCredentials() {
        return AwsBasicCredentials.create(this.awsId, this.awsSecret);
    }

}
