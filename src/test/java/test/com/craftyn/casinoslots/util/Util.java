package test.com.craftyn.casinoslots.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Util {
    private Util() {}

    //UT = Unit test
    public static final Logger logger = Logger.getLogger("CasinoSlots (UT)");

    static {
        logger.setUseParentHandlers(false);

        Handler handler = new ConsoleHandler();
        handler.setFormatter(new TestLogFormatter());
        Handler[] handlers = logger.getHandlers();

        for (Handler h : handlers)
            logger.removeHandler(h);

        logger.addHandler(handler);
    }

    public static void log(Throwable t) {
        log(Level.WARNING, t.getLocalizedMessage(), t);
    }

    public static void log(Level level, Throwable t) {
        log(level, t.getLocalizedMessage(), t);
    }

    public static void log(String message, Throwable t) {
        log(Level.WARNING, message, t);
    }

    public static void log(Level level, String message, Throwable t) {
        LogRecord record = new LogRecord(level, message);
        record.setThrown(t);
        logger.log(record);
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}