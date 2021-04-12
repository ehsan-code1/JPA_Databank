package logic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class LogicFactory {

    private static final String PACKAGE = "logic.";
    private static final String SUFFIX = "Logic";

    private LogicFactory() {
    }

    //TODO this code is not complete, it is just here for sake of programe working. need to be changed ocmpletely
    public static < T> T getFor(String entityName) {
        //this casting wont be needed.
        // Class<?> builder = Class.forName ("logic."+ entityName);
        //     return (T) builder.getDeclaredConstructor().newInstance();
        try {
            return getFor((Class<T>) Class.forName(PACKAGE + entityName + SUFFIX));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <R> R getFor(Class<R> form) {
        
        try {
            Constructor<R> declaredConstructor = form.getDeclaredConstructor();     
            return declaredConstructor.newInstance();       
       }catch(InstantiationException
               |
               IllegalAccessException
               |
               IllegalArgumentException
               |
               InvocationTargetException 
               |
               NoSuchMethodException 
               |
               SecurityException ex){
            throw new RuntimeException("wrong type: " + form, ex);  
            
        }
    }
}


