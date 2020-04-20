package pl.wsb.massmailingapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.wsb.massmailingapp.model.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {

    Iterable<Contact> findByIdLessThanEqual(Long count);
}