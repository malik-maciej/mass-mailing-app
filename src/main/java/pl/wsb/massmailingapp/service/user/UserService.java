package pl.wsb.massmailingapp.service.user;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wsb.massmailingapp.model.AppUser;
import pl.wsb.massmailingapp.repository.AppUserRepository;

@Service
class UserService {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void addAdminToDb() {
        AppUser user = new AppUser();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode(("password")));
        user.setRole("ADMIN");
        appUserRepository.save(user);
    }
}