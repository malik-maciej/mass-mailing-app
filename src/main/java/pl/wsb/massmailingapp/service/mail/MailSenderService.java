package pl.wsb.massmailingapp.service.mail;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
class MailSenderService {

    @Value("${sender}")
    private String senderEmailAddress;
    @Value("${api-key}")
    private String apiKey;

    private final Logger logger = LoggerFactory.getLogger(MailSenderService.class);

    void sendMail(String recipient, String subject, String msgContent) {
        Mail mail = prepareMail(recipient, subject, msgContent);
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            int httpStatus = response.getStatusCode();
            logger.info("STATUS CODE: " + httpStatus);
            logger.info("HEADERS: " + response.getHeaders());
            logger.info("BODY: " + response.getBody());

            if (httpStatus != HttpStatus.OK.value() && httpStatus != HttpStatus.ACCEPTED.value())
                throw new ResponseStatusException(HttpStatus.valueOf(httpStatus));
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    private Mail prepareMail(String recipient, String subject, String msgContent) {
        Email from = new Email(senderEmailAddress);
        Email to = new Email(recipient);
        Content content = new Content("text/html", msgContent);
        return new Mail(from, subject, to, content);
    }
}