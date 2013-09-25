package helpers;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * 
 *
 */
public class EmailClient {

	private String host;
	private String from;
	private String fromPassword;
	private Properties properties;
	private Session session;
	
	public EmailClient(String host, String from, String fromPassword) {
		this.host = host;
		this.from = from;
		this.fromPassword = fromPassword;
		properties = System.getProperties();
		properties.put("mail.smtp.starttls.enable", "true"); 
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.user", from);
		properties.put("mail.smtp.password", fromPassword);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		session = Session.getDefaultInstance(properties, null);
	}

	public void sendEmail (String[] to, String subject, String content) throws MessagingException {
		MimeMessage message = new MimeMessage(session);


		message.setFrom(new InternetAddress(from));

		InternetAddress[] toAddress = new InternetAddress[to.length];

		// To get the array of addresses
		for( int i=0; i < to.length; i++ ) {
			toAddress[i] = new InternetAddress(to[i]);
		}

		// TODO: Remove printlns
		// TODO: checkout GmailMessage
		System.out.println(Message.RecipientType.TO);

		for( int i=0; i < toAddress.length; i++) {
			message.addRecipient(Message.RecipientType.TO, toAddress[i]);
		}
		message.setSubject(subject);
		message.setText(content);
		Transport transport = session.getTransport("smtp");
		transport.connect(host, from, fromPassword);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		properties.put("mail.smtp.host", host);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
		properties.put("mail.smtp.user", from);
	}

	public String getFromPassword() {
		return fromPassword;
	}

	public void setFromPassword(String fromPassword) {
		this.fromPassword = fromPassword;
		properties.put("mail.smtp.password", fromPassword);
	}
}
