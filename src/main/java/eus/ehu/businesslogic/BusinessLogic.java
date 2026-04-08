package eus.ehu.businesslogic;

import eus.ehu.data_access.DbAccessManager;
import eus.ehu.domain.Pilot;
import eus.ehu.domain.Race;
import eus.ehu.domain.RaceResult;
import eus.ehu.domain.Team;

import java.util.List;

public class BusinessLogic implements BlInterface {

    DbAccessManager db = new DbAccessManager();

    @Override
    public List<Pilot> getPilots() {
        return db.getAllPilots();
    }

    @Override
    public List<Pilot> getPilotsByNationality(String nat) {
        return null;
    }

    @Override
    public void storePilot(String name, String nat, int pts) {
        db.storePilot(name, nat, pts);
    }

    @Override
    public void deletePilotByName(String name) {

    }

    @Override
    public void deletePilot(Pilot p) {
        db.deletePilot(p);
    }
    
    @Override
    public List<Team> getAllTeams() {
        return db.getAllTeams();
    }
    
    @Override
    public List<Race> getAllRaces() {
        return db.getAllRaces();
    }
    
    @Override
    public Race getRaceById(Long id) {
        return db.getRaceById(id);
    }
    
    @Override
    public void saveRaceResult(Race race, Pilot driver, int position, int points) {
        RaceResult result = new RaceResult(race, driver, position, points);
        db.saveRaceResult(result);
    }

    @Override
    public void updateRaceResult(RaceResult result, int position, int points) {
        db.updateRaceResult(result, position, points);
    }

    @Override
    public List<RaceResult> getRaceResults(Race race) {
        return db.getRaceResults(race);
    }
}
