package com.ferrissushi.tdreactadmin.controller;

import com.ferrissushi.tdreactadmin.entity.Intern;
import com.ferrissushi.tdreactadmin.service.InternService;
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
@RequestMapping("/interns")
@RequiredArgsConstructor
public class InternController {

  private final InternService service;

  @GetMapping
  public Map<String, Object> getAll(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size,
      @RequestParam(required = false) String sort,
      @RequestParam(required = false, defaultValue = "asc") String dir,
      @RequestParam(required = false) Long idManager,
      @RequestParam(required = false) String department,
      @RequestParam(required = false) Boolean hasSalary,
      @RequestParam(required = false) String q) {

    int start = page * size;
    int end = start + size;
    String order = "asc".equalsIgnoreCase(dir) ? "ASC" : "DESC";

    List<Intern> interns =
        service.getAll(sort, order, start, end, idManager, department, hasSalary, q);
    int total = service.count(idManager, department, hasSalary, q);

    return Map.of("data", interns, "total", total);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
    Intern intern = service.getById(id);
    if (intern == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(Map.of("data", intern));
  }

  @PostMapping
  public Map<String, Object> create(@RequestBody Intern intern) {
    return Map.of("data", service.create(intern));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, Object>> update(
      @PathVariable Long id, @RequestBody Intern intern) {
    if (service.getById(id) == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(Map.of("data", service.update(id, intern)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
    if (service.getById(id) == null) return ResponseEntity.notFound().build();
    service.delete(id);
    return ResponseEntity.ok(Map.of("data", Map.of("id", id)));
  }
}
