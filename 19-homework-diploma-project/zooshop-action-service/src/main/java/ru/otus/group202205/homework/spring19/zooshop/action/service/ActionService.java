package ru.otus.group202205.homework.spring19.zooshop.action.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.group202205.homework.spring19.zooshop.action.dto.ActionDto;

public interface ActionService {

  List<ActionDto> findAll(Pageable pageable, Sort sort);

  ActionDto findById(Long id);

  List<ActionDto> findAllByName(String name, Pageable pageable, Sort sort);

  List<ActionDto> findAllByGoodId(Long goodId, Pageable pageable, Sort sort);

  ActionDto save(ActionDto category);

  void deleteById(Long id);

  void deleteAll();

  boolean existsById(Long id);

}
