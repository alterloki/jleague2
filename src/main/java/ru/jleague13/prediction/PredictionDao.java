package ru.jleague13.prediction;

/**
 * @author ashevenkov 29.01.17 18:00.
 */
public interface PredictionDao {

    Prediction getUserMatchPrediction(int userId, int matchId);

    void savePrediction(Prediction prediction);
}
