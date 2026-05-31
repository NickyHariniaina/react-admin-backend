package com.ferrissushi.tdreactadmin.controller;

import com.ferrissushi.tdreactadmin.entity.Employee;
import com.ferrissushi.tdreactadmin.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping
    public ResponseEntity<List<Employee>> getAll(
            @RequestParam(required = false) String _sort,
            @RequestParam(required = false, defaultValue = "ASC") String _order,
            @RequestParam(required = false, defaultValue = "0") int _start,
            @RequestParam(required = false, defaultValue = "9999") int _end,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String q) {

        List<Employee> employees = service.getAll(_sort, _order, _start, _end, department, active, q);
        int total = service.count(department, active, q);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        Employee employee = service.getById(id);
        if (employee == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return service.create(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        if (service.getById(id) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(service.update(id, employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.getById(id) == null) return ResponseEntity.notFound().build();
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
