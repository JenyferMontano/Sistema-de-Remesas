package models.requestStatus;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

@Named
@ViewScoped
@Entity(name="solicitud_estado")
public class RequestStatus implements Serializable {
    @Id
    @Column(name="codigo")
    private int code;
    @Column(name="estado")
    private String status;
    public RequestStatus() {}

    public RequestStatus(Integer code,String status) {
        this.code = code;
        this.status = status;
    }

    public RequestStatus(String status) {
        this.status = status;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!status.equals("Pendiente") && !status.equals("Autorizado")) {
            throw new IllegalArgumentException("Estado inválido: debe ser 'Pendiente' o 'Autorizado'");
        }
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestStatus that = (RequestStatus) o;
        return code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
