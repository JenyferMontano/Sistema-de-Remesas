package user;

import models.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import services.user.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestUserService {
     private UserService instance;

    @BeforeEach
    public void setUp(){
        instance = new UserService();
    }

    @AfterEach
    public void tearDown() {
        instance = null;
    }

    private  User createTestUser() {
        User testUser = new User();
        testUser.setUsername("testr");
        testUser.setPassword("password");
        testUser.setProfile("Admin");
        testUser.setId(6);
        testUser.setName("Test");
        testUser.setLastName("User");
        testUser.setEmail("testuser@example.com");
        testUser.setPhone("123456789");
        testUser.setBirthDate(LocalDate.of(1990, 1, 1));
        return testUser;
    }


    @Test
    public void testAddUsername() throws Exception {
        User testUser = createTestUser();
        try {
            instance.store(testUser); // Guardar el usuario en la base de datos de prueba
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGetByUsername() throws Exception {
        try {
            instance.getByUsername("testr");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void testDelete() throws Exception {
        try {
            // Llama al método delete
            instance.delete("testr");
            // Intenta recuperar el usuario eliminado
            User deletedUser = instance.getByUsername("testr");
            // Verificación
            assertNull(deletedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdate() throws Exception {
        try {
            User testUser = new User();
            testUser.setUsername("Luci");
            testUser.setPassword("password");
            testUser.setProfile("Client");
            testUser.setId(8);
            testUser.setName("Test");
            testUser.setLastName("User");
            testUser.setEmail("tesr@example.com");
            testUser.setPhone("123459");
            testUser.setBirthDate(LocalDate.of(1995, 1, 1));
            instance.store(testUser);
            // Actualiza algunos atributos del usuario
            testUser.setPassword("newpassword");
            testUser.setName("UpdatedTest");
            testUser.setEmail("updateduser@example.com");

            // Llama al método update
            instance.update(testUser);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error: " + e.getMessage());
        }
    }

}
