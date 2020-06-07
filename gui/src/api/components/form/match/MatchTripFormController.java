package api.components.form.match;

import api.components.form.FormController;
import api.exception.RequiredFieldEmptyException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import data.transpool.TransPoolData;
import data.transpool.trip.offer.PossibleMatch;
import data.transpool.trip.request.BasicTripRequest;
import data.transpool.trip.request.TripRequest;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MatchTripFormController extends FormController {

    @FXML private JFXComboBox<TripRequest> comboBoxRideID;
    @FXML private JFXTextField textFieldNumOfResultsToFind;
    @FXML private JFXListView<PossibleMatch> listViewResults;
    @FXML private JFXButton buttonMatchTrip;
    @FXML private JFXButton buttonSearchTrips;
    @FXML private JFXButton buttonClearTrips;

    private BooleanProperty fileLoaded;
    private BooleanProperty foundResults;

    public MatchTripFormController() {
        fileLoaded = new SimpleBooleanProperty();
        foundResults = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        super.initialize();
        buttonMatchTrip.disableProperty().bind(foundResults.not());
        buttonSearchTrips.disableProperty().bind(foundResults);
        buttonClearTrips.disableProperty().bind(buttonSearchTrips.disableProperty().not());
    }

    @FXML
    public void searchTripsButtonAction(ActionEvent event) {
        searchForMatches();
    }

    @FXML void clearTripsButtonAction(ActionEvent event) {
        transpoolController.clearForm(this);
    }

    @FXML
    void createNewMatchButtonAction(ActionEvent event) {
        transpoolController.submitForm(this);
    }

    @Override
    public void submit() {
        transpoolController.createNewMatch(listViewResults.getSelectionModel().getSelectedItem());
    }

    @Override
    public void clear() {
        transpoolController.getEngine().clearPossibleMatches();
        textFieldNumOfResultsToFind.clear();

    }

    @Override
    public void setValidations() {
        textFieldNumOfResultsToFind.getValidators().add(requiredFieldValidator);
        comboBoxRideID.getValidators().add(requiredFieldValidator);

        textFieldNumOfResultsToFind.focusedProperty().addListener(
                ((observable, oldValue, newValue) -> textFieldNumOfResultsToFind.validate())
        );
        comboBoxRideID.focusedProperty().addListener(
                ((observable, oldValue, newValue) -> comboBoxRideID.validate())
        );
    }

    @Override
    public boolean isValid() {
        return textFieldNumOfResultsToFind.validate()
                && comboBoxRideID.validate();
    }
    private void searchForMatches() {
        try {
            if (isValid()) {
                TripRequest requestToMatch = (TripRequest) comboBoxRideID.getValue();
                int numOfResults = Integer.parseInt(textFieldNumOfResultsToFind.getText());
                transpoolController.findPossibleMatches(requestToMatch, numOfResults);
            } else {
                throw new RequiredFieldEmptyException();
            }
        } catch (RequiredFieldEmptyException e) {
            transpoolController.showAlert(e);
        }
    }

    public BooleanProperty fileLoadedProperty() {
        return fileLoaded;
    }

    public void bindUIToData(TransPoolData data) {
        foundResults.bind(transpoolController.getEngine().foundMatchesProperty());
        comboBoxRideID.setItems(data.getAllTripRequests());
        listViewResults.setItems(transpoolController.getEngine().getPossibleMatches());
    }
}