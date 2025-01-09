package services.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.user.User;
import services.Service;

import java.util.List;

public class UserService extends Service<User> {
    private EntityManagerFactory emf;

    public UserService() {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
    }
    @Override
    public List<User> getAll() throws Exception {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            List<User> data=em.createQuery("select u from usuario u", User.class).getResultList();
            em.getTransaction().commit();
            em.close();
            return data;
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public void store(User pojo) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pojo);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            throw e;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

    }


    @Override
    public void update(User pojo) throws Exception {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            User user = em.find(User.class, pojo.getUsername());
            user.setPassword(pojo.getPassword());
            user.setProfile(pojo.getProfile());
            user.setId(pojo.getId());
            user.setName(pojo.getName());
            user.setLastName(pojo.getLastName());
            user.setEmail(pojo.getEmail());
            user.setPhone(pojo.getPhone());
            user.setBirthDate(pojo.getBirthDate());
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            em.close();
            throw e;
        }
    }

    public void delete(String username) throws Exception {
        EntityManager em = emf.createEntityManager();
        User user =null;
        try{
            em.getTransaction().begin();
            user = em.find(User.class, username);
            em.remove(user);
            em.getTransaction().commit();
            em.close();
        }catch(Exception e){
            em.getTransaction().rollback();
            em.close();
            e.printStackTrace();
            throw e;
        }

    }


    public User getByUsername(String username) throws Exception {
        EntityManager em = emf.createEntityManager();
        User user = null;
        try{
            em.getTransaction().begin();
            user = em.createQuery("SELECT u FROM usuario u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            em.getTransaction().commit();
            em.close();
            return user;
        }catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            em.close();
            throw e;
        }
    }

    public User authenticate(String username, String password) throws Exception {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            User user = em.createQuery("SELECT u FROM usuario u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            if (user != null && user.getPassword().equals(password)) {
                em.getTransaction().commit();
                return user; // Retorna el usuario si la autenticación es exitosa
            } else {
                em.getTransaction().rollback();
                return null; // Retorna null si las credenciales son incorrectas
            }

        }catch (Exception e) {
            em.getTransaction().rollback();
            em.close();
            e.printStackTrace();
            throw e;

        }
    }

}
