package converters;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Named;
import models.user.User;
import services.user.UserService;


@Named
@ApplicationScoped
@FacesConverter(value = "userConverter", managed = true)
public class UserConverter implements Converter<User> {
    @Override
    public User getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(s!=null && !s.equals("")){
            try {
                return new UserService().getByUsername(s);
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, User user) {
        if (user == null) {
            return "";
        }
        // Devolver el username como cadena
        return user.getUsername();
    }
}
