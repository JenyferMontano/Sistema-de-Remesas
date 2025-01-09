package converters;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Named;
import models.vehicule.Vehicule;
import services.vehicule.VehiculeService;


@Named
@ApplicationScoped
@FacesConverter(value = "vehiculeConverter",managed = true)
public class VehiculeConverter implements Converter<Vehicule> {
    @Override
    public Vehicule getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s!=null && !s.equals("")){
            try {
                return new VehiculeService().getByPlate(s);
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Vehicule vehicule) {
        return vehicule.getPlate(); //Aqui el atributo
    }
}
