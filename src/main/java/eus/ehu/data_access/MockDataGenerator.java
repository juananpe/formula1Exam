package eus.ehu.data_access;

import eus.ehu.domain.Pilot;
import eus.ehu.domain.Race;
import eus.ehu.domain.RaceResult;
import eus.ehu.domain.Team;

import java.time.LocalDate;

public class MockDataGenerator {
    
    /**
     * Generates mock data for the Formula 1 database.
     * This can be called to reset/populate the database with sample data.
     * @param dataManager the database access manager to use
     */
    public static void generateMockData(DbAccessManager dataManager) {
        dataManager.storePilot("Lewis Hamilton", "British", 380);
        // Include here additional instructions for another 6 pilots. You can
        // get info at https://www.formula1.com/en/drivers.html
        dataManager.storePilot("Valtteri Bottas", "Finnish", 223);
        dataManager.storePilot("Max Verstappen", "Dutch", 226);
        dataManager.storePilot("Sergio Perez", "Mexican", 125);
        dataManager.storePilot("Lando Norris", "British", 132);
        dataManager.storePilot("Charles Leclerc", "Monaco", 98);
        dataManager.storePilot("Daniel Ricciardo", "Australian", 115);


        Pilot hamilton = dataManager.findPilotByName("Lewis Hamilton");
        Pilot verstappen = dataManager.findPilotByName("Max Verstappen");
        Pilot leclerc = dataManager.findPilotByName("Charles Leclerc");

        // Create races
        Race monzaGP = new Race("Monza", LocalDate.parse("2023-09-03"));
        Race monacaGP = new Race("Monte Carlo", LocalDate.parse("2023-05-28"));
        Race silverstone = new Race("Silverstone", LocalDate.parse("2023-07-09"));

        // Store races in the database
        dataManager.storeRace(monzaGP);
        dataManager.storeRace(monacaGP);
        dataManager.storeRace(silverstone);

        // Add drivers to races using the new method
        dataManager.addPilotToRace(hamilton, monzaGP);
        dataManager.addPilotToRace(verstappen, monzaGP);
        dataManager.addPilotToRace(leclerc, monzaGP);

        dataManager.addPilotToRace(hamilton, monacaGP);
        dataManager.addPilotToRace(verstappen, monacaGP);
        dataManager.addPilotToRace(leclerc, monacaGP);

        dataManager.addPilotToRace(hamilton, silverstone);
        dataManager.addPilotToRace(verstappen, silverstone);

        // Create teams
        dataManager.storeTeam("Mercedes", "Germany");
        dataManager.storeTeam("Red Bull Racing", "Austria");
        dataManager.storeTeam("Ferrari", "Italy");
        dataManager.storeTeam("McLaren", "United Kingdom");
        dataManager.storeTeam("Alfa Romeo", "Switzerland");
        dataManager.storeTeam("Alpine", "France");

        // Get teams from database
        Team mercedes = dataManager.findTeamByName("Mercedes");
        Team redBull = dataManager.findTeamByName("Red Bull Racing");
        Team ferrari = dataManager.findTeamByName("Ferrari");
        Team mclaren = dataManager.findTeamByName("McLaren");
        Team alfaRomeo = dataManager.findTeamByName("Alfa Romeo");
        Team alpine = dataManager.findTeamByName("Alpine");

        // Assign drivers to teams
        dataManager.addPilotToTeam(hamilton, mercedes);
        dataManager.addPilotToTeam(dataManager.findPilotByName("Valtteri Bottas"), alfaRomeo);
        dataManager.addPilotToTeam(verstappen, redBull);
        dataManager.addPilotToTeam(dataManager.findPilotByName("Sergio Perez"), redBull);
        dataManager.addPilotToTeam(dataManager.findPilotByName("Lando Norris"), mclaren);
        dataManager.addPilotToTeam(leclerc, ferrari);
        dataManager.addPilotToTeam(dataManager.findPilotByName("Daniel Ricciardo"), alpine);

        // Print all pilots with their teams
        System.out.println("\nDrivers and their teams:");
        for (Pilot p : dataManager.getAllPilots()) {
            System.out.println(p);
        }

        // Print race participants again to see teams
        System.out.println("\nRace Entries with Teams:");
        System.out.println("Monza GP Drivers: " + monzaGP.getDrivers());
        System.out.println("Monaco GP Drivers: " + monacaGP.getDrivers());
        System.out.println("Silverstone GP Drivers: " + silverstone.getDrivers());

        // Create some sample race results for Monza GP
        System.out.println("\nCreating sample race results for Monza GP...");
        // Create result for Hamilton (1st place, 25 points)
        RaceResult hamiltonMonza = new RaceResult(monzaGP, hamilton, 1, 25);
        dataManager.saveRaceResult(hamiltonMonza);
        
        // Create result for Verstappen (2nd place, 18 points)
        RaceResult verstappenMonza = new RaceResult(monzaGP, verstappen, 2, 18);
        dataManager.saveRaceResult(verstappenMonza);
        
        // Create result for Leclerc (3rd place, 15 points)
        RaceResult leclercMonza = new RaceResult(monzaGP, leclerc, 3, 15);
        dataManager.saveRaceResult(leclercMonza);
        
        System.out.println("Sample race results created successfully!");
    }
    
    public static void main(String[] args) {
        DbAccessManager dataManager = new DbAccessManager();
        generateMockData(dataManager);
        dataManager.close();
    }
}
