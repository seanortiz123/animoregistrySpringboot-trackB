package com.dlsu.animoregistry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

/**
 * Abstract parent class shared by every Lasallian who signs up on AnimoRegistry.
 *
 * OOP CONCEPT - ENCAPSULATION:
 * The idNumber and dlsuEmail fields are never set directly. They can only be
 * changed through their setters, and those setters enforce DLSU's own rules
 * (8-digit ID numbers, @dlsu.edu.ph emails only) before the value is ever
 * allowed to enter the object's state.
 *
 * OOP CONCEPT - INHERITANCE:
 * LasallianStudent and OrgOfficer both extend this class and inherit its
 * fields/behavior, while adding their own role-specific data.
 *
 * OOP CONCEPT - POLYMORPHISM:
 * displayDashboard() is declared abstract here and overridden differently by
 * each subclass, so the same method call produces role-appropriate output.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DLSUUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idNumber;
    private String name;
    private String dlsuEmail;

    @JsonIgnore
    private String password;

    protected DLSUUser() {
        // required by JPA
    }

    protected DLSUUser(String idNumber, String name, String dlsuEmail, String password) {
        setIdNumber(idNumber);
        this.name = name;
        setDlsuEmail(dlsuEmail);
        this.password = password;
    }

    // ---- Encapsulation: ID Number Verification ----
    public void setIdNumber(String idNumber) {
        if (idNumber == null || !idNumber.matches("\\d{8}")) {
            throw new IllegalArgumentException(
                    "Invalid DLSU ID Number. It must be exactly 8 digits (e.g., 12345678).");
        }
        this.idNumber = idNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    // ---- Encapsulation: DLSU Email Validation ----
    public void setDlsuEmail(String dlsuEmail) {
        if (dlsuEmail == null || !dlsuEmail.trim().toLowerCase().endsWith("@dlsu.edu.ph")) {
            throw new IllegalArgumentException(
                    "Invalid email. Only official @dlsu.edu.ph addresses may register.");
        }
        this.dlsuEmail = dlsuEmail.trim().toLowerCase();
    }

    public String getDlsuEmail() {
        return dlsuEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank.");
        }
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    /**
     * Polymorphism: each concrete subclass returns a dashboard summary
     * relevant to its own role (Applicant vs Organization Officer).
     */
    public abstract String displayDashboard();
}
