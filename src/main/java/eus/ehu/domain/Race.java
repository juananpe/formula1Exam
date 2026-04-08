package eus.ehu.domain;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


@Entity
public class Race {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String name;
   private LocalDate raceDate;

   @ManyToMany(mappedBy = "races")
   private Set<Pilot> pilots = new HashSet<>();

   // Required by JPA
   public Race() {
   }


   public Race(String name, LocalDate raceDate) {
      this.name = name;
      this.raceDate = raceDate;
   }
         
      /**
    * Get all pilots participating in this race
    * @return set of pilots
    */
    public Set<Pilot> getDrivers() {
      return pilots;
   }
   
   /**
    * Add a pilot to this race
    * Note: This method doesn't update the owning side of the relationship
    * @param pilot the pilot to add
    */
   public void addDriver(Pilot pilot) {
      pilots.add(pilot);
      pilot.addRace(this);
   }
   
   /**
    * Get the ID of this race
    * @return the ID
    */
   public Long getId() {
      return id;
   }
   
   /**
    * Get the name of this race
    * @return the name
    */
   public String getName() {
      return name;
   }
   
   /**
    * Get the date of this race
    * @return the race date
    */
   public java.time.LocalDate getRaceDate() {
      return raceDate;
   }
}
