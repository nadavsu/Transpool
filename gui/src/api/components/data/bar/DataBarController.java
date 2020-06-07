package api.components.data.bar;

import api.components.TransPoolController;
import api.components.card.match.MatchedTripCardController;
import api.components.card.offer.TripOfferCardController;
import api.components.card.request.TripRequestCardController;
import data.transpool.TransPoolData;
import data.transpool.trip.offer.TripOffer;
import data.transpool.trip.request.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DataBarController {

    private TransPoolController transPoolController;

    @FXML private Label labelTaskProgress;
    @FXML private ListView<TripRequest> listViewTripRequests;
    @FXML private ListView<TripOffer> listViewTripOffers;
    @FXML private ListView<MatchedTripRequest> listViewMatchedTrips;

    private StringProperty currentTaskProgress;
    //private ObservableList<TripRequest> tripRequests;


    public DataBarController() {
        currentTaskProgress = new SimpleStringProperty();
        //tripRequests = FXCollections.observableArrayList();
    }

    public void setTransPoolController(TransPoolController transPoolController) {
        this.transPoolController = transPoolController;
    }

    @FXML
    public void initialize() {
        labelTaskProgress.textProperty().bind(currentTaskProgress);
    }

    public StringProperty currentTaskProgressProperty() {
        return currentTaskProgress;
    }

    public void bindTaskToUI(Task theTask) {
        if (currentTaskProgress.isBound()) {
            currentTaskProgress.unbind();
        }
        currentTaskProgress.bind(theTask.messageProperty());
    }

    //Todo: should be transPoolController.getAllTripOffers() which tells the engine to get all trip offers?
    public void bindUIToData(TransPoolData data) {
        listViewTripOffers.setItems(data.getAllTripOffers());
        listViewTripOffers.setCellFactory(offerListView -> new TripOfferCardController());
        listViewTripRequests.setItems(data.getAllTripRequests());
        listViewTripRequests.setCellFactory(requestListView -> new TripRequestCardController());
        listViewMatchedTrips.setItems(data.getAllMatchedTripRequests());
        listViewMatchedTrips.setCellFactory(listViewMatchedTrips -> new MatchedTripCardController());
    }

}
