package com.swmaestro.web.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.*;

@Component
public class AmazonCredentials {

    @Value("${aws.access.key.id}")
    private String awsId;

    @Value("${aws.access.key.secret}")
    private String awsSecret;

    public AwsBasicCredentials getCredentials() {
        return AwsBasicCredentials.create(this.awsId, this.awsSecret);
    }

}
