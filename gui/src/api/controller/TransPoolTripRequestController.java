package api.controller;

import com.jfoenix.controls.*;
import exception.file.StopNotFoundException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.time.LocalTime;

public class TransPoolTripRequestController implements FormController {

    private TransPoolController transPoolController;

    @FXML private JFXTimePicker timeFieldTime;
    @FXML private JFXRadioButton radioButtonArrivalTime;
    @FXML private JFXRadioButton radioButtonDepartureTime;
    @FXML private JFXTextField textFieldSource;
    @FXML private JFXTextField textFieldRiderName;
    @FXML private JFXTextField textFieldDestination;
    @FXML private JFXButton buttonAddRequest;

    private BooleanProperty fileLoaded;

    public TransPoolTripRequestController() {
        fileLoaded = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() {
        timeFieldTime.set24HourView(true);
        timeFieldTime.setValue(LocalTime.now());
        buttonAddRequest.disableProperty().bind(fileLoaded.not());
    }

    public void setTransPoolController(TransPoolController transPoolController) {
        this.transPoolController = transPoolController;
    }

    @FXML
    void newTripRequestButtonAction(ActionEvent event) {
        transPoolController.addNewTripRequest();
    }

    void addNewTripRequest() {
        String source = textFieldSource.getText();
        String destination = textFieldDestination.getText();
        String riderName = textFieldRiderName.getText();
        LocalTime time = timeFieldTime.getValue();
        boolean isArrivalTime = radioButtonArrivalTime.isSelected();
        try {
            transPoolController.getEngine().createNewTransPoolTripRequest(riderName, source, destination, time, isArrivalTime, true);
            transPoolController.clearForm(this);
        } catch (StopNotFoundException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input error.");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @Override
    public void clearForm() {
        timeFieldTime.setValue(LocalTime.now());
        radioButtonDepartureTime.setSelected(true);
        textFieldDestination.setText("");
        textFieldSource.setText("");
        textFieldRiderName.setText("");
    }

    public BooleanProperty fileLoadedProperty() {
        return fileLoaded;
    }
}
