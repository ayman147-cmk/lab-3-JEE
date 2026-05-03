package org.example.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "venue_rooms")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Room label is required")
    @Column(name = "room_label", nullable = false)
    private String nom;

    @NotNull(message = "Capacity must be specified")
    @Positive(message = "Capacity must be greater than zero")
    @Column(name = "occupancy_limit", nullable = false)
    private Integer capacite;

    @Size(max = 450, message = "Summary text is too long")
    @Column(name = "room_details", length = 450)
    private String description;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Reservation> reservations = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "rel_room_assets",
            joinColumns = @JoinColumn(name = "room_ref"),
            inverseJoinColumns = @JoinColumn(name = "asset_ref")
    )
    private Set<Equipement> equipements = new HashSet<>();

    public Salle() {
    }

    public Salle(String nom, Integer capacite) {
        this.nom = nom;
        this.capacite = capacite;
    }

    public void registerBooking(Reservation res) {
        this.reservations.add(res);
        res.setSalle(this);
    }

    public void unregisterBooking(Reservation res) {
        this.reservations.remove(res);
        res.setSalle(null);
    }


    public void linkAsset(Equipement asset) {
        this.equipements.add(asset);
        asset.getSalles().add(this);
    }

    public void unlinkAsset(Equipement asset) {
        this.equipements.remove(asset);
        asset.getSalles().remove(this);
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

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<Equipement> getEquipements() {
        return equipements;
    }

    public void setEquipements(Set<Equipement> equipements) {
        this.equipements = equipements;
    }

    @Override
    public String toString() {
        return String.format("Room[ID=%d, Label=%s, Max=%d]", id, nom, capacite);
    }
}