package com.reece.addressbook.service;

import com.reece.addressbook.domain.AddressBook;
import com.reece.addressbook.domain.Contact;
import com.reece.addressbook.repository.AddressBookRepository;
import com.reece.addressbook.repository.ContactRepository;
import com.reece.addressbook.service.dto.ContactDTO;
import com.reece.addressbook.web.rest.errors.BadRequestAlertException;
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
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Contact.
 */
@Service
@Transactional
public class ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;
    private final AddressBookRepository addressBookRepository;

    public ContactService(ContactRepository contactRepository, AddressBookRepository addressBookRepository) {
        this.contactRepository = contactRepository;
        this.addressBookRepository = addressBookRepository;
    }

    /**
     * Save a contact.
     *
     * @param contactDto the entity to save
     * @return the persisted entity
     */
    public Contact save(ContactDTO contactDto) {
        log.debug("Request to save Contact : {}", contactDto);
        Optional<AddressBook> addressBookOptional = addressBookRepository.findById(contactDto.getAddressBookId());
        if (!addressBookOptional.isPresent()) {
            throw new BadRequestAlertException("Address Book doesn't exist.", "Address Book ID", " addressBookNotExists");
        }

        if (contactDto.getId() == null) {
            Contact contact = new Contact()
                .addressBook(addressBookOptional.get())
                .name(contactDto.getName())
                .phone(contactDto.getPhone());

            return contactRepository.save(contact);
        }

        Optional<Contact> contactOptional = contactRepository.findById(contactDto.getId());
        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get()
                .phone(contactDto.getPhone())
                .name(contactDto.getName());
            return contactRepository.save(contact);
        } else {
            throw new BadRequestAlertException("Invalid Contact ID.", "Contact ID", "invalidContactID");
        }
    }

    @Transactional(readOnly = true)
    public List<Contact> findAllUniqueContactsByAddressBook(List<Long> addressBookIds, Boolean unique) {
        List<Contact> contactList =
            addressBookIds.stream().map(
                addressBookId -> {
                    return contactRepository.findAllByAddressBook_Id(addressBookId);
                }
            )
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if(unique) {
            List<Contact> distinctContacts = contactList.stream().filter(distinctByKey(p -> p.getPhone()))
                .collect(Collectors.toList());
            return distinctContacts;
        }
        return contactList;
    }

    @Transactional(readOnly = true)
    public List<Contact> findAllContactsByAddressBook(Long addressBookId) {
        return contactRepository.findAllByAddressBook_Id(addressBookId);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Get all the contacts.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Contact> findAll() {
        log.debug("Request to get all Contacts");
        return contactRepository.findAll();
    }


    /**
     * Get one contact by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Contact> findOne(Long id) {
        log.debug("Request to get Contact : {}", id);
        return contactRepository.findById(id);
    }

    /**
     * Delete the contact by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Contact : {}", id);
        contactRepository.deleteById(id);
    }
}
