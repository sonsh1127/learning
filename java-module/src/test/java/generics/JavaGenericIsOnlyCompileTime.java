package generics;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class JavaGenericIsOnlyCompileTime {
    public static void main(String[] args) throws Exception {
        List<Integer> intList = Arrays.asList(1,2,3,4,5);
        Person p = new Person();
        Method m = Person.class.getMethod("setAddress", List.class);
        m.invoke(p, intList);
        System.out.println(p);
    }
}

class Person {
    List<String> address;
    public List<String> getAddress() {
        return address;
    }
    public void setAddress(List<String> address) {
        this.address = address;
    }
    @Override
    public String toString() {
        return "generics.Person{" +
                "address=" + address +
                '}';
    }
}
