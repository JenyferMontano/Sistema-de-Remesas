package converters;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Named;
import models.requestStatus.RequestStatus;
import services.requestStatus.RequestStatusService;

@Named
@ApplicationScoped
@FacesConverter(value = "requestStatusConverter", managed = true)
public class RequestStatusConverter implements Converter<RequestStatus> {


    @Override
    public RequestStatus getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s != null && !s.isEmpty()) {
            try {
                int id = Integer.parseInt(s);
                RequestStatusService service = new RequestStatusService();
                return service.getById(id);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, RequestStatus requestStatus) {
        if (requestStatus != null) {
            return String.valueOf(requestStatus.getCode());
        }
        return "";
    }
}
