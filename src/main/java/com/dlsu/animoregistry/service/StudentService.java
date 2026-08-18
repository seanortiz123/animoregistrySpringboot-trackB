package com.dlsu.animoregistry.service;

import com.dlsu.animoregistry.exception.ResourceNotFoundException;
import com.dlsu.animoregistry.model.LasallianStudent;
import com.dlsu.animoregistry.repository.LasallianStudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final LasallianStudentRepository studentRepository;

    public StudentService(LasallianStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public LasallianStudent register(LasallianStudent student) {
        studentRepository.findByDlsuEmail(student.getDlsuEmail()).ifPresent(existing -> {
            throw new IllegalArgumentException("An account with this DLSU email already exists.");
        });
        studentRepository.findByIdNumber(student.getIdNumber()).ifPresent(existing -> {
            throw new IllegalArgumentException("An account with this ID number already exists.");
        });
        return studentRepository.save(student);
    }

    public List<LasallianStudent> getAll() {
        return studentRepository.findAll();
    }

    public LasallianStudent getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public String getDashboard(Long id) {
        return getById(id).displayDashboard();
    }

    public LasallianStudent login(String dlsuEmail, String password) {
        LasallianStudent student = studentRepository.findByDlsuEmail(dlsuEmail)
                .orElseThrow(() -> new IllegalArgumentException("No account found for that email."));
        if (!student.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }
        return student;
    }
}
