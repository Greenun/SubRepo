package com.swmaestro.web.user.service;

import com.swmaestro.web.ResponseStatus;
import com.swmaestro.web.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

// AKIAVO6B3BACCOYDKKGH
// IbpkLMlLh7DX2sQ4Bwn6uNBQIWkppZdl/Xu/ljdV

@Service
public class MailService {

    private final WebClient.Builder builder;

    private final AmazonAuth amazonAuth;

    private final String host = "email.ap-northeast-2.amazonaws.com";

    private static final String SmtpHost = "email-smtp.ap-northeast-2.amazonaws.com";

//    static final String ConfigSet = ""; // temp

    static final int PORT = 587;

    @Value("${aws.smtp.username}")
    private String SmtpUsername;

    @Value("${aws.smtp.password}")
    private String SmtpPassword;

//    @Value("${aws.simple.email.service}")
//    private String ses;
    private static final String ResetSubject = "[PROsentation] 임시 비밀번호 발급";


    @Autowired
    public MailService (WebClient.Builder builder, AmazonAuth amazonAuth) {
        this.builder = builder;
        this.amazonAuth = amazonAuth;
    }

    // send reset email or verification email
    public UserDTO.GeneralResponse sendEmail(String email) {

        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        Transport transport = null;

        try {
            MimeMessage msg = this.buildMimeMessage(session, email);
            transport = session.getTransport();
            transport.connect(SmtpHost, SmtpUsername, SmtpPassword);

            // if wanna send email to anyone - aws ses needs to be get out of sandbox state.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("-------------------sent------------------");
            transport.close();
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return UserDTO.GeneralResponse.builder().status(ResponseStatus.ERROR).build();
        } finally {
            return UserDTO.GeneralResponse.builder().status(ResponseStatus.OK).build();
        }
    }

    private MimeMessage buildMimeMessage(Session session, String email)
            throws MessagingException, UnsupportedEncodingException{
        final String from = "maintainer@team-jyb.com";
        final String fromName = "maintainer";

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from, fromName));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        msg.setSubject(ResetSubject);
        msg.setContent(this.getResetContents(email, this.generatePassword()), "text/plain");
        // if config set exists..
        //    msg.setHeader("X-SES-CONFIGURATION-SET", this.ConfigSet);
        return msg;
    }

    // use ses api
//    public UserDTO.GeneralResponse sendMail(String email) {
//        MultiValueMap<String, String> mockData = new LinkedMultiValueMap<>();
//        // mock
//        mockData.add("Action", "SendEmail");
//        mockData.add("Source", "maintainer@team-jyb.com");
//        mockData.add("Destination.ToAddresses.member.1", email);
//        mockData.add("Message.Subject.Data", "TEST Subject 123");
//        mockData.add("Message.Body.Text.Data", "Hello Test Body Message 193854320;");
//        try {
//            Map<String, String> header = amazonAuth.getRequestHeader(host, this.ses, "GET", mockData);
//            // retrieve - get body
//            Mono<String> mono = WebClient.create("")
//                    .get()
//                    .uri(uriBuilder ->
//                            uriBuilder
//                                    .scheme("https")
//                                    .host(host)
//                                    .queryParams(mockData)
//                                    .build()
//                    ).headers(httpHeaders -> {
//                        header.keySet().forEach(key -> {
//                            httpHeaders.add(key, header.get(key));
//                        });
//                    })
//                    .retrieve()
//                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
//                        return clientResponse.bodyToMono(String.class)
//                                .flatMap(errorBody -> {
//                                    return Mono.error(new RuntimeException(errorBody));
//                                });
//                    }) // error
//                    .bodyToMono(String.class);
//            mono.subscribe(result -> {
//                System.out.println(result);
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return UserDTO.GeneralResponse.builder().status(ResponseStatus.OK).build();
//    }

    private String generatePassword() {
        // generate temporary password
        // set to mail body
        return "";
    }

    private String getResetContents(String username, String randomPassword) {
        if (username == null) {
            return "임시비밀번호를 발급합니다" + "\n" + "유출되지 않도록 유의해주시기 바랍니다" + "\n\n"
            + randomPassword;
        }
        return username + " 님의 임시비밀번호를 발급합니다." + "\n" +
                "유출되지 않도록 유의해주시기 바랍니다" + "\n\n"
                + randomPassword;
    }
}
