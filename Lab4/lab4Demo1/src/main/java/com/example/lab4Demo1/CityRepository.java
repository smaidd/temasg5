package com.example.lab4Demo1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CityRepository {

    private List<CityModel> cities;

    public CityRepository(@Value("${csv}") String csv) throws IOException {
        cities = Files.readAllLines(new ClassPathResource(csv).getFile().toPath()).stream()
           .map(line -> line.split(","))
                .filter(values -> values.length == 4)
                .map(values -> new CityModel(values))
                .collect(Collectors.toList());
    }
    public List<CityModel> findAll(){
        return cities;
    }
}
