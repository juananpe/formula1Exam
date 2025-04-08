package eus.ehu.presentation;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainLayoutController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private BorderPane contentPane;

    // Cache for loaded FXML content
    private final Map<String, AnchorPane> contentCache = new HashMap<>();

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

    private void loadContent(String fxmlFile) {
        try {
            // Check if content is already cached
            AnchorPane content = contentCache.get(fxmlFile);
            if (content == null) {
                // If not cached, load it and store in cache
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                content = loader.load();
                contentCache.put(fxmlFile, content);
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