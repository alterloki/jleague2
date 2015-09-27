package ru.jleague13.repository;

import ru.jleague13.entity.Player;
import ru.jleague13.entity.Transfer;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 26.09.15 0:39.
 */
public interface TransferDao {

    void saveTransfer(Transfer transfer);

    Transfer loadTransfer(Date date);

    List<Date> allTransferDates();

    boolean haveTransfer(Date date);

    Transfer readTransfer(Reader reader) throws IOException;

}
