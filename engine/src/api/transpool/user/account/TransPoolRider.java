package api.transpool.user.account;

import api.transpool.SingleMapEngine;
import api.transpool.trip.request.component.MatchedTripRequest;
import api.transpool.trip.request.component.MatchedTripRequestDTO;
import api.transpool.trip.request.component.TripRequest;
import api.transpool.trip.request.component.TripRequestDTO;
import api.transpool.user.component.feedback.Feedback;
import api.transpool.user.component.feedback.Feedbackable;
import api.transpool.user.component.feedback.Feedbacker;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The class for a TransPool user which is a rider.
 * Implements Rider and a Feedbacker
 */

public class TransPoolRider extends TransPoolUserAccount implements Rider, Feedbacker {

    private static int IDGenerator = 40000;

    private List<TripRequest> tripRequests;
    private List<MatchedTripRequest> matchedTripRequests;

    private Set<Feedbackable> feedbackables;

    public TransPoolRider(String username) {
        super(username);
        this.tripRequests = new ArrayList<>();
        this.matchedTripRequests = new ArrayList<>();
        this.feedbackables = new HashSet<>();

        this.tripRequests = new ArrayList<>();
        this.matchedTripRequests = new ArrayList<>();
        this.feedbackables = new HashSet<>();
        setID(IDGenerator++);
    }

    public TransPoolRider(TransPoolRider other) {
        super(other.getUsername());
        this.tripRequests = new ArrayList<>(other.tripRequests);
        this.matchedTripRequests = new ArrayList<>(other.matchedTripRequests);
        this.feedbackables = new HashSet<>(other.feedbackables);
        setID(other.getID());
    }

    //Rider functions-------------------------------------------------------------------------
    @Override
    public List<TripRequestDTO> getTripRequestsDetails() {
        return tripRequests
                .stream()
                .map(TripRequest::getDetails)
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchedTripRequestDTO> getMatchedTripRequestDetails() {
        return matchedTripRequests
                .stream()
                .map(MatchedTripRequest::getDetails)
                .collect(Collectors.toList());
    }

    @Override
    public List<TripRequestDTO> getTripRequestsDetailsFromMap(SingleMapEngine map) {
        return tripRequests
                .stream()
                .filter(request -> request.isBelongToMap(map))
                .map(TripRequest::getDetails)
                .collect(Collectors.toList());
    }

    @Override
    public void addRequest(TripRequest request) {
        tripRequests.add(request);
    }

    /**
     * Accepts a match created for this request. Adds the match to the rider's matches
     * Pays each relevant balance in the matched request.
     * Adds the feedbackable drivers.
     * @param matchedRequest - The matched request to accept.
     */
    @Override
    public void acceptMatch(MatchedTripRequest matchedRequest) {
        tripRequests.remove(getRequest(matchedRequest.getID()));
        matchedTripRequests.add(matchedRequest);
        feedbackables.addAll(matchedRequest.getTransPoolDrivers());

        matchedRequest
                .getRoute()
                .forEach(tripOfferPartOccurrence -> transferCredit(
                        tripOfferPartOccurrence.getPrice(),
                        tripOfferPartOccurrence.getTransPoolDriver(),
                        tripOfferPartOccurrence.getDepartureTime()
                ));
    }

    @Override
    public List<TripRequest> getAllTripRequests() {
        return tripRequests;
    }

    @Override
    public List<MatchedTripRequest> getAllMatchedTripRequests() {
        return matchedTripRequests;
    }

    @Override
    public TripRequest getRequest(int ID) {
        return tripRequests
                .stream()
                .filter(request -> request.getID() == ID)
                .findFirst()
                .orElse(null);
    }

    @Override
    public MatchedTripRequest getMatchedTripRequest(int ID) {
        return matchedTripRequests
                .stream()
                .filter(match -> match.getID() == ID)
                .findFirst()
                .orElse(null);
    }

    //Feedback functions-------------------------------------------------------------------------
    @Override
    public void leaveFeedback(Feedbackable feedbackee, Feedback feedback) {
        feedbackee.addFeedback(feedback);
        feedbackables.remove(feedbackee);
    }

    @Override
    public Set<Feedbackable> getAllFeedbackables() {
        return feedbackables;
    }

    public Set<String> getAllFeedbackablesUsernames() {
        return feedbackables
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
    }

}
