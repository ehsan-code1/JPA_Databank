package logic;

import common.ValidationException;
import java.util.List;
import java.util.Map;
import dal.BloodBankDAL;
import entity.BloodBank;
import entity.Person;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.function.ObjIntConsumer;


/**
 *
 * @author Nouran Nouh
 */
public class BloodBankLogic extends GenericLogic<BloodBank,BloodBankDAL> {
    
    //define variables
    public static final String OWNER_ID="owner_id";
    public static final String PRIVATELEY_OWNED="privateley owned";
    public static final String ESTABLISHED="established";
    public static final String NAME="name";
    public static final String EMPLOYEE_COUNT="employee count";
    public static final String ID="id";
    
    protected BloodBankLogic(){
        super(new BloodBankDAL());
     
        
    }
    
    
       @Override
    public List<BloodBank> getAll() {
           return get( () -> dal().findAll() );
    }

    @Override
    public BloodBank getWithId(int id) {
         return get( () -> dal().findById( id ) ); 
    
    }
    /**
     *
     * @param name
     * @return
     */
    public BloodBank getBloodBankWithName(String name) {
         return get( () -> dal().findByName(name ) ); 
    }
    
    
     public List<BloodBank> getBloodBankWithPrivateleyOwned(boolean privateleyOwned) {
         return get( () -> dal().findByPrivatelyOwened(privateleyOwned) ); 
    }
     
     
    public List<BloodBank> getBloodBankWithEstablished(Date established) {
        return get( () -> dal().findByEstablished(established) ); 
    }
    
    
      public BloodBank getBloodBankWithOwner(int ownerId) {
        return get( () -> dal().findByOwner(ownerId) ); 
    }
      
      
      public List<BloodBank> getBloodBankWithEmployeeCount(int count) {
        return get( () -> dal().findByEmployeeCount(count) ); 
    }

    @Override
    public List getColumnNames() {
       return Arrays.asList("Bank_id","Owner","Name","Privateley_Owned","Established","Employee_Count");
    }

    @Override
    public List getColumnCodes() {
          return Arrays.asList(ID,OWNER_ID,NAME,PRIVATELEY_OWNED,ESTABLISHED,EMPLOYEE_COUNT);
    }

    @Override
    public List<?> extractDataAsList(BloodBank e) {
        //if no id passed, return null
        if (e.getOwner()==null)
            return Arrays.asList(e.getId(),e.getOwner(),e.getName(),e.getPrivatelyOwned(),e.getEstablished(),e.getEmplyeeCount()); 
       //else return id 
        return Arrays.asList(e.getId(),e.getOwner().getId(),e.getName(),e.getPrivatelyOwned(),e.getEstablished(),e.getEmplyeeCount()); 
    }

    @Override
    public BloodBank createEntity(Map<String, String[]>  parameterMap) {
      
         Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );
         //intialize entity 
         BloodBank bloodBank= new BloodBank();
         
         
           if( parameterMap.containsKey( ID ) ){
            try {
                bloodBank.setId(Integer.parseInt(parameterMap.get(ID)[0]));
                        
            } catch( java.lang.NumberFormatException ex ) {
                throw new ValidationException( ex );
            }
        }
           
         ObjIntConsumer< String> validator = ( value, length ) -> {
            if( value == null || value.trim().isEmpty() || value.length() > length ){
                String error = "";
                if( value == null || value.trim().isEmpty() ){
                    error = "value cannot be null or empty: " + value;
                }
                if( value.length() > length ){
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException( error );
            }
        };
         
         //get Columns 
        String established=parameterMap.get(ESTABLISHED)[0];
        String name=parameterMap.get(NAME)[0];
        String ownerId=parameterMap.get(OWNER_ID)[0];
        String privateleyOwned=parameterMap.get(PRIVATELEY_OWNED)[0];
        String employeeCount=parameterMap.get(EMPLOYEE_COUNT)[0];
         
       
        
        //Validation
        validator.accept(name, 100);
       
        //validate ownerId to be not null
       // Objects.requireNonNull(ownerId);
           
        //validate Privately Owned 
        
       
       //set Owner 
//        bloodBank.setOwner(bloodBank.getOwner());
          
         //SetDate 
        bloodBank.setPrivatelyOwned(Boolean.parseBoolean(privateleyOwned));
        bloodBank.setName(name);
        bloodBank.setEmplyeeCount(Integer.parseInt(employeeCount));
        
        //setDate
        //get Local Date 
        //LocalDateTime now= LocalDateTime.now();
//        Instant

        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");  
        //check date format 
         String dateEstablished=established.replace("T", " ");
        if ((established.matches("\\d{4}-\\d{2}-\\d{2}"))) {
           
            bloodBank.setEstablished(convertStringToDate(dateEstablished));
        }else{
            bloodBank.setEstablished( new Date());
        }
         
        return bloodBank;
         
    }

 
            
    
}
