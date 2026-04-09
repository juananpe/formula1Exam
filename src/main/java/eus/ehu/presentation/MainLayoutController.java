package eus.ehu.presentation;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.businesslogic.BusinessLogic;
import eus.ehu.data_access.DbAccessManager;
import eus.ehu.data_access.MockDataGenerator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainLayoutController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private BorderPane contentPane;

    // Cache for loaded FXML content
    private final Map<String, AnchorPane> contentCache = new HashMap<>();
    private final Map<String, Object> controllerCache = new HashMap<>();

    private BlInterface bl = new BusinessLogic();

    @FXML
    void onDriversButtonClick(ActionEvent event) {
        loadContent("drivers.fxml");
    }

    @FXML
    void onTeamsButtonClick(ActionEvent event) {
        loadContent("teams.fxml");
    }

    @FXML
    void onRacesButtonClick(ActionEvent event) {
        loadContent("races.fxml");
    }

    @FXML
    void onResultsButtonClick(ActionEvent event) {
        loadContent("results.fxml");
    }

    @FXML
    void onResetButtonClick(ActionEvent event) {
        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Reset Database");
        confirmAlert.setHeaderText("Reset all data?");
        confirmAlert.setContentText(
                "This will delete ALL data and regenerate it with sample values. This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Reset the database
            bl.resetData();

            // Regenerate mock data
            DbAccessManager dataManager = new DbAccessManager();
            MockDataGenerator.generateMockData(dataManager);
            dataManager.close();

            // Clear the content cache so views will reload fresh data
            contentCache.clear();
            controllerCache.clear();

            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Database Reset");
            successAlert.setContentText("All data has been reset and regenerated successfully!");
            successAlert.showAndWait();

            // Reload current view to show fresh data
            loadContent("drivers.fxml");
        }
    }

    private void loadContent(String fxmlFile) {
        try {
            // Check if content is already cached
            AnchorPane content = contentCache.get(fxmlFile);
            if (content == null) {
                // If not cached, load it and store in cache
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                content = loader.load();
                contentCache.put(fxmlFile, content);
                controllerCache.put(fxmlFile, loader.getController());
            } else {
                // Refresh controller if it supports it
                Object controller = controllerCache.get(fxmlFile);
                if (controller instanceof Refreshable) {
                    ((Refreshable) controller).refresh();
                }
            }
            contentPane.setCenter(content);
        } catch (LoadException e) {
            // alert the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading content");
            alert.setContentText("Maybe you forgot to run the H2WebLauncher class?");
            alert.showAndWait();

            // exit javafx application
            Platform.exit();

        } catch (IOException e) {
            System.out.println("Error loading content");
        }
    }

    @FXML
    public void initialize() {
        // Load drivers view by default
        loadContent("drivers.fxml");
    }
}