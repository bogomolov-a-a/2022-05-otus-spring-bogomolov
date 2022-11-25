package ru.otus.group202205.homework.spring19.zooshop.customer.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.group202205.homework.spring19.zooshop.customer.model.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

  Page<Customer> findAllByAddressId(Long addressId, Pageable pageable);

  @Query("select 1 as result from Customer c where exists(select c.id from Customer c where id is not null)")
  Long exists();

}
