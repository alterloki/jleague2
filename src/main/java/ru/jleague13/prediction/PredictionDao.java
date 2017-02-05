package ru.jleague13.prediction;

import java.util.List;

/**
 * @author ashevenkov 29.01.17 18:00.
 */
public interface PredictionDao {

    Prediction getUserMatchPrediction(int userId, int matchId);

    void savePrediction(Prediction prediction);

    boolean isUserParticipant(int userId);

    void savePoints(int userId, int points);

    List<PredictionUser> loadPredictionUsers();

    List<Prediction> loadMatchPredictions(int matchId);
}
