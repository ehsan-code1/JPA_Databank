package entity;

import java.util.List;
import java.util.Arrays;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public enum BloodGroup {
    A, B, AB, O;

    public static List<String> getAll() {
        return Arrays.asList(A.toString(), B.toString(), AB.toString(), O.toString());
    }

    public static BloodGroup get(String str) {
        if (str.equals(A.toString())) {
            return BloodGroup.A;
        }

        if (str.equals(B.toString())) {
            return BloodGroup.B;
        }
        if (str.equals(AB.toString())) {
            return BloodGroup.AB;
        }
        if (str.equals(O.toString())) {
            return BloodGroup.O;
        } else {
            throw new IllegalArgumentException("Invalid Blood Group");
        }

    }

}
