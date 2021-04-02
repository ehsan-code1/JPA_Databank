package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import entity.BloodBank;
import entity.Person;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import static logic.BloodBankLogic.EMPLOYEE_COUNT;
import static logic.BloodBankLogic.ESTABLISHED;
import static logic.BloodBankLogic.ID;
import static logic.BloodBankLogic.NAME;
import static logic.BloodBankLogic.OWNER_ID;
import static logic.BloodBankLogic.PRIVATELEY_OWNED;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Nouran Nouh 
 */
public class BloodBankLogicTest {
    
    private BloodBankLogic logic; 
    private BloodBank expectedBloodBank;
    
    public BloodBankLogicTest() {
    }
    
    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
       TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
    }
    
    @AfterAll
    public static void tearDownAfterClass() throws Exception {
        
          TomcatStartUp.stopAndDestroyTomcat();
    }
    
    @BeforeEach
    public void setUp() {
        logic=LogicFactory.getFor("BloodBank");
       //PersonLogic personLogic=LogicFactory.getFor("Person");
        //Person person=personLogic.getWithId(logic.);
        
       
        
       
    
        //bloodBank.setId(2);
       
    
        //get instance of entity manager 
        
        EntityManager em = EMFactory.getEMF().createEntityManager();
          //start transaction
        em.getTransaction().begin();
          
        Person person=em.find(Person.class, 1);
        
        if(person==null){
            person=new Person();
            person.setFirstName("Junit");
            person.setLastName("Test");
            person.setAddress("11 Smith");
            person.setBirth(logic.convertStringToDate("1996-11-11  11:11:11"));
            person.setPhone("615888999");
            em.persist(person);
        }
          
        BloodBank bloodBank= new BloodBank();
        
        bloodBank.setOwner(person);
        bloodBank.setName("Life Bank");
        bloodBank.setPrivatelyOwned(true);
        bloodBank.setEstablished(new Date(10000));
        bloodBank.setEmplyeeCount(1000);
        
        //use merge to get the updated generated ID
        expectedBloodBank=em.merge(bloodBank);
        
        //comit changes 
        
        em.getTransaction().commit();
          
          //close Entity manager
        em.close();
        
        
    }
    
    @AfterEach
    public void tearDown() {
        if(expectedBloodBank!=null){
            logic.delete(expectedBloodBank);
        }
    }

    /**
     * Test of getAll method, of class BloodBankLogic.
     */
    @Test
    public void testGetAll() {
       //get all from db 
        List<BloodBank> result = logic.getAll();
        int originalSize=result.size();
        //make sure that BloodBank was created successfuly 
        assertNotNull(expectedBloodBank);
        //delete the new blood bank 
        logic.delete(expectedBloodBank);
        
        //get all blood bank 
        result=logic.getAll();
        //the original size must be one less
        assertEquals(originalSize-1, result.size());
       
    }
    
    /**
     * helper method used for testing all blood bank field 
     * @param expected 
     * @param actual 
     */
    
     private void assertBloodBankEquals(BloodBank expected, BloodBank actual){
        assertEquals(expected.getId(),actual.getId());
        assertEquals(expected.getOwner().getId(),actual.getOwner().getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrivatelyOwned(),actual.getPrivatelyOwned());
        assertEquals(expected.getEstablished(),actual.getEstablished());
        assertEquals(expected.getEmplyeeCount(),actual.getEmplyeeCount());
       
       
        
         
     }

    /**
     * Test of getWithId method, of class BloodBankLogic.
     * using the id get another blood bank 
     */
    @Test
    public void testGetWithId() {
        BloodBank returnedBloodBank=logic.getWithId(expectedBloodBank.getId());
        
        //the two blood bank must be the same 
        assertBloodBankEquals(expectedBloodBank, returnedBloodBank);
        
    }

    /**
     * Test of getBloodBankWithName method, of class BloodBankLogic.
     * 
     */
    @Test
    public void testGetBloodBankWithName() {
       BloodBank returnedBloodBank=logic.getBloodBankWithName(expectedBloodBank.getName());
       
        assertBloodBankEquals(expectedBloodBank, returnedBloodBank);
    }

    /**
     * Test of getBloodBankWithPrivateleyOwned method, of class BloodBankLogic.
     */
    @Test
    public void testGetBloodBankWithPrivateleyOwned() {
        int found=0;
       List <BloodBank> returnedBloodBank=logic.getBloodBankWithPrivateleyOwned(expectedBloodBank.getPrivatelyOwned());
       
       for(BloodBank b:returnedBloodBank){
            assertEquals(expectedBloodBank.getPrivatelyOwned(), b.getPrivatelyOwned());
            if(b.getId().equals(expectedBloodBank.getId())){
                assertBloodBankEquals(expectedBloodBank, b);
                found++;
            }
       }
        assertEquals(1, found,"if zero means it wasnot found");
    }

    /**
     * Test of getBloodBankWithEstablished method, of class BloodBankLogic.
     */
    @Test
    public void testGetBloodBankWithEstablished() {
        int found=0;
       List <BloodBank> returnedBloodBank=logic.getBloodBankWithEstablished(expectedBloodBank.getEstablished());
       
       for(BloodBank b:returnedBloodBank){
            assertEquals(expectedBloodBank.getEstablished(), b.getEstablished());
            if(b.getId().equals(expectedBloodBank.getId())){
                assertBloodBankEquals(expectedBloodBank, b);
                found++;
            }
       }
        assertEquals(1, found,"if zero means it wasnot found");
    }

    /**
     * Test of getBloodBankWithOwner method, of class BloodBankLogic.
     */
    @Test
    public void testGetBloodBankWithOwner() {
        
       BloodBank returnedBloodBank=logic.getBloodBankWithOwner(expectedBloodBank.getOwner().getId());
        assertEquals(expectedBloodBank, returnedBloodBank);
      
    }

    /**
     * Test of getBloodBankWithEmployeeCount method, of class BloodBankLogic.
     */
    @Test
    public void testGetBloodBankWithEmployeeCount() {
      int found=0;
      List <BloodBank> returnedBloodBank=logic.getBloodBankWithEmployeeCount(expectedBloodBank.getEmplyeeCount());
       
      for(BloodBank b:returnedBloodBank){
       
          assertEquals(expectedBloodBank.getEmplyeeCount(), b.getEmplyeeCount());
          if(b.getId().equals(expectedBloodBank.getId())){
            
            assertBloodBankEquals(expectedBloodBank, b);
            found++;
            
           }
        }
        assertEquals(1, found,"if zero means it wasnot found");
    }

    /**
     * Test of getColumnNames method, of class BloodBankLogic.
     */
    @Test
    public void testGetColumnNames() {
        List<String> list=logic.getColumnNames();
        assertEquals(Arrays.asList("Bank_id","Owner","Name","Privateley_Owned","Established","Employee_Count"),list);
    }

    /**
     * Test of getColumnCodes method, of class BloodBankLogic.
     */
    @Test
    public void testGetColumnCodes() {
       List<String> list=logic.getColumnCodes();
        assertEquals(Arrays.asList(BloodBankLogic.ID,BloodBankLogic.OWNER_ID,BloodBankLogic.NAME,BloodBankLogic.PRIVATELEY_OWNED,BloodBankLogic.ESTABLISHED,BloodBankLogic.EMPLOYEE_COUNT), list);
        
    }

    /**
     * Test of extractDataAsList method, of class BloodBankLogic.
     * Arrays.asList(e.getId(),e.getOwner(),e.getName(),e.getPrivatelyOwned(),e.getEstablished(),e.getEmplyeeCount()); 
     */
    @Test
    public void testExtractDataAsList() {
        List<?> list=logic.extractDataAsList(expectedBloodBank);
        assertEquals(expectedBloodBank.getId(), list.get(0));
        assertEquals(expectedBloodBank.getOwner().getId(), list.get(1));
        assertEquals(expectedBloodBank.getName(), list.get(2));
        assertEquals(expectedBloodBank.getPrivatelyOwned(), list.get(3));
        assertEquals(expectedBloodBank.getEstablished(), list.get(4));
        assertEquals(expectedBloodBank.getEmplyeeCount(), list.get(5));
        
        
    }

    /**
     * Test of createEntity method, of class BloodBankLogic.
     * Arrays.asList(e.getId(),e.getOwner(),e.getName(),e.getPrivatelyOwned(),e.getEstablished(),e.getEmplyeeCount()); 
     */
    @Test
    public void testCreateEntityAndAdd() {
        
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(BloodBankLogic.OWNER_ID, new String[]{"1"});
        sampleMap.put(BloodBankLogic.NAME, new String[]{"create1"});
        sampleMap.put(BloodBankLogic.PRIVATELEY_OWNED, new String[]{"true"});
        sampleMap.put(BloodBankLogic.ESTABLISHED, new String[]{"1995-11-11 11:11:11"});
        sampleMap.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{"100"});
        
        BloodBank returnedBloodBank=logic.createEntity(sampleMap);
        logic.add(returnedBloodBank);
        returnedBloodBank=logic.getBloodBankWithName(returnedBloodBank.getName());
        
        assertEquals(sampleMap.get(BloodBankLogic.OWNER_ID)[0],returnedBloodBank.getOwner());
        assertEquals(sampleMap.get(BloodBankLogic.NAME)[0],returnedBloodBank.getName());
        assertEquals(sampleMap.get(BloodBankLogic.PRIVATELEY_OWNED)[0],returnedBloodBank.getPrivatelyOwned());
        assertEquals(sampleMap.get(BloodBankLogic.ESTABLISHED)[0],returnedBloodBank.getEstablished());
        assertEquals(sampleMap.get(BloodBankLogic.EMPLOYEE_COUNT)[0],returnedBloodBank.getEmplyeeCount());
        
        logic.delete(returnedBloodBank);
       
    }
    
}
