package algorithm;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class QuickSortTest {

    @Test
    public void sort() {
        List<Integer> numbers = Arrays.asList(4 , 6 , 6 , 6 , 7);
        List<Integer> expected = Arrays.asList(4 , 6 , 6 , 6 , 7);
        expected.sort(Integer::compareTo);
        QuickSort.sort(numbers);
        Assert.assertEquals(expected, numbers);
    }
}