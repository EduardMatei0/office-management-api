package com.officemanagementsystemapi.service;


import com.officemanagementsystemapi.json.PeopleQuery;
import com.officemanagementsystemapi.json.PeopleResponse;

import java.util.List;

public interface PeopleService {

    PeopleResponse getOnePeople(Integer id);
    List<PeopleResponse> getAll();
    List<PeopleResponse> getAllByQuery(PeopleQuery peopleQuery);
    PeopleResponse saveOne(PeopleResponse peopleResponse);
    PeopleResponse editOne(PeopleResponse peopleResponse, Integer id);
    void deleteOne(Integer id);
}
