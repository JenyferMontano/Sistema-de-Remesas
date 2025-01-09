package controllers.remittances;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.servlet.http.HttpSession;

import models.remittances.Remittances;
import models.user.User;
import models.vehicule.Vehicule;
import services.remittances.RemittancesService;
import services.user.UserService;
import utils.MessageUtil;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Model
@ViewScoped
public class RemittancesController implements Serializable {
    private RemittancesService service;
    private Remittances remittance;
    private List<Remittances> remittances;
    private Logger logger;
    private UserService userService;
    private Integer selectedRemittanceId;
    private List<Remittances> pendingRemittances;
    private List<Remittances> authorizedClientRemittances;

    public RemittancesController() {
        this.service = new RemittancesService();
        this.logger = Logger.getLogger(this.getClass().getName());
        this.remittances = new ArrayList<>();
        this.userService = new UserService();
        this.pendingRemittances = new ArrayList<>();

    }

    public Remittances getRemittance() {
        if (remittance == null) {
            remittance = new Remittances();
        }
        if (remittance.getVehicule() == null) {
            remittance.setVehicule(new Vehicule());
        }
        return remittance;
    }

    @PostConstruct
    public void init() {
        loadPendingRemittances();
        getRemittance();
    }

    public void loadPendingRemittances() {
        try {
            pendingRemittances = service.getPendingRemittances();
            if (pendingRemittances == null || pendingRemittances.isEmpty()) {
                logger.info("No hay remesas pendientes.");
            } else {
                logger.info("Remesas pendientes cargadas: " + pendingRemittances.size());
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar las remesas pendientes", e);
            MessageUtil.addMessage("Error al cargar las remesas pendientes.");
        }
    }

    public void loadRemittanceDetails() {
        try {
            if (selectedRemittanceId != null) {
                logger.info("Cargando detalles para la remesa con ID: " + selectedRemittanceId);
                remittance = service.getById(selectedRemittanceId);
            } else {
                logger.warning("No se seleccionó ningún ID de remesa.");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar detalles de la remesa", e);
            MessageUtil.addMessage("Error al cargar detalles de la remesa: " + e.getMessage());
        }
    }

    public String requestRemittance(Remittances remittance) {
        logger.info("Solicitando remesa: " + remittance.getId());
        try {
            User sender = getLoggedInUser();

            if (sender == null) {
                MessageUtil.addMessage("Debe estar logueado para solicitar una remesa.");
                return "/login?faces-redirect=true";
            }

            User receiver = userService.getByUsername(remittance.getClientDestiny().getUsername());

            if (receiver == null) {
                MessageUtil.addMessage("El destinatario no está registrado.");
                return null;
            } else if (!receiver.isClient()) {
                MessageUtil.addMessage("El destinatario debe ser un usuario cliente.");
                return null;
            } else if (sender.getUsername().equals(receiver.getUsername())) {
                MessageUtil.addMessage("No puede enviarse una remesa a sí mismo.");
                return null;
            }

            remittance.setClientLogin(sender);
            service.requestRemittances(remittance);
            MessageUtil.addMessage("Remesa solicitada con éxito!");
            return "/home?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Error al solicitar la remesa: " + e.getMessage(), e);
            MessageUtil.addMessage("Error al solicitar la remesa!");
            return null;
        }
    }

    //Visualizacion de remesas(autorizadas) para los clientes
    public void loadAuthorized() {
        try {
            User userLogged = getLoggedInUser();
            this.authorizedClientRemittances = service.getAuthorizedRemittancesByClient(userLogged.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Error al cargar las remesas autorizadas: " + e.getMessage(), e);
            MessageUtil.addMessage("Error al cargar las remesas autorizadas!" + e.getMessage());
        }
    }

    public String authorizeRemittance(int id, LocalDate date, LocalTime time, String plate) {
        logger.info("Autorizando la remesa con ID: " + id);
        try {
            Remittances remittance = service.getById(id);

            if (remittance != null && "Pendiente".equalsIgnoreCase(remittance.getStatus().getStatus())) {
                service.authorizeRemittance(remittance.getId(), date, time, plate);
                MessageUtil.addMessage("Remesa autorizada exitosamente.");
                return "aqui redireccionan tilines";
            } else {
                MessageUtil.addMessage("La remesa no está en estado 'Pendiente' o no existe.");
                return null;
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al autorizar la remesa: " + id);
            return null;
        }
    }


    private User getLoggedInUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return (User) session.getAttribute("userLogged");
    }


    public List<Remittances> getPendingRemittances() {
        if (pendingRemittances == null || pendingRemittances.isEmpty()) {
            logger.info("No hay remesas pendientes.");
        } else {
            logger.info("Remesas pendientes cargadas: " + pendingRemittances.size());
        }
        return pendingRemittances;
    }


    public Integer getSelectedRemittanceId() {
        return selectedRemittanceId;
    }

    public void setSelectedRemittanceId(Integer selectedRemittanceId) {
        this.selectedRemittanceId = selectedRemittanceId;
    }


    public List<Remittances> getRemittances() {
        return remittances;
    }

    public void setRemittance(Remittances remittance) {
        this.remittance = remittance;
    }

    public List<Remittances> getAuthorizedClientRemittances() {
        return authorizedClientRemittances;
    }
}
