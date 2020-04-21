package pl.wsb.massmailingapp.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wsb.massmailingapp.model.AppUser;
import pl.wsb.massmailingapp.repository.AppUserRepository;

@Service
class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addAdminToDb() {
        AppUser user = new AppUser();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode(("password")));
        user.setRole("ADMIN");
        appUserRepository.save(user);
        logger.info("Created user - " + user.getUsername());
    }
}