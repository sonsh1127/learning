package algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class QuickSort {

    public static void sort(List<Integer> list) {
        sort(list, 0, list.size()-1);
    }

    /**
     * @param list list
     * @param start inclusive
     * @param end inclusive
     */
    private static void sort(List<Integer> list, int start, int end) {
        if (start < end) {
            int pivotIndex = partition(list, start, end);
            sort(list, start, pivotIndex-1);
            sort(list, pivotIndex + 1, end);
        }
    }

    private static int partition(List<Integer> list, int start, int end) {

        int i = start - 1;
        int right = end;

        int pivot = list.get(end);
        for (int j = start; j < end; j++){
            if (list.get(j) < pivot){
              i++;
              Collections.swap(list, j, i);
            }
        }
        Collections.swap(list, i+1, end);
        return right;
    }

    @Test
    public void test() {

        List<Integer> numbers = Arrays.asList(4 , 6 , 6 , 6 , 7);

        List<Integer> expected = Arrays.asList(4 , 6 , 6 , 6 , 7);
        expected.sort(Integer::compareTo);
        sort(numbers);
        Assert.assertEquals(expected, numbers);
    }

}
