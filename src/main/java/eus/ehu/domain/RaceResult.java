package eus.ehu.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class RaceResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Race race;
    
    @ManyToOne
    private Pilot driver;
    
    private int position;
    private int points;
    
    // Required by JPA
    public RaceResult() {
    }
    
    public RaceResult(Race race, Pilot driver, int position, int points) {
        this.race = race;
        this.driver = driver;
        this.position = position;
        this.points = points;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public Race getRace() {
        return race;
    }
    
    public void setRace(Race race) {
        this.race = race;
    }
    
    public Pilot getDriver() {
        return driver;
    }
    
    public void setDriver(Pilot driver) {
        this.driver = driver;
    }
    
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    
    public int getPoints() {
        return points;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }
} 