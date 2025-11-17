package com.addressbook.service;

import com.addressbook.dto.ContactDTO;
import com.addressbook.entity.Contact;
import com.addressbook.entity.User;
import com.addressbook.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapperService contactMapperService;

    // Public methods - available to everyone
    public List<ContactDTO> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return contactMapperService.toDTOList(contacts);
    }

    public List<ContactDTO> searchContacts(String query) {
        List<Contact> contacts = contactRepository.findByNameContainingIgnoreCase(query);
        return contactMapperService.toDTOList(contacts);
    }

    // User-specific methods - require authentication
    public List<ContactDTO> getAllContactsForUser(User user) {
        List<Contact> contacts = contactRepository.findByUser(user);
        return contactMapperService.toDTOList(contacts);
    }

    public List<Contact> getAllContactsAsEntities() {
        return contactRepository.allContactsAsEntities();
    }

//    public List<Contact> getAllContactsForUserAsEntities(User user) {
//        return contactRepository.findByUser(user);
//    }


    public Optional<Contact> getContactForUser(Long id, User user) {
        return contactRepository.findByIdAndUser(id, user);
    }

    public Contact createContact(Contact contact, User user) {
        // Check if contact name already exists for this user
        if (contactRepository.existsByNameAndUser(contact.getName(), user)) {
            throw new RuntimeException("A contact with the name '" + contact.getName() + "' already exists.");
        }

        contact.setUser(user);
        return contactRepository.save(contact);
    }

    public Contact updateContact(Long id, Contact contactDetails, User user) {
        Optional<Contact> optionalContact = contactRepository.findByIdAndUser(id, user);

        if (optionalContact.isPresent()) {
            Contact existingContact = optionalContact.get();

            // Check if name is being changed and if new name already exists for this user
            if (!existingContact.getName().equals(contactDetails.getName()) &&
                    contactRepository.existsByNameAndUser(contactDetails.getName(), user)) {
                throw new RuntimeException("A contact with the name '" + contactDetails.getName() + "' already exists.");
            }

            existingContact.setPicture(contactDetails.getPicture());
            existingContact.setName(contactDetails.getName());
            existingContact.setAddress(contactDetails.getAddress());

            return contactRepository.save(existingContact);
        } else {
            throw new RuntimeException("Contact not found or you don't have permission to edit it.");
        }
    }

    public void deleteContact(Long id, User user) {
        if (!contactRepository.existsByIdAndUser(id, user)) {
            throw new RuntimeException("Contact not found or you don't have permission to delete it.");
        }
        contactRepository.deleteByIdAndUser(id, user);
    }

    public List<ContactDTO> searchContactsForUser(String query, User user) {
        List<Contact> contacts = contactRepository.findByNameContainingIgnoreCaseAndUser(query, user);
        return contactMapperService.toDTOList(contacts);
    }


}