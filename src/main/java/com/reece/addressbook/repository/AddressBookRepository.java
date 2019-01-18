package com.reece.addressbook.repository;

import com.reece.addressbook.domain.AddressBook;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the AddressBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {

    @Query("select address_book from AddressBook address_book where address_book.user.login = ?#{principal.username}")
    List<AddressBook> findByUserIsCurrentUser();

}
