package com.example.parking.repositories;

import com.example.parking.models.Instance;
import com.example.parking.models.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface InstanceRepository extends CrudRepository<Instance, Integer> {
    @Transactional
    void deleteAllByVehicle(Vehicle vehicle);

    Iterable<Instance> findAllByVehicle(Vehicle vehicle);

}
