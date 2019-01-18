package com.reece.addressbook.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.reece.addressbook.domain.AddressBook;
import com.reece.addressbook.service.AddressBookService;
import com.reece.addressbook.web.rest.errors.BadRequestAlertException;
import com.reece.addressbook.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AddressBook.
 */
@RestController
@RequestMapping("/api")
public class AddressBookResource {

    private final Logger log = LoggerFactory.getLogger(AddressBookResource.class);

    private static final String ENTITY_NAME = "addressBook";

    private final AddressBookService addressBookService;

    public AddressBookResource(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    /**
     * POST  /address-books : Create a new addressBook.
     *
     * @param addressBook the addressBook to create
     * @return the ResponseEntity with status 201 (Created) and with body the new addressBook, or with status 400 (Bad Request) if the addressBook has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/address-books")
    @Timed
    public ResponseEntity<AddressBook> createAddressBook(@Valid @RequestBody AddressBook addressBook) throws URISyntaxException {
        log.debug("REST request to save AddressBook : {}", addressBook);
        if (addressBook.getId() != null) {
            throw new BadRequestAlertException("A new addressBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AddressBook result = addressBookService.save(addressBook);
        return ResponseEntity.created(new URI("/api/address-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /address-books : Updates an existing addressBook.
     *
     * @param addressBook the addressBook to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated addressBook,
     * or with status 400 (Bad Request) if the addressBook is not valid,
     * or with status 500 (Internal Server Error) if the addressBook couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/address-books")
    @Timed
    public ResponseEntity<AddressBook> updateAddressBook(@Valid @RequestBody AddressBook addressBook) throws URISyntaxException {
        log.debug("REST request to update AddressBook : {}", addressBook);
        if (addressBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AddressBook result = addressBookService.save(addressBook);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, addressBook.getId().toString()))
            .body(result);
    }

    /**
     * GET  /address-books : get all the addressBooks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of addressBooks in body
     */
    @GetMapping("/address-books")
    @Timed
    public List<AddressBook> getAllAddressBooks() {
        log.debug("REST request to get all AddressBooks");
        return addressBookService.findAll();
    }

    /**
     * GET  /address-books/:id : get the "id" addressBook.
     *
     * @param id the id of the addressBook to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the addressBook, or with status 404 (Not Found)
     */
    @GetMapping("/address-books/{id}")
    @Timed
    public ResponseEntity<AddressBook> getAddressBook(@PathVariable Long id) {
        log.debug("REST request to get AddressBook : {}", id);
        Optional<AddressBook> addressBook = addressBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(addressBook);
    }

    /**
     * DELETE  /address-books/:id : delete the "id" addressBook.
     *
     * @param id the id of the addressBook to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/address-books/{id}")
    @Timed
    public ResponseEntity<Void> deleteAddressBook(@PathVariable Long id) {
        log.debug("REST request to delete AddressBook : {}", id);
        addressBookService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
