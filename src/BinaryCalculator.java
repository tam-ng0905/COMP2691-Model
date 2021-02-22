/**
 * Class with methods to implement the basic arithmetic operations by
 * operating on bit fields.
 *
 * This is the skeleton code form COMP2691 Assignment #2.
 */
public class BinaryCalculator
{
    public static BitField add(BitField a, BitField b)
    {
	if(null == a || null == b || a.size() != b.size()){
	    throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
	}
	int carries = 0;
	int bitPlacementA = 0;
	int bitPlacementB = 0;
	BitField result = new BitField(a.size());

	for(int i = a.size() - 1; i >= 0; i--) {
		if (a.get(i)) {
			bitPlacementA = 1;
		}
		if (b.get(i)) {
			bitPlacementB = 1;
		}

		if((bitPlacementA + bitPlacementB) == 0 && carries == 0){
			result.set(i, false);
		} else if((bitPlacementA + bitPlacementB) == 0 && carries == 1){
			result.set(i, true);
			carries = 0;
		} else if((bitPlacementA + bitPlacementB) == 1 && carries == 0){
			result.set(i, true);
		} else if((bitPlacementA + bitPlacementB) == 1 && carries == 1){
			result.set(i, false);
			carries = 1;
		} else if((bitPlacementA + bitPlacementB) == 2 && carries == 0){
			result.set(i, false);
			carries = 1;
		} else if((bitPlacementA + bitPlacementB) == 2 && carries == 1){
			result.set(i, true);
			carries = 1;
		}

//		if ((bitPlacementA + bitPlacementB + carries) == 1) {
//			result.set(i, true);
//			carries = 0;
//		} else if((bitPlacementA + bitPlacementB) == 2){
//			if(carries == 1){
//				result.set(i, true);
//			} else if(carries == 0){
//				result.set(i, false);
//			}
//			carries = 1;
//		} else if((bitPlacementA + bitPlacementB + carries) == 0) {
//			result.set(i, false);
//		}

		//case for overflow
//		if(carries == 1){
//			result.set(0, true);
//		} else {
//			result.set(1, false);
//		}
	}
	return result;
    }

    public static BitField subtract(BitField a, BitField b)
    {
	if(null == a || null == b || a.size() != b.size()){
	    throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
	}
	return new BitField(a.size());
    }

    public static BitField multiply(BitField a, BitField b)
    {
	if(null == a || null == b || a.size() != b.size()){
	    throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
	}
	return new BitField(a.size());
    }

    public static BitField[] divide(BitField a, BitField b)
    {
	if(null == a || null == b || a.size() != b.size()){
	    throw new IllegalArgumentException("BinaryCalculator.add(a,b): a and b cannot be null and must be the same length.");
	}

	// Return both the quotient and the remainder
	BitField[] out = new BitField [ 2 ];
	out[0] = new BitField(a.size()); // quotient
	out[1] = new BitField(a.size()); // remainder
	return out;
    }
}
