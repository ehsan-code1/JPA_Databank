package dal;

import entity.DonationRecord;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Parnoor
 */
public class DonationRecordDAL extends GenericDAL<DonationRecord> {

    public DonationRecordDAL() {
        super(DonationRecord.class);
    }

    @Override
    public List<DonationRecord> findAll() {
        return findResults( "DonationRecord.findAll", null );
    }

    @Override
    public DonationRecord findById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put( "recordId", id );
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id" and value for it is put in map
        return findResult( "DonationRecord.findByRecordId", map );
    }
    
    public List<DonationRecord> findByTested(boolean tested)
    {
        Map<String, Object> map = new HashMap<>();
        map.put( "tested", tested );
        return findResults("DonationRecord.findByTested",map);
    }
    
    public List<DonationRecord> findByAdministrator(String administrator)
    {
        Map<String, Object> map = new HashMap<>();
        map.put( "administrator", administrator);
        return findResults("DonationRecord.findByAdministrator",map);
    }
    
    public List<DonationRecord> findByHospital(String username)
    {
        Map<String, Object> map = new HashMap<>();
        map.put( "hospital", username);
        return findResults("DonationRecord.findByHospital",map);
    }
    
    public List<DonationRecord> findByCreated(Date created)
    {
        Map<String, Object> map = new HashMap<>();
        map.put( "created", created);
        return findResults("DonationRecord.findByCreated",map);
    }
    
    public List<DonationRecord> findByPerson(int personId)
    {
        Map<String, Object> map = new HashMap<>();
        map.put( "personId", personId);
        return findResults("DonationRecord.findByPerson",map);
    }
    
    public List<DonationRecord> findByDonation(int donationId)
    {
        Map<String, Object> map = new HashMap<>();
        map.put( "donationId", donationId);
        return findResults("DonationRecord.findByDonation",map);
    }
}
