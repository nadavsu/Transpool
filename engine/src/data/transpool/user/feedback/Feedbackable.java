package data.transpool.user.feedback;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;

/**
 * An interface of an object which can be feedbacked.
 */
public interface Feedbackable {
    void addFeedback(Feedback feedback);
    Feedback getFeedback(int riderID);
    ObservableList<Feedback> getAllFeedbacks();
    double getAverageRating();
    DoubleProperty averageRatingProperty();
}