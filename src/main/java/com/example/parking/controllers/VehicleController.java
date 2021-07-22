package com.example.parking.controllers;

import com.example.parking.Services.InstanceService;
import com.example.parking.Services.VehicleService;
import com.example.parking.models.Instance;
import com.example.parking.models.Vehicle;
import com.example.parking.requests.RequestVehicle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class VehicleController {
    private final VehicleService vehicleService;
    private final InstanceService instanceService;

    public VehicleController(VehicleService vehicleService, InstanceService instanceService) {
        this.vehicleService = vehicleService;
        this.instanceService = instanceService;
    }

    @PostMapping(path = "vehicles/official")
    public @ResponseBody ResponseEntity<Vehicle> addOfficialVehicle(@RequestBody RequestVehicle requestVehicle){
        Vehicle vehicle = this.vehicleService.addOfficial(requestVehicle);
        return ResponseEntity.ok().body(vehicle);
    }

    @PostMapping(path = "vehicles/resident")
    public @ResponseBody ResponseEntity<Vehicle> addResidentVehicle(@RequestBody RequestVehicle requestVehicle){
        Vehicle vehicle = this.vehicleService.addResident(requestVehicle);
        return ResponseEntity.ok().body(vehicle);
    }

    @PostMapping(path = "/vehicles/entry")
    public @ResponseBody ResponseEntity<Void> entry(@RequestBody RequestVehicle requestVehicle){
        Optional<Vehicle> optionalVehicle = this.vehicleService.findByPlaca(requestVehicle.getId());
        if(optionalVehicle.isEmpty()){
            Vehicle vehicle = this.vehicleService.addNoResident(requestVehicle);
            Instance instance = this.instanceService.createInstance(vehicle);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        Vehicle vehicle = optionalVehicle.get();
        Instance instance = this.instanceService.createInstance(vehicle);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping(path = "/vehicles/exit")
    public @ResponseBody ResponseEntity<Double> exit(@RequestBody RequestVehicle requestVehicle){
        double pago = 0.0;
        Optional<Vehicle> optionalVehicle = this.vehicleService.findByPlaca(requestVehicle.getId());
        if(optionalVehicle.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Vehicle vehicle = optionalVehicle.get();
        Instance instance = new Instance();
        List<Instance> instances = (List<Instance>)this.instanceService.findByVehicle(vehicle);
        for(Instance i: instances){
            if(i.getSalida() == null){
                i.setSalida(new Date());
                this.instanceService.save(i);
                instance = i;
            }
        }
        if(vehicle.getType().equals("residente")){
            vehicle.setTime(instance.getMinutes());
            pago = instance.getMinutes()*8;
            vehicle.setValue(pago);
            this.vehicleService.save(vehicle);
        }else if(vehicle.getType().equals("no residente")){
            pago += instance.getMinutes()*65;
            vehicle.setValue(pago);
        }

        return  ResponseEntity.ok().body(pago);

    }

    @PutMapping(path = "vehicles/reset")
    public ResponseEntity<Void> reset(){
        List<Vehicle> vehicles = this.vehicleService.findAll();
        for(Vehicle v: vehicles){
            this.instanceService.deleteAllByVehicle(v);
            v.setTime(0);
            v.setValue(0);
        }
        return  new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "vehicles/report/{name}")
    public  ResponseEntity<Void> report(@PathVariable String name) throws IOException {
        this.vehicleService.createReport(name);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }
}
