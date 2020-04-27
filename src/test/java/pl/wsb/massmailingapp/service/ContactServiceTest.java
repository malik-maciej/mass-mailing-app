package pl.wsb.massmailingapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.core.io.ResourceLoader;
import pl.wsb.massmailingapp.model.Contact;
import pl.wsb.massmailingapp.repository.ContactRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.Paths.get;
import static java.util.Objects.requireNonNull;
import static org.mockito.Mockito.*;

class ContactServiceTest {

    private final ContactRepository contactRepository = mock(ContactRepository.class);
    private final ResourceLoader resourceLoader = new AnnotationConfigServletWebServerApplicationContext();

    private final ContactService service = new ContactService(contactRepository, resourceLoader);

    @Test
    void shouldAddContactsFromFileToDb() throws IOException {
        //given
        final List<String> mailLists =
                Files.readAllLines(get(new File(
                        requireNonNull(getClass().getClassLoader().getResource("mailList.txt")
                        ).getFile())
                        .getPath()));

        //when
        service.addContactsFromFileToDb();

        //then
        mailLists.forEach(email -> {
            final Contact contact = new Contact();
            contact.setMailAddress(email);
            verify(contactRepository, atLeastOnce()).save(contact);
        });
    }
}