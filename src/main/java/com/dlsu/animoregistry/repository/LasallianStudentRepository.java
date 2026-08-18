package com.dlsu.animoregistry.repository;

import com.dlsu.animoregistry.model.LasallianStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LasallianStudentRepository extends JpaRepository<LasallianStudent, Long> {
    Optional<LasallianStudent> findByDlsuEmail(String dlsuEmail);
    Optional<LasallianStudent> findByIdNumber(String idNumber);
}
