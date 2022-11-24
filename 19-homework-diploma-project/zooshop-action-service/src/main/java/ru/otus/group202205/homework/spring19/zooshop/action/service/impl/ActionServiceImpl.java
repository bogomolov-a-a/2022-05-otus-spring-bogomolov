package ru.otus.group202205.homework.spring19.zooshop.action.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.group202205.homework.spring19.zooshop.action.dao.ActionRepository;
import ru.otus.group202205.homework.spring19.zooshop.action.dto.ActionDto;
import ru.otus.group202205.homework.spring19.zooshop.action.feign.GoodServiceFeignProxy;
import ru.otus.group202205.homework.spring19.zooshop.action.mapper.ActionMapper;
import ru.otus.group202205.homework.spring19.zooshop.action.service.ActionService;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {

  private final ActionRepository actionRepository;
  private final ActionMapper actionMapper;
  private final GoodServiceFeignProxy goodServiceFeignProxy;

  @Override
  public List<ActionDto> findAll(Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return actionRepository
        .findAll(pageable)
        .stream()
        .map(actionMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public ActionDto findById(Long id) {
    return actionMapper.toDto(actionRepository
        .findById(id)
        .orElseThrow());
  }

  @Override
  public List<ActionDto> findAllByName(String name, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return actionRepository
        .findAllByName(name,
            pageable)
        .stream()
        .map(actionMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<ActionDto> findAllByGoodId(Long goodId, Pageable pageable, Sort sort) {
    pageable
        .getSort()
        .and(sort);
    return actionRepository
        .findAllByGoodId(goodId,
            pageable)
        .stream()
        .map(actionMapper::toDto)
        .collect(Collectors.toList());
  }


  @Override
  public ActionDto save(ActionDto action) {
    goodServiceFeignProxy.existsById(action.getGoodId());
    return actionMapper.toDto(actionRepository.save(actionMapper.toEntity(action)));
  }

  @Override
  public void deleteById(Long id) {
    //goodServiceFeignProxy.deleteAllByCategory(id);
    actionRepository.deleteById(id);
  }

  @Override
  public void deleteAll() {
    actionRepository
        .findAll()
        .forEach(address -> this.deleteById(address.getId()));
  }

  @Override
  public boolean existsById(Long id) {
    return actionRepository.existsById(id);
  }

}
