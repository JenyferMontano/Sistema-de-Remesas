package models.agent;


import jakarta.persistence.*;
import models.vehicule.Vehicule;

import java.util.Objects;

@Entity
@Table(name = "agente_vehiculo")
public class AgentVehicule {
    //Por si la llegaran a utilizar

    @Id
    @Column(name = "idAgente")
    private Integer idAgente;

    @Id
    @Column(name = "placaV")
    private String placaV;

    @ManyToOne
    @JoinColumn(name = "idAgente", referencedColumnName = "id", insertable = false, updatable = false)
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "placaV", referencedColumnName = "placa", insertable = false, updatable = false)
    private Vehicule vehicule;

    public AgentVehicule() {}

    public Integer getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Integer idAgente) {
        this.idAgente = idAgente;
    }

    public String getPlacaV() {
        return placaV;
    }

    public void setPlacaV(String placaV) {
        this.placaV = placaV;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentVehicule that = (AgentVehicule) o;
        return Objects.equals(idAgente, that.idAgente) && Objects.equals(placaV, that.placaV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAgente,placaV);}

}
