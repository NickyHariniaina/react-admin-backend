package com.ferrissushi.tdreactadmin.controller;

import com.ferrissushi.tdreactadmin.entity.Intern;
import com.ferrissushi.tdreactadmin.service.InternService;
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
@RequestMapping("/interns")
@RequiredArgsConstructor
public class InternController {

    private final InternService service;

    @GetMapping
    public ResponseEntity<List<Intern>> getAll(
            @RequestParam(required = false) String _sort,
            @RequestParam(required = false, defaultValue = "ASC") String _order,
            @RequestParam(required = false, defaultValue = "0") int _start,
            @RequestParam(required = false, defaultValue = "9999") int _end,
            @RequestParam(required = false) Long idManager,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean hasSalary,
            @RequestParam(required = false) String q) {

        List<Intern> interns = service.getAll(_sort, _order, _start, _end, idManager, department, hasSalary, q);
        int total = service.count(idManager, department, hasSalary, q);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        headers.add("Access-Control-Expose-Headers", "X-Total-Count");

        return ResponseEntity.ok().headers(headers).body(interns);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Intern> getById(@PathVariable Long id) {
        Intern intern = service.getById(id);
        if (intern == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(intern);
    }

    @PostMapping
    public Intern create(@RequestBody Intern intern) {
        return service.create(intern);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Intern> update(@PathVariable Long id, @RequestBody Intern intern) {
        if (service.getById(id) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(service.update(id, intern));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.getById(id) == null) return ResponseEntity.notFound().build();
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
