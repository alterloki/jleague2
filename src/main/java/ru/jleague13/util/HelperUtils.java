package ru.jleague13.util;

import ru.jleague13.entity.Player;
import ru.jleague13.entity.Team;
import ru.jleague13.entity.Transfer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ashevenkov 09.09.16 16:55.
 */
public class HelperUtils {

    public static Map<String, Player> toMap(Transfer transfer) {
        return transfer.getPlayers().stream().
                collect(Collectors.toMap(Player::getName, Function.<Player>identity()));
    }
}
