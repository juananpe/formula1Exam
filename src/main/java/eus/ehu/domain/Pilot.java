package eus.ehu.domain;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Pilot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nationality;
    private int points;

    @ManyToOne
    private Team team;

    @ManyToMany
    private Set<Race> races = new HashSet<>();

    public Pilot(String name, String nat, int pts) {
        this.name = name;
        this.nationality = nat;
        this.points = pts;
    }

    // overloaded constructor
    public Pilot(String name, String nat) {
        this.name = name;
        this.nationality = nat;
        this.points = 0;
    }

    public Pilot(String name) {
        this.name = name;
        this.nationality = "unknown";
        this.points = 0;
    }

    public Pilot() {

    }

    public void addPoints(int morePoints) {
        this.points += morePoints;
    }

    @Override
    public String toString() {

        return String.format("%s (%s) - %d points . Team : %s", name, nationality, points, team);
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Get the team of this pilot
     * @return the team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Add a race to this pilot's races
     * @param race the race to add
     */
    public void addRace(Race race) {
        races.add(race);
    }

    /**
     * Get all races this pilot participates in
     * @return set of races
     */
    public Set<Race> getRaces() {
        return races;
    }

    /**
     * Get the name of this pilot
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the nationality of this pilot
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Get the points of this pilot
     * @return the points
     */
    public int getPoints() {
        return points;
    }
}
