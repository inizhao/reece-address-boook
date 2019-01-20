package com.reece.addressbook.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.reece.addressbook.domain.Contact;
import com.reece.addressbook.service.ContactService;
import com.reece.addressbook.service.dto.ContactDTO;
import com.reece.addressbook.web.rest.errors.BadRequestAlertException;
import com.reece.addressbook.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Contact.
 */
@RestController
@RequestMapping("/api")
public class ContactResource {

    private final Logger log = LoggerFactory.getLogger(ContactResource.class);

    private static final String ENTITY_NAME = "contact";

    private final ContactService contactService;

    public ContactResource(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * POST  /contacts : Create a new contact.
     *
     * @param contactDto the contact to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contact, or with status 400 (Bad Request) if the contact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contacts")
    @Timed
    public ResponseEntity<Contact> createContact(@Valid @RequestBody ContactDTO contactDto) throws URISyntaxException {
        log.debug("REST request to save Contact : {}", contactDto);
        if (contactDto.getId() != null) {
            throw new BadRequestAlertException("A new contact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contact result = contactService.save(contactDto);
        return ResponseEntity.created(new URI("/api/contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contacts : Updates an existing contact.
     *
     * @param contactDTO the contact to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contact,
     * or with status 400 (Bad Request) if the contact is not valid,
     * or with status 500 (Internal Server Error) if the contact couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contacts")
    @Timed
    public ResponseEntity<Contact> updateContact(@Valid @RequestBody ContactDTO contactDTO) throws URISyntaxException {
        log.debug("REST request to update Contact : {}", contactDTO);
        if (contactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Contact result = contactService.save(contactDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contacts : get all the contacts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @GetMapping("/contacts")
    @Timed
    public List<Contact> getAllContacts() {
        log.debug("REST request to get all Contacts");
        return contactService.findAll();
    }

    @GetMapping("/contacts/byAddressBookUnique/{addressBookIdsString}")
    @Timed
    public List<Contact> getAllUniqueContactsByAddressBookIds(@PathVariable String addressBookIdsString) {
        List<Long> addressBookIds = Arrays.asList(addressBookIdsString.split(","))
            .stream()
            .map(idString -> {
                return Long.valueOf(idString);
            })
            .collect(Collectors.toList());
        return contactService.findAllUniqueContactsByAddressBook(addressBookIds);
    }

    @GetMapping("/contacts/byAddressBook/{addressBookId}")
    @Timed
    public List<Contact> getAllContactsByAddressBookIds(@PathVariable String addressBookId) {
        return contactService.findAllContactsByAddressBook(Long.valueOf(addressBookId));
    }

    /**
     * GET  /contacts/:id : get the "id" contact.
     *
     * @param id the id of the contact to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contact, or with status 404 (Not Found)
     */
    @GetMapping("/contacts/{id}")
    @Timed
    public ResponseEntity<Contact> getContact(@PathVariable Long id) {
        log.debug("REST request to get Contact : {}", id);
        Optional<Contact> contact = contactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contact);
    }

    /**
     * DELETE  /contacts/:id : delete the "id" contact.
     *
     * @param id the id of the contact to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contacts/{id}")
    @Timed
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.debug("REST request to delete Contact : {}", id);
        contactService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
