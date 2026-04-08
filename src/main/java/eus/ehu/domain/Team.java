package eus.ehu.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
    @Id
    private String name;
    private String country;

    @OneToMany(mappedBy = "team")
    private List<Pilot> pilots = new ArrayList<>();

    public Team(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public Team() {

    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public void addPilot(Pilot pilot) {
        pilots.add(pilot);
        pilot.setTeam(this);
    }

    @Override
    public String toString() {
        // return name - country
        return name + " - " + country;
    }
}
