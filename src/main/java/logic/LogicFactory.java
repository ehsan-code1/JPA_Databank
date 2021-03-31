package logic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class LogicFactory {

    private static final String PACKAGE = "logic.";
    private static final String SUFFIX = "Logic";

    private LogicFactory() {
    }

    //TODO this code is not complete, it is just here for sake of programe working. need to be changed ocmpletely
    public static < T> T getFor( String entityName ) {
        //this casting wont be needed.
        
        try{
            return getFor((Class<T>) Class.forName(PACKAGE+entityName+SUFFIX));
            
        }catch(ClassNotFoundException ex){
            throw new IllegalArgumentException(ex);
            
        }
  
    }
    
    public static <E> E getFor(Class<E> type){
       try{
        
            Constructor<E> declaredConstructor=  type.getDeclaredConstructor();
             E instance= declaredConstructor.newInstance();
            return instance;
          
       }catch(NoSuchMethodException  | SecurityException| InstantiationException | IllegalAccessException  | IllegalArgumentException  |InvocationTargetException e) {
           throw new IllegalArgumentException(e); 
       }
    }
}