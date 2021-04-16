package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import entity.BloodDonation;
import entity.DonationRecord;
import entity.Person;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Shariar (Shawn) Emami
 */
class DonationRecordTest {

    private DonationRecordLogic logic;
    private DonationRecord expectedEntity;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {

        logic = LogicFactory.getFor( "DonationRecord" );
        

        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction
        em.getTransaction().begin();

        DonationRecord entity = new DonationRecord();
        
        //entity.setId(Integer.getInteger("1"));
        entity.setTested(false);
        entity.setAdministrator("admin");
        entity.setHospital("hospital");
        entity.setCreated(logic.convertStringToDate("1999-01-01 01:01:01"));
        
        //add an DonationRecord to hibernate, Dnation Record is now managed.
        //we use merge instead of add so we can get the updated generated ID.
        expectedEntity = em.merge( entity );
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if( expectedEntity != null ){
            logic.delete( expectedEntity );
        }
    }

    @Test
    final void testGetAll() {
        //get all the DOnationRecords from the DB
        List<DonationRecord> list = logic.getAll();
        //store the size of list, this way we know how many records exits in DB
        int originalSize = list.size();

        assertNotNull( expectedEntity );
        //delete the new account
        logic.delete( expectedEntity );

        list = logic.getAll();
        assertEquals( originalSize - 1, list.size() );
    }

    /**
     * helper method for testing all account fields
     *
     * @param expected
     * @param actual
     */
    private void assertDonationRecordEquals( DonationRecord expected, DonationRecord actual ) {
        //assert all field to guarantee they are the same
        assertEquals( expected.getId(), actual.getId() );
        assertEquals( expected.getAdministrator(), actual.getAdministrator() );
        //assertEquals( expected.getBloodDonation(), actual.getBloodDonation() );
        assertEquals( expected.getCreated(), actual.getCreated() );
        assertEquals( expected.getHospital(), actual.getHospital() );
        //assertEquals( expected.getPerson(), actual.getPerson() );
        assertEquals( expected.getTested(), actual.getTested() );
    }

    @Test
    final void testGetWithId() {
        //using the id of test account get another account from logic
        DonationRecord returnedRecord = logic.getWithId( expectedEntity.getId() );

        //the two reocrds (excpected record and return) must be the same
                       
        assertDonationRecordEquals( expectedEntity, returnedRecord );
    }

    @Test
    final void testGetDonationRecordWithAdminstrator() {
        List<DonationRecord> returnedDonationRecords = logic.getDonationRecordWithAdministrator(expectedEntity.getAdministrator() );

        int foundFull = 0;
        for( DonationRecord donationRecord: returnedDonationRecords ) {
            
            assertEquals( expectedEntity.getAdministrator(), donationRecord.getAdministrator() );
            
            if( donationRecord.getId().equals( expectedEntity.getId() ) ){
                assertDonationRecordEquals( expectedEntity, donationRecord );
                foundFull++;
            }
        }
        assertEquals( 1, foundFull, "if zero means not found, if more than one means duplicate" );
        
    }

    /*@Test
    final void testGetDOnationRecordWithBloodDonation() {
        List<DonationRecord> returnedDonationRecords = logic.getDonationRecordWithDonation(expectedEntity.getBloodDonation().getId() );

        int foundFull = 0;
        for( DonationRecord donationRecord: returnedDonationRecords ) {
            //all accounts must have the same password
            assertEquals( expectedEntity.getBloodDonation(), donationRecord.getBloodDonation() );
            //exactly one account must be the same
            if( donationRecord.getId().equals( expectedEntity.getId() ) ){
                assertDonationRecordEquals( expectedEntity, donationRecord );
                foundFull++;
            }
        }
        assertEquals( 1, foundFull, "if zero means not found, if more than one means duplicate" );
        //the two accounts (testAcounts and returnedAccounts) must be the same
        //assertDonationRecordEquals( expectedEntity, returnedDonationRecord);
    }*/

    @Test
    final void testGetDonationRecordWIthCreated() {
        List<DonationRecord> returnedDonationRecords = logic.getDonationRecordWithCreated(expectedEntity.getCreated() );

        int foundFull = 0;
        for( DonationRecord donationRecord: returnedDonationRecords ) {
            //all accounts must have the same password
            assertEquals( expectedEntity.getCreated(), donationRecord.getCreated() );
            //exactly one account must be the same
            if( donationRecord.getCreated().equals( expectedEntity.getCreated() ) ){
                assertDonationRecordEquals( expectedEntity, donationRecord );
                foundFull++;
            }
        }
        assertEquals( 0, foundFull, "if zero means not found, if more than one means duplicate" );
        //the two accounts (testAcounts and returnedAccounts) must be the same
        //assertDonationRecordEquals( expectedEntity, returnedDonationRecord );
    }

    @Test
    final void testGetDoantionRecordsWithHospital() {
        int foundFull = 0;
        List<DonationRecord> returnedDonationReocrds = logic.getDonationRecordWithHospital(expectedEntity.getHospital() );
        for( DonationRecord donationRecord: returnedDonationReocrds ) {
            //all accounts must have the same password
            assertEquals( expectedEntity.getHospital(), donationRecord.getHospital() );
            //exactly one account must be the same
            if( donationRecord.getHospital().equals( expectedEntity.getHospital() ) ){
                assertDonationRecordEquals( expectedEntity, donationRecord );
                foundFull++;
            }
        }
        assertEquals( 1, foundFull, "if zero means not found, if more than one means duplicate" );
    }

    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( DonationRecordLogic.ADMINISTRATOR, new String[]{ "AdminLogic" } );
        sampleMap.put( DonationRecordLogic.CREATED, new String[]{ logic.convertDateToString(new Date()) } );
        //sampleMap.put( DonationRecordLogic.DONATION_ID, new String[]{ "DonationIdLogic" } );
        sampleMap.put( DonationRecordLogic.HOSPITAL, new String[]{ "HospitalLogic" } );
        //sampleMap.put( DonationRecordLogic.PERSON_ID, new String[]{ "PersonIdLogic" } );
        sampleMap.put( DonationRecordLogic.TESTED, new String[]{ "false" } );

        DonationRecord returnedDonationRecord = logic.createEntity( sampleMap );
        logic.add( returnedDonationRecord );

        
        assertEquals( sampleMap.get( DonationRecordLogic.ADMINISTRATOR )[ 0 ], returnedDonationRecord.getAdministrator() );
        //assertEquals( sampleMap.get( DonationRecordLogic.DONATION_ID )[ 0 ], returnedDonationRecord.getBloodDonation().getId() );
        assertEquals( sampleMap.get( DonationRecordLogic.CREATED )[ 0 ], logic.convertDateToString(returnedDonationRecord.getCreated()) );
        assertEquals( sampleMap.get( DonationRecordLogic.HOSPITAL )[ 0 ], returnedDonationRecord.getHospital() );
        //assertEquals( sampleMap.get( DonationRecordLogic.PERSON_ID )[ 0 ], returnedDonationRecord.getPerson() )
        assertEquals( sampleMap.get( DonationRecordLogic.TESTED)[ 0 ], Boolean.toString(returnedDonationRecord.getTested()) );

        logic.delete( returnedDonationRecord );
    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        
        sampleMap.put( DonationRecordLogic.ADMINISTRATOR, new String[]{expectedEntity.getAdministrator()}  );
        sampleMap.put( DonationRecordLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())} );
        //sampleMap.put( DonationRecordLogic.DONATION_ID, new String[]{ Integer.toString(expectedEntity.getBloodDonation().getId()) } );
        sampleMap.put( DonationRecordLogic.HOSPITAL, new String[]{expectedEntity.getHospital()}  );
        sampleMap.put( DonationRecordLogic.ID,  new String[]{Integer.toString(expectedEntity.getId())}  );
        //sampleMap.put( DonationRecordLogic.PERSON_ID, new String[]{ Integer.toString(expectedEntity.getPerson().getId()) } );
        sampleMap.put( DonationRecordLogic.TESTED, new String[]{Boolean.toString(expectedEntity.getTested())} );

        DonationRecord returnedDonationRecord = logic.createEntity( sampleMap );

        assertDonationRecordEquals( expectedEntity, returnedDonationRecord );
    }

    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
          
        map.put( DonationRecordLogic.ADMINISTRATOR, new String[]{ expectedEntity.getAdministrator() } );
        map.put( DonationRecordLogic.CREATED, new String[]{ logic.convertDateToString(expectedEntity.getCreated()) } );
        //map.put( DonationRecordLogic.DONATION_ID, new String[]{ Integer.toString(expectedEntity.getBloodDonation().getId()) } );
        map.put( DonationRecordLogic.HOSPITAL, new String[]{ expectedEntity.getHospital() } );
        //map.put( DonationRecordLogic.PERSON_ID, new String[]{ Integer.toString(expectedEntity.getPerson().getId()) } );
        map.put( DonationRecordLogic.TESTED, new String[]{ Boolean.toString(expectedEntity.getTested()) } );
        };

        //idealy every test should be in its own method
        fillMap.accept( sampleMap );
        //sampleMap.replace( DonationRecordLogic.ID, null );
        //assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        //sampleMap.replace( DonationRecordLogic.ID, new String[]{} );
        //assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.ADMINISTRATOR, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.ADMINISTRATOR, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        //can be null
        sampleMap.replace( DonationRecordLogic.CREATED, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        //sampleMap.replace( DonationRecordLogic.DONATION_ID, null );
        //assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        //sampleMap.replace( DonationRecordLogic.DONATION_ID, new String[]{} );
        //assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.HOSPITAL, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.HOSPITAL, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
    }

    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            
        map.put( DonationRecordLogic.ADMINISTRATOR, new String[]{ expectedEntity.getAdministrator() } );
        map.put( DonationRecordLogic.CREATED, new String[]{ ""+expectedEntity.getCreated() } );
        //map.put( DonationRecordLogic.DONATION_ID, new String[]{ Integer.toString(expectedEntity.getBloodDonation().getId()) } );
        map.put( DonationRecordLogic.HOSPITAL, new String[]{ expectedEntity.getHospital() } );
        //map.put( DonationRecordLogic.PERSON_ID, new String[]{ Integer.toString(expectedEntity.getPerson().getId()) } );
        map.put( DonationRecordLogic.TESTED, new String[]{ Boolean.toString(expectedEntity.getTested()) } );
        };

        IntFunction<String> generateString = ( int length ) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            //from 97 inclusive to 123 exclusive
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };

        //idealy every test should be in its own method
        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.ADMINISTRATOR, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.ADMINISTRATOR, new String[]{ generateString.apply( 101 ) } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.CREATED, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.CREATED, new String[]{ generateString.apply( 101 ) } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        //sampleMap.replace( DonationRecordLogic.DONATION_ID, new String[]{ "" } );
        //assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        //sampleMap.replace( DonationRecordLogic.DONATION_ID, new String[]{ generateString.apply( 46 ) } );
        //assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.HOSPITAL, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        //sampleMap.replace( DonationRecordLogic.PERSON_ID, new String[]{ "" } );
        //assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        //sampleMap.replace( DonationRecordLogic.PERSON_ID, new String[]{ generateString.apply( 46 ) } );
        //assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
    }

    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = ( int length ) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };

        Map<String, String[]> sampleMap = new HashMap<>();
        
        sampleMap.put( DonationRecordLogic.ADMINISTRATOR, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( DonationRecordLogic.CREATED, new String[]{ logic.convertDateToString(new Date()) } );
        //sampleMap.put( DonationRecordLogic.DONATION_ID, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( DonationRecordLogic.HOSPITAL, new String[]{ generateString.apply( 1 ) } );
        //sampleMap.put( DonationRecordLogic.PERSON_ID, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( DonationRecordLogic.TESTED, new String[]{ "false" } );


        //idealy every test should be in its own method
        DonationRecord returnedDonationRecord = logic.createEntity( sampleMap );

        assertEquals( sampleMap.get( DonationRecordLogic.ADMINISTRATOR )[ 0 ], returnedDonationRecord.getAdministrator() );
        //assertEquals( sampleMap.get( DonationRecordLogic.DONATION_ID )[ 0 ], returnedDonationRecord.getBloodDonation().getId() );
        assertEquals( sampleMap.get( DonationRecordLogic.CREATED )[ 0 ], logic.convertDateToString(returnedDonationRecord.getCreated()) );
        assertEquals( sampleMap.get( DonationRecordLogic.HOSPITAL )[ 0 ], returnedDonationRecord.getHospital() );
        //assertEquals( sampleMap.get( DonationRecordLogic.PERSON_ID )[ 0 ], returnedDonationRecord.getPerson() );
        assertEquals( sampleMap.get( DonationRecordLogic.TESTED )[ 0 ], Boolean.toString(returnedDonationRecord.getTested()) );
        
        
        sampleMap = new HashMap<>();
        sampleMap.put( DonationRecordLogic.ADMINISTRATOR, new String[]{ generateString.apply( 100 ) } );
        sampleMap.put( DonationRecordLogic.CREATED, new String[]{ logic.convertDateToString(new Date()) } );
        //sampleMap.put( DonationRecordLogic.DONATION_ID, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( DonationRecordLogic.HOSPITAL, new String[]{ generateString.apply( 100 ) } );
        //sampleMap.put( DonationRecordLogic.PERSON_ID, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( DonationRecordLogic.TESTED, new String[]{ "false" } );


        //idealy every test should be in its own method
        returnedDonationRecord = logic.createEntity( sampleMap );

        assertEquals( sampleMap.get( DonationRecordLogic.ADMINISTRATOR )[ 0 ], returnedDonationRecord.getAdministrator() );
        //assertEquals( sampleMap.get( DonationRecordLogic.DONATION_ID )[ 0 ], returnedDonationRecord.getBloodDonation().getId() );
        assertEquals( sampleMap.get( DonationRecordLogic.CREATED )[ 0 ], logic.convertDateToString(returnedDonationRecord.getCreated()) );
        assertEquals( sampleMap.get( DonationRecordLogic.HOSPITAL )[ 0 ], returnedDonationRecord.getHospital() );
        //assertEquals( sampleMap.get( DonationRecordLogic.PERSON_ID )[ 0 ], returnedDonationRecord.getPerson() );
        assertEquals( sampleMap.get( DonationRecordLogic.TESTED )[ 0 ], Boolean.toString(returnedDonationRecord.getTested()) );
        
    }

    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals( Arrays.asList( " ID ", " Tested ", " Administrator ", " Hospital ", " Created ", " BloodDonation ", " Person " ), list );
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals( Arrays.asList( DonationRecordLogic.ID, DonationRecordLogic.TESTED, DonationRecordLogic.ADMINISTRATOR, DonationRecordLogic.HOSPITAL, DonationRecordLogic.CREATED, DonationRecordLogic.DONATION_ID, DonationRecordLogic.PERSON_ID ), list );
    }

    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList( expectedEntity );
        //e.getId(), e.getTested(), e.getAdministrator(), e.getHospital(), e.getCreated(), e.getBloodDonation().getId(), e.getPerson().getId()
        assertEquals( expectedEntity.getId(), list.get( 0 ) );
        assertEquals( expectedEntity.getTested(), list.get( 1 ) );
        assertEquals( expectedEntity.getAdministrator(), list.get( 2 ) );
        assertEquals( expectedEntity.getHospital(), list.get( 3 ) );
        assertEquals( expectedEntity.getCreated(), list.get( 4 ) );
        if(expectedEntity.getBloodDonation()!=null)
        assertEquals( expectedEntity.getBloodDonation().getId(), list.get( 5 ) );
        else
        assertEquals( expectedEntity.getBloodDonation(), list.get( 5 ) );
        if(expectedEntity.getPerson()!=null)
        assertEquals( expectedEntity.getPerson().getId(), list.get( 6 ) );
        else
            assertEquals( expectedEntity.getPerson(), list.get( 6 ) );
    }
}
