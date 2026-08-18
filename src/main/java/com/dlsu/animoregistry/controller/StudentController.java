package com.dlsu.animoregistry.controller;

import com.dlsu.animoregistry.dto.LoginRequest;
import com.dlsu.animoregistry.model.LasallianStudent;
import com.dlsu.animoregistry.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LasallianStudent register(@RequestBody LasallianStudent student) {
        return studentService.register(student);
    }

    @GetMapping
    public List<LasallianStudent> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public LasallianStudent getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @GetMapping("/{id}/dashboard")
    public String getDashboard(@PathVariable Long id) {
        return studentService.getDashboard(id);
    }

    @PostMapping("/login")
    public LasallianStudent login(@RequestBody LoginRequest request) {
        return studentService.login(request.getDlsuEmail(), request.getPassword());
    }
}
