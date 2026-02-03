package com.phantask.notification.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendAccountCreationEmail(String toEmail, String username, String tempPassword) {

    	log.info("Sending account creation mail to {}", toEmail);
    	
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Phantask Account Has Been Created");

        message.setText("""
                Hello,

                Your account has been successfully created.

                Username: %s
                Temporary Password: %s

                Please re-login and change your password to gohead.

                Regards,
                Phantask Team
                """.formatted(username, tempPassword));

        mailSender.send(message);

        log.info("Account creation email sent to {}", toEmail);
    }
    
    @Override
    public void sendEarlyCheckoutAlert(
            String managerEmail,
            String employeeName,
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            long workedMinutes) {

        long hours = workedMinutes / 60;
        long minutes = workedMinutes % 60;

        log.info("Sending early checkout alert for {} to {}", employeeName, managerEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(managerEmail);
        message.setSubject("Early Checkout Alert – " + employeeName);

        message.setText("""
                Hello,

                This is to inform you that %s has checked out early today.

                Check-in Time : %s
                Check-out Time: %s
                Worked Duration: %dh %dm
                Required Duration: 8h

                Please review this attendance entry if required.

                Regards,
                Phantask Team
                """.formatted(
                employeeName,
                checkIn,
                checkOut,
                hours,
                minutes
        ));

        mailSender.send(message);
        log.info("Early checkout email sent to {}", managerEmail);
    }

}

