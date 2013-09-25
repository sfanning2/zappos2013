package test.helpers;

import javax.mail.MessagingException;

import org.junit.Test;

import helpers.EmailClient;

public class WebClientTest {
	
	String host = "smtp.gmail.com";
	String user = "2013zapposinternshipchallenge@gmail.com";
	String password = "Plaintextissupersecure";
	
	String[] recipients = {"fanningsarahe@gmail.com"};
	
	String subject = "Sample Subject";
	String content = "Sample content.";
	
	// Send email
	@Test
	public void sendEmail() throws MessagingException{
		EmailClient client = new EmailClient(host, user, password);
		client.sendEmail(recipients, subject, content);
	}
}
