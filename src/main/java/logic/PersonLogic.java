package logic;

import common.ValidationException;
import dal.PersonDAL;
import entity.Person;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author Abdul
 */
public class PersonLogic extends GenericLogic<Person, PersonDAL>{
     /**
     * create static final variables with proper name of each column. this way you will never manually type it again,
     * instead always refer to these variables.
     *
     * by using the same name as column id and HTML element names we can make our code simpler. this is not recommended
     * for proper production project.
     */
  public static final String FIRST_NAME = "first_name";
  public static final String LAST_NAME = "last_name";
  public static final String PHONE = "phone";
  public static final String ADDRESS = "address";
  public static final String BIRTH = "birth";
  public static final String ID = "id";
    
    /**
     *this class used to refer immediate parent class object and super is reference variable
     */
    PersonLogic(){
        super (new PersonDAL());
    }/**
     * this method is get all the entities form personEntity
     * @return findAll
     */
    public List<Person> getAll(){
         return get( () -> dal().findAll() );
    }/**
     * getting the id from entity class
     * @param id
     * @return id
     */
    public Person getWithId ( int id){
        return get( () -> dal().findById( id ) );
    }/**
     * getting the phone form entity class
     * @param phone
     * @return phone
     */
    public List<Person> getPersonWithPhone (String phone){
        return get( () -> dal().findByPhone(phone));
    }
    /**
     * getting the firstName from entity class
     * @param firstName
     * @return firstName
     */
    public List<Person> getPersonWithFirstName ( String firstName){
        return get ( () -> dal ().findByFirstName(firstName));
    }
    /**
     * getting the lastName form entity class
     * @param lastName
     * @return LastName
     */
    public List<Person> getPersonWithLastName ( String lastName){
        return get ( () -> dal ().findByLastName(lastName));
    }
    /**
     * getting the address from entity class
     * @param Address
     * @return Address
     */
    public List<Person> getPersonWithAddress (String Address){
         return get ( () -> dal ().findByAddress(Address));
    }
    /**
     * getting the birth from entity class
     * @param birth
     * @return birth
     */
    public List<Person> getPersonWithBirth (Date birth){
         return get ( () -> dal ().findByBirth(birth));
    }
    /**
     * Construct a new map with the same mappings as the given map.
     * @param parameterMap
     * @return 
     */
    public Person createEntity (Map <String, String[]> parameterMap){
        /**
         * requireNonNull is checking the reference object to be not null
         *  return new PersonBuilder().SetData( parameterMap ).build();
         */
         Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );
         /**
          * creating a new entity object
          */
         Person entity = new Person();
         /**
          * ID is generated, so if it exists add it to the entity object
          * otherwise it does not matter as mysql will create an if for it.
          * the only time that we will have id is for update behaviour.
          */
         if( parameterMap.containsKey( ID ) ){
            try {
                entity.setId( Integer.parseInt( parameterMap.get( ID )[ 0 ] ) );
            } catch( java.lang.NumberFormatException ex ) {
                throw new ValidationException( ex );
            }
         }
         /**
          * before using the values in the map, make sure to do error checking.
          * simple lambda to validate a string, this can also be place in another
          * method to be shared amoung all logic classes.
          */
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
         /**
          * here is the extracting the data form map, and each attribute is converted to its type
          * and using the index of [0] because we're not using duplicated key
          */
         String firstName=parameterMap.get(FIRST_NAME)[0];
         String lastName = parameterMap.get(LAST_NAME)[0];
         String phone = parameterMap.get(PHONE)[0];
         String address = parameterMap.get(ADDRESS)[0];
         String birth = parameterMap.get (BIRTH) [0];
         /**
          * data validation for each attribute include varchar characters
          */
         validator.accept (firstName, 50);
         validator.accept (lastName, 50);
         validator.accept (phone, 15);
         validator.accept (address, 100);
         /**
          * setting the value on the entities
          */
         entity.setFirstName(firstName);
         entity.setLastName(lastName);
         entity.setPhone (phone);
         entity.setAddress(address);
         birth+=" 01:00:00";
         entity.setBirth (convertStringToDate(birth));
         return entity;
         /**
          * 
          */
    }
    /**
     * this method is used to send a list of all names to be used form table column headers. by having all names in one
     * location there is less chance of mistakes.
     * using the same column names order
     * @return all column names 
     */
    public List<String> getColumnNames (){
        return Arrays.asList("ID", "First_Name", "Last_Name","Phone","Address","Birth");
    }
    /**
     * this method returns a list of column names that match the official column names in the db. by having all names in
     * one location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnNames and extractDataAsList
     * @return list of all column names in DB. 
     */
    public List<String> getColumnCodes (){
        return Arrays.asList (ID,FIRST_NAME,LAST_NAME,PHONE,ADDRESS,BIRTH );
    }
    
    /**
     * return the list of values of all columns (variables) in given entity.
     *
     * this list must be in the same order as getColumnNames and getColumnCodes
     *
     * @param e - given Entity to extract data from.
     *
     * @return list of extracted values
     */
    public List<?> extractDataAsList (Person e){
        return Arrays.asList(e.getId(),e.getFirstName(),e.getLastName(),e.getPhone(),e.getAddress(),convertDateToString(e.getBirth()));
    }


}