
package com.multipurpose.apps.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.multipurpose.apps.entity.Department;
import com.multipurpose.apps.entity.Employee;
import com.multipurpose.apps.repository.DepartmentRepository;
import com.multipurpose.apps.repository.EmployeeRepository;

@SpringBootTest
public class EmployeeServiceTest {

	@InjectMocks
	private EmployeeService employeeService;
	
	@Mock
	private EmployeeRepository employeeRepository;
	
	@Mock
	private DepartmentRepository departmentRepository;
	
	@Mock
	private CacheManager cacheManager;

	@Test
	public void getEmployees() {

		List<Employee> employeeList = new ArrayList<>();
		employeeList.add(new Employee());
		
		Mockito.when(employeeRepository.findAll()).thenReturn(employeeList);
		
		Cache cache = Mockito.mock(Cache.class);
		
		Mockito.when(cacheManager.getCache("Employee")).thenReturn(cache);
		
		List<Employee> employees = employeeService.getEmployees();

		assertFalse(employees.isEmpty());
	}

	@Test
	public void addEmployee() {

		Employee employee = new Employee();
		
		Department department=new Department();
		employee.setDepartment(department);
		
		department.setId(1L);
		department.setName("GIL");
		
		Optional<Department> optionalDepartment= Optional.of(department);

		Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
		
		Mockito.when(departmentRepository.getDepartmentsByName("GIL")).thenReturn(optionalDepartment);

		employeeService.addEmployee(employee);
	}

}
