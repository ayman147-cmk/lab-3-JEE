package org.example;

import org.example.model.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class App {

    private static final String UNIT_NAME = "gestion-reservations";

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(UNIT_NAME);

        try {
            App manager = new App();

            manager.processCascadeOperations(factory);
            manager.processOrphanRemoval(factory);
            manager.processInventoryMapping(factory);

        } finally {
            if (factory != null) factory.close();
        }
    }


    private void processCascadeOperations(EntityManagerFactory emf) {
        runInTransaction(emf, em -> {
            Utilisateur lead = new Utilisateur("Smith", "John", "john.smith@provider.com");
            Salle workshop = new Salle("Studio 5", 15);
            workshop.setDescription("High-end creative studio");

            Reservation booking = new Reservation(
                    LocalDateTime.now().plusDays(5),
                    LocalDateTime.now().plusDays(5).plusHours(4),
                    "Project Kickoff"
            );

            lead.addReservation(booking);
            workshop.addReservation(booking);

            em.persist(lead);
            em.persist(workshop);
        });
    }


    private void processOrphanRemoval(EntityManagerFactory emf) {
        // Setup initial data
        runInTransaction(emf, em -> {
            Utilisateur member = new Utilisateur("Brown", "Alice", "alice.b@domain.org");
            Salle hub = new Salle("Meeting Hub", 25);
            em.persist(hub);

            Reservation resA = new Reservation(LocalDateTime.now().plusWeeks(1), LocalDateTime.now().plusWeeks(1).plusHours(2), "Audit");
            Reservation resB = new Reservation(LocalDateTime.now().plusWeeks(2), LocalDateTime.now().plusWeeks(2).plusHours(2), "Review");

            member.addReservation(resA);
            member.addReservation(resB);
            hub.addReservation(resA);
            hub.addReservation(resB);

            em.persist(member);
        });

        runInTransaction(emf, em -> {
            Utilisateur target = em.createQuery("SELECT u FROM Utilisateur u WHERE u.lastName = 'Brown'", Utilisateur.class)
                    .getSingleResult();

            if (!target.getReservations().isEmpty()) {
                Reservation toDelete = target.getReservations().get(0);
                target.removeReservation(toDelete);
            }
        });
    }


    private void processInventoryMapping(EntityManagerFactory emf) {
        runInTransaction(emf, em -> {
            Equipement hardware1 = new Equipement("4K Monitor", "32-inch display");
            Equipement hardware2 = new Equipement("Webcam", "Logitech C920");

            Salle techRoom = new Salle("Tech Zone", 8);
            Salle flexSpace = new Salle("Flex Office", 4);

            techRoom.addEquipement(hardware1);
            techRoom.addEquipement(hardware2);
            flexSpace.addEquipement(hardware2);

            em.persist(techRoom);
            em.persist(flexSpace);
        });
    }


    private void runInTransaction(EntityManagerFactory emf, Consumer<EntityManager> action) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            action.accept(em);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}