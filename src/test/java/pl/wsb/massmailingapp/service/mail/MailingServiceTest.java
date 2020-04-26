package pl.wsb.massmailingapp.service.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static pl.wsb.massmailingapp.service.mail.Status.FAILURE;
import static pl.wsb.massmailingapp.service.mail.Status.SUCCESS;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import pl.wsb.massmailingapp.dto.MailFormDTO;
import pl.wsb.massmailingapp.model.Contact;
import pl.wsb.massmailingapp.repository.ContactRepository;

class MailingServiceTest {

  private final ContactRepository contactRepository = mock(ContactRepository.class);
  private final MailSenderService mailSender = mock(MailSenderService.class);

  private final MailingService service = new MailingService(contactRepository, mailSender);

  @Test
  void shouldSenMailing() {
    //given
    final MailFormDTO mailFormDTO = new MailFormDTO("TestSubject", "SomeContent", 2);
    when(contactRepository.findByIdLessThanEqual(2L)).thenReturn(Lists.newArrayList(
        new Contact(10L, "wladek@gmail.com"),
        new Contact(2L, "zenon@wp.pl")));

    //when
    final Status status = service.sendMailing(mailFormDTO);

    //then
    verify(mailSender).sendMail("wladek@gmail.com", "TestSubject", "SomeContent");
    verify(mailSender).sendMail("zenon@wp.pl", "TestSubject", "SomeContent");
    assertEquals(SUCCESS, status);
  }

  @Test
  void shouldFailureWhenProblemWithMailSender() {
    //given
    final MailFormDTO mailFormDTO = new MailFormDTO("TestSubject", "SomeContent", 1);
    when(contactRepository.findByIdLessThanEqual(1L)).thenReturn(Lists.newArrayList(
        new Contact(10L, "wladek@gmail.com")));

    doThrow(new ResponseStatusException(BAD_REQUEST, "Something went wrong!"))
        .when(mailSender).sendMail("wladek@gmail.com", "TestSubject", "SomeContent");

    //when
    final Status status = service.sendMailing(mailFormDTO);

    //then
    assertEquals(FAILURE, status);
  }

  @Test
  void shouldNotSendMailAndReturnStatusSuccessWhenNoContactsInDb() {
    //given
    final MailFormDTO mailFormDTO = new MailFormDTO("TestSubject", "SomeContent", 1);
    when(contactRepository.findByIdLessThanEqual(1L)).thenReturn(Lists.emptyList());

    //when
    final Status status = service.sendMailing(mailFormDTO);

    //then
    verifyNoInteractions(mailSender);
    assertEquals(SUCCESS, status);
  }
}