package util.aprational;

import org.apfloat.Aprational;

/**
 * Created by matt on 7/26/17.
 */
public class AprationalFactory {


    public static Aprational fromString(String numString) throws NumberFormatException {

        if(numString.contains("/")) {

            String[] frac = numString.split("/");
            if (frac.length != 2) {
                throw new NumberFormatException("Invalid fraction" + numString.trim());
            }

            Aprational num = new Aprational(frac[0].trim());
            Aprational denom = new Aprational(frac[1].trim());

            return num.divide(denom);

        } else {
            return  new Aprational(numString);
        }
    }
}
