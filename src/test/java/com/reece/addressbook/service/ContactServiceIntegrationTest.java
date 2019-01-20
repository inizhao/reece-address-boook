package com.reece.addressbook.service;

import com.reece.addressbook.ReeceaddressbookApp;
import com.reece.addressbook.domain.Contact;
import com.reece.addressbook.repository.AddressBookRepository;
import com.reece.addressbook.repository.ContactRepository;
import com.reece.addressbook.service.dto.ContactDTO;
import com.reece.addressbook.web.rest.errors.BadRequestAlertException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReeceaddressbookApp.class)
public class ContactServiceIntegrationTest {

    private static String DEFAULT_PHONE = "0411111111";
    private static String DEFAULT_NAME = "forrest gum";

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private AddressBookRepository addressBookRepository;
    @Autowired
    private ContactService contactService;

    @Before
    public void setUp() throws Exception {
    }

    private ContactDTO createContactDto(Long addressBookId) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setAddressBookId(addressBookId);
        contactDTO.setPhone(DEFAULT_PHONE);
        contactDTO.setName(DEFAULT_NAME);
        return contactDTO;
    }

    @Test(expected = BadRequestAlertException.class)
    public void shouldThrowExceptionIfAddressBookNotExist() throws IOException {
        contactService.save(createContactDto(99l));
    }

    @Test
    @DirtiesContext
    public void shouldSaveNewContact() throws IOException {
        int oldContactSize = contactRepository.findAll().size();
        contactService.save(createContactDto(1l));
        int newContactSize = contactRepository.findAll().size();
        assertEquals(newContactSize, 1 + oldContactSize);
    }

    @Test
    @DirtiesContext
    public void shouldGetAllContactInOneAddressBook() throws Exception {
        List<Contact> contactList = contactService.findAllUniqueContactsByAddressBook(Arrays.asList(1l), false);
        assertEquals(contactList.size(), 3);
    }

    @Test
    @DirtiesContext
    public void shouldGetUniqueContactFromMultiAddressBook() throws Exception {
        List<Contact> contactList = contactService.findAllUniqueContactsByAddressBook(Arrays.asList(1l,2l), true);
        assertEquals(contactList.size(), 4);
    }

    @Test
    @DirtiesContext
    public void shouldGetAllContactFromMultiAddressBook() throws Exception {
        List<Contact> contactList = contactService.findAllUniqueContactsByAddressBook(Arrays.asList(1l,2l), false);
        assertEquals(contactList.size(), 6);
    }
}
