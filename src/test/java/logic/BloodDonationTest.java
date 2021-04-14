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
import java.util.function.Consumer;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
            byte[] array = new byte[5]; // length is bounded by 7, to have a random string each time for setup
            new Random().nextBytes(array);
          
            String generatedString = new String(array, Charset.forName("UTF-8"));
            bb.setName(generatedString);
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
        BloodDonation bD = logic.getWithId(expectedEntity.getId());

        //the two BloodDonation (testBloodDonation and returnedBloodDonation) must be the same
        assertBloodDonationEquals(expectedEntity, bD);
    }

    @Test
    final void testGetWithBloodBank() {
        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithBloodBank(expectedEntity.getBloodBank().getId());
        int found = 0;

        found = returnedBloodDonation.stream().map(b -> {
            assertEquals(expectedEntity.getBloodBank().getId(), b.getBloodBank().getId());
            return b;
        }).filter(b -> (b.getId().equals(expectedEntity.getId()))).map(b -> {
            assertBloodDonationEquals(expectedEntity, b);
            return b;
        }).map(_item -> 1).reduce(found, Integer::sum);
        assertEquals(1, found, "if zero means it wasnot found");
    }

    @Test
    final void testGetWithRhd() {
        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithRhd(expectedEntity.getRhd());
        int found = 0;

        found = returnedBloodDonation.stream().map(b -> {
            assertEquals(expectedEntity.getRhd(), b.getRhd());
            return b;
        }).filter(b -> (b.getId().equals(expectedEntity.getId()))).map(b -> {
            assertBloodDonationEquals(expectedEntity, b);
            return b;
        }).map(_item -> 1).reduce(found, Integer::sum);
        assertEquals(1, found, "if zero means it wasnot found");
    }

    @Test
    final void testGetWithMilliliters() {
        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithMilliliters(expectedEntity.getMilliliters());
        int found = 0;

        found = returnedBloodDonation.stream().map(b -> {
            assertEquals(expectedEntity.getMilliliters(), b.getMilliliters());
            return b;
        }).filter(b -> (b.getId().equals(expectedEntity.getId()))).map(b -> {
            assertBloodDonationEquals(expectedEntity, b);
            return b;
        }).map(_item -> 1).reduce(found, Integer::sum);
        assertEquals(1, found, "if zero means it wasnot found");
    }

    @Test
    final void testGetWithBloodGroup() {
        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithBloodGroup(expectedEntity.getBloodGroup());
        int found = 0;

        found = returnedBloodDonation.stream().map(b -> {
            assertEquals(expectedEntity.getBloodGroup(), b.getBloodGroup());
            return b;
        }).filter(b -> (b.getId().equals(expectedEntity.getId()))).map(b -> {
            assertBloodDonationEquals(expectedEntity, b);
            return b;
        }).map(_item -> 1).reduce(found, Integer::sum);
        assertEquals(1, found, "if zero means it wasnot found");
    }

    @Test
    final void testGetWithCreated() {
        List<BloodDonation> returnedBloodDonation = logic.getBloodDonationWithCreated(expectedEntity.getCreated());
        int found = 0;

        found = returnedBloodDonation.stream().map(b -> {
            assertEquals(expectedEntity.getCreated(), b.getCreated());
            return b;
        }).filter(b -> (b.getId().equals(expectedEntity.getId()))).map(b -> {
            assertBloodDonationEquals(expectedEntity, b);
            return b;
        }).map(_item -> 1).reduce(found, Integer::sum);
        assertEquals(1, found, "if zero means it wasnot found");
    }

    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedEntity);
        assertEquals(expectedEntity.getId(), list.get(0));
        assertEquals(expectedEntity.getBloodBank().getId(), list.get(1));
        assertEquals(expectedEntity.getMilliliters(), list.get(2));
        assertEquals(expectedEntity.getBloodGroup(), list.get(3));
        assertEquals(expectedEntity.getRhd().getSymbol(), list.get(4));
        assertEquals(expectedEntity.getCreated().toString(), list.get(5));

    }

    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("Donation_ID", "Bank_ID", "Milliliters", "Blood_Group", "RHD", "Created"), list);
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(BloodDonationLogic.ID, BloodDonationLogic.BANKID, BloodDonationLogic.MILLILITERS, BloodDonationLogic.BLOOD_GROUP, BloodDonationLogic.RHESUS_FACTOR, BloodDonationLogic.CREATED), list);

    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        BloodBankLogic bLogic = LogicFactory.getFor("BloodBank");

        sampleMap.put(BloodDonationLogic.ID, new String[]{expectedEntity.getId().toString()});
        sampleMap.put(BloodDonationLogic.BANKID, new String[]{expectedEntity.getBloodBank().getId().toString()});
        sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
        sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().toString()});
        sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().toString()});
        sampleMap.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});

        BloodDonation returnedBloodDonation = logic.createEntity(sampleMap);

        BloodBank bB = bLogic.getWithId(expectedEntity.getBloodBank().getId());

        returnedBloodDonation.setBloodBank(bB);

        assertBloodDonationEquals(expectedEntity, returnedBloodDonation);

    }

    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        BloodBankLogic bLogic = LogicFactory.getFor("BloodBank");

        sampleMap.put(BloodDonationLogic.BANKID, new String[]{expectedEntity.getBloodBank().getId().toString()});

        sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{"7200"});

        sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{"AB"});

        sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{"Positive"});

        sampleMap.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(new Date())});

        BloodDonation returnedBloodDonation = logic.createEntity(sampleMap);

        BloodBank bB = bLogic.getWithId(expectedEntity.getBloodBank().getId());

        returnedBloodDonation.setBloodBank(bB);

        logic.add(returnedBloodDonation);

        assertEquals(sampleMap.get(BloodDonationLogic.BANKID)[0], returnedBloodDonation.getBloodBank().getId().toString());
        assertEquals(sampleMap.get(BloodDonationLogic.BLOOD_GROUP)[0], returnedBloodDonation.getBloodGroup().toString());
        assertEquals(sampleMap.get(BloodDonationLogic.MILLILITERS)[0], Integer.toString(returnedBloodDonation.getMilliliters()));
        assertEquals(sampleMap.get(BloodDonationLogic.RHESUS_FACTOR)[0], returnedBloodDonation.getRhd().toString());
        assertEquals(sampleMap.get(BloodDonationLogic.CREATED)[0], logic.convertDateToString(returnedBloodDonation.getCreated()));

        logic.delete(returnedBloodDonation);
    }

    @Test
    final void createEntityWithNullValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            sampleMap.put(BloodDonationLogic.ID, new String[]{expectedEntity.getId().toString()});
            sampleMap.put(BloodDonationLogic.BANKID, new String[]{expectedEntity.getBloodBank().getId().toString()});
            sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
            sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().toString()});
            sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().toString()});
            sampleMap.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});

        };

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodDonationLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));

        sampleMap.replace(BloodDonationLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodDonationLogic.MILLILITERS, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));

        sampleMap.replace(BloodDonationLogic.MILLILITERS, new String[]{});
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodDonationLogic.BLOOD_GROUP, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));

        sampleMap.replace(BloodDonationLogic.BLOOD_GROUP, new String[]{});
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodDonationLogic.RHESUS_FACTOR, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));

        sampleMap.replace(BloodDonationLogic.RHESUS_FACTOR, new String[]{});
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(BloodDonationLogic.CREATED, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));

        sampleMap.replace(BloodDonationLogic.CREATED, new String[]{});
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
    }

    @Test
    final void createEntityWrongValues() {
        Map<String, String[]> sampleMap = new HashMap<>();

        sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{"7200"});

        sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{"ABB"});

        sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{"Positives"});

        sampleMap.put(BloodDonationLogic.CREATED, new String[]{"no"});

        assertThrows(IllegalArgumentException.class, () -> logic.createEntity(sampleMap));

    }

    @Test
    final void createEntityWithWrongDateFormat() {
        Map<String, String[]> sampleMap = new HashMap<>();

        sampleMap.put(BloodDonationLogic.MILLILITERS, new String[]{"7200"});

        sampleMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{"AB"});

        sampleMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{"Positive"});

        sampleMap.put(BloodDonationLogic.CREATED, new String[]{"2222-1-1 11-45-666"});

        BloodDonation bD = logic.createEntity(sampleMap);

        assertTrue(bD.getCreated().equals(logic.convertStringToDate(LocalDateTime.now().toString())));

    }
}
