package ru.jleague13.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.jleague13.calendar.Calendar;
import ru.jleague13.calendar.CalendarManager;
import ru.jleague13.download.DownloadInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author ashevenkov 15.10.16 16:23.
 */
@Controller
@RequestMapping("new/admin/calendar")
public class CalendarController {

    private Log log = LogFactory.getLog(CalendarController.class);

    @Autowired
    private DownloadInfo downloadInfo;

    @Autowired
    private CalendarManager calendarManager;

    @RequestMapping(method = RequestMethod.GET)
    public String showCurrentCalendar(Model model) {
        return showCalendar(model, new Date());
    }

    @RequestMapping(value="/{date}", method = RequestMethod.GET)
    public String showCalendar(Model model,
                               @PathVariable("date") @DateTimeFormat(pattern = "MM-yyyy") Date date) {
        Calendar monthCalendar = calendarManager.getMonthCalendar(date);
        model.addAttribute("calendar", monthCalendar);
        model.addAttribute("current", string(date));
        model.addAttribute("next", nextMonth(date));
        model.addAttribute("prev", prevMonth(date));
        return "admin/calendar";
    }

    private Object string(Date date) {
        return new SimpleDateFormat("MMMMM, yyyy").format(date);
    }

    private String prevMonth(Date date) {
        java.util.Calendar calendarInst = new GregorianCalendar();
        calendarInst.setTime(date);
        calendarInst.add(java.util.Calendar.MONTH, -1);
        return new SimpleDateFormat("MM-yyyy").format(calendarInst.getTime());
    }

    private String nextMonth(Date date) {
        java.util.Calendar calendarInst = new GregorianCalendar();
        calendarInst.setTime(date);
        calendarInst.add(java.util.Calendar.MONTH, 1);
        return new SimpleDateFormat("MM-yyyy").format(calendarInst.getTime());
    }

    @RequestMapping(value="/parse", method = RequestMethod.POST)
    public String parseCalendar() {
        return parseCalendarFrom(new Date());
    }

    @RequestMapping(value="/parse/{date}", method = RequestMethod.POST)
    public String parseCalendarFrom(@PathVariable("date") @DateTimeFormat(pattern = "MM-yyyy") Date date) {
        String dateUrl = new SimpleDateFormat("/yyyy/MM").format(date);
        try {
            Calendar calendar = downloadInfo.downloadCalendar(dateUrl);
            calendarManager.saveCalendar(calendar);
        } catch (IOException e) {
            log.error("Error downloading calendar.", e);
            return "error";
        }
        return "ok";
    }
}
