package converters;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Named;
import models.agent.Agent;
import services.agent.AgentService;


@Named
@ApplicationScoped
@FacesConverter(value = "agentConverter", managed = true)
public class AgentConverter implements Converter<Agent> {
    @Override
    public Agent getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s!=null && !s.equals("")){
            try {
                int id = Integer.parseInt(s); // Convertir el String a un ID
                return new AgentService().getById(id);
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Agent agent) {
        return String.valueOf(agent.getId());
    }
}
