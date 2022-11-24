package ru.otus.group202205.homework.spring12.web.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring12.dao.UserRepository;
import ru.otus.group202205.homework.spring12.model.Role;
import ru.otus.group202205.homework.spring12.model.User;
import ru.otus.group202205.homework.spring12.model.UserRole;

@Service
@RequiredArgsConstructor
public class UserDetailServiceJpa implements UserDetailsService {

  private final UserRepository userRepository;

  @Transactional
  @Override
  public UserDetails loadUserByUsername(String username) throws
      UsernameNotFoundException {
    Optional<User> user = userRepository.findByLogin(username);
    User loadedUser = user.orElseThrow(() -> new UsernameNotFoundException(String.format("user with login %s not found!",
        username)));
    List<Role> authorities = loadedUser
        .getUserRoles()
        .stream()
        .map(UserRole::getRole)
        .collect(Collectors.toList());
    return new org.springframework.security.core.userdetails.User(loadedUser.getLogin(),
        loadedUser.getPassword(),
        authorities
    );
  }

}
