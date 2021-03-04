package com.capgemini.mrchecker.test.core.logger;

//import org.apache.commons.configuration.XMLConfiguration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.routing.Route;
import org.apache.logging.log4j.core.appender.routing.Routes;
import org.apache.logging.log4j.core.appender.routing.RoutingAppender;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.spi.LoggerContextFactory;

import java.io.File;
import java.util.regex.Pattern;

public class BFLoggerConfiguration extends XmlConfiguration {
    private static final String LOG_PATTERN = "%d{yyyy-MM-dd 'at' HH:mm:ss z} %M - %m%x%n"; //TODO: Rework this pattern
    public BFLoggerConfiguration(LoggerContext context, ConfigurationSource configSource)
    {
        super(context,configSource);
    }

    @Override
    public void doConfigure(){
        //String appenderName = Thread.currentThread().getName();

        super.doConfigure();
//        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
//
//        final Configuration config = ctx.getConfiguration();
//        final Layout layout = PatternLayout.newBuilder().withPattern(LOG_PATTERN).withConfiguration(config).build();
//        //final String logName = "${ctx:threadName}";
//        final String logName = Thread.currentThread().getName();
//        final String logFile = "logs"+File.separator+logName+".log";
//        System.err.println(logName);
//
//        final Appender appender = FileAppender.newBuilder().setName(logName).withFileName(logFile).build();
//
//
//
//        AppenderRef ref = AppenderRef.createAppenderRef(appenderName,null,null);
//        AppenderRef[] refs = new AppenderRef[]{ref};
//        //Route route =  Route.createRoute(ref)
//        //Routes routes = ;
//        //final Appender routingAppender = RoutingAppender.newBuilder().setName(logName).withRoutes().build();
//        //appender.start();
////        final AsyncAppender asyncAppender = AsyncAppender.newBuilder().setName("async").setAppenderRefs(refs).build();
////        AppenderRef asyncRef = AppenderRef.createAppenderRef("async",null,null);
////        AppenderRef[] asyncRefs = new AppenderRef[]{asyncRef};
////        config.addAppender(asyncAppender);
//        config.addAppender(appender);
//        //config.addAppender(routingAppender);
//        appender.start();
//        //asyncAppender.start();
//        //LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.DEBUG,"org.apapache.logging.log4j","true",asyncRefs,null,config,null);
//        LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.DEBUG,"org.apapache.logging.log4j","true",refs,null,config,null);
//        config.addLogger("org.apache.logging.log4j",loggerConfig);
//        ctx.updateLoggers();
    }
}
