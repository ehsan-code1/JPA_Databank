package logic;

public abstract class LogicFactory {
    private LogicFactory() {
    }

    //TODO this code is not complete, it is just here for sake of programe working. need to be changed ocmpletely
    
    public static < T> T getFor( String entityName ) throws Exception {
                
	Class<?> builder = Class.forName("logic."+entityName);
        
       return (T) builder.getDeclaredConstructor().newInstance();
   
    }
}
