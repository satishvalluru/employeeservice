package com.java.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.model.EmployeeDetails;

/**
 * 
 * @author santo employee repository
 */
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeDetails, Long> {

	Optional<EmployeeDetails> findByEmpCode(Long empCode);

	List<EmployeeDetails> findByStatus(String empStatus);

}