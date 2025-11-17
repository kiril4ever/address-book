package com.addressbook.service;

import com.addressbook.dto.ContactDTO;
import com.addressbook.dto.WeatherDTO;
import com.addressbook.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactMapperService {

    @Autowired
    private WeatherClientService weatherClientService;

    public ContactDTO toDTO(Contact contact) {
        if (contact == null) {
            return null;
        }

        String ownerUsername = contact.getUser() != null ? contact.getUser().getUsername() : null;

        ContactDTO contactDTO = new ContactDTO(
                contact.getId(),
                contact.getPicture(),
                contact.getName(),
                contact.getAddress(),
                ownerUsername
        );

        // Add weather information for the contact's address
        if (contact.getAddress() != null && !contact.getAddress().trim().isEmpty()) {
            WeatherDTO weather = weatherClientService.getWeatherForLocation(contact.getAddress());
            contactDTO.setWeather(weather);
        }

        return contactDTO;
    }

    public List<ContactDTO> toDTOList(List<Contact> contacts) {
        return contacts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Contact toEntity(ContactDTO contactDTO) {
        if (contactDTO == null) {
            return null;
        }

        Contact contact = new Contact();
        contact.setId(contactDTO.getId());
        contact.setPicture(contactDTO.getPicture());
        contact.setName(contactDTO.getName());
        contact.setAddress(contactDTO.getAddress());
        // Note: User is not set here as it should be set separately based on authentication
        // Weather is not stored in the entity

        return contact;
    }
}