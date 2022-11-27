package ru.otus.group202205.homework.spring19.zooshop.category.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.group202205.homework.spring19.zooshop.category.model.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

  Page<Category> findAllByName(String name, Pageable pageable);

  @Query("select 1 as result from Category c where exists(select c.id from Category c where id is not null)")
  Long exists();

}
