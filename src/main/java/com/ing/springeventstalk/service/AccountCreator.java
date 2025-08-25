package com.ing.springeventstalk.service;

import com.ing.springeventstalk.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCreator {

    private final AuditLogger auditLogger;
    private final ConfirmationEmailSender confirmationEmailSender;
    private final CrossSellingAnalyser crossSellingAnalyser;
    private final PushNotificationSender pushNotificationSender;
    private final WelcomePackGenerator welcomePackGenerator;

    public void create(Account account){

        //***
        // HERE THE REAL ACCOUNT CREATION STUFF
        //***

        auditLogger.log(account);

        confirmationEmailSender.send(account);

        crossSellingAnalyser.send(account);

        pushNotificationSender.send(account);

        welcomePackGenerator.generate(account);

        // FUTURE FEATURES?

    }
}
