package services.remittances;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.remittances.Remittances;
import models.requestStatus.RequestStatus;
import models.vehicule.Vehicule;
import services.Service;
import utils.MessageUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RemittancesService extends Service<Remittances> {

    private EntityManagerFactory emf;
    public RemittancesService() {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
    }

    @Override
    public List<Remittances> getAll() throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            List<Remittances> data = em.createQuery("select r from remesas r", Remittances.class).getResultList();
            em.getTransaction().commit();
            return data;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void store(Remittances pojo) throws Exception {

    }

    @Override
    public void update(Remittances pojo) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pojo);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Remittances getById(int id) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Remittances.class, id);
        } catch (Exception e) {
            throw new Exception("Error al buscar la remesa por ID: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void requestRemittances(Remittances remittance) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();


            if (remittance.getId() == null || remittance.getClientDestiny() == null || remittance.getTypeRemittance() == null) {
                throw new IllegalArgumentException("ID, clientUsername, y typeRemittance son obligatorios.");
            }

            RequestStatus status = em.createQuery("select s from solicitud_estado s where" +
                            " s.status = :status", RequestStatus.class)
                    .setParameter("status", "Pendiente")
                    .getResultStream()
                    .findFirst()
                    .orElseGet(() -> {
                        RequestStatus newStatus = new RequestStatus();
                        newStatus.setStatus("Pendiente");
                        em.persist(newStatus);
                        return newStatus;
                    });

            remittance.setStatus(status);
            em.persist(remittance);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al solicitar la remesa: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Remittances remittance = em.find(Remittances.class, id);
            if (remittance != null) {
                em.remove(remittance);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void authorizeRemittance(int remittanceId, LocalDate date, LocalTime time, String vehiclePlate) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();


            Remittances remittance = em.find(Remittances.class, remittanceId);
            if (remittance == null) {
                throw new Exception("No se encontró una remesa con el ID especificado.");
            }

            if (!"Pendiente".equals(remittance.getStatus().getStatus())) {
                throw new Exception("La remesa no está en estado 'Pendiente'.");
            }

            remittance.setDate(date);
            remittance.setTime(time);

            Vehicule vehicle = em.find(Vehicule.class, vehiclePlate);
            if (vehicle == null) {
                throw new Exception("No se encontró un vehículo con la placa especificada.");
            }
            remittance.setVehicule(vehicle);

            RequestStatus authorizedStatus = em.createQuery("SELECT rs FROM solicitud_estado rs WHERE rs.status = :status", RequestStatus.class)
                    .setParameter("status", "Autorizado")
                    .getSingleResult();
            remittance.setStatus(authorizedStatus);

            em.getTransaction().commit();
            MessageUtil.addMessage("Remesa autorizada exitosamente.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            MessageUtil.addMessage("Error al autorizar la remesa: " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Remittances> getAuthorizedRemittancesByClient(String clientUsername) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM remesas r WHERE r.clientLogin.username = :username " +
                            "AND r.status.status = :status", Remittances.class)
                    .setParameter("username", clientUsername)
                    .setParameter("status", "Autorizado")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Remittances> getPendingRemittances() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM remesas r WHERE r.status.status = :status", Remittances.class)
                    .setParameter("status", "Pendiente")
                    .getResultList();
        } finally {
            em.close();
        }
    }



}
