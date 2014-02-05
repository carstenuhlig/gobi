package main;

import data.Gen;

import java.util.List;

public interface GenService {

    /**
     * Retrieve all cars in the catalog.
     *
     * @return all cars
     */
    public List<Gen> findAll();

    /**
     * search cars according to keyword in model and make.
     *
     * @param keyword for search
     * @return list of car that match the keyword
     */
    public List<Gen> search(String keyword);
}
