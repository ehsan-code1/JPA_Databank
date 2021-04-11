package logic;

import common.ValidationException;
import entity.BloodDonation;
import entity.RhesusFactor;
import entity.BloodGroup;
import dal.BloodDonationDAL;
import entity.BloodBank;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    public BloodDonation getWithId(int id) {
        return get(() -> dal().findById(id));
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

        if (e.getBloodBank() == null) {
            return Arrays.asList(e.getId(), e.getBloodBank(), e.getMilliliters(), e.getBloodGroup(), e.getRhd().getSymbol(), e.getCreated().toString());
        }
        return Arrays.asList(e.getId(), e.getBloodBank().getId(), e.getMilliliters(), e.getBloodGroup(), e.getRhd().getSymbol(), e.getCreated().toString());
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
    public List<BloodDonation> search(String search) {
        return get(() -> dal().findContaining(search));
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

        String milliliters = parameterMap.get(MILLILITERS)[0];
        String bloodGroup = parameterMap.get(BLOOD_GROUP)[0];
        String rhd = parameterMap.get(RHESUS_FACTOR)[0];
        String dateTime = parameterMap.get(CREATED)[0];

        validator(milliliters, "Milliliters ");
        validator(bloodGroup, "BloodGroup ");
        validator(rhd, "RHD ");
        String pr = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]";

        if (dateTime != null && dateTime.matches(pr)) {
            Date created = convertStringToDate(dateTime);
            entity.setCreated(created);
        } else {
            entity.setCreated(convertStringToDate(LocalDateTime.now().toString()));
        }
        entity.setMilliliters(Integer.parseInt(milliliters));

        entity.setBloodGroup(BloodGroup.get(bloodGroup));

        entity.setRhd(RhesusFactor.getRhesusFactor(rhd));

        return entity;
    }

    public void validator(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(name + "can not be null");
        }
    }

    @Override
    public BloodDonation updateEntity(Map<String, String[]> parameterMap) {

        //getwithid(id) get the current entity from db
        BloodDonation entity = getWithId(Integer.parseInt(parameterMap.get(ID)[0]));

        if (!entity.getBloodBank().getId().toString().equals(parameterMap.get(MILLILITERS)[0])) {
            entity.setMilliliters(Integer.parseInt(parameterMap.get(MILLILITERS)[0]));
        }
        if (!entity.getBloodGroup().toString().equals(parameterMap.get(BLOOD_GROUP)[0])) {
            entity.setBloodGroup(BloodGroup.get(parameterMap.get(BLOOD_GROUP)[0]));
        }
        if (!entity.getRhd().toString().equals(parameterMap.get(RHESUS_FACTOR))) {
            entity.setRhd(RhesusFactor.getRhesusFactor(parameterMap.get(RHESUS_FACTOR)[0]));
        }
        if (!entity.getCreated().toString().equals(parameterMap.get(CREATED))) {
            try {
                entity.setCreated(convertStringToDate(parameterMap.get(CREATED)[0]));
            } catch (Exception e) {
                entity.setCreated(convertStringToDate(LocalDateTime.now().toString()));
            }
        }
        BloodBankLogic bl = new BloodBankLogic();
        if (!entity.getBloodBank().equals(bl.getWithId(Integer.parseInt(parameterMap.get(BANKID)[0])))) {
            if (bl.getWithId(Integer.parseInt(parameterMap.get(BANKID)[0])) == null) {
                throw new ValidationException("Foreign Key Constraint Failed");               
            }
            entity.setBloodBank(bl.getWithId(Integer.parseInt(parameterMap.get(BANKID)[0])));
        }

        //check data from map against entity and udpate it
        //check if depdendecy has changed, if so update it using depedency logic
        return entity;
    }
}
