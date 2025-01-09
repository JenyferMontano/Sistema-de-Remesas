package controllers.vehicule;

import jakarta.enterprise.inject.Model;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import models.agent.Agent;
import models.agent.AgentVehicule;
import models.vehicule.Vehicule;
import org.jetbrains.annotations.NotNull;
import services.vehicule.VehiculeService;
import utils.MessageUtil;
import services.agent.AgentService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Model
@ViewScoped
public class VehiculeController implements Serializable {
    private Agent agent;
    private List<Vehicule> vehicules;
    private List<Agent> selectedAgents = new ArrayList<>();
    private VehiculeService service;
    private Logger logger;
    private Vehicule vehicule;
    private AgentService agentS;
    private String vehiculoPlaca;
    private Integer agentId;
    private AgentVehicule agentVehicule;
    private List<Agent> agentsAux = new ArrayList<>();



    public VehiculeController() {
        this.service = new VehiculeService();
        this.logger = Logger.getLogger(this.getClass().getName());
        this.vehicules = new ArrayList<>();
        this.agentS = new AgentService();
        this.vehicule= new Vehicule();
    }


    public void loadVehicules() {
        this.vehicules.clear();
        try {
            this.vehicules = service.getAll();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al cargar los vehículos");
        }
    }

    public void loadVehicule() {
        String plate = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("plate");
        logger.info("Cargando información del vehículo con placa: " + plate);
        try {
            Vehicule vehicule = service.getByPlate(plate);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("vehiculeupdate", vehicule);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al cargar el vehículo!!!");
        }
    }

    public String add(@NotNull Vehicule vehicule) {
        logger.info("Agregando un nuevo vehículo con la placa: " + vehicule.getPlate());
        try {
            vehicule.setAgenteVehiculo(agentsAux);
            service.store(vehicule);
            MessageUtil.addMessage("Vehiculo agregado exitosamente!!!!!");
            return "REDECCIIONAN";
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al agregar el vehículo!!!");
            return null;
        }
    }

    public String update(@NotNull Vehicule vehicule) {
        logger.info("Actualizando el vehículo con placa: " + vehicule.getPlate());
        try {
            service.update(vehicule);
            return "redicerij";
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al actualizar el vehículo!!!");
            return null;
        }
    }

    public String deleteVehicule(String plate) {
        logger.info("Eliminando el vehículo con placa: " + plate);
        try {
            service.deleteByPlate(plate);
            MessageUtil.addMessage("Vehículo eliminado con éxito!");
            return "redireccionan";

        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al eliminar el vehículo!!!");
            return null;
        }
    }

    public Vehicule findByPlate(String plate) {
        logger.info("Buscando el vehiculo con el siguiente id:  " + plate);
        try {
            Vehicule foundVehicule = service.getByPlate(plate);
            if (foundVehicule != null) {
                this.vehicule = foundVehicule;
                MessageUtil.addMessage("Vehículo encontrado!!!");
            } else {
                MessageUtil.addMessage("No se encontró ningún vehículo con la placa especificada!!!");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al buscar el vehículo con la placa: " + plate);
            return null;
        }
        return vehicule;
    }

    public  void  loadAgents() {
        try {
            List<Agent> agents = agentS.getAll();
            if (agents.isEmpty()) {
                MessageUtil.addMessage("No se encontraron agentes");
            } else {
                selectedAgents.clear();
                selectedAgents.addAll(agents); //Agregar nuevos agentes seleccionados
                MessageUtil.addMessage("Agentes cargados exitosamente.");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al cargar los agentes");
        }
    }
    public Vehicule loadAgentsForVehicule(String plate) {
        try {
            selectedAgents.clear();
            vehicule = service.getByPlate(plate);
            selectedAgents = vehicule.getAgenteVehiculo(); // Carga los agentes relacionados con el vehículo
            int agentCount = selectedAgents.size();
            logger.log(Level.INFO, "Cantidad de agentes cargados para el vehículo con placa " + plate + ": " + agentCount);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedAgents", selectedAgents);
            return vehicule;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al cargar los agentes relacionados con el vehículo.");
            return null;
        }
    }


    public List<Agent> getSelectedAgents() {
        return selectedAgents;
    }

    public void setSelectedAgents(List<Agent> selectedAgents) {
        this.selectedAgents = selectedAgents;
    }


    public List<Vehicule> getVehicules() {
        return vehicules;
    }


    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }
    public List<Agent> getAgentsAux() {
        return agentsAux;
    }

    public void setAgentsAux(List<Agent> agentsAux) {
        this.agentsAux = agentsAux;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Agent getAgent() {
        return agent;
    }
    public void setAgent(Agent agent) {
        this.agent = agent;
    }


    public void addSelectedAgent(Agent agent) {
        agentsAux.add(agent);
    }
}