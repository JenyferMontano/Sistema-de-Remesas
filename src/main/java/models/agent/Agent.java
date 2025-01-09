package models.agent;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import models.Person;
import models.vehicule.Vehicule;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Named
@ViewScoped
@Entity(name ="agente")
public class Agent extends Person implements Serializable {
    @Id
    @Column(name="id")
    private int id;
    @Column(name="tipoAgente")
    private String typeAgent;
    @Lob
    @Column(name="foto", nullable = true)
    private byte[] photo;
    @Column(name="nombre_foto",nullable = true)
    private String photoName;
    @ManyToMany(mappedBy = "agenteVehiculo")
    private List<Vehicule> agenteVehiculo = new ArrayList<>();

    //Atributos heredados de Person deben mapearse también
    @Column(name="nombre")
    private String name;
    @Column(name="apellidos")
    private String lastName;
    @Column(name="telefono")
    private String phone;
    @Column(name="correo")
    private String email;
    @Column(name="fNacimiento")
    private LocalDate birthDate;


    public Agent () {
        super();
    }

    public String getTypeAgent() {
        return typeAgent;
    }

    public void setTypeAgent(String typeAgent) {
        this.typeAgent = typeAgent;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public List<Vehicule> getAgenteVehiculo() {
        return agenteVehiculo;
    }

    public void setAgenteVehiculo(List<Vehicule> agenteVehiculo) {
        this.agenteVehiculo = agenteVehiculo;
    }

    @Override
    public int getId() {return id;}

    @Override
    public void setId(int id) {this.id = id;}

    @Override
    public String getName() {return name;}

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public LocalDate getBirthDate() {
        return birthDate;
    }
    @Override
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    public void addVehicule(Vehicule vehicule) {
        this.agenteVehiculo.add(vehicule);
        vehicule.getAgenteVehiculo().add(this);
    }

    public void removeVehicule(Vehicule vehicule) {
        this.agenteVehiculo.remove(vehicule);
        vehicule.getAgenteVehiculo().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(id, agent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), getLastName(),typeAgent); //Aqui tener en cuenta que getName y el otro, son heredados de persona
    }

//    @Override
//    public String toString() {
//        return "Agent{" +
//                "id=" + id +
//                ", name='" + getName() + '\'' +
//                ", lastName='" + getLastName() + '\'' +
//                ", typeAgent='" + typeAgent + '\'' +
//                '}';
//    }


}
