package com.officemanagementsystemapi.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.officemanagementsystemapi.model.Department;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentResponse {

    private Integer id;
    private String name;
    private Set<CategoryResponse> categories;
    private Set<PeopleResponse> leaders;
    private List<Integer> leadersIds;

    public DepartmentResponse(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.leaders = department.getLeaders()
                .stream()
                .map(PeopleResponse::new)
                .collect(Collectors.toSet());
        this.leadersIds = this.leaders
                .stream()
                .map(PeopleResponse::getId)
                .collect(Collectors.toList());
        this.categories = department.getCategoryList()
                .stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toSet());
    }
}
