package dal;

import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ehsan
 */
public class BloodDonationDAL extends GenericDAL<BloodDonation> {

    public BloodDonationDAL() {
        super(BloodDonation.class);
    }

    @Override
    public List<BloodDonation> findAll() {
        return findResults("BloodDonation.findAll", null);
    }

    @Override
    public BloodDonation findById(int donationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("donationId", donationId);

        return findResult("BloodDonation.findByDonationId", map);
    }

    public List<BloodDonation> findByMilliliters(int milliliters) {
        Map<String, Object> map = new HashMap<>();
        map.put("milliliters", milliliters);

        return findResults("BloodDonation.findByMilliliters", null);

    }

    public List<BloodDonation> findByBloodGroup(BloodGroup bloodGroup) {
        Map<String, Object> map = new HashMap<>();
        map.put("bloodGroup", bloodGroup);

        return findResults("BloodDonation.findByBloodGroup", null);
    }
        public List<BloodDonation> findContaining( String search ) {
        Map<String, Object> map = new HashMap<>();
        map.put( "search", search );
        return findResults( "BloodDonation.findContaining", map );
    }

    public List<BloodDonation> findByRhd(RhesusFactor rhd) {
        Map<String, Object> map = new HashMap<>();
        map.put("rhd", rhd);
        return findResults("BloodDonation.findByRhd", null);
    }

    public List<BloodDonation> findByCreated(Date created) {
        Map<String, Object> map = new HashMap<>();
        map.put("created", created);
        return findResults("BloodDonation.findByCreated", null);
    }

    public List<BloodDonation> findByBloodBank(int bloodBankId) {
        Map<String, Object> map = new HashMap<>();
        map.put("bloodBankId", bloodBankId);
        return findResults("BloodDonation.findByCreated", null);
    }

}
