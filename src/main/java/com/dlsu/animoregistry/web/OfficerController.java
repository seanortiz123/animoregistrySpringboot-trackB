package com.dlsu.animoregistry.web;

import com.dlsu.animoregistry.model.ApplicationForm;
import com.dlsu.animoregistry.model.ApplicationStatus;
import com.dlsu.animoregistry.model.OrgOfficer;
import com.dlsu.animoregistry.model.Organization;
import com.dlsu.animoregistry.service.ApplicationService;
import com.dlsu.animoregistry.service.OrgOfficerService;
import com.dlsu.animoregistry.service.OrganizationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/officer")
public class OfficerController {

    private final OrgOfficerService officerService;
    private final OrganizationService organizationService;
    private final ApplicationService applicationService;

    public OfficerController(OrgOfficerService officerService, OrganizationService organizationService,
                              ApplicationService applicationService) {
        this.officerService = officerService;
        this.organizationService = organizationService;
        this.applicationService = applicationService;
    }

    /** Redirects to login if the session isn't an officer; otherwise returns the org id. */
    private Long requireOfficerOrgId(HttpSession session) {
        if (!"officer".equals(session.getAttribute("role"))) return null;
        return (Long) session.getAttribute("organizationId");
    }

    @GetMapping
    public String panel(@RequestParam(required = false) ApplicationStatus status,
                         HttpSession session, Model model) {
        Long orgId = requireOfficerOrgId(session);
        if (orgId == null) return "redirect:/login";

        Long officerId = (Long) session.getAttribute("userId");
        OrgOfficer officer = officerService.getById(officerId);
        Organization org = organizationService.getById(orgId);

        List<ApplicationForm> apps = status != null
                ? applicationService.getByOrganizationAndStatus(orgId, status)
                : applicationService.getByOrganization(orgId);
        apps.sort(Comparator.comparing(ApplicationForm::getDateApplied).reversed());

        model.addAttribute("title", "Officer panel");
        model.addAttribute("officer", officer);
        model.addAttribute("org", org);
        model.addAttribute("applications", apps);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", ApplicationStatus.values());
        return "officer";
    }

    @PostMapping("/registration-status")
    public String setRegistrationStatus(@RequestParam boolean open, HttpSession session,
                                         RedirectAttributes redirectAttributes) {
        Long orgId = requireOfficerOrgId(session);
        if (orgId == null) return "redirect:/login";
        try {
            organizationService.setRegistrationOpen(orgId, open);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("officerError", ex.getMessage());
        }
        return "redirect:/officer";
    }

    @PostMapping("/membership-cap")
    public String setMembershipCap(@RequestParam int membershipCap, HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Long orgId = requireOfficerOrgId(session);
        if (orgId == null) return "redirect:/login";
        try {
            organizationService.setMembershipCap(orgId, membershipCap);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("officerError", ex.getMessage());
        }
        return "redirect:/officer";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String category, @RequestParam String description,
                                 @RequestParam(required = false) String socialMediaHandle,
                                 @RequestParam(required = false) String logoUrl,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        Long orgId = requireOfficerOrgId(session);
        if (orgId == null) return "redirect:/login";
        try {
            Organization updates = new Organization(null, category, description, 0, 0, null);
            updates.setSocialMediaHandle(socialMediaHandle);
            updates.setLogoUrl(logoUrl);
            organizationService.updateProfile(orgId, updates);
            redirectAttributes.addFlashAttribute("officerSuccess", "Profile updated.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("officerError", ex.getMessage());
        }
        return "redirect:/officer";
    }

    @PostMapping("/collect-fee")
    public String collectFee(@RequestParam String payerName, HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Long orgId = requireOfficerOrgId(session);
        if (orgId == null) return "redirect:/login";
        try {
            String receipt = organizationService.collectMembershipFee(orgId, payerName);
            redirectAttributes.addFlashAttribute("feeReceipt", receipt);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("officerError", ex.getMessage());
        }
        return "redirect:/officer";
    }

    @PostMapping("/applications/{id}/status")
    public String updateApplicationStatus(@PathVariable Long id, @RequestParam ApplicationStatus status,
                                           HttpSession session, RedirectAttributes redirectAttributes) {
        if (requireOfficerOrgId(session) == null) return "redirect:/login";
        try {
            applicationService.updateStatus(id, status);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("officerError", ex.getMessage());
        }
        return "redirect:/officer";
    }

    @PostMapping("/applications/{id}/interview")
    public String scheduleInterview(@PathVariable Long id,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime interviewSchedule,
                                     HttpSession session, RedirectAttributes redirectAttributes) {
        if (requireOfficerOrgId(session) == null) return "redirect:/login";
        try {
            applicationService.scheduleInterview(id, interviewSchedule);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("officerError", ex.getMessage());
        }
        return "redirect:/officer";
    }
}
