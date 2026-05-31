package com.ferrissushi.tdreactadmin.repository;

import com.ferrissushi.tdreactadmin.entity.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByDepartmentAndActive(String department, Boolean active);

    List<Employee> findByActive(Boolean active);
}
