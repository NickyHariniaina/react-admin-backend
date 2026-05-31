package com.ferrissushi.tdreactadmin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "intern")
@Getter
@Setter
public class Intern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idManager;

    private String prenom;

    private String nom;

    private String email;

    private String department;

    private int salary;

    private Boolean hasSalary;
}
