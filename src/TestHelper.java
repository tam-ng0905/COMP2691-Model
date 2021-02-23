public class TestHelper {
    public static void main(String[] args){
        BitField a = new BitField(5);
//        10101
        a.set(0, true);
        a.set(1, false);
        a.set(2, true);
        a.set(3, false);
        a.set(4, true);
        System.out.println(BinaryCalculator.complement(a));
        System.out.println(BinaryCalculator.negate(a));
//        10100
        BitField b = new BitField(5);
        b.set(0, true);
        b.set(1, false);
        b.set(2,true);
        b.set(3, false);
        b.set(4,false);

//        = 10010
//        01001
//        01001
//        01011
        BitField result = BinaryCalculator.add(a, b);

//        System.out.println(result);

    }
}
