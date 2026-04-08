package eus.ehu.presentation;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.businesslogic.BusinessLogic;
import eus.ehu.domain.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TeamsController {

    @FXML
    private TableView<Team> tableTeams;
    
    @FXML
    private TableColumn<Team, String> nameColumn;
    
    @FXML
    private TableColumn<Team, String> countryColumn;

    @FXML
    private Label numTeams;

    private ObservableList<Team> teams;

    private BlInterface bl = new BusinessLogic();

    @FXML
    public void initialize() {
        // Set up the column cell value factories
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        
        // Initialize the observable list and populate the table
        teams = FXCollections.observableArrayList();
        teams.addAll(bl.getAllTeams());
        tableTeams.setItems(teams);
        
        // Create a binding that updates when the list changes
        numTeams.setText(String.valueOf(teams.size()));
        teams.addListener((javafx.collections.ListChangeListener<Team>) c -> numTeams.setText(String.valueOf(teams.size())));
    }
}
