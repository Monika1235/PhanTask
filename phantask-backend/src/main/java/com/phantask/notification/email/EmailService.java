package com.phantask.notification.email;

import java.time.LocalDateTime;

public interface EmailService {

	void sendAccountCreationEmail(String toEmail, String username, String tempPassword);

	void sendEarlyCheckoutAlert(String managerEmail, String employeeName, LocalDateTime checkIn, LocalDateTime checkOut,
			long workedMinutes);

}
