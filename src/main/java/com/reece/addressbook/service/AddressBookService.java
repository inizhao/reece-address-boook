package com.reece.addressbook.service;

import com.reece.addressbook.domain.AddressBook;
import com.reece.addressbook.repository.AddressBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing AddressBook.
 */
@Service
@Transactional
public class AddressBookService {

    private final Logger log = LoggerFactory.getLogger(AddressBookService.class);

    private final AddressBookRepository addressBookRepository;

    public AddressBookService(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    /**
     * Save a addressBook.
     *
     * @param addressBook the entity to save
     * @return the persisted entity
     */
    public AddressBook save(AddressBook addressBook) {
        log.debug("Request to save AddressBook : {}", addressBook);
        return addressBookRepository.save(addressBook);
    }

    /**
     * Get all the addressBooks.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AddressBook> findAll() {
        log.debug("Request to get all AddressBooks");
        return addressBookRepository.findAll();
    }


    /**
     * Get one addressBook by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AddressBook> findOne(Long id) {
        log.debug("Request to get AddressBook : {}", id);
        return addressBookRepository.findById(id);
    }

    /**
     * Delete the addressBook by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AddressBook : {}", id);
        addressBookRepository.deleteById(id);
    }
}
