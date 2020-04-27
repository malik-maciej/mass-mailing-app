package pl.wsb.massmailingapp.service.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.wsb.massmailingapp.model.AppUser;
import pl.wsb.massmailingapp.repository.AppUserRepository;

import static org.mockito.Mockito.*;

class UserServiceTest {

    private final AppUserRepository repository = mock(AppUserRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);

    private final UserService service = new UserService(repository, encoder);

    @Test
    void shouldAddAdminToDb() {
        //when
        final AppUser appUser = new AppUser();
        appUser.setPassword("xyz");
        appUser.setRole("ADMIN");
        appUser.setUsername("admin");

        when(encoder.encode(("password"))).thenReturn("xyz");

        //when
        service.addAdminToDb();

        //then
        verify(repository).save(appUser);
    }
}