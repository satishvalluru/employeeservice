package com.java.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.dto.EmployeeCodesRequestDto;
import com.java.dto.EmployeeRequestDTO;
import com.java.dto.EmployeeResponseDTO;
import com.java.exceptions.EmployeeNotFoundException;
import com.java.model.EmployeeDetails;
import com.java.repository.EmployeeDetailsRepository;
import com.java.repository.EmployeeRepository;
import com.java.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	EmployeeDetailsRepository employeeDetailsRepo;

	@Autowired
	EmployeeRepository employeeRepo;

	/**
	 * saving employees
	 */
	@Override
	public EmployeeRequestDTO saveEmployee(EmployeeRequestDTO employeeDetailsDTO) throws Exception {
		logger.debug("Started save employee method");
		List<EmployeeDetails> employeeDetailsList = new ArrayList<>();
		employeeDetailsDTO.getEmployeeRequestDto().stream().forEach(employeeDto -> {
			EmployeeDetails empDetails = new EmployeeDetails();
			empDetails.setFlag(false);
			empDetails.setEmpDesignation(employeeDto.getEmpDesignation());
			empDetails.setEmpEmailId(employeeDto.getEmpEmailId());
			empDetails.setEmpExperience(employeeDto.getEmpExperience());
			empDetails.setEmpGender(employeeDto.getEmpGender());
			empDetails.setEmpLocation(employeeDto.getEmpLocation());
			empDetails.setEmpCode(employeeDto.getEmpCode());
			empDetails.setEmpName(employeeDto.getEmpName());
			empDetails.setEmpPhoneNo(Long.valueOf(employeeDto.getEmpPhoneNo()));
			empDetails.setStatus(employeeDetailsDTO.getStatus());
			BeanUtils.copyProperties(employeeDto, employeeDetailsList);
			employeeDetailsList.add(empDetails);
		});
		logger.debug("Ended save employee method");

		employeeDetailsRepo.saveAll(employeeDetailsList);
		return employeeDetailsDTO;
	}

	/**
	 * getting the employees
	 */
	@Override
	public List<EmployeeResponseDTO> getEmployees() {
		List<EmployeeDetails> empDetailsList = employeeDetailsRepo.findAll();
		List<EmployeeResponseDTO> empRequestList = new ArrayList<>();
		empDetailsList.stream().forEach(employeeDetails -> {
			EmployeeResponseDTO empRequestDto = new EmployeeResponseDTO();
			BeanUtils.copyProperties(employeeDetails, empRequestDto);
			empRequestList.add(empRequestDto);
		});
		return empRequestList;
	}

	/**
	 * retrieving employee name
	 */
	@Override
	public List<EmployeeResponseDTO> viewEmployeeNameContains(String empName) throws EmployeeNotFoundException {
		logger.debug("Started view employee method");
		List<EmployeeDetails> empDetails = employeeDetailsRepo.findByEmpNameContains(empName);
		if (empDetails.isEmpty()) {
			throw new EmployeeNotFoundException("employee name not found");
		}
		List<EmployeeResponseDTO> employeeDetailsRequestDTO = new ArrayList<>();
		EmployeeResponseDTO employeeDetailsDTO = null;
		for (EmployeeDetails employeeDetails : empDetails) {
			employeeDetailsDTO = new EmployeeResponseDTO();
			BeanUtils.copyProperties(employeeDetails, employeeDetailsDTO);
			employeeDetailsRequestDTO.add(employeeDetailsDTO);
		}
		logger.debug("Ended view employee method");

		return employeeDetailsRequestDTO;
	}

	/**
	 * modifying employee
	 */
	@Override
	public boolean modifyEmployee(Map<String, Object> updates, Long empCode) throws EmployeeNotFoundException {
		logger.debug("Started modify employee method");
		EmployeeDetails employee = new EmployeeDetails();
		Optional<EmployeeDetails> optionalEmployee = employeeDetailsRepo.findById(empCode);

		if (!optionalEmployee.isPresent()) {
			throw new EmployeeNotFoundException("employee code not found");
		}
		if (!optionalEmployee.isPresent())
			return false;
		employee = optionalEmployee.get();

		for (Map.Entry<String, Object> entry : updates.entrySet()) {
			try {
				org.apache.commons.beanutils.BeanUtils.setProperty(employee, entry.getKey(), entry.getValue());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		logger.debug("Ended modify employee method");

		employeeDetailsRepo.save(employee);
		return true;
	}

	/**
	 * deleting employee
	 */
	@Override
	public boolean deleteEmployee(Long empCode) throws EmployeeNotFoundException {
		logger.debug("Started delete employee method");
		EmployeeDetails employee = new EmployeeDetails();

		List<EmployeeDetails> optionalEmployee = employeeDetailsRepo.findByEmpCode(empCode);
		if (optionalEmployee.isEmpty()) {
			throw new EmployeeNotFoundException("employee code not found");
		}
		employee = optionalEmployee.get(0);

		employee.setFlag(true);
		employeeDetailsRepo.save(employee);
		logger.debug("Ended view employee method");

		return true;

	}

	@Override
	public EmployeeResponseDTO viewEmployeeCode(Long empCode) throws EmployeeNotFoundException {
		Optional<EmployeeDetails> empDetails = employeeRepo.findByEmpCode(empCode);
		if (!empDetails.isPresent()) {
			throw new EmployeeNotFoundException("employee code not found");

		}
		EmployeeDetails employeeDetails = empDetails.get();

		EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO();
		BeanUtils.copyProperties(employeeDetails, employeeResponseDTO);
		return employeeResponseDTO;
	}

	@Override
	public List<EmployeeResponseDTO> fetchEmployees(EmployeeCodesRequestDto employeeCodesRequestDto) {

		List<EmployeeResponseDTO> fetchEmployeeResponceDtoList = new ArrayList<>();
		List<EmployeeDetails> employeeList = employeeRepo.findByStatus(employeeCodesRequestDto.getEmpStatus());

		for (EmployeeDetails emp : employeeList) {
			Long comparecode2 = emp.getEmpCode();
			for (Long empCode : employeeCodesRequestDto.getEmpCode()) {
				Long empCode1 = empCode;
				if (empCode1.equals(comparecode2)) {
					EmployeeResponseDTO fetchEmployeeResponceDto = new EmployeeResponseDTO();
					BeanUtils.copyProperties(emp, fetchEmployeeResponceDto);
					fetchEmployeeResponceDtoList.add(fetchEmployeeResponceDto);
				}
			}

		}

		return fetchEmployeeResponceDtoList;
	}

}
