package es.sidelab;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailRestController {

	
	@RequestMapping(value = "/registro_nuevo", method = RequestMethod.POST)
	public ResponseEntity<String> sendEmail(@RequestParam("email") String email,@RequestParam("subject") String subject, @RequestParam("body") String body){
		
		if(!isValidEmailAddress(email)){
			
			return new ResponseEntity<>("La dirección de email no es válida",HttpStatus.BAD_REQUEST);
			
		}
		
	 	final String username = "vitualcoach@gmail.com";
        final String password = "urjc1995";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("vitualcoach@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Ha habido un error al enviar",HttpStatus.INTERNAL_SERVER_ERROR);
        }
	
	
        return new ResponseEntity<>("Mensaje enviado correctamente",HttpStatus.ACCEPTED);
	}
	
	public static boolean isValidEmailAddress(String email) {
	       boolean result = true;
	       try {
	          InternetAddress emailAddr = new InternetAddress(email);
	          emailAddr.validate();
	       } catch (AddressException ex) {
	          result = false;
	       }
	       return result;
	}


}
