package com.reece.addressbook.service.dto;

import javax.validation.constraints.NotNull;

public class ContactDTO {
    private Long id;
    @NotNull
    private Long addressBookId;
    @NotNull
    private String name;
    @NotNull
    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddressBookId() {
        return addressBookId;
    }

    public void setAddressBookId(Long addressBookId) {
        this.addressBookId = addressBookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
