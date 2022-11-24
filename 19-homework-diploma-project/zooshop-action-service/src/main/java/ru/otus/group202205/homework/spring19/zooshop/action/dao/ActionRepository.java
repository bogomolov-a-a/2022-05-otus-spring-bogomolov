package ru.otus.group202205.homework.spring19.zooshop.action.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.group202205.homework.spring19.zooshop.action.model.Action;

public interface ActionRepository extends PagingAndSortingRepository<Action, Long> {

  Page<Action> findAllByName(String name, Pageable pageable);

  Page<Action> findAllByGoodId(Long goodId, Pageable pageable);

  @Query("select 1 as result from Action c where exists(select ac.id from Action ac where id is not null)")
  Long exists();

}
