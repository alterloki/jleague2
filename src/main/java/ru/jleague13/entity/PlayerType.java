package ru.jleague13.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashevenkov 25.09.15 23:05.
 */
public enum PlayerType {

    GK("ВР"),
    CF("ЦФ"),
    LF("ЛФ"),
    RF("ПФ"),
    CD("ЦЗ"),
    LD("ЛЗ"),
    RD("ПЗ"),
    CH("ЦП"),
    LH("ЛП"),
    RH("ПП");

    private String name;

    PlayerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static PlayerType resolveByFormValue(String s) {
        switch (s) {
            case "ВР": return GK;
            case "ЦФ": return CF;
            case "ЛФ": return LF;
            case "ПФ": return RF;
            case "ЦЗ": return CD;
            case "ЛЗ": return LD;
            case "ПЗ": return RD;
            case "ЦП": return CH;
            case "ЛП": return LH;
            case "ПП": return RH;
        }
        return null;
    }

}
