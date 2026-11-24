package com.example.SaaS.company.employee;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, Long> {
//    Page<Employee> findByCompanyId(Long companyId , Pageable pageable);
    Optional<Employee> findByIdAndCompanyId(Long id, Long companyId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByName(String name);
    Optional<Employee> findByEmail(String email);


    @Query("""
SELECT e FROM Employee e
WHERE e.company.id = :companyId
""")
    Page<Employee> findByCompanyId(@Param("companyId") Long companyId,Pageable pageable);
}

