package remittances;

import models.remittances.Remittances;
import models.requestStatus.RequestStatus;
import models.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.remittances.RemittancesService;
import services.user.UserService;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class RemittanceServiceTest {
    private RemittancesService instance;
    private UserService userService;
    private String clientUsername;
    private String destinyUsername;

    @BeforeEach
    public void setUp() {
        instance = new RemittancesService();
        userService = new UserService();
        clientUsername = "User_" + System.currentTimeMillis();
        destinyUsername = "Destiny_" + System.currentTimeMillis();
    }

    @AfterEach
    public void tearDown() {

        try {
            userService.delete(clientUsername);
            userService.delete(destinyUsername);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Remittances createTestRemittance(String clientUsername, String destinyUsername, String typeRemittance) throws Exception {
        Remittances remittance = new Remittances();
        remittance.setId(1);


        User clientLogin = new User();
        clientLogin.setUsername(clientUsername);
        clientLogin.isClient();
        userService.store(clientLogin);


        remittance.setClientLogin(clientLogin);

        User clientDestiny = new User();
        clientDestiny.setUsername(destinyUsername);
        clientDestiny.isClient();
        userService.store(clientDestiny);


        remittance.setClientDestiny(clientDestiny);

        remittance.setDate(LocalDate.now());
        remittance.setTime(LocalTime.now());
        remittance.setTypeRemittance(typeRemittance);

        RequestStatus status = new RequestStatus();
        status.setStatus("Pendiente");
        remittance.setStatus(status);

        return remittance;
    }

    @Test
    public void testRequestRemittances() {
        String typeRemittance = "Entrega";

        Remittances testRemittance = null;
        try {
            testRemittance = createTestRemittance(clientUsername, destinyUsername, typeRemittance);
            instance.requestRemittances(testRemittance);

            Remittances retrieved = instance.getById(testRemittance.getId());
            assertNotNull(retrieved, "La remesa no se almacenó correctamente.");
            assertEquals(clientUsername, retrieved.getClientLogin().getUsername(), "El nombre de usuario no coincide.");
            assertEquals(destinyUsername, retrieved.getClientDestiny().getUsername(), "El cliente destino no coincide.");
            assertEquals("Pendiente", retrieved.getStatus().getStatus(), "El estado inicial de la remesa no es 'Pendiente'.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("No se pudo solicitar la remesa: " + e.getMessage());
        }
    }
}
