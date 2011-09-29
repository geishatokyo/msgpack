package org.msgpack.template.builder;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;

/**
 * Keep current building classes.
 * User: takeshita
 * Create: 11/09/29 11:41
 */
public class CurrentBuilding {

    static ThreadLocal<HashSet<Type>> buildingClasses = new ThreadLocal<HashSet<Type>>(){
        @Override
        protected HashSet<Type> initialValue() {
            return new HashSet<Type>();
        }
    };


    public static boolean isBuilding(Type clazz){
        return buildingClasses.get().contains(clazz);
    }

    public static void beginBuilding(Type clazz){
        buildingClasses.get().add(clazz);
    }

    public static void endBuilding(Type clazz){
        buildingClasses.get().remove(clazz);
    }


}
