package logic;

import common.ValidationException;
import dal.DonationRecordDAL;
import entity.BloodDonation;
import entity.DonationRecord;
import entity.Person;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Parnoor Singh Gill
 */
public class DonationRecordLogic extends GenericLogic<DonationRecord,DonationRecordDAL> {
    
    public static String PERSON_ID="person_id";
    public static String DONATION_ID="donation_Id";
    public static String TESTED="tested";
    public static String ADMINISTRATOR="administrator";
    public static String HOSPITAL="hospital";
    public static String CREATED="created";
    public static String ID="id";
    
    DonationRecordLogic() {
        super( new DonationRecordDAL() );
    }

    @Override
    public List<DonationRecord> getAll() {
        return get( () -> dal().findAll() );
    }

    @Override
    public DonationRecord getWithId(int id) {
        return get( () -> dal().findById( id ) );
    }
    
    public List<DonationRecord> getDonationRecordWithTested(boolean tested){
        return get( () -> dal().findByTested( tested ) );
    }
    
    public List<DonationRecord> getDonationRecordWithAdministrator(String administrator){
        return get( () -> dal().findByAdministrator( administrator) );
    }
    
    public List<DonationRecord> getDonationRecordWithHospital(String username){
        return get( () -> dal().findByHospital( username ) );
    }
    
    public List<DonationRecord> getDonationRecordWithCreated(Date created){
        return get( () -> dal().findByCreated( created ) );
    }
    
    public List<DonationRecord> getDonationRecordWithPerson(int personId){
        return get( () -> dal().findByPerson( personId ) );
    }
    
    public List<DonationRecord> getDonationRecordWithDonation(int donationId){
        return get( () -> dal().findByDonation( donationId ) );
    }
    
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList( " ID ", " Tested ", " Administrator ", " Hospital ", " Created ", " BloodDonation ", " Person " );
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList( ID, TESTED, ADMINISTRATOR, HOSPITAL, CREATED, DONATION_ID, PERSON_ID );
    }

    @Override
    public List<?> extractDataAsList(DonationRecord e) {
        if(e.getBloodDonation()!=null || e.getBloodDonation()!=null)
            return Arrays.asList( e.getId(), e.getTested(), e.getAdministrator(), e.getHospital(), e.getCreated(), e.getBloodDonation().getId(), e.getPerson().getId());
        else 
            return Arrays.asList( e.getId(), e.getTested(), e.getAdministrator(), e.getHospital(), e.getCreated(), e.getBloodDonation(), e.getPerson());
    }

    /**
     * This method check the values that present in map and then validate and change according to required data type and then set those values
     * @param parameterMap
     * @return DonationRecord
     */
    @Override
    public DonationRecord createEntity(Map<String, String[]> parameterMap) {
       
        Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );
        

        System.out.print(parameterMap+"hello");
        //create a new Entity object
        DonationRecord entity = new DonationRecord();

        //ID is generated, so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        //the only time that we will have id is for update behaviour.
        if( parameterMap.containsKey( ID ) ){
            try {
                entity.setId( Integer.parseInt( parameterMap.get( ID )[ 0 ] ) );
            } catch( java.lang.NumberFormatException ex ) {
                throw new ValidationException( ex );
            }
        }

        //before using the values in the map, make sure to do error checking.
        //simple lambda to validate a string, this can also be place in another
        //method to be shared amoung all logic classes.
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

        //extract the date from map first.
        //everything in the parameterMap is string so it must first be
        //converted to appropriate type. have in mind that values are
        //stored in an array of String; almost always the value is at
        //index zero unless you have used duplicated key/name somewhere.
        
        
        String personId = null;
        if( parameterMap.containsKey( PERSON_ID ) ){
             personId= parameterMap.get( PERSON_ID )[ 0 ];
            //validator.accept( personId, 45 );
            if(personId!="")
            entity.setPerson(new Person(Integer.parseInt(personId)) );
        }      
        String donationId = null;
        if( parameterMap.containsKey( DONATION_ID ) ){
             donationId= parameterMap.get( DONATION_ID )[ 0 ];
            //validator.accept( donationId, 45 );
            if(donationId!="")
            entity.setBloodDonation( new BloodDonation(Integer.parseInt(donationId)) );
        }
        //String personId = parameterMap.get( PERSON_ID )[ 0 ];
        //String donationId = parameterMap.get( DONATION_ID )[ 0 ];
        String tested = parameterMap.get( TESTED )[ 0 ];
        String hospital = parameterMap.get( HOSPITAL )[ 0 ];
        String created = parameterMap.get( CREATED )[ 0 ];
        String adminstrator = parameterMap.get( ADMINISTRATOR )[ 0 ];
        

        //validate the data
        //validator.accept( personId, 45 );
        //validator.accept( donationId, 45 );
        validator.accept( tested, 5 );
        validator.accept( hospital, 100 );
        validator.accept( created, 45 );
        validator.accept( adminstrator, 100  );

        //set values on entity
        //entity.setId(Integer.parseInt(id) );
        //entity.setPerson(new Person(Integer.parseInt(personId)) );
        //entity.setBloodDonation( new BloodDonation(Integer.parseInt(donationId)) );
        if("1".equals(tested))
            entity.setTested(true);
        else
            entity.setTested(false);
        entity.setHospital(hospital);
        //System.out.print(created);
        try{
            entity.setCreated( convertStringToDate(created) );
        }
        catch(Exception e){
            Date date =new Date();
            entity.setCreated(date);
        }
        
        entity.setAdministrator(adminstrator);
    

        return entity;
    }
    
}
