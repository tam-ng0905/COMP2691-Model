/**
 * Class with methods to implement the basic arithmetic operations by
 * operating on bit fields.
 *
 * This is the skeleton code form COMP2691 Assignment #2.
 */
public class BinaryCalculator
{
    public static BitField add(BitField A, BitField B) {
	if(null == A || null == B || A.size() != B.size()){
	    throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
	}

	BitField result = new BitField(A.size());
	BitField a = reverse(A);
	BitField b = reverse(B);
	boolean carries = false;
	for(int i = a.size() - 1; i >= 0; i--) {
		if(a.get(i) && b.get(i)){
			if(carries){
				result.set(i, true);
				carries = true;
			} else {
				result.set(i, false);
				carries = true;
			}
		} else if(!a.get(i) && !b.get(i)){
			if(carries){
				result.set(i, true);
				carries = false;

			} else {
				result.set(i, false);
				carries = false;
			}
		} else if(a.get(i) || b.get(i)){
			if(carries){
				result.set(i, false);
				carries = true;
			} else {
				result.set(i, true);
				carries = false;
			}
		}
	}
		return reverse(result);
    }
	public static BitField subtract(BitField A, BitField B) {
	if(null == A || null == B || A.size() != B.size()){
	    throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
	}
		BitField negated = negate(B);

		return add(A, negated);
    }
    public static BitField multiply(BitField A, BitField B) {
		if(null == A || null == B || A.size() != B.size()){
			throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
		}

		BitField result = new BitField(A.size());
		BitField nullBit = new BitField(A.size());

		nullBit.setAllFalse();
		result.setAllFalse();
		while(!A.equals(nullBit)){
			result = add(result, B);
			A= shiftRight(A);
			B = shiftLeft(B);
		}
		return result;
    }
    public static BitField[] divide(BitField a, BitField b) {
		if(null == a || null == b || a.size() != b.size()){
			throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
		}
		// Return both the quotient and the remainder
		BitField[] out = new BitField [ 2 ];
		out[0] = new BitField(a.size()); // quotient
		out[1] = new BitField(a.size()); // remainder
		return out;
    }

	public static BitField shiftLeft(BitField a){
		BitField dup = new BitField(a.size());
		for(int i = 0; i < a.size() - 1; i++){
			dup.set(i, a.get(i+1));
		}
		dup.set(a.size() - 1, false);
		return reverse(dup);
	}
	public static BitField shiftRight(BitField a){
		BitField dup = new BitField(a.size());
		dup.set(0, false);
		for(int i = 1; i < a.size(); i++){
			dup.set(i, a.get(i-1));
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
	public static BitField negate(BitField a){
		BitField c = complement(a);
		BitField one = new BitField(a.size());
		one.set(0, true);
		return add(c, one);
	}
	public static BitField complement(BitField a){
		BitField dup = new BitField(a.size());
		for(int i = 0; i < a.size(); i++){
			dup.set(i, !a.get(i));
		}
		return dup;
	}

}
