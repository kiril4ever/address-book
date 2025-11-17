package com.addressbook.service;

import com.addressbook.entity.Contact;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class CsvExportService {

    public ByteArrayInputStream exportContactsToCsv(List<Contact> contacts) {
        return generateCsv(contacts);
    }

    private ByteArrayInputStream generateCsv(List<Contact> contacts) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out)) {

            // Write CSV header
            writer.println("ID,Name,Address,Picture,Owner");

            // Write CSV data
            for (Contact contact : contacts) {
                String ownerUsername = contact.getUser() != null ? contact.getUser().getUsername() : "Unknown";
                writer.println(
                        escapeCsvField(contact.getId().toString()) + "," +
                                escapeCsvField(contact.getName()) + "," +
                                escapeCsvField(contact.getAddress()) + "," +
                                escapeCsvField(contact.getPicture()) + "," +
                                escapeCsvField(ownerUsername)
                );
            }

            writer.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate CSV file: " + e.getMessage());
        }
    }

    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        // Escape quotes and wrap in quotes if contains comma, newline, or quotes
        if (field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r")) {
            field = field.replace("\"", "\"\"");
            return "\"" + field + "\"";
        }
        return field;
    }
}