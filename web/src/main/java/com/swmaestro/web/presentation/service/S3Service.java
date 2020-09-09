package com.swmaestro.web.presentation.service;

import com.swmaestro.web.auth.AmazonCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class S3Service {
    private final String bucketName = "presentation-storage";

    private final AmazonCredentials awsCredentials;

    @Autowired
    public S3Service(AmazonCredentials awsCredentials) {
        this.awsCredentials = awsCredentials;
    }
    // key: s3 object key
    //S3AsyncClient s3,
    public ByteBuffer getS3Object(String key) {
        // implemented as sync
        S3Client s3 = S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(this.awsCredentials.getCredentials()))
                .build();

//        S3AsyncClient s3 = S3AsyncClient.builder()
//                .region(Region.AP_NORTHEAST_2)
//                .credentialsProvider(StaticCredentialsProvider.create(this.awsCredentials.getCredentials()))
//                .build();

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(this.bucketName)
                .key(key)
                .build();
        ResponseBytes<GetObjectResponse> resp = s3.getObject(request, ResponseTransformer.toBytes());
        return resp.asByteBuffer();

//        CompletableFuture<ResponseBytes<GetObjectResponse>> future
//                = s3.getObject(request, AsyncResponseTransformer.toBytes());
//        future.whenComplete((resp, err) -> {
//           try {
//               if (resp == null) {
//                   err.printStackTrace();
//               } else {
//                   System.out.println(resp.toString());
//               }
//           } finally {
//               s3.close();
//           }
//        });
//        GetObjectResponse resp = future.join().response();

    }

    // key: object key for save
    public void putS3Object(String key, byte[] data) {
        S3AsyncClient s3 = S3AsyncClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(this.awsCredentials.getCredentials()))
                .build();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(this.bucketName)
                .key(key)
                .build();
        CompletableFuture<PutObjectResponse> future
                = s3.putObject(request, AsyncRequestBody.fromBytes(data));
        future.whenComplete((resp, err) -> {
            try {
                if (resp != null) {
                    System.out.println(resp.toString());
                } else {
                    err.printStackTrace();
                }
            } finally {
                s3.close();
            }
        });
    }

    public void putMultipartS3Object(String key, byte[] data) {

    }

    public void deleteS3Object(String key) {

    }

    public String generateFileKey(String filepath) {
        return filepath + "_" + (new SimpleDateFormat("yyyymmddSSS")).format(new Date());
    }
}
