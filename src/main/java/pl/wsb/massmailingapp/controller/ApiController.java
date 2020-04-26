package pl.wsb.massmailingapp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.wsb.massmailingapp.dto.MailFormDTO;
import pl.wsb.massmailingapp.service.mail.MailingService;

@RestController
class ApiController {

    private final MailingService mailingService;

    ApiController(MailingService mailingService) {
        this.mailingService = mailingService;
    }

    @PostMapping("/api")
    String sendMailing(@RequestBody MailFormDTO mailForm) {
        return mailingService.sendMailing(mailForm).toString();
    }
}