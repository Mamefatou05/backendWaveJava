package com.backendwave.services.impl;

import com.backendwave.services.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class SmsServiceImpl implements SmsService {

     @Value("${twilio.account_sid}")
     private String accountSid;

     @Value("${twilio.auth_token}")
     private String authToken;

     @Value("${twilio.phone_number}")
     private String fromPhoneNumber;

     // Initialisation de Twilio
     @PostConstruct
     public void init() {
          Twilio.init(accountSid, authToken);
     }

     @Override
     public void sendSms(String phoneNumber, String message) {
          try {
               Message.creator(
                       new PhoneNumber(phoneNumber), // Numéro de destination
                       new PhoneNumber(fromPhoneNumber), // Numéro Twilio
                       message // Contenu du message
               ).create();

               System.out.println("Message envoyé avec succès à " + phoneNumber);
          } catch (Exception e) {
               System.err.println("Erreur lors de l'envoi du message: " + e.getMessage());
          }
     }
}
