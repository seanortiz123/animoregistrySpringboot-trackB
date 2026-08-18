package com.dlsu.animoregistry.web;

import com.dlsu.animoregistry.dto.ApplicationRequest;
import com.dlsu.animoregistry.model.Organization;
import com.dlsu.animoregistry.service.ApplicationService;
import com.dlsu.animoregistry.service.OrganizationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final OrganizationService organizationService;
    private final ApplicationService applicationService;

    public HomeController(OrganizationService organizationService, ApplicationService applicationService) {
        this.organizationService = organizationService;
        this.applicationService = applicationService;
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false) String category,
                        @RequestParam(required = false) String q,
                        Model model) {
        List<Organization> orgs = organizationService.getAll();

        if (category != null && !category.isBlank()) {
            orgs = orgs.stream().filter(o -> category.equals(o.getCategory())).collect(Collectors.toList());
        }
        if (q != null && !q.isBlank()) {
            String term = q.toLowerCase();
            orgs = orgs.stream()
                    .filter(o -> o.getName().toLowerCase().contains(term)
                            || (o.getDescription() != null && o.getDescription().toLowerCase().contains(term)))
                    .collect(Collectors.toList());
        }

        List<Organization> allOrgs = organizationService.getAll();

        List<String> categories = allOrgs.stream()
                .map(Organization::getCategory)
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .collect(Collectors.toList());

        long recruitingCount = allOrgs.stream().filter(Organization::isRegistrationOpen).count();
        int openSlots = allOrgs.stream()
                .mapToInt(o -> Math.max(0, o.getMembershipCap() - o.getCurrentMemberCount()))
                .sum();

        model.addAttribute("title", "Browse orgs");
        model.addAttribute("organizations", orgs);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("searchTerm", q);
        model.addAttribute("totalOrgCount", allOrgs.size());
        model.addAttribute("recruitingCount", recruitingCount);
        model.addAttribute("openSlots", openSlots);
        return "index";
    }

    @GetMapping("/organizations/{id}")
    public String orgDetail(@PathVariable Long id, Model model) {
        model.addAttribute("title", "Organization");
        model.addAttribute("org", organizationService.getById(id));
        return "organization";
    }

    @PostMapping("/organizations/{id}/apply")
    public String apply(@PathVariable Long id, @RequestParam String answers,
                         HttpSession session, RedirectAttributes redirectAttributes) {
        Long studentId = (Long) session.getAttribute("userId");
        if (!"student".equals(session.getAttribute("role")) || studentId == null) {
            return "redirect:/login";
        }
        try {
            ApplicationRequest request = new ApplicationRequest();
            request.setApplicantId(studentId);
            request.setOrganizationId(id);
            request.setAnswers(answers);
            applicationService.submitApplication(request);
            redirectAttributes.addFlashAttribute("applySuccess", "Application submitted! Track it from your dashboard.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("applyError", ex.getMessage());
        }
        return "redirect:/organizations/" + id;
    }
}
