package services.vehicule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

import models.agent.Agent;
import models.agent.AgentVehicule;
import models.vehicule.Vehicule;
import services.Service;
import services.agent.AgentService;

import java.util.List;

public class VehiculeService extends Service<Vehicule> {
    private EntityManagerFactory emf;

    public VehiculeService() {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
    }

    @Override
    public List<Vehicule> getAll() throws Exception {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            List<Vehicule> data=em.createQuery("SELECT v FROM vehiculo v LEFT JOIN FETCH v.agenteVehiculo", Vehicule.class).getResultList();
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
    public void store(Vehicule pojo) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Sincroniza la relación ManyToMany antes de guardar
            for (Agent agent : pojo.getAgenteVehiculo()) {
                agent = em.merge(agent);
                if (!agent.getAgenteVehiculo().contains(pojo)) {
                    agent.getAgenteVehiculo().add(pojo);
                }
            }
            em.merge(pojo);
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
    public void update(Vehicule pojo) throws Exception {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Vehicule vehicule = em.find(Vehicule.class, pojo.getPlate());
            vehicule.setUnitNumber(pojo.getUnitNumber());
            vehicule.setBrand(pojo.getBrand());
            vehicule.setColor(pojo.getColor());
            vehicule.setModel(pojo.getModel());
            vehicule.setTypeVehicule(pojo.getTypeVehicule());
            vehicule.setAgenteVehiculo(pojo.getAgenteVehiculo());
            em.getTransaction().commit();
            em.close();
        }catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            em.close();
            throw e;
        }

    }

    public Vehicule getByPlate(String plate) throws Exception {
        EntityManager em = emf.createEntityManager();
        Vehicule vehicule = null;
        try{
            em.getTransaction().begin();
            vehicule = em.createQuery("SELECT DISTINCT v FROM vehiculo v LEFT JOIN FETCH v.agenteVehiculo av WHERE v.plate = :plate", Vehicule.class)
                    .setParameter("plate", plate)
                    .getSingleResult();
            em.getTransaction().commit();
            em.close();
            return vehicule;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            em.close();
            throw e;
        }
    }

    public void deleteByPlate(String plate) throws Exception {
        EntityManager em = emf.createEntityManager();
        Vehicule vehicule =null;
        try{
            em.getTransaction().begin();
            vehicule = em.find(Vehicule.class, plate);
            em.remove(vehicule);
            em.getTransaction().commit();
            em.close();
        }catch(Exception e){
            em.getTransaction().rollback();
            em.close();
            e.printStackTrace();
            throw e;
        }
    }

    private Agent findAgentById(Integer idAgente) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Agent.class, idAgente);
        } catch (NoResultException e) {
            return null;
        }
    }
    private Vehicule findVehiculeByPlaca(String placaVehiculo) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT v FROM vehiculo v WHERE v.plate = :placa", Vehicule.class)
                    .setParameter("placa", placaVehiculo)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}