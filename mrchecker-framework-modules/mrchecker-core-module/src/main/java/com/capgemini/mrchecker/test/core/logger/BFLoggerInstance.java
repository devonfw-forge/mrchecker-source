package com.capgemini.mrchecker.test.core.logger;

import com.capgemini.mrchecker.test.core.base.environment.EnvironmentModule;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.File;
import java.net.URI;
import java.util.List;

public class BFLoggerInstance {
    private Logger logger;
    //private File logFile;

    private static final String	FBEGIN	= "Function: ";
    private static final String	FEND	= "END";
    private static final String LOG_PATTERN = "%d{yyyy-MM-dd 'at' HH:mm:ss z} %M - %m%x%n";
    public BFLoggerInstance(){
        System.err.println("[MRZ] BFLogger instance "+Thread.currentThread().getName());
        ThreadContext.put("threadName",Thread.currentThread().getName());
        getLogger();
    }
    private String getFileLogName(){
        return Thread.currentThread().getName() + ".log";
    }
    private Logger getLogger(){
        //LoggerConfig l = new LoggerConfig.RootLogger();
        List<AppenderRef> l = new LoggerConfig().getAppenderRefs();
        System.err.println("L: "+l.size());
        for (AppenderRef ar: l){
            System.err.println("MRZ app ref: "+ar);
        }

//        Appender LogFileAppender = FileAppender.newBuilder().setName(getFileLogName()).build();
//         uilder().build();
//        Filter LogFileFilter = new FilterComponentBuilder().build() ;

//        l.addAppender(LogFileAppender,Level.DEBUG, null);

        if (logger == null) {
            logger = LogManager.getLogger("thread");
            //logger = LogManager.getLogger();
        }
        return logger;
    }
    public void log(Level level, String message){
        logger.log(level,message);
    }
    public void logDebug(String message){ log(Level.DEBUG,message); }
    public void logInfo(String message){
        log(Level.INFO,message);
    }
    public void logError(String message){
        log(Level.ERROR, message);
    }
    public void logFunctionBegin(String functionName){
        log(Level.DEBUG,FBEGIN+functionName);
    }
    public void logFunctionEnd(){
        log(Level.DEBUG,FEND);
    }

    public void logEnv(String message){log(Level.forName("ENV",700),message);}
    public void logAnalytics(String message){
        log(Level.forName("ANALYTICS",800),message);
    }
    public void startSeparateLog(){
        //TODO: make it work
    }
    public String dumpSeparateLog(){
        //TODO: make it work
        return "test";
    }
}
