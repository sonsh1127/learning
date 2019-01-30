package algorithm;

import java.util.Collections;
import java.util.List;

public class QuickSort {

    public static void sort(List<Integer> list) {
        sort(list, 0, list.size() - 1);
    }


    private static void sort(List<Integer> list, int start, int end) {
        if (start < end) {
            int pivotIndex = partition(list, start, end);
            sort(list, start, pivotIndex - 1);
            sort(list, pivotIndex + 1, end);
        }
    }

    private static int partition(List<Integer> list, int start, int end) {
        int i = start - 1;
        int right = end;
        int pivot = list.get(end);
        for (int j = start; j < end; j++) {
            if (list.get(j) < pivot) {
                i++;
                Collections.swap(list, j, i);
            }
        }
        Collections.swap(list, i + 1, end);
        return right;
    }

}
