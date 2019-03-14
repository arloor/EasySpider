package com.arloor.taskmanager;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StepContainer {
    private static final Logger logger = LoggerFactory.getLogger(StepContainer.class);

    private static Map<String, Step> steps = new HashMap<>();

    private static Save save;

    static {
        GroovyClassLoader loader = new GroovyClassLoader();

        java.net.URL uri = StepContainer.class.getResource("/task/seedrs");
        File dir = new File(uri.getPath());
        File[] groovyFiles = dir.listFiles();
        for (File groovyFile : groovyFiles
        ) {
            Class groovyClass = null;
            try {
                groovyClass = loader.parseClass(groovyFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (groovyClass != null) {
                    GroovyObject object = (GroovyObject) groovyClass.newInstance();
                    if (object instanceof Step) {
                        steps.put(groovyFile.getName(), (Step) object);
                    }else if(object instanceof Save){
                        save=(Save) object;
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        logger.info("load groovy scripts success {}",steps.toString());
    }


    public static void list(){
        System.err.println("============<groovy class list>============");
        for (Map.Entry<String,Step> entry:steps.entrySet()
             ) {
            System.err.println(entry.getKey()+"="+entry.getValue());
        }
        System.err.println("============<groovy class list>============");
    }

    public static Step getStepByName(String name){
        return steps.get(name+".groovy");
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Save getSave() {
        return save;
    }
}
