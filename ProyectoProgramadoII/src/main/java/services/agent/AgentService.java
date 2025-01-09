package services.agent;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.agent.Agent;
import models.vehicule.Vehicule;
import services.Service;

import java.util.List;

public class AgentService extends Service<Agent> {

    private EntityManagerFactory emf;

    public AgentService() {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
    }

    @Override
    public List<Agent> getAll() throws Exception {
        EntityManager em = emf.createEntityManager();
       try{
           em.getTransaction().begin();
           List<Agent> data = em.createQuery("select u from agente u", Agent.class).getResultList();
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
    public void store(Agent pojo) throws Exception {
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
    public void update(Agent pojo) throws Exception {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Agent agent = em.find(Agent.class, pojo.getId());
            agent.setName(pojo.getName());
            agent.setLastName(pojo.getLastName());
            agent.setPhone(pojo.getPhone());
            agent.setEmail(pojo.getEmail());
            agent.setBirthDate(pojo.getBirthDate());
            agent.setTypeAgent(pojo.getTypeAgent());
            agent.setPhoto(pojo.getPhoto());
            agent.setPhotoName(pojo.getPhotoName());

            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            em.close();
            throw e;
        }


    }

    public Agent getById(int id) throws Exception {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Agent agent = em.find(Agent.class, id);
            em.getTransaction().commit();
            em.close();
            return agent;
        }catch (Exception e) {
            em.getTransaction().rollback();
            em.close();
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteById(int id) throws Exception {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Agent agent = em.find(Agent.class, id);
            //Desvincular el agente de todos los vehículos
            for (Vehicule vehicule : agent.getAgenteVehiculo()) {
                vehicule.getAgenteVehiculo().remove(agent);
            }
            em.remove(agent);
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            em.getTransaction().rollback();
            em.close();
            e.printStackTrace();
            throw e;
        }
    }

    public List<Agent> getAgentsByType(String typeAgent) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            List<Agent> data = em.createQuery("select a from agente a where a.typeAgent = :type", Agent.class)
                    .setParameter("type", typeAgent)
                    .getResultList();
            em.getTransaction().commit();
            em.close();
            return data;
        } catch (Exception e) {
            em.close();
            e.printStackTrace();
            throw e;
        }
    }

}
