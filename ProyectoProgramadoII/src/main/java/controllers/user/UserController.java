package controllers.user;


import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import models.user.User;
import org.jetbrains.annotations.NotNull;
import services.user.UserService;
import utils.MessageUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class UserController implements Serializable{
    private UserService service;
    private User user;
    private List<User> users;
    private Logger logger;


    public UserController() {
        this.service = new UserService();
        this.logger = Logger.getLogger(this.getClass().getName());
        this.users = new ArrayList<>();
    }

    public User getUser() {
        if(user==null){
            user = new User();
        }
        return user;
    }


    //Aqui registran un usuario o bueno el admin registra al usuario/Dependen lo que quieran hacer
    public String register() {
        logger.info("Agregando nuevo usuario: " + user.getUsername());
        try {
            service.store(user);
            MessageUtil.addMessage("Usuario registrado con éxito!!!");
            return "/login?faces-redirect=true";
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
           MessageUtil.addMessage("Error al registrar usuario!");
            e.printStackTrace();
            return null;
        }
    }

    public String store(@NotNull User user) {
        logger.info("Añadiendo un usuario");
        try {
            service.store(user);
            MessageUtil.addMessage("Usuario agregado correctamente");
            return "/users/list-users?faces-redirect=true";
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al agregar usuario");
            return null;
        }
    }

    public void loadUsers() {
        this.users.clear();
        try {
            this.users = service.getAll();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al cargar usuarios.");
        }
    }

    public void loadUser() {
        String username = FacesContext.getCurrentInstance().getExternalContext()
                          .getRequestParameterMap().get("username");
        logger.info("Cargando usuario con nombre de usuario: " + username);
        try {
            User loadedUser = service.getByUsername(username);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userUpdate", loadedUser);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al cargar el usuario!");
        }
    }


    public String updateUser(@NotNull User user) {
        logger.info("Actualizando usuario: " + user.getUsername());
        try {
            service.update(user);
            MessageUtil.addMessage("Usuario actualizado con éxito!");
            return "/users/list-users?faces-redirect=true";
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al actualizar el usuario!");
            return null;
        }
    }

    public String deleteUser(String username) {
        User loggedUser = (User) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("userLogged");

        if (loggedUser != null && loggedUser.getUsername().equals(username)) {
            MessageUtil.addMessage("No se puede eliminar al usuario logueado!");
            return null; //Esto evita que el usuario loggeado se elimine
        }
        logger.info("Eliminando usuario con nombre de usuario: " + username);

        try {
            service.delete(username);
            MessageUtil.addMessage("Usuario eliminado con éxito!");
            return "/users/list-users?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al eliminar el usuario!");
            return null;
        }
    }

    public User findUserByUsername(String username) {
        logger.info("Buscando usuario con nombre de usuario: " + username);
        try {
            User foundUser = service.getByUsername(username);
            if (foundUser != null) {
                this.user = foundUser;
                MessageUtil.addMessage("Usuario encontrado!");
            } else {
                MessageUtil.addMessage("Usuario no encontrado!");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al buscar el usuario!");
        }
        return user;
    }


    //Solo admin puede ini
    public String login() {
        logger.info("Iniciando sesión para el usuario: " + user.getUsername());
        try {
            User foundUser = service.authenticate(user.getUsername(), user.getPassword());
            if (foundUser != null) {
                FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap().put("userLogged", foundUser);
                return "/home?faces-redirect=true";
            } else {
                MessageUtil.addMessage("Usuario o contraseña inválidos!");
                return null;
            }
        }catch (Exception e) {
            logger.log(Level.WARNING, "Error al iniciar sesión: ", e);
            MessageUtil.addMessage("Error en el sistema durante el inicio de sesión");
            return null;
        }
    }


    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().remove("userLogged");
        return "/index?faces-redirect=true";
    }


    public List<User> getUsers() {
        return users;
    }


}
