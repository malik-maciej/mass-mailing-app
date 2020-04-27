package pl.wsb.massmailingapp.service.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.wsb.massmailingapp.model.AppUser;
import pl.wsb.massmailingapp.repository.AppUserRepository;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    private final AppUserRepository repository = mock(AppUserRepository.class);
    private final UserDetailsService service = new UserDetailsServiceImpl(repository);

    @Test
    void shouldLoadUserByUsername() {
        //given
        final AppUser appUser = new AppUser();
        appUser.setPassword("xyz");
        appUser.setRole("ADMIN");
        appUser.setUsername("admin");

        when(repository.findByUsername("someUser")).thenReturn(Optional.of(appUser));

        //when
        final UserDetails user = service.loadUserByUsername("someUser");

        //then
        assertEquals(appUser, user);
    }

    @Test
    void shouldThrowExceptionWhenNoUserInDb() {
        //given
        when(repository.findByUsername("someUser")).thenReturn(empty());

        //when //then
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.loadUserByUsername("someUser"));

        assertEquals("Invalid username", exception.getMessage());
    }
}