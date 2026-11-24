package com.example.SaaS.company;

import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.employee.EmployeeRepository;
import com.example.SaaS.company.employee.EmployeeRequest;
import com.example.SaaS.company.employee.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeRequest request;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .name("Ahmed")
                .email("ahmed@test.com")
                .phone("01012345678")
                .password("hashed_password")
                .isMain(false)
                .build();

        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "emp.jpg", "image/jpeg", "data".getBytes()
        );

        request = new EmployeeRequest();
        request.setName("Ahmed");
        request.setEmail("ahmed@test.com");
        request.setPhone("01012345678");
        request.setPassword("password123");
        request.setImage(mockImage);
    }

    @Test
    void findEmployeeById_shouldReturn_whenFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.findEmployeeById(1L);

        assertThat(result.getName()).isEqualTo("Ahmed");
    }

    @Test
    void findEmployeeById_shouldThrow_whenNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.findEmployeeById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("employee not found");
    }

    @Test
    void findAllEmployees_shouldReturnList() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<Employee> result = employeeService.findAllEmployees();

        assertThat(result).hasSize(1);
    }

    @Test
    void createEmployee_shouldCreateSuccessfully() {
        // لا يوجد تعارض في البيانات
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.existsByPhone(anyString())).thenReturn(false);
        when(employeeRepository.existsByName(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(fileStorageService.saveFile(any())).thenReturn("emp.jpg");
        when(employeeRepository.save(any())).thenReturn(employee);

        Employee result = employeeService.createEmployee(request);

        assertThat(result).isNotNull();
        verify(employeeRepository).save(any());
    }

    @Test
    void createEmployee_shouldThrow_whenEmailExists() {
        when(employeeRepository.existsByEmail("ahmed@test.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email already exists");

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void createEmployee_shouldThrow_whenPhoneExists() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.existsByPhone("01012345678")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Phone already exists");
    }

    @Test
    void deleteEmployee_shouldDelete_whenFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        Employee result = employeeService.deleteEmployee(1L);

        assertThat(result.getName()).isEqualTo("Ahmed");
        verify(employeeRepository).delete(employee);
    }
}