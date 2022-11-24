package ru.otus.group202205.homework.spring13.web.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.group202205.homework.spring13.dao.UserRepository;
import ru.otus.group202205.homework.spring13.testdata.UserTestDataComponent;

@SpringBootTest(classes = {UserDetailServiceJpa.class, UserTestDataComponent.class})
class UserDetailServiceJpaTest {

  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private UserTestDataComponent userTestDataComponent;

  @MockBean
  private UserRepository userRepository;

  @BeforeEach
  void init() {
    Mockito
        .doReturn(Optional.of(userTestDataComponent.getAdminUser()))
        .when(userRepository)
        .findByLogin("admin");
    Mockito
        .doReturn(Optional.of(userTestDataComponent.getUserUser()))
        .when(userRepository)
        .findByLogin("user");
    Mockito
        .doReturn(Optional.empty())
        .when(userRepository)
        .findByLogin("user1");
  }

  @Test
  void shouldBeAuthenticateAdminUser() {
    UserDetails details = userDetailsService.loadUserByUsername("admin");
    assertThat(details)
        .extracting(UserDetails::getUsername)
        .isEqualTo("admin");
    assertThat(details)
        .extracting(UserDetails::getPassword)
        .isEqualTo("admin");
    List<String> authoritiesNameList = details
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    assertThat(authoritiesNameList)
        .isNotEmpty()
        .containsOnly("admin");

  }

  @Test
  void shouldBeAuthenticateUserUser() {
    UserDetails details = userDetailsService.loadUserByUsername("user");
    assertThat(details)
        .extracting(UserDetails::getUsername)
        .isEqualTo("user");
    assertThat(details)
        .extracting(UserDetails::getPassword)
        .isEqualTo("user");
    List<String> authoritiesNameList = details
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    assertThat(authoritiesNameList)
        .isNotEmpty()
        .containsOnly("user");
  }

  @Test
  void shouldBeThrowUserNotFoundException() {
    assertThatCode(() -> userDetailsService.loadUserByUsername("user1")
    )
        .withFailMessage(
            "user with login user1 not found"
        )
        .isInstanceOf(UsernameNotFoundException.class);
  }

}