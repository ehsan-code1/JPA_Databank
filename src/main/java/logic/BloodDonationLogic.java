package logic;

import common.ValidationException;
import entity.BloodDonation;
import entity.BloodBank;
import entity.RhesusFactor;
import entity.BloodGroup;
import dal.BloodDonationDAL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Ehsan
 */
public class BloodDonationLogic extends GenericLogic<BloodDonation, BloodDonationDAL> {

    public static final String BANKID = "bank_id";
    public static final String MILLILITERS = "milliliters";
    public static final String BLOOD_GROUP = "blood_group";
    public static final String RHESUS_FACTOR = "rhesus_factor";
    public static final String CREATED = "created";
    public static final String ID = "id";

    BloodDonationLogic() {
        super(new BloodDonationDAL());
    }

    @Override
    public List<BloodDonation> getAll() {
        return get(() -> dal().findAll());
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("Donation_ID", "Bank_ID", "Milliliters", "Blood_Group", "RHD", "Created");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, BANKID, MILLILITERS, BLOOD_GROUP, RHESUS_FACTOR, CREATED);
    }

    @Override
    public List<?> extractDataAsList(BloodDonation e) {
        return Arrays.asList(e.getId(), e.getBloodBank().getId(), e.getMilliliters(), e.getBloodGroup(), e.getRhd().getSymbol(), e.getCreated().toString());
    }

    @Override
    public BloodDonation getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    public List<BloodDonation> getBloodDonationWithMilliliters(int milliliters) {
        return get(() -> dal().findByMilliliters(milliliters));
    }

    public List<BloodDonation> getBloodDonationWithBloodGroup(BloodGroup bloodGroup) {
        return get(() -> dal().findByBloodGroup(bloodGroup));
    }

    public List<BloodDonation> getBloodDonationWithRhd(RhesusFactor rhd) {
        return get(() -> dal().findByRhd(rhd));
    }

    public List<BloodDonation> getBloodDonationWithCreated(Date created) {
        return get(() -> dal().findByCreated(created));
    }

    public List<BloodDonation> getBloodDonationWithBloodBank(int bankId) {
        return get(() -> dal().findByBloodBank(bankId));
    }
     @Override
    public List<BloodDonation> search( String search ) {
        return get( () -> dal().findContaining( search ) );
    }

    @Override
    public BloodDonation createEntity(Map<String, String[]> parameterMap) {

        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");        
       
        BloodDonation entity = new BloodDonation();

        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }

        String bankId = parameterMap.get(BANKID)[0];
        String milliliters = parameterMap.get(MILLILITERS)[0];
        String bloodGroup = parameterMap.get(BLOOD_GROUP)[0];
        String rhd = parameterMap.get(RHESUS_FACTOR)[0];
        String dateTime = parameterMap.get(CREATED)[0];
        
        validator(bankId, "bloodBank ID ");
        validator(milliliters, "Milliliters ");
        validator(bloodGroup, "BloodGroup ");
        validator(rhd, "RHD ");

        BloodBank bb = new BloodBank(Integer.parseInt(bankId));

        
        String pr = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]";

        if (dateTime != null) {
            Date created = convertStringToDate(dateTime);
            if (convertDateToString(created).matches(pr)) {
                entity.setCreated(created);
            } else {
                DateFormat df = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
                Date dateobj = new Date();
                entity.setCreated(convertStringToDate(df.format(dateobj)));
            }
        } else {
            DateFormat df = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
            Date dateobj = new Date();
            entity.setCreated(convertStringToDate(df.format(dateobj)));
        }

        entity.setMilliliters(Integer.parseInt(milliliters));

        entity.setBloodGroup(BloodGroup.get(bloodGroup));

        entity.setRhd(RhesusFactor.getRhesusFactor(rhd));

        entity.setBloodBank(bb);

        return entity;
    }
    
    public void validator (String value,String name) {
        if (value == null || value.trim().isEmpty()) {
                throw new ValidationException( name + "can not be null" );
        }
    }
}
