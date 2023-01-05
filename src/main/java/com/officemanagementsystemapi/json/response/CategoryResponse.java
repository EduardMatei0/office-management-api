package com.officemanagementsystemapi.json.response;

import com.officemanagementsystemapi.model.Category;
import com.officemanagementsystemapi.model.People;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
public class CategoryResponse {

    private Integer id;
    private String name;
    private List<PeopleResponse> peopleList;
    private List<Integer> peopleIds = new ArrayList<>();


    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.peopleList = category.getPeopleSet()
                .stream()
                .map(PeopleResponse::new)
                .collect(Collectors.toList());
        this.peopleIds = category.getPeopleSet()
                .stream()
                .map(People::getId)
                .collect(Collectors.toList());
    }
}
