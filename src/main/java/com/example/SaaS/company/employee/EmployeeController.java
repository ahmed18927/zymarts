package com.example.SaaS.company.employee;


import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/company/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    @PostMapping
    public ResponseEntity<ApiResponse< EmployeeResourceDTO>> create(@Valid @ModelAttribute EmployeeRequest request){

        Employee saved = employeeService.createEmployee(request);
        return  ResponseEntity.ok(
                new ApiResponse<>(
                        true , "Added successfully" , employeeMapper.toResource(saved)
                )
        );
    }
     @GetMapping("/{id}")
    public ResponseEntity<ApiResponse< EmployeeResourceDTO>> findById(@PathVariable Long id){
        Employee employee = employeeService.findEmployeeById(id);
        return ResponseEntity.ok(new ApiResponse<>(true ,"retired successfully" , employeeMapper.toResource(employee)));
     }
      @GetMapping
    public ResponseEntity<ApiResponse<Page<EmployeeResourceDTO>>> findAll( @RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "10") int size){
          Pageable  pageable = PageRequest.of(page , size);
        Page<EmployeeResourceDTO> responce =
                employeeService.findAllEmployees(pageable).map(
                        employee -> employeeMapper.toResource(employee));
        return ResponseEntity.ok(new ApiResponse<>(true ,"retired successfully" , responce));
      }
   @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse< Void>> delete(@PathVariable Long id){

        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true ,"delete successfully" , null)
        );
   }
   @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResourceDTO>> update(@PathVariable Long id, @Valid @ModelAttribute EmployeeRequest request){

        Employee updated = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true ,"update successfully" , employeeMapper.toResource(updated)));
   }
}
