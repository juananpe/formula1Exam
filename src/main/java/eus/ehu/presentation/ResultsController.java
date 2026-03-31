package eus.ehu.presentation;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.businesslogic.BusinessLogic;
import eus.ehu.domain.Pilot;
import eus.ehu.domain.Race;
import eus.ehu.domain.RaceResult;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultsController {

    @FXML
    private ComboBox<Race> raceComboBox;

    @FXML
    private TableView<Pilot> driversTable;

    @FXML
    private TableColumn<Pilot, String> driverNameColumn;

    @FXML
    private TableColumn<Pilot, String> driverTeamColumn;

    @FXML
    private TableColumn<Pilot, Integer> positionColumn;

    @FXML
    private TableColumn<Pilot, Integer> pointsColumn;

    @FXML
    private Button loadDriversButton;

    @FXML
    private Button saveResultsButton;

    private BlInterface bl = new BusinessLogic();
    private ObservableList<Pilot> drivers = FXCollections.observableArrayList();
    private Map<Pilot, Integer> positionsMap = new HashMap<>();
    private Map<Pilot, Integer> pointsMap = new HashMap<>();
    private Map<Pilot, RaceResult> existingResultsMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Setup the race combo box
        ObservableList<Race> races = FXCollections.observableArrayList(bl.getAllRaces());
        raceComboBox.setItems(races);
        
        // Setup driver display name in ComboBox
        raceComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Race race) {
                return race != null ? race.getName() : "";
            }

            @Override
            public Race fromString(String string) {
                return raceComboBox.getItems().stream()
                        .filter(race -> race.getName().equals(string))
                        .findFirst().orElse(null);
            }
        });

        // Setup table columns
        driverNameColumn.setCellValueFactory(data -> 
                new SimpleStringProperty(data.getValue().getName()));
        
        driverTeamColumn.setCellValueFactory(data -> 
                new SimpleStringProperty(data.getValue().getTeam() != null ? 
                        data.getValue().getTeam().toString() : "No Team"));
        
        // Position column with editable cells
        positionColumn.setCellValueFactory(data -> {
            // Default position is 0 if not set
            Integer position = positionsMap.getOrDefault(data.getValue(), 0);
            return new SimpleObjectProperty<>(position);
        });
        
        positionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        positionColumn.setOnEditCommit(event -> {
            Pilot driver = event.getRowValue();
            positionsMap.put(driver, event.getNewValue());
            updateSaveButtonState();
        });
        
        // Points column with editable cells
        pointsColumn.setCellValueFactory(data -> {
            // Default points is 0 if not set
            Integer points = pointsMap.getOrDefault(data.getValue(), 0);
            return new SimpleObjectProperty<>(points);
        });
        
        pointsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        pointsColumn.setOnEditCommit(event -> {
            Pilot driver = event.getRowValue();
            pointsMap.put(driver, event.getNewValue());
            updateSaveButtonState();
        });
        
        // Enable table editing
        driversTable.setEditable(true);
        
        // Set table items
        driversTable.setItems(drivers);
    }

    @FXML
    void onLoadDrivers(ActionEvent event) {
        Race selectedRace = raceComboBox.getValue();
        
        if (selectedRace == null) {
            showAlert("Error", "Please select a race first.");
            return;
        }
        
        // Clear previous data
        drivers.clear();
        positionsMap.clear();
        pointsMap.clear();
        existingResultsMap.clear();
        
        // Check if we already have results for this race
        List<RaceResult> existingResults = bl.getRaceResults(selectedRace);
        
        if (!existingResults.isEmpty()) {
            // Load existing results
            for (RaceResult result : existingResults) {
                Pilot driver = result.getDriver();
                drivers.add(driver);
                positionsMap.put(driver, result.getPosition());
                pointsMap.put(driver, result.getPoints());
                existingResultsMap.put(driver, result);
            }
            showAlert("Information", "Loaded existing results for this race. You can edit and save changes.");
        } else {
            // Load all drivers participating in this race
            drivers.addAll(selectedRace.getDrivers());
            
            // Initialize with default values
            for (Pilot driver : drivers) {
                positionsMap.put(driver, 0);
                pointsMap.put(driver, 0);
            }
        }
        
        // Enable save button if we have drivers
        saveResultsButton.setDisable(drivers.isEmpty());
        
        // Refresh the table
        driversTable.refresh();
    }

    @FXML
    void onSaveResults(ActionEvent event) {
        Race selectedRace = raceComboBox.getValue();
        
        if (selectedRace == null) {
            showAlert("Error", "Please select a race first.");
            return;
        }
        
        // Validate all drivers have positions and points
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder("Please fix the following issues:\n");
        
        for (Pilot driver : drivers) {
            int position = positionsMap.getOrDefault(driver, 0);
            int points = pointsMap.getOrDefault(driver, 0);
            
            if (position <= 0) {
                isValid = false;
                errorMessage.append("- ").append(driver.getName()).append(" has an invalid position\n");
            }
            
            if (points < 0) {
                isValid = false;
                errorMessage.append("- ").append(driver.getName()).append(" has negative points\n");
            }
        }
        
        // Check for duplicate positions
        Map<Integer, List<Pilot>> positionGroups = drivers.stream()
                .collect(Collectors.groupingBy(driver -> positionsMap.getOrDefault(driver, 0)));
        
        for (Map.Entry<Integer, List<Pilot>> entry : positionGroups.entrySet()) {
            if (entry.getKey() > 0 && entry.getValue().size() > 1) {
                isValid = false;
                errorMessage.append("- Position ").append(entry.getKey())
                        .append(" has multiple drivers assigned\n");
            }
        }
        
        if (!isValid) {
            showAlert("Validation Error", errorMessage.toString());
            return;
        }
        
        // Save results for each driver
        for (Pilot driver : drivers) {
            int position = positionsMap.get(driver);
            int points = pointsMap.get(driver);

            if (existingResultsMap.containsKey(driver)) {
                bl.updateRaceResult(existingResultsMap.get(driver), position, points);
            } else {
                bl.saveRaceResult(selectedRace, driver, position, points);
            }
        }
        
        showAlert("Success", "Race results saved successfully!");
    }
    
    private void updateSaveButtonState() {
        // Enable save button if we have valid data
        boolean hasValidData = false;
        
        for (Pilot driver : drivers) {
            int position = positionsMap.getOrDefault(driver, 0);
            if (position > 0) {
                hasValidData = true;
                break;
            }
        }
        
        saveResultsButton.setDisable(!hasValidData);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 