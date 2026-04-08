package eus.ehu.presentation;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.businesslogic.BusinessLogic;
import eus.ehu.domain.Race;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RacesController {

    @FXML
    private TableView<Race> tableRaces;
    
    @FXML
    private TableColumn<Race, String> nameColumn;
    
    @FXML
    private TableColumn<Race, java.time.LocalDate> dateColumn;

    @FXML
    private Label numRaces;

    private ObservableList<Race> races;

    private BlInterface bl = new BusinessLogic();

    @FXML
    public void initialize() {
        // Set up the column cell value factories
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("raceDate"));
        
        // Initialize the observable list and populate the table
        races = FXCollections.observableArrayList();
        races.addAll(bl.getAllRaces());
        tableRaces.setItems(races);
        
        // Create a binding that updates when the list changes
        numRaces.setText(String.valueOf(races.size()));
        races.addListener((javafx.collections.ListChangeListener<Race>) c -> numRaces.setText(String.valueOf(races.size())));
    }
}
