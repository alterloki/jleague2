package ru.jleague13.prediction;

import org.springframework.stereotype.Repository;

/**
 * @author ashevenkov 29.01.17 18:22.
 */
@Repository
public class DbPredictionDao implements PredictionDao {
    @Override
    public Prediction getUserMatchPrediction(int userId, int matchId) {
        return null;
    }

    @Override
    public void savePrediction(Prediction prediction) {

    }
}
