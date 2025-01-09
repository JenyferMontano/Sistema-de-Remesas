package services.requestStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.requestStatus.RequestStatus;
import services.Service;

import java.util.List;

public class RequestStatusService extends Service<RequestStatus> {
    private EntityManagerFactory emf;
    public RequestStatusService() {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
    }


    @Override
    public List<RequestStatus> getAll() throws Exception {
        EntityManager em = emf.createEntityManager();
        List<RequestStatus> data = null;
        try {
            em.getTransaction().begin();
            data = em.createQuery("SELECT rs FROM solicitud_estado rs", RequestStatus.class).getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return data;
    }

    @Override
    public void store(RequestStatus pojo) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pojo);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

    }

    @Override
    public void update(RequestStatus pojo) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            RequestStatus existingStatus = em.find(RequestStatus.class, pojo.getCode());
            if (existingStatus != null) {
                existingStatus.setStatus(pojo.getStatus());
                em.merge(existingStatus);
                em.getTransaction().commit();
            } else {

                System.out.println("No se encontró el estado con código: " + pojo.getCode());
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

    }

    public RequestStatus findByStatus(String status) {
        EntityManager em = emf.createEntityManager();
        RequestStatus result = null;

        try {
            result = em.createQuery("SELECT rs FROM solicitud_estado rs WHERE rs.status = :status", RequestStatus.class)
                    .setParameter("status", status)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return result;
    }

    public RequestStatus getById(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        RequestStatus status = em.find(RequestStatus.class, id);
        em.getTransaction().commit();
        em.close();
        return status;
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public void delete(Integer code) {
    }
}
