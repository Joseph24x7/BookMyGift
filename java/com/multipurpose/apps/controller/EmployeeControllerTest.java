
package com.multipurpose.apps.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.multipurpose.apps.entity.Employee;
import com.multipurpose.apps.repository.EmployeeRepository;
import com.multipurpose.apps.service.EmployeeService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService employeeService;
	
	@MockBean
	private EmployeeRepository employeeRepository;

	Employee employee = new Employee();

	public String getRequestBody(Employee employee) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return objectMapper.writeValueAsString(employee);
	}

	@Test
	public void getEmployees() throws Exception {
		mockMvc.perform(get("/getAllEmployees")).andExpect(status().isOk());
	}

	@Test
	public void addEmployee() throws Exception {
		
		Mockito.when(employeeService.addEmployee(employee)).thenReturn(new Employee());
		mockMvc.perform(post("/addEmployee").contentType(MediaType.APPLICATION_JSON).content(getRequestBody(employee))).andExpect(status().isCreated());
		
	}

	@Test
	public void updateEmployee() throws Exception {
		
		Employee employee = new Employee();
		employee.setId(123L);
		
		mockMvc.perform(put("/updateEmployee").contentType(MediaType.APPLICATION_JSON).content(getRequestBody(employee))).andExpect(status().isOk());
		
	}

	@Test
	public void deleteEmployee() throws Exception {
		
		Employee employee = new Employee();
		employee.setId(123L);
		
		mockMvc.perform(delete("/deleteEmployee/123")).andExpect(status().isOk());
	}
}
