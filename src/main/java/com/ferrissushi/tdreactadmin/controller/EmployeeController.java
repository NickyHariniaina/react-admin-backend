package com.ferrissushi.tdreactadmin.controller;

import com.ferrissushi.tdreactadmin.entity.Employee;
import com.ferrissushi.tdreactadmin.service.EmployeeService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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
  public Map<String, Object> getAll(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size,
      @RequestParam(required = false) String sort,
      @RequestParam(required = false, defaultValue = "asc") String dir,
      @RequestParam(required = false) String department,
      @RequestParam(value = "isActive", required = false) Boolean active,
      @RequestParam(required = false) String q) {

    int start = page * size;
    int end = start + size;
    String order = "asc".equalsIgnoreCase(dir) ? "ASC" : "DESC";

    List<Employee> employees = service.getAll(sort, order, start, end, department, active, q);
    int total = service.count(department, active, q);

    return Map.of("data", employees, "total", total);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
    Employee employee = service.getById(id);
    if (employee == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(Map.of("data", employee));
  }

  @PostMapping
  public Map<String, Object> create(@RequestBody Employee employee) {
    return Map.of("data", service.create(employee));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, Object>> update(
      @PathVariable Long id, @RequestBody Employee employee) {
    if (service.getById(id) == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(Map.of("data", service.update(id, employee)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
    if (service.getById(id) == null) return ResponseEntity.notFound().build();
    service.delete(id);
    return ResponseEntity.ok(Map.of("data", Map.of("id", id)));
  }
}
