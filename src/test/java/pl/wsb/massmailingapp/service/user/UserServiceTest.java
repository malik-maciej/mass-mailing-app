package pl.wsb.massmailingapp.service.user;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.wsb.massmailingapp.model.AppUser;
import pl.wsb.massmailingapp.repository.AppUserRepository;

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