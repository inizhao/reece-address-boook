package com.reece.addressbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AddressBook.
 */
@Entity
@Table(name = "address_book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    @OneToMany(mappedBy = "addressBook")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contact> contacts = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AddressBook name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public AddressBook user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public AddressBook contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public AddressBook addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setAddressBook(this);
        return this;
    }

    public AddressBook removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setAddressBook(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddressBook addressBook = (AddressBook) o;
        if (addressBook.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), addressBook.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AddressBook{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
