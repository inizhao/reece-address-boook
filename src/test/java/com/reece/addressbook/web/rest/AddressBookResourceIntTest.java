package com.reece.addressbook.web.rest;

import com.reece.addressbook.ReeceaddressbookApp;

import com.reece.addressbook.domain.AddressBook;
import com.reece.addressbook.repository.AddressBookRepository;
import com.reece.addressbook.service.AddressBookService;
import com.reece.addressbook.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.reece.addressbook.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AddressBookResource REST controller.
 *
 * @see AddressBookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReeceaddressbookApp.class)
public class AddressBookResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAddressBookMockMvc;

    private AddressBook addressBook;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AddressBookResource addressBookResource = new AddressBookResource(addressBookService);
        this.restAddressBookMockMvc = MockMvcBuilders.standaloneSetup(addressBookResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressBook createEntity(EntityManager em) {
        AddressBook addressBook = new AddressBook()
            .name(DEFAULT_NAME);
        return addressBook;
    }

    @Before
    public void initTest() {
        addressBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createAddressBook() throws Exception {
        int databaseSizeBeforeCreate = addressBookRepository.findAll().size();

        // Create the AddressBook
        restAddressBookMockMvc.perform(post("/api/address-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressBook)))
            .andExpect(status().isCreated());

        // Validate the AddressBook in the database
        List<AddressBook> addressBookList = addressBookRepository.findAll();
        assertThat(addressBookList).hasSize(databaseSizeBeforeCreate + 1);
        AddressBook testAddressBook = addressBookList.get(addressBookList.size() - 1);
        assertThat(testAddressBook.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAddressBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = addressBookRepository.findAll().size();

        // Create the AddressBook with an existing ID
        addressBook.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressBookMockMvc.perform(post("/api/address-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressBook)))
            .andExpect(status().isBadRequest());

        // Validate the AddressBook in the database
        List<AddressBook> addressBookList = addressBookRepository.findAll();
        assertThat(addressBookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressBookRepository.findAll().size();
        // set the field null
        addressBook.setName(null);

        // Create the AddressBook, which fails.

        restAddressBookMockMvc.perform(post("/api/address-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressBook)))
            .andExpect(status().isBadRequest());

        List<AddressBook> addressBookList = addressBookRepository.findAll();
        assertThat(addressBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAddressBooks() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);

        // Get all the addressBookList
        restAddressBookMockMvc.perform(get("/api/address-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addressBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getAddressBook() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);

        // Get the addressBook
        restAddressBookMockMvc.perform(get("/api/address-books/{id}", addressBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(addressBook.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAddressBook() throws Exception {
        // Get the addressBook
        restAddressBookMockMvc.perform(get("/api/address-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddressBook() throws Exception {
        // Initialize the database
        addressBookService.save(addressBook);

        int databaseSizeBeforeUpdate = addressBookRepository.findAll().size();

        // Update the addressBook
        AddressBook updatedAddressBook = addressBookRepository.findById(addressBook.getId()).get();
        // Disconnect from session so that the updates on updatedAddressBook are not directly saved in db
        em.detach(updatedAddressBook);
        updatedAddressBook
            .name(UPDATED_NAME);

        restAddressBookMockMvc.perform(put("/api/address-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAddressBook)))
            .andExpect(status().isOk());

        // Validate the AddressBook in the database
        List<AddressBook> addressBookList = addressBookRepository.findAll();
        assertThat(addressBookList).hasSize(databaseSizeBeforeUpdate);
        AddressBook testAddressBook = addressBookList.get(addressBookList.size() - 1);
        assertThat(testAddressBook.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAddressBook() throws Exception {
        int databaseSizeBeforeUpdate = addressBookRepository.findAll().size();

        // Create the AddressBook

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressBookMockMvc.perform(put("/api/address-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(addressBook)))
            .andExpect(status().isBadRequest());

        // Validate the AddressBook in the database
        List<AddressBook> addressBookList = addressBookRepository.findAll();
        assertThat(addressBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAddressBook() throws Exception {
        // Initialize the database
        addressBookService.save(addressBook);

        int databaseSizeBeforeDelete = addressBookRepository.findAll().size();

        // Get the addressBook
        restAddressBookMockMvc.perform(delete("/api/address-books/{id}", addressBook.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AddressBook> addressBookList = addressBookRepository.findAll();
        assertThat(addressBookList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressBook.class);
        AddressBook addressBook1 = new AddressBook();
        addressBook1.setId(1L);
        AddressBook addressBook2 = new AddressBook();
        addressBook2.setId(addressBook1.getId());
        assertThat(addressBook1).isEqualTo(addressBook2);
        addressBook2.setId(2L);
        assertThat(addressBook1).isNotEqualTo(addressBook2);
        addressBook1.setId(null);
        assertThat(addressBook1).isNotEqualTo(addressBook2);
    }
}
