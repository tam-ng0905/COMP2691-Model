/**
 * Class with methods to implement the basic arithmetic operations by
 * operating on bit fields.
 * <p>
 * This is the skeleton code form COMP2691 Assignment #2.
 */

/*
    Author: Tam Nguyen

 */
public class BinaryCalculator {
    public static BitField add(BitField a, BitField b) {
        if (null == a || null == b || a.size() != b.size()) {
            throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
        }

        BitField result = new BitField(a.size());
        boolean  carry = false;
        for (int i = 0; i < a.size(); i++) {
            // get current bit from each number
            boolean ai = a.get(i);
            boolean bi = b.get(i);

            int val = 0;
            if (ai) { val ++; }
            if (bi) { val ++; }
            if (carry) { val ++; }
            // val will be one of 00 01 10 11
            //                     0  1  2  3
            result.set(i, val % 2 == 1);  // take LSB from val (set if odd, unset if even)
            carry = (val >= 2); // take next bit from val (set if >= 2, otherwise unset)
        }

        return result;
    }
//    public static BitField add(BitField a, BitField b) {
//        if (null == a || null == b || a.size() != b.size()) {
//            throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
//        }
//
//        BitField result = new BitField(a.size());
//        boolean carries = false;
//        for (int i = 0; i < a.size(); i++) {
//            if (a.get(i) && b.get(i)) {
//                if (carries) {
//                    result.set(i, true);
//                    carries = true;
//                } else {
//                    result.set(i, false);
//                    carries = true;
//                }
//            } else if (!a.get(i) && !b.get(i)) {
//                if (carries) {
//                    result.set(i, true);
//                    carries = false;
//
//                } else {
//                    result.set(i, false);
//                    carries = false;
//                }
//            } else if (a.get(i) || b.get(i)) {
//                if (carries) {
//                    result.set(i, false);
//                    carries = true;
//                } else {
//                    result.set(i, true);
//                    carries = false;
//                }
//            }
//        }
//        return result ;
//    }

    public static BitField subtract(BitField A, BitField B) {
        if (null == A || null == B || A.size() != B.size()) {
            throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
        }
        BitField negated = negate(B);

        return add(A, negated);
    }
    public static BitField multiply(BitField a, BitField b) {
        if (null == a || null == b || a.size() != b.size()) {
            throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
        }
        // 13 * 5
        // 0000 1101
        // 0000 0101
        // ----------
        // 0000 0101 5*2^0
        // 0001 0100 5*2^2
        //+0010 1000 5*2^3
        // -----------
        BitField result = new BitField(a.size());
        for (int i = 0; i < a.size(); i++) {
            if(a.get(i)){
                result = add(result, shift(b, i));
            }
        }
        return result;
    }
    public static BitField[] divide(BitField a, BitField b) {
        if (null == a || null == b || a.size() != b.size()) {
            throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
        }
        int sz = a.size();
        boolean aNeg = a.getMSB();
        boolean bNeg = b.getMSB();
        boolean resNeg = aNeg != bNeg;
        boolean remNeg = aNeg;

        // (condition) ? (value if true) : (value if false)
        a = aNeg ? negate(a) : a;
        b = bNeg ? negate(b) : b;
        // From here, inputs are positive. (unsigned)
         // Return both the quotient and the remainder
        BitField[] out = new BitField[2];
        BitField quotient = out[0] = new BitField(sz); // quotient
        BitField remainder = a;

        // b = 00010110
        int bMSBi = mostSignificantEnabledBit(b); // 4
        if(bMSBi == -1){
            return null;
        }
        int maxShift = sz - bMSBi - 1; // 8 - 4 - 1 = 3

        // 17 / 6 = [ 2, 5 ]

        // 6*2^13....
        // 6*2^2 = 24
        // 17 - 24 = 65528

        // 6*2^1 = 12
        // 17 - 12 = 5 , 5 < 17, remainder = 5, quotient gets bit 1 set (0000....010)

        // 5 - 6 = 65535
        // [ 2, 5 ]

        for (int i = maxShift; i >= 0; i--) {
            // shift b over by i places, store in new variable
            BitField shiftedB = shift(b, i); // b * 2^i
            // subtract that shifted b from current remainder, store in new variable.
            BitField rem = subtract(remainder, shiftedB);
            // if the difference is less than the current remainder:
            if(compare(rem, remainder) == -1){
                // use that difference as the next remainder
                remainder = rem;
                // set the current bit in the quotient
                quotient.set(i, true);
            }
        }

        // put final remainder in output array
        out[1] = remainder;
        // apply the signs we decided on in the begging to the output array
        if(resNeg) {
            out[0] = negate(out[0]);
        }
        if (remNeg) {
            out[1] = negate(out[1]);
        }

        return out;
    }

    /** Do unsigned comparison between numbers, get a result indicating the ordering of the two numbers.
     * @param a
     * @param b
     * @return 1 when a > b, -1 when a < b, and 0 when a == b */
    public static int compare(BitField a, BitField b){
        // Work from MSB to LSB
        for (int i = b.size()-1; i >= 0; i--) {
            // sample both numbers
            boolean ai = a.get(i);
            boolean bi = b.get(i);
            // if the numbers differ, at this position
            if (ai != bi) {
                // then ai being set implies bi is not set, and a > b
                // otherwise ai not being set implies bi is set, and a < b
                return ai ? 1 : -1;
            }
        }
        // if we get through all bits, numbers are equal.
        return 0;
    }
    public static int mostSignificantEnabledBit(BitField b) {
        // Work from MSB to LSB
        for (int i = b.size()-1; i >= 0; i--) {
            if (b.get(i)) { return i; }
        }
        // If we get through number without seeing any enabled bits,
        // return an invalid index to signify  nothing was found
        return -1;
    }
    public static BitField shift(BitField a, int distance){
        BitField dup = new BitField(a.size());
        for(int i = 0; i < a.size(); i++){
            if(distance + i < 0 ) { continue; } // skip bits that fall off the right edge
            if(distance + i >= a.size()){ break;} // stop once a bit falls off the left edge
            dup.set(i + distance, a.get(i)); // move bit over by distance
        }
        return dup;
    }
    public static BitField shiftLeft(BitField a) {
        BitField dup = new BitField(a.size());
        for (int i = 0; i < a.size() - 1; i++) {
            dup.set(i, a.get(i + 1));
        }
        dup.set(a.size() - 1, false);
        return dup;
    }
    public static BitField shiftRight(BitField a) {
        BitField dup = new BitField(a.size());
        dup.set(0, false);
        for (int i = 1; i < a.size(); i++) {
            dup.set(i, a.get(i - 1));
        }
        return dup;
    }
    public static BitField reverse(BitField a) {
        BitField b = new BitField(a.size());
        int j = a.size();
        for (int i = 0; i < a.size(); i++) {
            b.set(j - 1, a.get(i));
            j--;
        }
        return b;
    }
    public static BitField negate(BitField a) {
        BitField c = complement(a);
        BitField one = new BitField(a.size());
        one.set(0, true);
        return add(c, one);
    }
    public static BitField complement(BitField a) {
        BitField dup = new BitField(a.size());
        for (int i = 0; i < a.size(); i++) {
            dup.set(i, !a.get(i));
        }
        return dup;
    }

}
