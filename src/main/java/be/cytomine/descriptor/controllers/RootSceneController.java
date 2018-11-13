package be.cytomine.descriptor.controllers;

import be.cytomine.descriptor.data.JobParameter;
import be.cytomine.descriptor.util.AlertHelper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import static be.cytomine.descriptor.util.AlertHelper.*;

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
    @FXML public ComboBox<String> paramTypeCombo;
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
    @FXML public Button addParamButton;
    @FXML public Button editParamButton;
    @FXML public Button removeParamButton;
    @FXML public Button clearParamFieldsButton;
    @FXML public TableView<JobParameter> paramTable;
    @FXML public TableColumn<JobParameter, String> idColumn;
    @FXML public TableColumn<JobParameter, String> nameColumn;
    @FXML public TableColumn<JobParameter, String> descColumn;
    @FXML public TableColumn<JobParameter, String> typeColumn;
    @FXML public TableColumn<JobParameter, String> defaultValueColumn;
    @FXML public TableColumn<JobParameter, Boolean> optionalColumn;
    @FXML public TableColumn<JobParameter, Boolean> setByServerColumn;


    private static int PARAM_TABLE_FROZEN_ROWS_COUNT = 5;
    private ObservableList<String> types;
    private ObservableList<JobParameter> parameters;

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
        paramTypeCombo.setItems(types);

        // buttons
        addParamButton.setText("Add");
        editParamButton.setText("Edit");
        removeParamButton.setText("Delete");
        clearParamFieldsButton.setText("Clear");

        addParamButton.setOnMouseClicked(event -> {
            JobParameter jobParameter = getParamFromFields();
            if (nullorempty(jobParameter.getId()) || nullorempty(jobParameter.getName()) || nullorempty(jobParameter.getType())) {
                AlertHelper.popAlert(Alert.AlertType.WARNING, "Add parameter", "Invalid field", "One of the parameter field {id, name, type} is invalid.", true);
                return;
            }
            parameters.add(jobParameter);
            clearParamFields();
            paramTable.getSelectionModel().select(parameters.size() - 1);
        });

        editParamButton.setDisable(true);
        editParamButton.setOnMouseClicked(event -> {
            JobParameter parameter = paramTable.getSelectionModel().getSelectedItem();
            if (parameter == null) {
                return;
            }
            int index = paramTable.getSelectionModel().getSelectedIndex();
            parameters.set(index, parameter);
        });

        removeParamButton.setOnMouseClicked(event -> {
            int index = paramTable.getSelectionModel().getSelectedIndex();
            paramTable.getSelectionModel().select(-1);
            parameters.remove(index);
        });

        clearParamFieldsButton.setOnMouseClicked(event -> {
            clearParamFields();
        });

        // table
        parameters = FXCollections.observableArrayList();
        parameters.addAll(JobParameter.getDefaultCytomineParameters());
        paramTable.setItems(parameters);
        paramTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean disableButtons = paramTable.getSelectionModel().getSelectedIndex() < PARAM_TABLE_FROZEN_ROWS_COUNT;
            editParamButton.setDisable(disableButtons);
            removeParamButton.setDisable(disableButtons);
            if (newValue == null) {
                clearParamFields();
                return;
            }
            fillParamFields(newValue);
        });

        // columns names
        idColumn.setText("Id");
        nameColumn.setText("Name");
        descColumn.setText("Desc.");
        typeColumn.setText("Type");
        defaultValueColumn.setText("Default");
        optionalColumn.setText("Opt.");
        setByServerColumn.setText("Server");

        // columns values
        idColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getId()));
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        descColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDescription()));
        typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType()));
        defaultValueColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDefaultValue()));
        optionalColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().getOptional()));
        setByServerColumn.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().getSetByServer()));

        // Columns widths
        idColumn.prefWidthProperty().bind(paramTable.widthProperty().divide(14).multiply(3));
        nameColumn.prefWidthProperty().bind(paramTable.widthProperty().divide(14).multiply(3));
        descColumn.prefWidthProperty().bind(paramTable.widthProperty().divide(14).multiply(4));
        typeColumn.prefWidthProperty().bind(paramTable.widthProperty().divide(14).multiply(1));
        defaultValueColumn.prefWidthProperty().bind(paramTable.widthProperty().divide(14).multiply(1));
        optionalColumn.prefWidthProperty().bind(paramTable.widthProperty().divide(14).multiply(1));
        setByServerColumn.prefWidthProperty().bind(paramTable.widthProperty().divide(14).multiply(1));

    }

    private void clearParamFields() {
        paramIdField.setText("");
        paramDefaultField.setText("");
        paramNameField.setText("");
        paramDescField.setText("");
        paramTypeCombo.getSelectionModel().clearSelection();
        optionalCheckBox.setSelected(false);
        setByServerCheckBox.setSelected(false);
    }

    private void fillParamFields(JobParameter parameter) {
        paramIdField.setText(parameter.getId());
        paramNameField.setText(parameter.getName());
        paramDescField.setText(parameter.getDescription());
        paramTypeCombo.getSelectionModel().select(parameter.getType());
        paramDefaultField.setText(parameter.getDefaultValue());
        optionalCheckBox.setSelected(parameter.getOptional());
        setByServerCheckBox.setSelected(parameter.getSetByServer());
    }

    private JobParameter getParamFromFields() {
        return new JobParameter(
            trimornull(paramIdField.getText()),
            trimornull(paramNameField.getText()),
            trimornull(paramDescField.getText()),
            paramTypeCombo.getSelectionModel().getSelectedItem(),
            trimornull(paramDefaultField.getText()),
            optionalCheckBox.isSelected(),
            setByServerCheckBox.isSelected()
        );
    }

    private static String trimornull(String s) {
        return s == null ? null : s.trim();
    }

    private static boolean nullorempty(String s) {
        return s == null || s.isEmpty();
    }
}
