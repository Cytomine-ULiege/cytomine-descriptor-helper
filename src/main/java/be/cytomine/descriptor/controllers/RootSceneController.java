package be.cytomine.descriptor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    @FXML public Label editParamsTitleLabel;
    @FXML public Label paramIdLabel;
    @FXML public TextField paramIdField;
    @FXML public Label paramTypeLabel;
    @FXML public ComboBox<String> paramNameCombo;
    @FXML public Label paramDefaultLabel;
    @FXML public TextField paramDefaultField;
    @FXML public Label optionalLabel;
    @FXML public CheckBox optionalCheckBox;
    @FXML public Label setByServerLabel;
    @FXML public CheckBox setByServerCheckBox;
    @FXML public Label paramNameLabel;
    @FXML public TextField paramNameField;
    @FXML public Label paramDescLabel;
    @FXML public TextArea paramDescField;

    private ObservableList<String> types;

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
        editParamsTitleLabel.setText("Parameters");
        paramIdLabel.setText("Id");
        paramNameLabel.setText("Name");
        paramDescLabel.setText("Description");
        paramTypeLabel.setText("Type");
        paramDefaultLabel.setText("Default value");
        optionalLabel.setText("Optional");
        setByServerLabel.setText("Set-by-server");

        types = FXCollections.observableArrayList();
        types.addAll("Number", "String", "Boolean", "Domain", "ListDomain", "Date");
        paramNameCombo.setItems(types);


    }
}
