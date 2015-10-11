package ru.jleague13.controller;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.jleague13.entity.PlayerType;
import ru.jleague13.entity.Team;
import ru.jleague13.entity.Transfer;
import ru.jleague13.repository.TeamDao;
import ru.jleague13.repository.TransferDao;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        model.addAttribute("japanTeams", teamDao.getJapanLiveTeams());
        List<Date> transfers = transferDao.allTransferDates();
        model.addAttribute("transfers", transfers);
        if(transfers.size() > 0) {
            Date lastDate = transfers.get(0);
            model.addAttribute("players", transferDao.loadTransfer(lastDate).getPlayers());
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
}
