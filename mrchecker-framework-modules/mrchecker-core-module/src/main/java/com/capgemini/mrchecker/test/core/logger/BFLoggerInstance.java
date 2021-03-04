package com.capgemini.mrchecker.test.core.logger;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class BFLoggerInstance {
//    static {
//        //LogManager.getRootLogger().
//    }
    private Logger logger;
    //private File logFile;

    private static final String	FBEGIN	= "Function: ";
    private static final String	FEND	= "END";
    public BFLoggerInstance(){

        ThreadContext.put("threadName",Thread.currentThread().getName());
        getLogger();
    }
    private String getFileLogName(){
        return Thread.currentThread().getName() + ".log";
    }
    private Logger getLogger(){
        //LoggerConfig l = new LoggerConfig.RootLogger();
//        Appender LogFileAppender = FileAppender.newBuilder().setName(getFileLogName()).build();
//         uilder().build();
//        Filter LogFileFilter = new FilterComponentBuilder().build() ;

//        l.addAppender(LogFileAppender,Level.DEBUG, null);

        if (logger == null) {
            //logger = LogManager.getLogger("thread");
            logger = LogManager.getRootLogger();
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

    private String getAppenderName(){
        return ThreadContext.get("threadName");
    }
    protected File getDirectory(){
        File directory = new File("./logs");
        return directory;
    }
    private File getLogFile() {
        File logFile = new File(getDirectory().getPath(), getAppenderName() + ".log");
        //logFile.deleteOnExit();

        return logFile;
    }
    public void startSeparateLog(){
        try {
            PrintWriter pw = new PrintWriter(getLogFile());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public String dumpSeparateLog(){
        try {
            return Files.toString(getLogFile(), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
