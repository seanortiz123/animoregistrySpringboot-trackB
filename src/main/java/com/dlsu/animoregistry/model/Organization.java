package com.dlsu.animoregistry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;              // e.g. "LSCS", "animo.sys", "Green Media Group"
    private String category;          // e.g. "Academic", "Special Interest", "Community Service"
    private String description;
    private String logoUrl;
    private String socialMediaHandle;

    private boolean registrationOpen = true;
    private int membershipCap;
    private int currentMemberCount = 0;

    private double membershipFeeAmount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ApplicationForm> applications = new ArrayList<>();

    protected Organization() {
    }

    public Organization(String name, String category, String description, int membershipCap,
                         double membershipFeeAmount, PaymentType paymentType) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.membershipCap = membershipCap;
        this.membershipFeeAmount = membershipFeeAmount;
        this.paymentType = paymentType;
    }

    // ---- Registration status control (Executive Board Member user story) ----
    public void closeRegistration() {
        this.registrationOpen = false;
    }

    public void openRegistration() {
        this.registrationOpen = true;
    }

    // ---- Membership slot logic (Org Officer user story) ----
    public boolean hasAvailableSlot() {
        return currentMemberCount < membershipCap;
    }

    public void incrementMemberCount() {
        if (!hasAvailableSlot()) {
            throw new IllegalStateException("Membership cap already reached for " + name + ".");
        }
        this.currentMemberCount++;
        if (!hasAvailableSlot()) {
            this.registrationOpen = false;
        }
    }

    // ---- Payment (Abstraction in action) ----
    public String collectMembershipFee(String payerName) {
        PaymentMethod method = PaymentMethodFactory.from(this.paymentType);
        return method.processPayment(this.membershipFeeAmount, payerName);
    }

    // ---- Getters / Setters (Central Committee Member profile-editing user story) ----
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getSocialMediaHandle() {
        return socialMediaHandle;
    }

    public void setSocialMediaHandle(String socialMediaHandle) {
        this.socialMediaHandle = socialMediaHandle;
    }

    public boolean isRegistrationOpen() {
        return registrationOpen;
    }

    public int getMembershipCap() {
        return membershipCap;
    }

    public void setMembershipCap(int membershipCap) {
        if (membershipCap < currentMemberCount) {
            throw new IllegalArgumentException(
                    "Membership cap cannot be lower than the current member count (" + currentMemberCount + ").");
        }
        this.membershipCap = membershipCap;
    }

    public int getCurrentMemberCount() {
        return currentMemberCount;
    }

    public double getMembershipFeeAmount() {
        return membershipFeeAmount;
    }

    public void setMembershipFeeAmount(double membershipFeeAmount) {
        this.membershipFeeAmount = membershipFeeAmount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public List<ApplicationForm> getApplications() {
        return applications;
    }
}
