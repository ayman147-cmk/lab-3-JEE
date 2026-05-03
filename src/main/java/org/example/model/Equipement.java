package org.example.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "facility_assets")
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "The asset title must be provided")
    @Column(name = "asset_title", nullable = false)
    private String nom;

    @Size(max = 480, message = "The asset overview is limited to 480 characters")
    @Column(name = "asset_summary", length = 480)
    private String description;

    @ManyToMany(mappedBy = "equipements", fetch = FetchType.LAZY)
    private Set<Salle> salles = new HashSet<>();


    public Equipement() {
    }


    public Equipement(String nom) {
        this.nom = nom;
    }

    public Equipement(String nom, String description) {
        this.nom = nom;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<Salle> getSalles() {
        return salles;
    }

    public void setSalles(Set<Salle> salles) {
        this.salles = salles;
    }

    @Override
    public String toString() {
        return String.format("Asset #%d: %s (Description: %s)",
                id, nom, description != null ? description : "N/A");
    }
}