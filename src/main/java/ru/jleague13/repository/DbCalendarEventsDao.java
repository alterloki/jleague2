package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jleague13.calendar.*;
import ru.jleague13.calendar.Calendar;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author ashevenkov 15.10.16 21:54.
 */
@Repository
public class DbCalendarEventsDao implements CalendarEventsDao {

    private static final String CALENDAR_FIELDS =
            "event_date, event_type, schedule_for, text, tour_num, part, match_type";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> loadCalendarEvents(Date from, Date to) {
        return jdbcTemplate.query("select " + CALENDAR_FIELDS + " from calendar_event " +
                        "where event_date >= ? AND event_date < ?",
                (rs, i) -> eventFromRs(rs), from, to);
    }

    @Override
    public void saveCalendarEvents(List<Event> events) {
        Map<Date, CalendarDay> map = new HashMap<>();
        for (Event event : events) {
            CalendarDay calendarDay = map.get(event.getDay());
            if(calendarDay == null) {
                calendarDay = new CalendarDay(event.getDay());
                map.put(event.getDay(), calendarDay);
            }
            calendarDay.addEvent(event);
        }
        saveCalendarEvents(map);
    }

    @Override
    public void saveCalendarEvents(Map<Date, CalendarDay> calendarEvents) {
        for (Map.Entry<Date, CalendarDay> entry : calendarEvents.entrySet()) {
            Date date = entry.getKey();
            jdbcTemplate.update("delete from calendar_event where event_date = ?", date);
            List<Object[]> params = new ArrayList<>();
            Set<Event> events = entry.getValue().getEvents();
            for (Event event : events) {
                if(event == null || event.getEventType() == null) {
                    System.out.println(event);
                }
                params.add(new Object[]{
                        date, nvl(event.getEventType()), nvl(event.getScheduleFor()),
                        event.getText(), event.getTourNum(), event.getPart(), nvl(event.getMatchType())});
            }
            jdbcTemplate.batchUpdate("insert into calendar_event " +
                    "(event_date, event_type, schedule_for, text, tour_num, part, match_type) values " +
                    "(?,?,?,?,?,?,?)", params);
        }
    }

    private <B extends Enum> Object nvl(B type) {
        return type != null ? type.ordinal() : null;
    }

    @Override
    public List<Event> getDayEvents(Date day) {
        return jdbcTemplate.query("select " + CALENDAR_FIELDS + " from calendar_event where event_date = ?",
                (rs, i) -> eventFromRs(rs), day);
    }

    private Event eventFromRs(ResultSet rs) throws SQLException {
        Event event = new Event(
                EventType.values()[rs.getInt("event_type")],
                rs.getString("text"),
                rs.getInt("tour_num"),
                rs.getInt("part"),
                MatchType.values()[rs.getInt("match_type")]);
        EventType schedule = EventType.values()[rs.getInt("schedule_for")];
        event.setScheduleFor(schedule);
        event.setDay(rs.getDate("event_date"));
        return event;
    }
}
