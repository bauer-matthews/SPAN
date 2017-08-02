package util.apfloat;

import org.apfloat.Apfloat;
import parser.protocol.ProtocolParseException;

/**
 * Created by matt on 7/26/17.
 */
public class ApfloatFactory {


    public static Apfloat fromString(String numString) throws NumberFormatException {

        String[] frac = numString.split("/");
        if (frac.length != 2) {
            throw new NumberFormatException("Invalid fraction" + numString.trim());
        }

        Apfloat num = new Apfloat(frac[0].trim());
        Apfloat denom = new Apfloat(frac[1].trim());

        return num.divide(denom);
    }

}
