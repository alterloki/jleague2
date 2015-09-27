package ru.jleague13.prediction;

import ru.jleague13.entity.Player;

import java.io.FileReader;
import java.io.Reader;
import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 25.09.15 23:00.
 */
public interface PredictionService {

    int predictPrice(Player player);

    List<Player> transferPrediction();



}
