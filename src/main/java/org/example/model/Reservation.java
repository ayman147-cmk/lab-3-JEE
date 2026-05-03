package org.example.model;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_records")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Start timestamp is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime dateDebut;

    @NotNull(message = "End timestamp is required")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime dateFin;

    @Size(max = 400, message = "The reason description is too lengthy")
    @Column(name = "booking_reason", length = 400)
    private String motif;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_ref", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_ref", nullable = false)
    private Salle salle;


    public Reservation() {
    }


    public Reservation(LocalDateTime dateDebut, LocalDateTime dateFin, String motif) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    @Override
    public String toString() {
        return String.format("Booking ID: %d [From: %s To: %s | Reason: %s]",
                id, dateDebut, dateFin, motif);
    }
}