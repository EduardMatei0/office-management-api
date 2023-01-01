package com.officemanagementsystemapi.service;


import com.officemanagementsystemapi.json.PeopleResponse;

import java.util.List;

public interface PeopleService {

    PeopleResponse getOnePeople(Integer id);
    List<PeopleResponse> getAll();
    PeopleResponse saveOne(PeopleResponse peopleResponse);
    PeopleResponse editOne(PeopleResponse peopleResponse, Integer id);
    void deleteOne(Integer id);
}
