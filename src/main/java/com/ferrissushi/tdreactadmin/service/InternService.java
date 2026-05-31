package com.ferrissushi.tdreactadmin.service;

import com.ferrissushi.tdreactadmin.entity.Intern;
import com.ferrissushi.tdreactadmin.repository.InternRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternService {

  private final InternRepository repo;

  public List<Intern> getAll(
      String sort,
      String order,
      int start,
      int end,
      Long idManager,
      String department,
      Boolean hasSalary,
      String q) {
    Stream<Intern> stream = repo.findAll().stream();

    if (idManager != null) stream = stream.filter(i -> idManager.equals(i.getIdManager()));
    if (department != null) stream = stream.filter(i -> department.equals(i.getDepartment()));
    if (hasSalary != null) stream = stream.filter(i -> hasSalary.equals(i.getHasSalary()));
    if (q != null && !q.isBlank())
      stream =
          stream.filter(
              i ->
                  i.getPrenom().toLowerCase().contains(q.toLowerCase())
                      || i.getNom().toLowerCase().contains(q.toLowerCase())
                      || i.getEmail().toLowerCase().contains(q.toLowerCase())
                      || i.getDepartment().toLowerCase().contains(q.toLowerCase()));

    Comparator<Intern> comp =
        switch (sort) {
          case "prenom" -> Comparator.comparing(Intern::getPrenom);
          case "nom" -> Comparator.comparing(Intern::getNom);
          case "email" -> Comparator.comparing(Intern::getEmail);
          case "department" -> Comparator.comparing(Intern::getDepartment);
          case "salary" -> Comparator.comparingInt(Intern::getSalary);
          case "idManager" -> Comparator.comparingLong(Intern::getIdManager);
          case "hasSalary" -> Comparator.comparing(Intern::getHasSalary);
          default -> Comparator.comparing(Intern::getId);
        };
    if ("DESC".equalsIgnoreCase(order)) comp = comp.reversed();

    List<Intern> sorted = stream.sorted(comp).toList();
    int from = Math.min(start, sorted.size());
    int to = Math.min(end, sorted.size());
    return sorted.subList(from, to);
  }

  public int count(Long idManager, String department, Boolean hasSalary, String q) {
    Stream<Intern> stream = repo.findAll().stream();
    if (idManager != null) stream = stream.filter(i -> idManager.equals(i.getIdManager()));
    if (department != null) stream = stream.filter(i -> department.equals(i.getDepartment()));
    if (hasSalary != null) stream = stream.filter(i -> hasSalary.equals(i.getHasSalary()));
    if (q != null && !q.isBlank())
      stream =
          stream.filter(
              i ->
                  i.getPrenom().toLowerCase().contains(q.toLowerCase())
                      || i.getNom().toLowerCase().contains(q.toLowerCase())
                      || i.getEmail().toLowerCase().contains(q.toLowerCase())
                      || i.getDepartment().toLowerCase().contains(q.toLowerCase()));
    return (int) stream.count();
  }

  public Intern getById(Long id) {
    return repo.findById(id).orElse(null);
  }

  public Intern create(Intern intern) {
    return repo.save(intern);
  }

  public Intern update(Long id, Intern intern) {
    intern.setId(id);
    return repo.save(intern);
  }

  public void delete(Long id) {
    repo.deleteById(id);
  }
}
