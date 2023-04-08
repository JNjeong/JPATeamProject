package com.lec.spring.service;

import com.lec.spring.domain.Book;
import com.lec.spring.repository.UserRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Random;

@Service
public class RegisterMail implements MailServiceInter {

    @Autowired
    JavaMailSender emailsender; // Bean 등록해둔 MailConfig 를 emailsender 라는 이름으로 autowired

    private String ePw; // 인증번호

    private long bookId;

    private String name;

    private String phoneNumber;

    private String dp_name;

    private LocalDate visitDate;


    @Autowired
    private UserRepository userRepository;

    public void sendMail(String to, long bookId, String name, String phoneNumber, String dp_name, LocalDate visitDate){
        this.bookId = bookId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dp_name = dp_name;
        this.visitDate = visitDate;

        try {
            sendSimpleMessage(to);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // 메일 내용 작성
    @Override
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
		System.out.println("보내는 대상 : " + to);
		System.out.println("인증 번호 : " + ePw);

        MimeMessage message = emailsender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);    // 보내는 대상
        message.setSubject("미술랭 예약증");  // 제목

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h3> <미술랭 예약증> </h3>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h4>예약번호 : " + bookId + "</h4>";
        msgg += "<h4>예약자명 : " + name + "</h4>";
        msgg += "<h4>예약자 전화번호 : " + phoneNumber + "</h4>";
        msgg += "<h4>전시명 : " + dp_name + "</h4>";
        msgg += "<h4>전시관람일 : " + visitDate + "</h4>";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("yiyap11@nate.com", "Admin"));// 보내는 사람

        return message;
    }

    // 랜덤 인증 코드 전송
    @Override
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) {
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }


    // 메일 발송
    // sendSimpleMessage 의 매개변수로 들어온 to 는 곧 이메일 주소가 되고,
    // MimeMessage 객체 안에 내가 전송할 메일의 내용을 담는다.
    // 그리고 bean 으로 등록해둔 javaMail 객체를 사용해서 이메일 send
    @Override
    public String sendSimpleMessage(String to) throws Exception {

        ePw = createKey(); // 랜덤 인증번호 생성

        MimeMessage message = createMessage(to); // 메일 발송
        try {// 예외처리
            emailsender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }


        return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
    }
}
