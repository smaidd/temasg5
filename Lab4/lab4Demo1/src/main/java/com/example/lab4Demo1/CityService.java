package com.example.lab4Demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {
    @Autowired
    private CityRepository repository;

    public List<CityModel>  searchCitiesByName(String name){
        return name == null ? Collections.emptyList() :
                repository.findAll().stream()
                .filter(city -> city.getName().toLowerCase().startsWith(name.toLowerCase()))
                .sorted((firstCity, secondCity) -> firstCity.getName().compareTo(secondCity.getName()))
                .collect(Collectors.toList());
    }
}
