package ru.otus.group202205.homework.spring19.zooshop.address.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.group202205.homework.spring19.zooshop.address.model.Address;

public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {

  @Query("select a from Address a where "
      + "a.postalCode like :text or "
      + "a.country like :text or "
      + "a.state like :text or "
      + "a.city like :text or "
      + "a.district like :text or "
      + "a.street like :text or "
      + "a.house like :text or "
      + "a.specificPart like :text")
  Page<Address> findAllBySomeText(@Param("text") String text, Pageable pageable);

  @Query("select 1 as result from Address a where exists(select a.id from Address a where id is not null)")
  Long exists();

}
