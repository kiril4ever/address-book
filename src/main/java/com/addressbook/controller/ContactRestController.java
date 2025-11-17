package com.addressbook.controller;

import com.addressbook.dto.ContactDTO;
import com.addressbook.entity.Contact;
import com.addressbook.entity.User;
import com.addressbook.repository.UserRepository;
import com.addressbook.service.ContactMapperService;
import com.addressbook.service.ContactService;
import com.addressbook.service.CsvExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
@Transactional
public class ContactRestController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactMapperService contactMapperService;

    @Autowired
    private CsvExportService csvExportService;

    // Helper method to get current user (returns null if not authenticated)
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getName().equals("anonymousUser")) {
            return null;
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    // PUBLIC: GET all contacts (available to everyone - ALWAYS returns all contacts)
    @GetMapping
    public List<ContactDTO> getAllContacts() {
        // Always return all contacts for public viewing
        return contactService.getAllContacts();
    }

    // PUBLIC: SEARCH contacts by name (available to everyone - ALWAYS searches all contacts)
    @GetMapping("/search")
    public List<ContactDTO> searchContacts(@RequestParam String query) {
        // Always search all contacts for public viewing
        return contactService.searchContacts(query);
    }

    // PUBLIC: GET single contact by ID (available to everyone)
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        // For public access, return any contact
        List<ContactDTO> allContacts = contactService.getAllContacts();
        Optional<ContactDTO> contact = allContacts.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        return contact.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // PROTECTED: GET current user's contacts (for personal management)
    @GetMapping("/my-contacts")
    public ResponseEntity<List<ContactDTO>> getMyContacts() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        List<ContactDTO> userContacts = contactService.getAllContactsForUser(currentUser);
        return ResponseEntity.ok(userContacts);
    }

    // PROTECTED: CREATE new contact for current user
    @PostMapping
    public ResponseEntity<?> createContact(@RequestBody ContactDTO contactDTO) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body("Authentication required to create contacts");
            }

            // Convert DTO to Entity
            Contact contact = contactMapperService.toEntity(contactDTO);

            // Manual validation
            String validationError = validateContact(contact);
            if (validationError != null) {
                return ResponseEntity.badRequest().body(validationError);
            }

            Contact savedContact = contactService.createContact(contact, currentUser);
            ContactDTO savedContactDTO = contactMapperService.toDTO(savedContact);
            return ResponseEntity.ok(savedContactDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PROTECTED: UPDATE existing contact for current user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @RequestBody ContactDTO contactDTO) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body("Authentication required to update contacts");
            }

            // Convert DTO to Entity
            Contact contactDetails = contactMapperService.toEntity(contactDTO);

            // Manual validation
            String validationError = validateContact(contactDetails);
            if (validationError != null) {
                return ResponseEntity.badRequest().body(validationError);
            }

            Contact updatedContact = contactService.updateContact(id, contactDetails, currentUser);
            ContactDTO updatedContactDTO = contactMapperService.toDTO(updatedContact);
            return ResponseEntity.ok(updatedContactDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PROTECTED: DELETE contact for current user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body("Authentication required to delete contacts");
            }

            contactService.deleteContact(id, currentUser);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PROTECTED: EXPORT current user's contacts to CSV
    @GetMapping("/export/csv")
    public ResponseEntity<InputStreamResource> exportContactsToCsv() {

        // For now, we'll get the entities directly for export
        List<Contact> contactEntities = contactService.getAllContactsAsEntities();
        ByteArrayInputStream csvStream = csvExportService.exportContactsToCsv(contactEntities);

        String filename = "my-contacts-" + System.currentTimeMillis() + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new InputStreamResource(csvStream));
    }

    // Manual validation method
    private String validateContact(Contact contact) {
        if (contact.getPicture() == null || contact.getPicture().trim().isEmpty()) {
            return "Picture URL is required";
        }
        if (contact.getName() == null || contact.getName().trim().isEmpty()) {
            return "Name is required";
        }
        if (contact.getAddress() == null || contact.getAddress().trim().isEmpty()) {
            return "Address is required";
        }
        return null; // No errors
    }
}