package com.officemanagementsystemapi.json.request;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class PeopleQuery {
    private List<String> names = new ArrayList<>();
    private List<String> emails = new ArrayList<>();
    private List<String> phoneNumbers = new ArrayList<>();
    private List<String> departments = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
}
