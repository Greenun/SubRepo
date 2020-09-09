package com.swmaestro.web.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AmazonAuth {
    @Value("${aws.access.key.id}")
    private String awsId;

    @Value("${aws.access.key.secret}")
    private String awsSecret;

    private String algorithm = "HmacSHA256";

    @Value("${aws.region.name}")
    private String region;

    private final String apiScheme = "https";

    private final String signedHeader = "host;x-amz-date";

    protected byte[] sign(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance(this.algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(msg.getBytes("UTF-8"));
    }

    private byte[] getSignatureKey(String date, String regionName, String service) throws Exception {
        byte[] eKey = ("AWS4" + awsSecret).getBytes("UTF-8");
        byte[] eDate = sign(eKey, date);
        byte[] eRegion = sign(eDate, regionName);
        byte[] eService = sign(eRegion, service);
        byte[] kSign = sign(eService, "aws4_request");
        return kSign;
    }

    public byte[] getSignature(String date, String region, String service,
                               String credentialScope, String canonicalRequest) {
        String signAlgorithm = "AWS4-HMAC-SHA256";
        String beforeSign = signAlgorithm + "\n" + date + "\n" + credentialScope + "\n" + canonicalRequest;
        try {
            byte[] signKey = getSignatureKey(date, region, service);
            byte[] signature = sign(signKey, beforeSign);
            return signature;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, String> getRequestHeader
            (String host, String service, String method, MultiValueMap params) throws Exception{
        LocalDateTime now = LocalDateTime.now();
        String awsDate = now.format(DateTimeFormatter.ofPattern("%Y%m%d'T'%H%M%S'Z'"));
        String timestamp = now.format(DateTimeFormatter.ofPattern("%Y%m%d"));
        Map<String, String> header = new HashMap<>();

        String credentialScope = timestamp + "/" + region + "/" + service + "/" + "aws4_request";
        String signature = getSignature(awsDate, region, service,
                credentialScope, this.getCanonicalRequest(host, method, awsDate, this.getURI(host, params))).toString();
        String authHeader = "AWS4-HMAC-SHA256 " + "Credential=" + this.awsId + "/" + credentialScope + ", " +
                "SignedHeaders=" + signedHeader + ", " + "Signature=" + signature;
        // add headers
        header.put("x-amz-date", awsDate);
        header.put("X-Amzn-Authorization", authHeader);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        return header;
    }

    private UriComponents getURI(String host, MultiValueMap params) {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme(this.apiScheme)
                .host(host)
                .queryParams(params)
                .build();
        return uri;
    }

    private String getCanonicalRequest(String host, String method, String date, UriComponents uri)
    throws Exception {
        String path = "/"; // 임시로 지정중
        String cHeader = "host:" + host + "\n" + "x-amz-date:" + date + "\n";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update("".getBytes("UTF-8"));
        return method + "\n" + path + "\n" + uri.getQuery() + "\n" + cHeader + "\n" + signedHeader
                +"\n" + md.digest().toString();
    }
}
