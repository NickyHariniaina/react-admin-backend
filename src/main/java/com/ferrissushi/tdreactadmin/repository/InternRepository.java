package com.ferrissushi.tdreactadmin.repository;

import com.ferrissushi.tdreactadmin.entity.Intern;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternRepository extends JpaRepository<Intern, Long> {

  List<Intern> findByIdManager(Long idManager);

  List<Intern> findByHasSalary(Boolean hasSalary);

  List<Intern> findByDepartment(String department);
}
