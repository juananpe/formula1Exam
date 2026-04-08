package eus.ehu.businesslogic;

import eus.ehu.domain.Pilot;
import eus.ehu.domain.Race;
import eus.ehu.domain.RaceResult;
import eus.ehu.domain.Team;

import java.util.List;

public interface BlInterface {
    List<Pilot> getPilots();
    List<Pilot> getPilotsByNationality(String nat);
    void storePilot(String name, String nat, int pts);
    void deletePilotByName(String name);
    void deletePilot(Pilot selectedPilot);
    
    // Team-related methods
    List<Team> getAllTeams();
    
    // Race-related methods
    List<Race> getAllRaces();
    Race getRaceById(Long id);
    
    // Race results methods
    void saveRaceResult(Race race, Pilot driver, int position, int points);
    void updateRaceResult(RaceResult result, int position, int points);
    List<RaceResult> getRaceResults(Race race);
    
    // Reset method
    void resetData();
}
