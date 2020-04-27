package pl.wsb.massmailingapp.service.mail;

import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class MailSenderServiceTest {

    private final SendGrid sendGrid = Mockito.mock(SendGrid.class);
    private final MailSenderService senderService = new MailSenderService("sender@yahoo.com", sendGrid);

    @SneakyThrows
    @Test
    void shouldSendMail() {
        //given
        when(sendGrid.api(any())).thenReturn(new Response(200, "no-content", new HashMap<>()));
        //when  //then do nothing
        senderService.sendMail("bambo@tlen.pl", "Info subject", "Message content");
    }

    @SneakyThrows
    @Test
    void shouldSendMailWithAcceptedStATUS() {
        //given
        when(sendGrid.api(any())).thenReturn(new Response(202, "no-content", new HashMap<>()));
        //when  //then do nothing
        senderService.sendMail("bambo@tlen.pl", "Info subject", "Message content");
    }

    @SneakyThrows
    @Test
    void shouldThrowExceptionWhenBadResponse() {
        //given
        when(sendGrid.api(any())).thenReturn(new Response(400, "no-content", new HashMap<>()));

        //when  //then throw exception
        final ResponseStatusException statusException = assertThrows(
                ResponseStatusException.class,
                () -> senderService.sendMail("bambo@tlen.pl", "Info subject", "Message content"));

        assertEquals(BAD_REQUEST, statusException.getStatus());
    }
}