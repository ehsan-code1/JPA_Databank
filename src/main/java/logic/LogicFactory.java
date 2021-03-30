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
        
        try{
            return getFor( (Class <T>)Class.forName(PACKAGE+entityName+SUFFIX));
        }
        catch(ClassNotFoundException e){
            e.getMessage();
        } 
        return null;
    }
    
    public static <R> R getFor(Class<R> type)
    {
        try{
            Constructor<R> declaredConstructor = type.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        }
        
        catch(InstantiationException e)
        {
            e.getMessage();
        }
        
        catch(IllegalAccessException e)
        {
            e.getMessage();
        }
        
        catch(IllegalArgumentException e)
        {
            e.getMessage();
        }
        catch(InvocationTargetException e)
        {
            e.getMessage();
        }
        catch(NoSuchMethodException e)
        {
            e.getMessage();
        }
        catch(SecurityException e)
        {
            e.getMessage();
        }

        return null;
    }
}
