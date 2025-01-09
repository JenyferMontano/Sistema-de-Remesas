package models.vehicule;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import models.agent.Agent;


import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
@Entity(name ="vehiculo")
public class Vehicule implements Serializable {
    @Id
    @Column(name="placa")
    private String plate;
    @Column(name="numeroUnidad")
    private String unitNumber;
    @Column(name="marca")
    private String brand;
    @Column(name="color")
    private String color;
    @Column(name="modelo")
    private String model;
    @Column(name="tipoVehiculo")
    private String typeVehicule;
    @ManyToMany
    @JoinTable(
            name = "agente_vehiculo",
            joinColumns = @JoinColumn(name = "placaV", referencedColumnName = "placa"),
            inverseJoinColumns = @JoinColumn(name = "idAgente",referencedColumnName = "id")
    )
    private List<Agent> agenteVehiculo = new ArrayList<>(); //Colección de agentes asociados al vehículo
    //Recordar si List -> permite duplicado y no es tan efic, HashSet->No permite duplicado, efic

    public Vehicule() {}

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTypeVehicule() {
        return typeVehicule;
    }

    public void setTypeVehicule(String typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public List<Agent> getAgenteVehiculo() {
        return agenteVehiculo;
    }

    public void setAgenteVehiculo(List<Agent> agenteVehiculo) {
        this.agenteVehiculo = agenteVehiculo;
    }

    public void addAgent(Agent agent) {
        this.agenteVehiculo.add(agent);
        agent.getAgenteVehiculo().add(this);
    }

    public void removeAgent(Agent agent) {
        this.agenteVehiculo.remove(agent);
        agent.getAgenteVehiculo().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicule vehicule = (Vehicule) o;
        return Objects.equals(plate, vehicule.plate);
    }


    @Override
    public int hashCode(){
        return Objects.hash(plate,model,typeVehicule);
    }


}
