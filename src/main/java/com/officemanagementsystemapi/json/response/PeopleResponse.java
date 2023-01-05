package com.officemanagementsystemapi.json.response;

import com.officemanagementsystemapi.model.Category;
import com.officemanagementsystemapi.model.Department;
import com.officemanagementsystemapi.model.People;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PeopleResponse {

    private Integer id;
    @NotNull(message = "Field should not be null")
    @NotBlank(message = "Field shouldn't be blank")
    private String name;
    @Min(message = "Age should be positive", value = 0)
    @Max(message = "Age shouldn't be higher than 100", value = 100)
    private String age;
    @Email(message = "Not a email format")
    private String email;
    @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", message = "Not a valid phone format")
    private String phoneNumber;
    private List<String> departments;
    private List<String> categories;

    public PeopleResponse(People people) {
        this.id = people.getId();
        this.name = people.getName();
        this.email = people.getEmail();
        this.phoneNumber = people.getPhoneNumber();
        this.departments = people.getDepartments()
                .stream()
                .map(Department::getName)
                .collect(Collectors.toList());
        this.categories = people.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}
