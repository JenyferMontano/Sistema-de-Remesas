package models.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import models.Person;

import java.time.LocalDate;
import java.util.Objects;


@Entity(name = "usuario")
public class User extends Person {
    @Id
    @Column(name = "nombreUsuario")
    private String username;
    @Column(name="contrasena")
    private String password;
    @Column(name="perfil")
    private String profile;

    //Atributos heredados de Person deben mapearse también
    @Column(name="id")
    private int id;
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

    public User(){
        super();
    }

    public User(String username, String password, String profile, int id, String name, String lastName,
                String phone, String email, LocalDate birthDate) {
        super(id, name, lastName, phone, email,birthDate);
        this.username = username;
        this.password = password;
        this.setProfile(profile); //Usar setProfile para validar

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        if (profile.equalsIgnoreCase("Administrador") || profile.equalsIgnoreCase("Cliente")) {
            this.profile = profile;
        }
    }

    public boolean isAdmin() {
        return "Administrador".equalsIgnoreCase(this.profile);
    }

    public boolean isClient() {
        return "Cliente".equalsIgnoreCase(this.profile);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}
