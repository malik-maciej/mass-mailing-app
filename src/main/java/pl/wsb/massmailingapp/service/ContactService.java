package pl.wsb.massmailingapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pl.wsb.massmailingapp.model.Contact;
import pl.wsb.massmailingapp.repository.ContactRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
class ContactService {

    private final Logger logger = LoggerFactory.getLogger(ContactService.class);

    private ContactRepository contactRepository;
    private ResourceLoader resourceLoader;

    ContactService(ContactRepository contactRepository, ResourceLoader resourceLoader) {
        this.contactRepository = contactRepository;
        this.resourceLoader = resourceLoader;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void addContactsFromFileToDb() {
        logger.info("Saving contacts from file to database...");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                resourceLoader.getResource("classpath:mailList.txt").getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                saveContact(line);
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    private void saveContact(String mailAddress) {
        Contact contact = new Contact();
        contact.setMailAddress(mailAddress);
        contactRepository.save(contact);
    }
}