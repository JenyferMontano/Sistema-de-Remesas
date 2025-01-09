package utils;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public class MessageUtil {

    public static void addMessage(String message) {
        FacesMessage msg = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static void addMessage(String message, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(severity, message, null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
