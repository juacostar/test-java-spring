package com.example.parking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "instance")
public class Instance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instance_id")
    private int id;

    @Column(name = "entrada")
    private Date entrada;

    @JsonIgnore
    @Column(name = "salida")
    private Date salida;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "id")
    private Vehicle vehicle;

    public Instance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getSalida() {
        return salida;
    }

    public void setSalida(Date salida) {
        this.salida = salida;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public int getMinutes(){
        int minutes = 0;
        long mili = this.salida.getTime() - this.entrada.getTime();
        minutes = (int)((mili/(1000*60))%60);
        return  minutes;
    }

}
