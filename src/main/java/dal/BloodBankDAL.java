package dal;
import entity.BloodBank;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Nouran Nouh
 * 
 */
public class BloodBankDAL extends GenericDAL<BloodBank>{
    
    
    public BloodBankDAL(){
        super(BloodBank.class);
        
    }

    
    /**
     * first Argument is the name given to a named query which is defined in appropriate entity
     * //second parameter is map used for parameter substitution 
     * @return List of found results
     */
    @Override
    public List<BloodBank> findAll() {
        return findResults("BloodBank.findAll", null); //To change body of generated methods, choose Tools | Templates.
    }

    
    /**
     * first Argument is the name given to a named query which is defined in appropriate entity
     * @param id
     * //second parameter is map used for parameter substitution which has key of "id" and value of the id
     * @return result by id 
     */
    @Override
    public BloodBank findById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put( "bankId", id );
        return findResult("BloodBank.findByBankId", map);
    }
    
    
    /**
     * first Argument is the name given to a named query which is defined in appropriate entity
     * //second parameter is map used for parameter substitution which has key of "name" and value of the name
     * @param name
     * @return result by Name
     */
    
    public BloodBank findByName(String name){
        Map<String, Object> map= new HashMap<>();
        map.put("name", name);
        return findResult("BloodBank.findByName", map);
    }
    
     /**
     * first Argument is the name given to a named query which is defined in appropriate entity
     * //second parameter is map used for parameter substitution which has key of "privatelyOwned" and value of the privatelyOwned
     * @param privatelyOwned
     * @return List of found results by privatelyOwned
     */
    
    public List<BloodBank> findByPrivatelyOwened(boolean privatelyOwned){
        Map<String, Object> map= new HashMap<>();
        map.put("privatelyOwned", privatelyOwned);
        return findResults("BloodBank.findByPrivatelyOwened", map);
    }
    
     /**
     * first Argument is the name given to a named query which is defined in appropriate entity
     * //second parameter is map used for parameter substitution which has key of "EstablishedDate" and value of the established date
     * @param established 
     * @return List of found results by establishedDate
     */
    
    public List<BloodBank> findByEstablished(Date established){
        Map<String, Object> map= new HashMap<>();
        map.put("established", established);
        return findResults("BloodBank.findByEstablished", map);
    }
    
    
    
     /**
     * first Argument is the name given to a named query which is defined in appropriate entity
     * second parameter is map used for parameter substitution which has key of employeeCount" and value of the employeeCount
     * @param employeeCount 
     * @return List of found results by EmployeeCount 
     */
    
    
    public List<BloodBank> findByEmployeeCount(int employeeCount){
        Map<String, Object> map= new HashMap<>();
        map.put("employeeCount", employeeCount);
        return findResults("BloodBank.findByEmployeeCount", map);
    
    }
    
     /**
     * first Argument is the name given to a named query which is defined in appropriate entity
     * second parameter is map used for parameter substitution which has key of "OwnerId" and value of the ownerId
     * @param ownerId 
     * @return result by ownerId 
     */
    
    public BloodBank findByOwner(int ownerId){
        Map<String, Object> map= new HashMap<>();
        map.put("ownerId", ownerId);
        return findResult("BloodBank.findByOwner", map);
        
        
    }
    
     /**
     * first Argument is the name given to a named query which is defined in appropriate entity
     * //second parameter is map used for parameter substitution which has key of "search" and value of the search
     * @param search
     * @return List of found results by search
     */
    
    public List<BloodBank> findContaining(String search){
        
        Map<String, Object> map= new HashMap<>();
        map.put("search", search);
        return findResults("BloodBank.findContaining", map);
        
        
    }
    
    
}