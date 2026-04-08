package eus.ehu.data_access;

import eus.ehu.domain.Pilot;
import eus.ehu.domain.RaceResult;
import eus.ehu.domain.Team;
import eus.ehu.domain.Race;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class DbAccessManager {

    protected EntityManager db;
    protected EntityManagerFactory emf;

    public DbAccessManager() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            emf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            db = emf.createEntityManager();
            System.out.println("DataBase opened");
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Error creating EntityManagerFactory: " + e.getMessage(), e);
        }

    }

    public List<Pilot> getPilotsByNationality(String nat) {
        TypedQuery<Pilot> q2 = db.createQuery(
                "SELECT p FROM Pilot p WHERE p.nationality = \"" + nat + "\"",
                Pilot.class);
        List<Pilot> pilots = q2.getResultList();

        return pilots;
    }


    public void storePilot(String name, String nat, int pts) {
        db.getTransaction().begin();
        Pilot pilot = new Pilot(name, nat, pts);
        db.persist(pilot);
        db.getTransaction().commit();
        System.out.println(pilot + " has been saved");
    }

    public List<Pilot> getAllPilots() {
        TypedQuery<Pilot> q1 = db.createQuery(
                "SELECT p FROM Pilot p", Pilot.class);
        List<Pilot> pilots = q1.getResultList();
        return pilots;
    }

    public List<Pilot> getPilotsWithMoreThanPoints(int points) {
        TypedQuery<Pilot> q3 = db.createQuery(
                "SELECT p FROM Pilot p WHERE p.points > :points", Pilot.class);
        q3.setParameter("points", points);
        List<Pilot> pilots = q3.getResultList();
        return pilots;
    }

    public void deletePilotByName(String name) {
        db.getTransaction().begin();
        Query query = db.createQuery("DELETE FROM Pilot p WHERE p.name = :name");
        query.setParameter("name", name);
        int rowsDeleted = query.executeUpdate();
        db.getTransaction().commit();
        System.out.println(rowsDeleted + " pilot(s) deleted");
    }

    /**
     * Deletes a pilot from the database
     * @param p the pilot to delete
     */
    public void deletePilot(Pilot p) {
        db.getTransaction().begin();
        db.remove(p);
        db.getTransaction().commit();
        System.out.println(p + " has been deleted");
    }

    /**
     * Stores a team in the database
     * @param name the name of the team
     * @param country the country of the team
     */
    public void storeTeam(String name, String country){
        db.getTransaction().begin();
        Team team = new Team(name, country);
        db.persist(team);
        db.getTransaction().commit();
        System.out.println(team + " has been saved");
    }

    /**
     * Retrieves a team from the database by its name
     * @param name the name of the team
     * @return the team with the given name
     */
    public Team findTeamByName(String name){
        TypedQuery<Team> q = db.createQuery(
                "SELECT t FROM Team t WHERE t.name = :name", Team.class);
        q.setParameter("name", name);
        return q.getSingleResult();
    }

    /**
     * Retrieves a pilot from the database by their name
     * @param name the name of the pilot
     * @return the pilot with the given name
     */
    public Pilot findPilotByName(String name){
        TypedQuery<Pilot> q = db.createQuery(
                "SELECT p FROM Pilot p WHERE p.name = :name", Pilot.class);
        q.setParameter("name", name);
        return q.getSingleResult();
    }

    /**
     * Adds a pilot to a team
     * @param pilot
     * @param team
     */
    public void addPilotToTeam(Pilot pilot, Team team){
        db.getTransaction().begin();
        team.addPilot(pilot);
        db.getTransaction().commit();
        System.out.println(pilot + " has been added to " + team);
    }

    /**
     * Adds a pilot to a race and persists the relationship
     * @param pilot the pilot to add
     * @param race the race to add the pilot to
     */
    public void addPilotToRace(Pilot pilot, Race race) {
        db.getTransaction().begin();
        
        // If the race is not yet persisted, persist it first
        if (race.getId() == null) {
            db.persist(race);
        }
        
        // Update the relationship
        race.addDriver(pilot);
        
        // Merge the entities to update them in the database
        db.merge(pilot);
        db.merge(race);
        
        db.getTransaction().commit();
        System.out.println(pilot.getName() + " has been added to race: " + race.getName());
    }

    /**
     * Stores a race in the database
     * @param race the race to store
     */
    public void storeRace(Race race) {
        db.getTransaction().begin();
        db.persist(race);
        db.getTransaction().commit();
        System.out.println("Race " + race.getName() + " has been saved");
    }
    
    /**
     * Get all races from the database
     * @return list of all races
     */
    public List<Race> getAllRaces() {
        TypedQuery<Race> query = db.createQuery("SELECT r FROM Race r", Race.class);
        return query.getResultList();
    }
    
    /**
     * Get a race by its ID
     * @param id the race ID
     * @return the race
     */
    public Race getRaceById(Long id) {
        return db.find(Race.class, id);
    }
    
    /**
     * Save a new race result
     * @param result the race result to save
     */
    public void saveRaceResult(RaceResult result) {
        db.getTransaction().begin();
        db.persist(result);
        result.getDriver().addPoints(result.getPoints());
        db.getTransaction().commit();
    }

    /**
     * Update an existing race result, adjusting the driver's points by the difference.
     * @param result   the already-persisted RaceResult to update
     * @param position new position
     * @param points   new points
     */
    public void updateRaceResult(RaceResult result, int position, int points) {
        int pointsDiff = points - result.getPoints();
        db.getTransaction().begin();
        result.setPosition(position);
        result.setPoints(points);
        result.getDriver().addPoints(pointsDiff);
        db.getTransaction().commit();
    }
    
    /**
     * Update a driver's points by adding more points
     * @param driver the driver to update
     * @param additionalPoints points to add
     */
    public void updateDriverPoints(Pilot driver, int additionalPoints) {
        db.getTransaction().begin();
        driver.addPoints(additionalPoints);
        db.merge(driver);
        db.getTransaction().commit();
        System.out.println("Updated " + driver.getName() + "'s points by adding " + additionalPoints);
    }
    
    /**
     * Get existing race results for a race
     * @param race the race
     * @return list of race results
     */
    /**
     * Get all teams from the database
     * @return list of all teams
     */
    public List<Team> getAllTeams() {
        TypedQuery<Team> query = db.createQuery("SELECT t FROM Team t", Team.class);
        return query.getResultList();
    }

    public List<RaceResult> getRaceResults(Race race) {
        TypedQuery<RaceResult> query = db.createQuery(
                "SELECT rr FROM RaceResult rr WHERE rr.race = :race", RaceResult.class);
        query.setParameter("race", race);
        return query.getResultList();
    }

    public void close() {
        db.close();
        System.out.println("DataBase is closed");
    }
}
