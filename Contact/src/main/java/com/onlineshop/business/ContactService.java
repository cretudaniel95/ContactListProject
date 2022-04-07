package com.onlineshop.business;

import com.onlineshop.business.domain.Contact;
import com.onlineshop.business.domain.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;

    public List<Contact> getAllContacts(){
        return contactRepository.findAll(Sort.by("name").ascending());
    }

    public void addContact(Contact contact) {
        contactRepository.save(contact);
    }

    public void removeContactById(UUID id) {
        contactRepository.deleteById(id);
    }

    public List<Contact> getContactByName(String name){
        return contactRepository.findByKeyword(name);
    }

    public Optional<Contact> getContactById(UUID id){
        return contactRepository.findById(id);
    }

}
