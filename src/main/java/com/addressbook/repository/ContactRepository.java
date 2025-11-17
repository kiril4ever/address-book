package com.addressbook.repository;

import com.addressbook.entity.Contact;
import com.addressbook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    // Find all contacts for a specific user
    List<Contact> findByUser(User user);

    // Find all contacts
    @Query("SELECT c FROM Contact c")
    List<Contact> allContactsAsEntities();

    // Find contact by ID and user (for security)
    Optional<Contact> findByIdAndUser(Long id, User user);

    // Check if a contact with the given name already exists for a specific user
    boolean existsByNameAndUser(String name, User user);

    // Search contacts by name for a specific user (case-insensitive, partial match)
    @Query("SELECT c FROM Contact c WHERE c.user = :user AND LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Contact> findByNameContainingIgnoreCaseAndUser(@Param("searchTerm") String searchTerm, @Param("user") User user);

    // Search all contacts by name (case-insensitive, partial match) - PUBLIC
    @Query("SELECT c FROM Contact c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Contact> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    // Delete contact by ID and user (for security)
    void deleteByIdAndUser(Long id, User user);

    // Check if contact exists for specific user
    boolean existsByIdAndUser(Long id, User user);
}