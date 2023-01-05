package com.officemanagementsystemapi.model;

import com.officemanagementsystemapi.json.response.PeopleResponse;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "people")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class People {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "people_id_seq")
    @SequenceGenerator(name = "people_id_seq", sequenceName = "people_id_seq", allocationSize = 1)
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime updated;

    @ManyToMany
    @JoinTable(name = "people_departments",
            joinColumns = @JoinColumn(name = "people_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"))
    private Set<Department> departments = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "people_category",
            joinColumns = @JoinColumn(name = "people_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public People(PeopleResponse peopleResponse) {
        this.name = peopleResponse.getName();
        this.email = peopleResponse.getEmail();
        this.phoneNumber = peopleResponse.getPhoneNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        People people = (People) o;
        return id != null && Objects.equals(id, people.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
