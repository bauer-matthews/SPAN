package util.aprational;

import org.apfloat.Aprational;

/**
 * SPAN - Stochastic Protocol Analyzer
 * <p>
 * Created: 1/10/18
 *
 * @author Matthew S. Bauer
 * @version 1.0
 */
public class ApUtils {

    public static Aprational[] vectorScalarMult(Aprational[] vector, Aprational scalar) {

        for (int i = 0; i < vector.length; i++) {
            vector[i] = vector[i].multiply(scalar);
        }

        return vector;
    }

    public static Aprational[] deepVectorScalarMult(Aprational[] vector, Aprational scalar) {

        Aprational[] newVector = new Aprational[vector.length];

        for (int i = 0; i < vector.length; i++) {
            newVector[i] = vector[i].multiply(scalar);
        }

        return newVector;
    }

    public static Aprational[] vectorVectorAdd(Aprational[] vector1, Aprational[] vector2) {

        for (int i = 0; i < Math.min(vector1.length, vector2.length); i++) {
            vector1[i] = vector1[i].add(vector2[i]);
        }

        return vector1;
    }
}
