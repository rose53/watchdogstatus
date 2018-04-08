package de.rose53.watchdogstatus;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class JaxRsApplication extends Application {

    private static final Set<Class<?>> CLASSES;

    static {
        HashSet<Class<?>> tmp = new HashSet<Class<?>>();
        tmp.add(WatchdogStatusResource.class);

        CLASSES = Collections.unmodifiableSet(tmp);
    }

    @Override
    public Set<Class<?>> getClasses(){

       return  CLASSES;
    }


}