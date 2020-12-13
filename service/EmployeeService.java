package com.java.service;

import java.util.List;
import java.util.Map;

import com.java.dto.EmployeeCodesRequestDto;
import com.java.dto.EmployeeRequestDTO;
import com.java.dto.EmployeeResponseDTO;
import com.java.exceptions.EmployeeNotFoundException;

/**
 * 
 * @author santo
 * save, delete, retrieving and modifying employee operations
 */
public interface EmployeeService {

	public EmployeeRequestDTO saveEmployee(EmployeeRequestDTO employeeDetailsDTO) throws Exception;

	public List<EmployeeResponseDTO> getEmployees();

	public boolean deleteEmployee(Long empCode) throws EmployeeNotFoundException;

	public List<EmployeeResponseDTO> viewEmployeeNameContains(String empName) throws EmployeeNotFoundException;

	public boolean modifyEmployee(Map<String, Object> updates, Long empCode) throws EmployeeNotFoundException;

	public EmployeeResponseDTO viewEmployeeCode(Long empCode) throws EmployeeNotFoundException;
	
	public List<EmployeeResponseDTO> fetchEmployees(EmployeeCodesRequestDto employeeCodesRequestDto);


}
