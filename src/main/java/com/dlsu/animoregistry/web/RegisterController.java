package com.dlsu.animoregistry.web;

import com.dlsu.animoregistry.model.LasallianStudent;
import com.dlsu.animoregistry.model.Organization;
import com.dlsu.animoregistry.model.OrgOfficer;
import com.dlsu.animoregistry.model.PaymentType;
import com.dlsu.animoregistry.service.OrgOfficerService;
import com.dlsu.animoregistry.service.OrganizationService;
import com.dlsu.animoregistry.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final StudentService studentService;
    private final OrgOfficerService officerService;
    private final OrganizationService organizationService;

    public RegisterController(StudentService studentService, OrgOfficerService officerService,
                               OrganizationService organizationService) {
        this.studentService = studentService;
        this.officerService = officerService;
        this.organizationService = organizationService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("organizations", organizationService.getAll());
        return "register";
    }

    @PostMapping("/register/student")
    public String registerStudent(@RequestParam String idNumber, @RequestParam String name,
                                   @RequestParam String dlsuEmail, @RequestParam String password,
                                   @RequestParam String college, @RequestParam String yearLevel,
                                   RedirectAttributes redirectAttributes) {
        try {
            studentService.register(new LasallianStudent(idNumber, name, dlsuEmail, password, college, yearLevel));
            redirectAttributes.addFlashAttribute("registerSuccess", "Account created! Log in below.");
            return "redirect:/login";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("registerError", ex.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/register/officer")
    public String registerOfficer(@RequestParam String idNumber, @RequestParam String name,
                                   @RequestParam String dlsuEmail, @RequestParam String password,
                                   @RequestParam String position, @RequestParam Long organizationId,
                                   RedirectAttributes redirectAttributes) {
        try {
            OrgOfficer officer = new OrgOfficer(idNumber, name, dlsuEmail, password, null, position);
            officerService.register(organizationId, officer);
            redirectAttributes.addFlashAttribute("registerSuccess", "Account created! Log in below.");
            return "redirect:/login";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("registerError", ex.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/register/officer-new-org")
    public String registerOfficerNewOrg(@RequestParam String idNumber, @RequestParam String name,
                                         @RequestParam String dlsuEmail, @RequestParam String password,
                                         @RequestParam String position,
                                         @RequestParam String newOrgName, @RequestParam String newOrgCategory,
                                         @RequestParam String newOrgDescription, @RequestParam int newOrgCap,
                                         @RequestParam double newOrgFee, @RequestParam PaymentType newOrgPayment,
                                         @RequestParam(required = false) String newOrgSocial,
                                         RedirectAttributes redirectAttributes) {
        try {
            Organization org = new Organization(newOrgName, newOrgCategory, newOrgDescription, newOrgCap,
                    newOrgFee, newOrgPayment);
            if (newOrgSocial != null && !newOrgSocial.isBlank()) {
                org.setSocialMediaHandle(newOrgSocial);
            }
            Organization created = organizationService.create(org);

            OrgOfficer officer = new OrgOfficer(idNumber, name, dlsuEmail, password, null, position);
            officerService.register(created.getId(), officer);

            redirectAttributes.addFlashAttribute("registerSuccess",
                    "Organization created and your officer account is ready! Log in below.");
            return "redirect:/login";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("registerError", ex.getMessage());
            return "redirect:/register";
        }
    }
}
