package pl.wsb.massmailingapp.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.wsb.massmailingapp.dto.MailFormDTO;
import pl.wsb.massmailingapp.repository.ContactRepository;

@Service
public class MailingService {

    private final Logger logger = LoggerFactory.getLogger(MailingService.class);

    private ContactRepository contactRepository;
    private MailSenderService mailSender;

    public MailingService(ContactRepository contactRepository, MailSenderService mailSender) {
        this.contactRepository = contactRepository;
        this.mailSender = mailSender;
    }

    public Status sendMailing(MailFormDTO mailForm) {
        long count = mailForm.getCount();
        logger.info("Sending " + count + " mails...");

        try {
            contactRepository.findByIdLessThanEqual(count).forEach(contact -> {
                String recipient = contact.getMailAddress();
                logger.info("Recipient: " + recipient);
                mailSender.sendMail(recipient, mailForm.getSubject(), mailForm.getContent());
            });
        } catch (ResponseStatusException e) {
            logger.error(e.toString());
            return Status.FAILURE;
        }

        return Status.SUCCESS;
    }
}