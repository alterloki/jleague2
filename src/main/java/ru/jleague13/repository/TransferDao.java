package ru.jleague13.repository;

import org.jsoup.nodes.Element;
import ru.jleague13.entity.Player;
import ru.jleague13.entity.Transfer;
import ru.jleague13.entity.TransferQueryPlayer;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ashevenkov 26.09.15 0:39.
 */
public interface TransferDao {

    void saveTransfer(Transfer transfer);

    Transfer loadTransfer(Date date);

    List<Date> allTransferDates();

    boolean haveTransfer(Date date);

    Transfer readTransfer(Reader reader) throws IOException;

    void saveTransferResult(Player player);

    Map<String, Player> readTransferResult(Reader reader, Date transferDate) throws IOException;

    List<TransferQueryPlayer> readTransferQuery(Reader reader) throws IOException;

    Player extractPlayer(Element str, Map<String, Player> playerMap);
}
