package com.dlsu.animoregistry.web;

import com.dlsu.animoregistry.model.LasallianStudent;
import com.dlsu.animoregistry.model.OrgOfficer;
import com.dlsu.animoregistry.service.OrgOfficerService;
import com.dlsu.animoregistry.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final StudentService studentService;
    private final OrgOfficerService officerService;

    public AuthController(StudentService studentService, OrgOfficerService officerService) {
        this.studentService = studentService;
        this.officerService = officerService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "Log in");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String role,
                         @RequestParam String dlsuEmail,
                         @RequestParam String password,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        try {
            if ("officer".equals(role)) {
                OrgOfficer officer = officerService.login(dlsuEmail, password);
                session.setAttribute("role", "officer");
                session.setAttribute("userId", officer.getId());
                session.setAttribute("name", officer.getName());
                session.setAttribute("organizationId", officer.getOrganization().getId());
                return "redirect:/officer";
            } else {
                LasallianStudent student = studentService.login(dlsuEmail, password);
                session.setAttribute("role", "student");
                session.setAttribute("userId", student.getId());
                session.setAttribute("name", student.getName());
                return "redirect:/dashboard";
            }
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("loginError", ex.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
