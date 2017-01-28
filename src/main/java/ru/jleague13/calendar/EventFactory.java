package ru.jleague13.calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashevenkov 08.10.16 15:12.
 */
@Component
public class EventFactory {

    private Log log = LogFactory.getLog(EventFactory.class);

    private Map<String, EventType> simpleEventType = new HashMap<>();

    public EventFactory() {
        simpleEventType.put("тренировки", EventType.TRAIN);
        simpleEventType.put("обмены", EventType.CHANGE);
        simpleEventType.put("банкротство", EventType.BANKRUPCY);
        simpleEventType.put("выкуп из спортшкол", EventType.SPORT_SCHOOL_BUY);
        simpleEventType.put("выкуп игроков банком ФА13", EventType.FA13_BUYOUT);
        simpleEventType.put("изменение названий клубов", EventType.CLUB_NAME_CHANGE);
        simpleEventType.put("закрытие приёма заявок на трансфер", EventType.CLOSE_TRANSFER_CLAIM);
        simpleEventType.put("первоначальный трансферный лист", EventType.TRANSFER_FIRST);
        simpleEventType.put("финальный трансферный лист", EventType.TRANSFER_FINAL);
        simpleEventType.put("трансферный аукцион", EventType.T_AUCTION);
        simpleEventType.put("постройки/приглашения", EventType.BUILDINGS);
        simpleEventType.put("последний срок подтверждения участия в новом сезоне", EventType.LAST_CHECK);
        simpleEventType.put("выпускной день спортшкол", EventType.SPORT_SCHOOL_OUT);
        simpleEventType.put("Аренда", EventType.RENT);
        simpleEventType.put("подготовка к старту сезона", EventType.SEASON_PREPARE);
        simpleEventType.put("матчи на новом генераторе", EventType.NEW_TEST);
        simpleEventType.put("старт сезона", EventType.SEASON_START);
        simpleEventType.put("взятка Блаттеру", EventType.BLATTER);
        simpleEventType.put("Подведение итогов сезона", EventType.SEASON_FINISH);
    }

    public Event createEvent(String input) {
        try {
            if(input.startsWith("расписание")) {
                String replace = input.replace("расписание ", "");
                EventType forType = extractEventTypeForMatch(replace);
                int tourNum = extractTourNum(input, forType);
                int gameNum = extractGameNum(input);
                MatchType matchType = extractMatchType(input);
                Event event = new Event(EventType.SCHEDULE, input, tourNum, gameNum, matchType);
                event.setScheduleFor(forType);
                return event;
            } else {
                EventType trySimpleType = simpleEventType.get(input);
                if(trySimpleType != null) {
                    return new Event(trySimpleType, input, 0, 0);
                } else if(input.startsWith("регулярные чемпионаты")) {
                    String tourNumStr = input.replace("регулярные чемпионаты (тур-", "").replace(")", "");
                    return new Event(EventType.REGULAR_TOUR, input, Integer.parseInt(tourNumStr), 0, MatchType.TOUR);
                } else if (input.contains("товарищеские матчи")){
                    return new Event(EventType.FRIEND_GAME, input, 0, 0, MatchType.SINGLE);
                } else {
                    EventType eventType = extractEventTypeForMatch(input);
                    int tourNum = extractTourNum(input, eventType);
                    int gameNum = extractGameNum(input);
                    MatchType matchType = extractMatchType(input);
                    return new Event(eventType, input, tourNum, gameNum, matchType);
                }
            }
        } catch (Exception e) {
            log.error("Error extracting event from string = \"" + input + "\"", e);
            throw e;
        }
    }

    private MatchType extractMatchType(String input) {
        if(input.contains("/")) {
            return MatchType.PLAYOFF;
        } else if(input.contains("групп")) {
            return MatchType.GROUP;
        } else if(input.contains("отбор")) {
            return MatchType.PREPARE;
        } else if(input.contains("финал")) {
            return MatchType.PLAYOFF;
        } else if(input.contains("золот")) {
            return MatchType.GOLDEN;
        } else if(input.contains("переход")) {
            return MatchType.BORDER;
        }
        return null;
    }

    private int extractGameNum(String input) {
        if(input.contains("первые игры")) {
            return 1;
        } else if(input.contains("ответные игры")) {
            return 2;
        }
        return 0;
    }

    private int extractTourNum(String input, EventType forType) {
        if(input.contains("/") && forType != EventType.COMMERCE) {
            int i = input.indexOf('/');
            int j = input.indexOf(' ', i);
            return Integer.parseInt(input.substring(i + 1, j));
        } else if(input.contains("финал")){
            return 1;
        } else if(input.contains("тур-")) {
            int i = input.indexOf("тур-");
            int j = input.indexOf(")");
            return Integer.parseInt(input.substring(i+4, j));
        }
        return 0;
    }

    private EventType extractEventTypeForMatch(String str) {
        if(str.toLowerCase().contains("лиг")) {
            return EventType.LEAGUE_CUP;
        } else if(str.contains("ЛЧ")) {
            return EventType.L_CHAMP;
        } else if(str.contains("КА") || str.contains("KA")) {
            return EventType.CUP_A;
        } else if(str.contains("КФ") || str.contains("KФ")) {
            return EventType.CUP_F;
        } else if(str.contains("ЮЧМ")) {
            return EventType.YOUTH_WORLD_CH;
        } else if(str.contains("ЧМ")) {
            return EventType.WORLD_CH;
        } else if(str.contains("Лига Чемпионов")) {
            return EventType.L_CHAMP;
        } else if(str.contains("Кубок Ассоциации")) {
            return EventType.CUP_A;
        } else if(str.contains("Кубок Федераций")) {
            return EventType.CUP_F;
        } else if(str.contains("золот")) {
            return EventType.REGULAR_TOUR;
        } else if(str.contains("переход")) {
            return EventType.REGULAR_TOUR;
        } else if(str.contains("коммерч") || str.contains("Комтурниры")) {
            return EventType.COMMERCE;
        } else if(str.contains("чемпионатов")) {
            return EventType.ALL_CHEMP;
        } else if(str.contains("КБЛ")) {
            return EventType.KBL;
        }
        log.error("EventType is null for input = \"" + str + "\"");
        return null;
    }
}