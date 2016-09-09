package ru.jleague13.controller;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.jleague13.entity.*;
import ru.jleague13.repository.TeamDao;
import ru.jleague13.repository.TransferDao;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.*;

/**
 * @author ashevenkov 27.09.15 19:53.
 */
@Controller
public class TransferController {

    private Log log = LogFactory.getLog(TransferController.class);

    @Autowired
    private TransferDao transferDao;
    @Autowired
    private TeamDao teamDao;


    @RequestMapping(value="/transfer", method = RequestMethod.GET)
    public String transferTable(Model model) {
        List<Date> transfers = transferDao.allTransferDates();
        if(transfers.size() > 0) {
            Date lastDate = transfers.get(0);
            return transferTableByDate(model, lastDate);
        } else {
            return transferTableByDate(model, null);
        }
    }

    @RequestMapping(value="/transfer/{date}", method = RequestMethod.GET)
    public String transferTableByDate(Model model,
                                      @PathVariable("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date) {
        model.addAttribute("japanTeams", teamDao.getJapanLiveTeams());
        List<Date> transfers = transferDao.allTransferDates();
        model.addAttribute("transfers", transfers);
        if(date != null) {
            model.addAttribute("players", transferDao.loadTransfer(date).getPlayers());
            model.addAttribute("currentDate", date);
        }
        model.addAttribute("mytext", "MYTEXT");
        List<String> positions = new ArrayList<>(generatePositions());
        positions.add(0, "*");
        model.addAttribute("positions", positions);
        return "transfer";
    }

    private List<String> generatePositions() {
        ArrayList<String> al = new ArrayList<>();
        for(PlayerType pt : PlayerType.values()) {
            al.add(pt.getName());
        }
        return al;
    }

    @RequestMapping(value="/transfer", method = RequestMethod.POST)
    public String uploadTransfer(@RequestParam("file") MultipartFile uploadfile) throws IOException {
        Transfer transfer = transferDao.readTransfer(
                new InputStreamReader(uploadfile.getInputStream(), "cp1251"));
        transferDao.saveTransfer(transfer);
        return "ok";
    }

    @RequestMapping(value="/transfer/result", method = RequestMethod.POST)
    public String uploadTransferResult(@RequestParam("file") MultipartFile uploadfile,
                                 @RequestParam("date")
                                 @DateTimeFormat(pattern = "dd-MM-yyyy") Date transferDate) throws IOException {
        Map<String, Player> resultMap = transferDao.readTransferResult(
                new InputStreamReader(uploadfile.getInputStream(), "cp1251"));
        if(transferDao.haveTransfer(transferDate)) {
            Transfer transfer = transferDao.loadTransfer(transferDate);
            List<Player> players = transfer.getPlayers();
            for (Player player : players) {
                Player withPrice = resultMap.get(player.getName());
                if(withPrice != null) {
                    player.setPayed(withPrice.getPayed());
                    transferDao.saveTransferResult(player);
                }
            }
        }
        return "ok";
    }

    @RequestMapping(value="/transfer/check", method = RequestMethod.POST)
    public String transferCheck(@RequestParam("file") MultipartFile transferFile,
                                       @RequestParam("date")
                                       @DateTimeFormat(pattern = "dd-MM-yyyy") Date transferDate,
                                Model model) throws IOException {
        if(transferDao.haveTransfer(transferDate)) {
            Transfer transfer = transferDao.loadTransfer(transferDate);
            Map<String, Player> playersMap = toMap(transfer);
            List<TransferQueryPlayer> queryPlayers = transferDao.readTransferQuery(
                    new InputStreamReader(transferFile.getInputStream(), "cp1251"));
            for (TransferQueryPlayer queryPlayer : queryPlayers) {
                queryPlayer.setPlayer(playersMap.get(queryPlayer.getPlayerName()));
            }
            model.addAttribute("queryPlayers", queryPlayers);
        }
        return "transfer-check";
    }

    private Map<String, Player> toMap(Transfer transfer) {
        Map<String, Player> result = new HashMap<>();
        for (Player player : transfer.getPlayers()) {
            result.put(player.getName(), player);
        }
        return result;
    }
}
