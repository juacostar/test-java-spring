package com.example.parking.Services;

import com.example.parking.models.Instance;
import com.example.parking.models.Vehicle;
import org.springframework.stereotype.Service;
import com.example.parking.repositories.VehicleRepository;
import com.example.parking.requests.RequestVehicle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;


    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;

    }

    public Optional<Vehicle> findByPlaca(String placa){
        return this.vehicleRepository.findByPlaca(placa);
    }

    public Vehicle save(Vehicle vehicle){
        return this.vehicleRepository.save(vehicle);
    }

    public List<Vehicle> findAll(){
        return (List<Vehicle>)this.vehicleRepository.findAll();
    }

    public List<Vehicle> findAllByType(String type){
        return (List<Vehicle>)this.vehicleRepository.findAllByType(type);
    }

    public void createReport(String name) throws IOException {
        FileWriter fileWriter = new FileWriter(name + ".txt");
        PrintWriter pw = new PrintWriter(fileWriter);
        List<Vehicle> vehicles = findAllByType("residente");
        for(Vehicle v: vehicles){
            pw.println("Núm.placa" + "        " + "Tiempo estacionado(min)" + "         " + "cantidad a pagar");
            pw.println(v.getPlaca() + "                 " + v.getTime() + "                              " + v.getValue());
        }
        pw.close();
        fileWriter.close();
    }

    public Vehicle addOfficial(RequestVehicle requestVehicle){
        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca(requestVehicle.getId());
        vehicle.setType("oficial");
        return getVehicle(vehicle);
    }

    public Vehicle addResident(RequestVehicle requestVehicle){
        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca(requestVehicle.getId());
        vehicle.setType("residente");
        return getVehicle(vehicle);
    }

    public Vehicle addNoResident(RequestVehicle requestVehicle){
        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca(requestVehicle.getId());
        vehicle.setType("no residente");
        return getVehicle(vehicle);
    }

    private Vehicle getVehicle(Vehicle vehicle) {
        vehicle.setTime(0);
        vehicle.setValue(0.0);
        Vehicle saved = save(vehicle);
        return  saved;
    }

}
