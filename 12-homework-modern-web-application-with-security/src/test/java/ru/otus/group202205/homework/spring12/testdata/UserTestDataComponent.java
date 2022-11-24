package ru.otus.group202205.homework.spring12.testdata;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.otus.group202205.homework.spring12.model.Role;
import ru.otus.group202205.homework.spring12.model.User;
import ru.otus.group202205.homework.spring12.model.UserRole;

@Component
public class UserTestDataComponent {

  public User getAdminUser() {
    User result = new User();
    result.setId(1L);
    result.setLogin("admin");
    result.setPassword("admin");
    UserRole userRole = new UserRole();
    userRole.setUser(result);
    userRole.setId(1L);
    Role role = new Role();
    role.setId(1L);
    role.setName("admin");
    userRole.setRole(role);
    result.setUserRoles(List.of(userRole));
    return result;
  }

  public User getUserUser() {
    User result = new User();
    result.setId(2L);
    result.setLogin("user");
    result.setPassword("user");
    UserRole userRole = new UserRole();
    userRole.setUser(result);
    userRole.setId(2L);
    Role role = new Role();
    role.setId(2L);
    role.setName("user");
    userRole.setRole(role);
    result.setUserRoles(List.of(userRole));
    return result;
  }

}
