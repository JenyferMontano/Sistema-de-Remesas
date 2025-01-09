package models.remittances;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import models.requestStatus.RequestStatus;
import models.user.User;
import models.vehicule.Vehicule;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Named
@ViewScoped
@Entity(name = "remesas")
public class Remittances implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha",nullable = true)
    private LocalDate date;

    @Column(name = "hora",nullable = true)
    private LocalTime time;

    @Column(name = "tipoRemesa",nullable = true)
    private String typeRemittance;


    @ManyToOne
    @JoinColumn(name = "placaVehiculo", referencedColumnName = "placa",nullable = true)
    private Vehicule vehicule;


    @ManyToOne
    @JoinColumn(name = "codigoEstado", referencedColumnName = "codigo",nullable = true)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "clienteDestinoUsuario", referencedColumnName = "nombreUsuario", nullable = false)
    private User clientDestiny;

    @ManyToOne
    @JoinColumn(name = "clienteLoginUsuario", referencedColumnName = "nombreUsuario", nullable = false)
    private User clientLogin;


    public Remittances() {
    }


    public Remittances(LocalDate date, LocalTime time, String typeRemittance,
                       Vehicule vehicule, RequestStatus status) {
        this.date = date;
        this.time = time;
        this.typeRemittance = typeRemittance;
        this.vehicule = vehicule;
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getTypeRemittance() {
        return typeRemittance;
    }

    public void setTypeRemittance(String typeRemittance) {
        if (!typeRemittance.equals("Entrega") && !typeRemittance.equals("Envio")) {
            throw new IllegalArgumentException("Tipo de remesa inválido: debe ser 'Entrega' o 'Envio'");
        }
        this.typeRemittance = typeRemittance;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }


    public boolean isAuthorized() {
        return status != null && "Autorizada".equalsIgnoreCase(status.getStatus());
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public User getClientDestiny() {
        return clientDestiny;
    }

    public void setClientDestiny(User clientDestiny) {
        this.clientDestiny = clientDestiny;
    }

    public User getClientLogin() {
        return clientLogin;
    }

    public void setClientLogin(User clientLogin) {
        this.clientLogin = clientLogin;
    }

}
