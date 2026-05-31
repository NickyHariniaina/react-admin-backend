package com.ferrissushi.tdreactadmin.service;

import com.ferrissushi.tdreactadmin.entity.Employee;
import com.ferrissushi.tdreactadmin.repository.EmployeeRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repo;

    public List<Employee> getAll(String sort, String order, int start, int end,
                                  String department, Boolean active, String q) {
        Stream<Employee> stream = repo.findAll().stream();

        if (active != null)
            stream = stream.filter(e -> active.equals(e.getActive()));
        if (department != null)
            stream = stream.filter(e -> department.equals(e.getDepartment()));
        if (q != null && !q.isBlank())
            stream = stream.filter(e ->
                e.getFirstname().toLowerCase().contains(q.toLowerCase()) ||
                e.getEmail().toLowerCase().contains(q.toLowerCase()) ||
                e.getDepartment().toLowerCase().contains(q.toLowerCase())
            );

        Comparator<Employee> comp = switch (sort) {
            case "firstname" -> Comparator.comparing(Employee::getFirstname);
            case "email" -> Comparator.comparing(Employee::getEmail);
            case "department" -> Comparator.comparing(Employee::getDepartment);
            case "salary" -> Comparator.comparingInt(Employee::getSalary);
            case "active" -> Comparator.comparing(Employee::getActive);
            default -> Comparator.comparing(Employee::getId);
        };
        if ("DESC".equalsIgnoreCase(order)) comp = comp.reversed();

        List<Employee> sorted = stream.sorted(comp).toList();
        int from = Math.min(start, sorted.size());
        int to = Math.min(end, sorted.size());
        return sorted.subList(from, to);
    }

    public int count(String department, Boolean active, String q) {
        Stream<Employee> stream = repo.findAll().stream();
        if (active != null)
            stream = stream.filter(e -> active.equals(e.getActive()));
        if (department != null)
            stream = stream.filter(e -> department.equals(e.getDepartment()));
        if (q != null && !q.isBlank())
            stream = stream.filter(e ->
                e.getFirstname().toLowerCase().contains(q.toLowerCase()) ||
                e.getEmail().toLowerCase().contains(q.toLowerCase()) ||
                e.getDepartment().toLowerCase().contains(q.toLowerCase())
            );
        return (int) stream.count();
    }

    public Employee getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Employee create(Employee employee) {
        return repo.save(employee);
    }

    public Employee update(Long id, Employee employee) {
        employee.setId(id);
        return repo.save(employee);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
