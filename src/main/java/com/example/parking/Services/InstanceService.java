package com.example.parking.Services;

import com.example.parking.models.Instance;
import com.example.parking.models.Vehicle;
import org.springframework.stereotype.Service;
import com.example.parking.repositories.InstanceRepository;

import java.util.Date;

@Service
public class InstanceService {
    private final InstanceRepository instanceRepository;

    public InstanceService(InstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    public Iterable<Instance> findByVehicle(Vehicle vehicle){
        return this.instanceRepository.findAllByVehicle(vehicle);
    }

    public void deleteAllByVehicle(Vehicle vehicle){
        this.instanceRepository.deleteAllByVehicle(vehicle);
    }

    public Instance save(Instance instance){
        return this.instanceRepository.save(instance);
    }

    public Instance createInstance(Vehicle vehicle){
        Instance instance = new Instance();
        instance.setEntrada(new Date());
        instance.setVehicle(vehicle);
        save(instance);
        return instance;
    }


}
