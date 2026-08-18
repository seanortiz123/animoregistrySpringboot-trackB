package com.dlsu.animoregistry.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Injects the current session's identity into every page controller's model,
 * so templates (via the nav fragment) always know who's logged in without
 * each controller having to look it up itself.
 */
@ControllerAdvice(basePackages = "com.dlsu.animoregistry.web")
public class GlobalModelAttributes {

    @ModelAttribute("currentRole")
    public String currentRole(HttpSession session) {
        return (String) session.getAttribute("role");
    }

    @ModelAttribute("currentName")
    public String currentName(HttpSession session) {
        return (String) session.getAttribute("name");
    }

    @ModelAttribute("currentUserId")
    public Long currentUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    @ModelAttribute("currentOrganizationId")
    public Long currentOrganizationId(HttpSession session) {
        return (Long) session.getAttribute("organizationId");
    }
}
