package com.reece.addressbook.service;

import com.reece.addressbook.domain.AddressBook;
import com.reece.addressbook.domain.Contact;
import com.reece.addressbook.domain.User;
import com.reece.addressbook.repository.AddressBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Service Implementation for managing AddressBook.
 */
@Service
@Transactional
public class AddressBookService {

    @Autowired
    private UserService userService;

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
        Optional<User> user = userService.getUserWithAuthorities();
        if(user.isPresent()) {
            addressBook.setUser(user.get());
        }
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

    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
