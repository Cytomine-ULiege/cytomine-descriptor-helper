package be.cytomine.descriptor.controllers;

import be.cytomine.descriptor.data.JobParameter;
import be.cytomine.descriptor.data.Software;
import be.cytomine.descriptor.util.AlertHelper;
import be.cytomine.descriptor.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
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
    @FXML public Button paramUpButton;
    @FXML public Button paramDownButton;
    @FXML public TableView<JobParameter> paramTable;
    @FXML public TableColumn<JobParameter, String> idColumn;
    @FXML public TableColumn<JobParameter, String> nameColumn;
    @FXML public TableColumn<JobParameter, String> descColumn;
    @FXML public TableColumn<JobParameter, String> typeColumn;
    @FXML public TableColumn<JobParameter, String> defaultValueColumn;
    @FXML public TableColumn<JobParameter, Boolean> optionalColumn;
    @FXML public TableColumn<JobParameter, Boolean> setByServerColumn;
    @FXML public Button generateButton;
    @FXML public Button loadButton;
    @FXML public Button newButton;

    private static int PARAM_TABLE_FROZEN_ROWS_COUNT = 5;
    private ObservableList<String> types;
    private ObservableList<JobParameter> parameters;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText("Descriptor helper");
        softwareNameLabel.setText("Software name");
        prefixLabel.setText("Prefix");
        dockerhubLabel.setText("DockerHub");
        schemaVersionLabel.setText("Schema version");
        descriptionLabel.setText("Description");
        cliPythonLabel.setText("Executable");
        cliScriptLabel.setText("Script");
        editParamsTitleLabel.setText("Parameters");
        paramIdLabel.setText("Id");
        paramNameLabel.setText("Name");
        paramDescLabel.setText("Description");
        paramTypeLabel.setText("Type");
        paramDefaultLabel.setText("Default value");
        optionalLabel.setText("Optional");
        setByServerLabel.setText("Set-by-server");
        generateButton.setText("Save file");
        loadButton.setText("Load from file");
        newButton.setText("New descriptor");

        types = FXCollections.observableArrayList();
        types.addAll("Number", "String", "Boolean", "Domain", "ListDomain", "Date");
        paramTypeCombo.setItems(types);

        // buttons
        addParamButton.setText("Add");
        editParamButton.setText("Save");
        removeParamButton.setText("Delete");
        clearParamFieldsButton.setText("Clear form");
        paramUpButton.setText("Up");
        paramDownButton.setText("Down");

        addParamButton.setOnMouseClicked(event -> {
            JobParameter jobParameter = getParamFromFields();
            if (StringUtil.nullorempty(jobParameter.getId()) || StringUtil.nullorempty(jobParameter.getName()) || StringUtil.nullorempty(jobParameter.getType())) {
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
            parameters.set(index, getParamFromFields());
        });

        removeParamButton.setDisable(true);
        removeParamButton.setOnMouseClicked(event -> {
            int index = paramTable.getSelectionModel().getSelectedIndex();
            paramTable.getSelectionModel().select(-1);
            parameters.remove(index);
        });

        clearParamFieldsButton.setOnMouseClicked(event -> {
            clearParamFields();
            paramTable.getSelectionModel().select(-1);
            setDisableParamButtons(true);
        });

        paramUpButton.setDisable(true);
        paramUpButton.setOnMouseClicked(event -> {
            int index = paramTable.getSelectionModel().getSelectedIndex();
            if (index <= PARAM_TABLE_FROZEN_ROWS_COUNT) {
                return;
            }
            swapParamsInTable(index, index - 1);
            paramTable.getSelectionModel().select(index - 1);
        });

        paramDownButton.setDisable(true);
        paramDownButton.setOnMouseClicked(event -> {
            int index = paramTable.getSelectionModel().getSelectedIndex();
            if (index == parameters.size() - 1) {
                return;
            }
            swapParamsInTable(index, index + 1);
            paramTable.getSelectionModel().select(index + 1);
        });

        // table
        parameters = FXCollections.observableArrayList();
        paramTable.setItems(parameters);
        paramTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setDisableParamButtons(paramTable.getSelectionModel().getSelectedIndex() < PARAM_TABLE_FROZEN_ROWS_COUNT);
            int index = paramTable.getSelectionModel().getSelectedIndex();
            if (index == parameters.size() - 1) {
                paramDownButton.setDisable(true);
            } else if (index == PARAM_TABLE_FROZEN_ROWS_COUNT) {
                paramUpButton.setDisable(true);
            }
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

        // disable sorting
        idColumn.setSortable(false);
        nameColumn.setSortable(false);
        descColumn.setSortable(false);
        typeColumn.setSortable(false);
        defaultValueColumn.setSortable(false);
        optionalColumn.setSortable(false);
        setByServerColumn.setSortable(false);

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

        // export
        loadButton.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            File file = fileChooser.showOpenDialog(titleLabel.getScene().getWindow());
            if (file == null) {
                return;
            }
            try {
                String json = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                Gson gson = new GsonBuilder().create();
                Type type = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> myMap = gson.fromJson(json, type);
                Software software = Software.fromMap(myMap);
                fillSoftwareForm(software);
            } catch (IOException e) {
                AlertHelper.popException(e);
            }
        });

        generateButton.setOnMouseClicked(event -> {
            Software software = getSofwareFromFields();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(software.toFullMap());
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("descriptor.json");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            File file = fileChooser.showSaveDialog(titleLabel.getScene().getWindow());
            try {
                if (file != null) {
                    try(FileOutputStream fos = new FileOutputStream(file); PrintStream ps = new PrintStream(fos)) {
                        ps.append(json);
                    }
                }
            } catch (IOException e) {
                AlertHelper.popException(e);
            }
        });

        newButton.setOnMouseClicked(event -> {
            initFormDefault();
        });

        initFormDefault();
    }

    private void swapParamsInTable(int row1, int row2) {
        if (row1 < 0 && row2 >= parameters.size()) {
            return;
        }
        JobParameter tmp = parameters.get(row1);
        parameters.set(row1, parameters.get(row2));
        parameters.set(row2, tmp);
    }

    private void setDisableParamButtons(boolean disable) {
        editParamButton.setDisable(disable);
        removeParamButton.setDisable(disable);
        paramUpButton.setDisable(disable);
        paramDownButton.setDisable(disable);
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
        String id = StringUtil.trimornull(paramIdField.getText());
        String name = StringUtil.trimornull(paramNameField.getText());
        name = name == null ? StringUtil.toUpperCaseHuman(id) : name;
        return new JobParameter(
            id,
            name,
            StringUtil.trimornull(paramDescField.getText()),
            paramTypeCombo.getSelectionModel().getSelectedItem(),
            StringUtil.trimornull(paramDefaultField.getText()),
            optionalCheckBox.isSelected(),
            setByServerCheckBox.isSelected()
        );
    }

    private Software getSofwareFromFields() {
        return new Software(
                softwareNameField.getText(),
                dockerhubField.getText(),
                prefixField.getText(),
                schemaVersionField.getText(),
                descriptionField.getText(),
                cliPythonField.getText(),
                cliScriptField.getText(),
                new ArrayList<>(parameters)
        );
    }
    private void initFormDefault() {
        // fields
        prefixField.setText("S_");
        dockerhubField.setText("cytomine-uliege");
        schemaVersionField.setText("cytomine-0.1");
        descriptionField.setText("A cytomine software ....");
        cliPythonField.setText("python");
        cliScriptField.setText("wrapper.py");
        parameters.setAll(JobParameter.getDefaultCytomineParameters());
        clearParamFields();
    }

    private void fillSoftwareForm(Software software) {
        softwareNameField.setText(software.getName());
        prefixField.setText(software.getPrefix());
        dockerhubField.setText(software.getDockerHub());
        schemaVersionField.setText(software.getSchemaVersion());
        descriptionField.setText(software.getDescription());
        cliPythonField.setText(software.getExecutable());
        cliScriptField.setText(software.getScript());
        parameters.setAll(software.getParameters());
        clearParamFields();
    }
}
