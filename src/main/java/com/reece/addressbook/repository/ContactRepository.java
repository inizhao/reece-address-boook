package com.reece.addressbook.repository;

import com.reece.addressbook.domain.Contact;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Contact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    public List<Contact> findAllByAddressBook_Id(Long addressBookId);

}
