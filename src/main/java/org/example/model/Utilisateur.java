package org.example.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_users")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Last name cannot be empty")
    @Column(name = "last_name", nullable = false)
    private String nom;

    @NotEmpty(message = "First name cannot be empty")
    @Column(name = "first_name", nullable = false)
    private String prenom;

    @Email(message = "Please provide a valid email address")
    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();


    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }


    public void assignBooking(Reservation res) {
        this.reservations.add(res);
        res.setUtilisateur(this);
    }

    public void cancelBooking(Reservation res) {
        this.reservations.remove(res);
        res.setUtilisateur(null);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return String.format("User Profile [ID: %d, Name: %s %s, Contact: %s]",
                id, prenom, nom, email);
    }
}