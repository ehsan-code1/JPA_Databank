package dal;

import dal.GenericDAL;
import dal.GenericDAL;
import entity.Person;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Abdul
 */
public class PersonDAL extends GenericDAL<Person> {

    public PersonDAL() {
        super(Person.class);
    }
    /**
     * first argument is a name given to a named query defined in appropriate entity
     * second argument is map used for parameter substitution.
     * parameters are names starting with : in named queries, :[name]
     * @return findAll
     */
     @Override
    public List<Person> findAll(){
        return findResults( "Person.findAll", null );
    }
    /**
     * first argument is a name given to a named query defined in appropriate entity
     * second argument is map used for parameter substitution.
     * parameters are names starting with : in named queries, :[name]
     * in this case the parameter is named "id" and value for it is put in map
     * @param id
     * @return findById
     */
    @Override
    public Person findById(int id){
        Map<String, Object> map = new HashMap<>();
        map.put( "id", id );
        return findResult( "Person.findById", map );
    }
    /**
     * first argument is a name given to a named query defined in appropriate entity
     * second argument is map used for parameter substitution.
     * parameters are names starting with : in named queries, :[name]
     * in this case the parameter is named "id" and value for it is put in map
     * @param firstName
     * @return findByFirstName
     */
    public List<Person> findByFirstName (String firstName){
        Map<String, Object> map = new HashMap<>();
        map.put( "firstName", firstName );
        return findResults( "Person.findByFirstName", map );
    }
    /**
     * first argument is a name given to a named query defined in appropriate entity
     * second argument is map used for parameter substitution.
     * parameters are names starting with : in named queries, :[name]
     * in this case the parameter is named "id" and value for it is put in map
     * @param lastName
     * @return findByLastName
     */
    public List<Person> findByLastName (String lastName){
        Map<String, Object> map = new HashMap<>();
        map.put( "lastName", lastName );
        return findResults( "Person.findByLastName", map );
    }
    /**
     * first argument is a name given to a named query defined in appropriate entity
     * second argument is map used for parameter substitution.
     * parameters are names starting with : in named queries, :[name]
     * in this case the parameter is named "id" and value for it is put in map 
     * @param phone
     * @return findByPhone
     */
    public List<Person> findByPhone (String phone){
        Map<String, Object> map = new HashMap<>();
        map.put("phone",phone);
        return findResults("Person.findByPhone", map);
    }
    /**
     * first argument is a name given to a named query defined in appropriate entity
     * second argument is map used for parameter substitution.
     * parameters are names starting with : in named queries, :[name]
     * in this case the parameter is named "id" and value for it is put in map
     * @param address
     * @return findByAddress 
     */
    public List<Person> findByAddress (String address){
        Map<String, Object> map = new HashMap<>();
        map.put("address",address);
        return findResults("Person.findByAddress", map);
    }
    /**
     * first argument is a name given to a named query defined in appropriate entity
     * second argument is map used for parameter substitution.
     * parameters are names starting with : in named queries, :[name]
     * in this case the parameter is named "id" and value for it is put in map
     * @param birth
     * @return findByBirth
     */
    public List<Person> findByBirth (Date birth){
        Map<String, Object> map = new HashMap<>();
        map.put("birth",birth);
        return findResults("Person.findByBirth", map);
       
    }
}