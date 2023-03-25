package com.bilgeadam.service;


import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender javaMailSender;


    public void sendMail(RegisterMailModel model) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("emreust26@gmail.com"); //kimden gideceği
        mailMessage.setTo(model.getEmail()); //kime gideceği
        mailMessage.setSubject("AKTIVASYON KODU");
        mailMessage.setText(model.getUsername()+" adıyla başarılı bir şekilde kayıt oldunuz.\n"+"Aktivasyon kodunuz ...:"+model.getActivationCode());
        javaMailSender.send(mailMessage);
        Scanner sc = new Scanner(System.in);

        int sayi = sc.nextInt();
        String sayi1 = String.valueOf(sayi);
    }
}
