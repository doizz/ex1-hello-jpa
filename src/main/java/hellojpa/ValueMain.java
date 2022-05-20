package hellojpa;

public class ValueMain {

    public static void main(String[] args) {
        //값 타입 비교 예제

        int a = 10;
        int b = 10;
        Address address1 = new Address("city", "street", "10000");
        Address address2 = new Address("city", "street", "10000");

        System.out.println("(a ==b) = " + (a == b));
        System.out.println("(address1.equals(addre)) = " + (address1.equals(address2)));

    }
}
