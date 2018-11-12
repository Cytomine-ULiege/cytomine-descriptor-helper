package be.cytomine.descriptor.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Romain on 12-11-18.
 * This is a class.
 */
public class RootSceneController implements Initializable {
    @FXML public Label titleLabel;

    @FXML public Label softwareNameLabel;
    @FXML public TextField softwareNameField;
    @FXML public Label prefixLabel;
    @FXML public TextField prefixField;
    @FXML public Label dockerhubLabel;
    @FXML public TextField dockerhubField;
    @FXML public Label schemaVersionLabel;
    @FXML public TextField schemaVersionField;
    @FXML public Label descriptionLabel;
    @FXML public TextArea descriptionField;
    @FXML public Label cliPythonLabel;
    @FXML public TextField cliPythonField;
    @FXML public Label cliScriptLabel;
    @FXML public TextField cliScriptField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText("Descriptor helper");
        softwareNameLabel.setText("Software name");
        prefixLabel.setText("Prefix");
        prefixField.setText("S_");
        dockerhubLabel.setText("DockerHub");
        dockerhubField.setText("cytomine-uliege");
        schemaVersionLabel.setText("Schema version");
        schemaVersionField.setText("cytomine-0.1");
        descriptionLabel.setText("Description");
        descriptionField.setText("A cytomine software ....");
        cliPythonLabel.setText("Executable");
        cliPythonField.setText("python");
        cliScriptLabel.setText("Script");
        cliScriptField.setText("wrapper.py");

    }
}
