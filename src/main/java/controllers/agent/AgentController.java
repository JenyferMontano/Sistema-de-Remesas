package controllers.agent;

import jakarta.annotation.Nonnull;
import jakarta.enterprise.inject.Model;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import models.agent.Agent;
import org.jetbrains.annotations.NotNull;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import services.agent.AgentService;
import utils.MessageUtil;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Model
@ViewScoped
public class AgentController implements Serializable {
    private AgentService service;
    private Agent auxAgent;
    private Agent agent;
    private List<Agent> agents;
    private Logger logger;
    private byte[] tempPhoto;
    private String tempFilename;


    public AgentController() {
        this.service = new AgentService();
        this.auxAgent = new Agent();
        this.agents = new ArrayList<>();
        this.agent = new Agent();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    public Agent getAuxAgent(){
        if(auxAgent==null){
            auxAgent = new Agent();
        }
        return auxAgent;
    }

    public Agent getAgent(){
        if(agent==null){
            agent = new Agent();
        }
        return agent;
    }


    //ESTO ES PARA AGREGAR LA IMAGEN
    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            this.tempPhoto = file.getContent();
            this.tempFilename = file.getFileName();
            MessageUtil.addMessage("Archivo" + tempFilename + "cargado con éxito!!!!");
        } catch (Exception e) {
            MessageUtil.addMessage("Error al cargar archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Aqui escribe/guarda el archivo en bites y se le asigan la carpeta  en el proyecto
    public void writerBytes(byte[] bytes, String carpeta, String nombreImagen) {
        try{
            Path path = Paths.get(carpeta, nombreImagen);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            logger.info("Ruta de almacenamiento de archivos: " + carpeta);
            Files.write(path, bytes);
            MessageUtil.addMessage("Archivo guardado: " + path.toString());

        }catch(IOException e) {
            logger.log(Level.SEVERE,e.getMessage(),e);
            MessageUtil.addMessage("Error al guardar el archivo: " + e.getMessage());
        }
    }



    public void loadAgents() {
        this.agents.clear();
        try {
            this.agents = service.getAll();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al cargar los agentes!!!!");
        }
    }


    public String add(@NotNull Agent agent) {
        logger.info("Agregando nuevo agente: " + agent.getName());
        String uploadDir ="D:\\UNA\\PROGRAMACIÓN III\\PROYECTOS\\PROYECTO 2\\PPII\\ProyectoProgramadoII\\src\\main\\webapp\\resources\\images";
        try {
            if (this.tempPhoto != null) {
                agent.setPhoto(tempPhoto);
                agent.setPhotoName(tempFilename);
                writerBytes(tempPhoto,uploadDir, tempFilename);
            }
            System.out.println("Nombre del agente: " + agent.getName());
            service.store(agent);
            MessageUtil.addMessage("Agente guardado exitosamente!!!");
            //this.agent = new Agent(); //Reinicia el objeto
            tempPhoto = null;
            tempFilename = null;
            return "/agent/list-agents?faces-redirect=true";
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al agregar el agente!!!");
            e.printStackTrace();
            return null;
        }
    }

    public void findById(int id) {
        logger.info("Buscando agente con el siguiente id:  " + id);
        try {
            Agent foundAgent = service.getById(id);
            if (foundAgent != null) {
                this.agent = foundAgent;
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("foundAgent",foundAgent);
                //this.tempFilename = foundAgent.getPhotoName();

                MessageUtil.addMessage("Agente encontrado!!!!");
            } else {
                MessageUtil.addMessage("No se encontró ningún agente con el Id especificado!!!");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al buscar el agente!!!");
            e.printStackTrace();
        }
    }

    public void loadAgent(){
        String id=FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap().get("id");
        logger.info("Cargando la información de id#"+id);
        try{
            this.agent =service.getById(Integer.parseInt(id));
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("agentupdate",agent);
        }catch (Exception e){
            logger.log(Level.WARNING, e.getMessage(),e);
            MessageUtil.addMessage("Error en el registro!!!!");
        }
    }

    public String update(@Nonnull Agent agent){
        logger.info("Actualizando el agente con id" + agent.getId());
        String uploadDir ="D:\\UNA\\PROGRAMACIÓN III\\PROYECTOS\\PROYECTO 2\\PPII\\ProyectoProgramadoII\\src\\main\\webapp\\resources\\images";

        try{
            if (this.tempPhoto != null) {
                agent.setPhoto(tempPhoto);
                agent.setPhotoName(tempFilename);
                writerBytes(tempPhoto,uploadDir, tempFilename);

            }
            service.update(agent);
            tempPhoto = null;
            tempFilename = null;
            //PONGAN RUTA DE RETORNO
            return "/agent/list-agents?faces-redirect=true";
        }catch (Exception e){
            logger.log(Level.WARNING, e.getMessage(),e);
            MessageUtil.addMessage("Error al actualizar el agente!!!!");
            e.printStackTrace();
            return null;
        }
    }

    public String deleteAgent(int id) {
        logger.info("Eliminando el agente con la siguiente identificacion:" + id);
        try {
            service.deleteById(id);
            MessageUtil.addMessage("Agente eliminado con éxito!!!");
            return "/agent/list-agents?faces-redirect=true";
        } catch (Exception e){
            logger.log(Level.WARNING, e.getMessage(), e);
            MessageUtil.addMessage("Error al eliminar el agente!");
            e.printStackTrace();
            return null;
        }
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
    public void setAuxAgent(Agent auxAgent) {
        this.auxAgent = auxAgent;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public String getTempFilename() {
        return tempFilename;
    }

    public void setTempFilename(String tempFilename) {
        this.tempFilename = tempFilename;
    }

}
