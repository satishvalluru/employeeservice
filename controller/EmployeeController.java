package com.java.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.dto.EmployeeCodesRequestDto;
import com.java.dto.EmployeeRequestDTO;
import com.java.dto.EmployeeResponseDTO;
import com.java.exceptions.EmployeeNotFoundException;
import com.java.service.EmployeeService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/employee")
@Api(value = "EmployeeController", description = "Rest api for EmployeeController", tags = {
		"REST API for EmployeeController" })

public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	/**
	 * 
	 * @param employeeDetailsDTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveEmployee")
	public ResponseEntity<String> addEmployee(@Valid @RequestBody EmployeeRequestDTO employeeDetailsDTO)
			throws Exception {
		employeeService.saveEmployee(employeeDetailsDTO);
		return new ResponseEntity<>("employee added successfully", HttpStatus.CREATED);

	}

	/**
	 * 
	 * @return employees
	 */
	@GetMapping("/viewEmployees")
	public ResponseEntity<List<EmployeeResponseDTO>> viewEmployees() {
		List<EmployeeResponseDTO> details = employeeService.getEmployees();
		return new ResponseEntity<>(details, HttpStatus.CREATED);

	}

	/**
	 * 
	 * @param empName
	 * @return
	 * @throws EmployeeNotFoundException
	 */
	@GetMapping("/viewEmployee/{empName}")
	public ResponseEntity<List<EmployeeResponseDTO>> viewEmployee(@RequestParam String empName)
			throws EmployeeNotFoundException {
		List<EmployeeResponseDTO> details = employeeService.viewEmployeeNameContains(empName);
		return new ResponseEntity<>(details, HttpStatus.CREATED);

	}

	/**
	 * 
	 * @param empName
	 * @return
	 * @throws EmployeeNotFoundException
	 */
	@GetMapping("/viewEmployeeWithCode/{empCode}")
	public ResponseEntity<EmployeeResponseDTO> viewEmployeeWithCode(@RequestParam Long empCode)
			throws EmployeeNotFoundException {
		EmployeeResponseDTO details = employeeService.viewEmployeeCode(empCode);
		if(details !=null) {
			return new ResponseEntity<>(details, HttpStatus.OK);

		}
		return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);

	}

	/**
	 * 
	 * @param empCode
	 * @return
	 * @throws EmployeeNotFoundException
	 */
	@DeleteMapping("/deleteEmployee/{empCode}")
	public ResponseEntity<String> deleteEmployees(@PathVariable Long empCode) throws EmployeeNotFoundException {
		employeeService.deleteEmployee(empCode);
		return new ResponseEntity<>("employee details deleted successfully", HttpStatus.CREATED);

	}

	/**
	 * 
	 * @param updates
	 * @param empCode
	 * @return
	 * @throws EmployeeNotFoundException
	 */
	@PatchMapping("/update/{empCode}")
	public ResponseEntity<String> modifyEmployee(@RequestBody Map<String, Object> updates, @PathVariable Long empCode)
			throws EmployeeNotFoundException {
		System.out.println(updates.size());

		boolean isUpdated = employeeService.modifyEmployee(updates, empCode);
		if (isUpdated)
			return new ResponseEntity<>("Successfully modified employee", HttpStatus.OK);

		return new ResponseEntity<>("Failed to modify employee", HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/fetch/empdetails")
	public  List<EmployeeResponseDTO>fetchEmployeeDetails(@RequestBody EmployeeCodesRequestDto employeeCodesRequestDto){
		return employeeService.fetchEmployees(employeeCodesRequestDto);
		
	}

}
