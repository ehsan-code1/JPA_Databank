package logic;

import logic.BloodDonationLogic;
import logic.BloodBankLogic;
import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.*;

/**
 * This class is has the example of how to add dependency when working with junit. it is commented because some components need to be made first. You will have to import everything you need.
 *
 * @author Shariar (Shawn) Emami
 */
class BloodDonationTest {

    private BloodDonationLogic logic;

    private BloodDonation expectedEntity;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat("/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test");
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {

        logic = LogicFactory.getFor("BloodDonation");
        /* **********************************
         * ***********IMPORTANT**************
         * **********************************/
        //we only do this for the test.
        //always create Entity using logic.
        //we manually make the account to not rely on any logic functionality , just for testing

        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction
        em.getTransaction().begin();
        //check if the depdendecy exists on DB already
        //em.find takes two arguments, the class type of return result and the primery key.
        BloodBank bb = em.find(BloodBank.class, 1);
        //if result is null create the entity and persist it
        if (bb == null) {
            //cearet object
            bb = new BloodBank();
            bb.setName("JUNIT");
            bb.setPrivatelyOwned(true);
            bb.setEstablished(logic.convertStringToDate("1111-11-11 11:11:11"));
            bb.setEmplyeeCount(111);
            //persist the dependency first
            em.persist(bb);
        }

        //create the desired entity
        BloodDonation entity = new BloodDonation();
        entity.setMilliliters(100);
        entity.setBloodGroup(BloodGroup.AB);
        entity.setRhd(RhesusFactor.Negative);
        entity.setCreated(logic.convertStringToDate("1111-11-11 11:11:11"));
        //add dependency to the desired entity
        entity.setBloodBank(bb);

        //add desired entity to hibernate, entity is now managed.
        //we use merge instead of add so we can get the managed entity.
        expectedEntity = em.merge(entity);
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if (expectedEntity != null) {
            logic.delete(expectedEntity);
        }
    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<BloodDonation> list = logic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();

        //make sure account was created successfully
        assertNotNull(expectedEntity);
        //delete the new account
        logic.delete(expectedEntity);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be one less
        assertEquals(originalSize - 1, list.size());
    }

    /**
     * helper method for testing all BloodDonation fields
     *
     * @param expected
     * @param actual
     */
    private void assertBloodDonationEquals(BloodDonation expected, BloodDonation actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBloodBank().getId(), actual.getBloodBank().getId());
        assertEquals(expected.getBloodGroup(), actual.getBloodGroup());
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getMilliliters(), actual.getMilliliters());
        assertEquals(expected.getRhd(), actual.getRhd());
    }
    
      @Test
    final void testGetWithId() {
        //using the id of test BloodDonation get another account from logic
        BloodDonation bD = logic.getWithId( expectedEntity.getId() );

        //the two BloodDonation (testBloodDonation and returnedBloodDonation) must be the same
        assertBloodDonationEquals( expectedEntity, bD );
    }
    
      @Test
    final void testGetWithBloodBank() {
        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithBloodBank(1);
          assertTrue(returnedBloodDonation.size() > 0);
    }
    
       @Test
    final void testGetWithRhd() {
        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithCreated(expectedEntity.getCreated());
           assertEquals(returnedBloodDonation.size(),1,"size was " + returnedBloodDonation.size() );
  
    }
        @Test
    final void testGetWithMilliliters() {
        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithMilliliters(100);
           assertEquals(returnedBloodDonation.size(),1,"size was " + returnedBloodDonation.size() + "Milli " + expectedEntity.getMilliliters() );
  
    }
  
}