package com.officemanagementsystemapi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "departments")
@Getter
@Setter
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departments_id_seq")
    @SequenceGenerator(name = "departments_id_seq", sequenceName = "departments_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    @ManyToMany
    @JoinTable(name = "people_departments",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "people_id"))
    private Set<People> peoples = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "leaders_departments",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "people_id"))
    private Set<People> leaders = new HashSet<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE)
    private List<Category> categoryList = new ArrayList<>();

    public Department(String name) {
        this.name = name;
    }

}
