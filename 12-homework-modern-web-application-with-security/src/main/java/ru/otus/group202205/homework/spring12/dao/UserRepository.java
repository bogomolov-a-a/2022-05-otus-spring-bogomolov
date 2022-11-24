package ru.otus.group202205.homework.spring12.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.group202205.homework.spring12.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLogin(String login);

}
