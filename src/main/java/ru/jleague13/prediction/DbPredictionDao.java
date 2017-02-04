package ru.jleague13.prediction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ashevenkov 29.01.17 18:22.
 */
@Repository
public class DbPredictionDao implements PredictionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FULL_FIELDS =
            "match_id, user_id, owner_score, guest_score, pred_time";

    @Override
    public Prediction getUserMatchPrediction(int userId, int matchId) {
        return jdbcTemplate.query(
                "select " + FULL_FIELDS +" from prediction where user_id = ? and match_id = ?",
                rs -> {
                    if(rs.next()) {
                        return predictionFromRs(rs);
                    }
                    return null;
                }, userId, matchId);
    }

    private Prediction predictionFromRs(ResultSet rs) throws SQLException {
        return new Prediction(rs.getInt("user_id"), rs.getInt("match_id"),
                rs.getInt("owner_score"), rs.getInt("guest_score"), rs.getDate("pred_time"));
    }

    @Override
    public void savePrediction(Prediction prediction) {
        if (hasPrediction(prediction.getUserId(), prediction.getMatchId())) {
            jdbcTemplate.update(
                    "update prediction set owner_score = ?, guest_score = ?, pred_time = ? " +
                            " where user_id = ? and match_id = ?",
                    prediction.getOwnerScore(), prediction.getGuestScore(), prediction.getPredictionTime(),
                    prediction.getUserId(), prediction.getMatchId());
        } else {
            jdbcTemplate.update("insert into prediction " +
                            " (match_id, user_id, owner_score, guest_score, pred_time) values " +
                            " (?,?,?,?,?)",
                    prediction.getMatchId(), prediction.getUserId(), prediction.getOwnerScore(),
                    prediction.getGuestScore(), prediction.getPredictionTime());
        }
    }

    private boolean hasPrediction(int userId, int matchId) {
        return jdbcTemplate.queryForObject(
                "select count(1) " +
                        " from prediction where user_id = ? and match_id = ?", Integer.class, userId, matchId) > 0;
    }
}
