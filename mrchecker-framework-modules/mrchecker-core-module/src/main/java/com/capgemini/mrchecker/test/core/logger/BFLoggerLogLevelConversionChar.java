package com.capgemini.mrchecker.test.core.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

@Plugin(name="LogLevelConversionChar", category= PatternConverter.CATEGORY)
@ConverterKeys({"BFLL", "BFLoggerLevel"})
public class BFLoggerLogLevelConversionChar extends LogEventPatternConverter {
    private BFLoggerLogLevelConversionChar(final String name, final String key){
        super(name,key);
    }
    public static BFLoggerLogLevelConversionChar newInstance(String[] options){
        return new BFLoggerLogLevelConversionChar("router","BFLL");
    }
    @Override
    public void format(LogEvent logEvent, StringBuilder stringBuilder){
        String llevel = logEvent.getLevel().name().toLowerCase();
        String formatedLogLevel = "log"+llevel.substring(0,1).toUpperCase() + llevel.substring(1);
        stringBuilder.append(formatedLogLevel);
    }
}
