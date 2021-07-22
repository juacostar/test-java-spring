package com.example.parking.repositories;

import com.example.parking.models.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {
    Optional<Vehicle> findByPlaca(String placa);
    Iterable<Vehicle> findAllByType(String type);
}
