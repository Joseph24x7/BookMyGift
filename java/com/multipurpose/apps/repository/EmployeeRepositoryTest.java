package com.multipurpose.apps.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipurpose.apps.entity.Employee;

@SpringBootTest
public class EmployeeRepositoryTest{
	
	@Mock
	private EmployeeRepository employeeRepository;
	
	@Test
	public void findAll() {
		
		List<Employee> employeeList = new ArrayList<>();
		employeeList.add(new Employee());
		Mockito.when(employeeRepository.findAll()).thenReturn(employeeList);
		
		List<Employee> employees = employeeRepository.findAll();
		
		assertFalse(employees.isEmpty());
	}

}
