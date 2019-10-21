package com.accesodatos.sql.misc;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.FluentLogger.Api;
import com.google.common.flogger.LoggerConfig;
import java.util.logging.Level;

public class Flogger implements Globals {

    private static final FluentLogger flogger = FluentLogger.forEnclosingClass();

    static {
        LoggerConfig.of(flogger).setLevel(Globals.LOG_LEVEL);
    }

    public static Api at(Level level) {
        return flogger.at(level);
    }

    public static Api atSevere() {
        return flogger.atSevere();
    }

    public static Api atWarning() {
        return flogger.atWarning();
    }

    public static Api atInfo() {
        return flogger.atInfo();
    }

    public static Api atConfig() {
        return flogger.atConfig();
    }

    public static Api atFine() {
        return flogger.atFine();
    }

    public static Api atFiner() {
        return flogger.atFiner();
    }

    public static Api atFinest() {
        return flogger.atFinest();
    }


}
