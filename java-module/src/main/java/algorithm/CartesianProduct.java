package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CartesianProduct {

    public static void main(String[] args) {

        List<List<String>> list = new ArrayList<>();
        list.add(Arrays.asList("A", "B", "C"));
        list.add(Arrays.asList("1", "2", "3"));
        list.add(Arrays.asList("!", "@"));

        System.out.println("###### Start to Print Imperative CartesianProduct #####");
        printCartesianProduct(list);
        System.out.println();

        System.out.println("###### Start to Print Functional CartesianProduct #####");

        List<List<String>> res = makeCartesianProduct(list);
        System.out.println(res.size());
        System.out.println(res);

        List<String> words = Arrays.asList("apple", "banana", "carrrot");
        List<String> upperWords = words.stream().map(String::toUpperCase).collect(Collectors.toList());
        upperWords.forEach(System.out::println);
    }

    static void printCartesianProduct(List<List<String>> list) {
        printCartesianProduct(list, 0, new ArrayList<>());
    }

    static List<List<String>> makeCartesianProduct(List<List<String>> list) {
        return makeCartesianProduct(list, 0);
    }

    private static List<List<String>> makeCartesianProduct(List<List<String>> list, int i) {
        if (i == list.size()) {
            List<List<String>> temp = new ArrayList<>();
            temp.add(new ArrayList<>());
            return temp;
        } else {
            List<List<String>> newResults = new ArrayList<>();
            for (String curr : list.get(i)) {
                for (List<String> subResult : makeCartesianProduct(list, i + 1)) {
                    List<String> temp = new ArrayList<>(subResult);
                    temp.add(0, curr);
                    newResults.add(temp);
                }
            }
            return newResults;
        }
    }

    private static void printCartesianProduct(List<List<String>> list, int i, List<String> result) {
        if (i == list.size()) {
            System.out.println(result);
        } else {
            list.get(i).forEach(x -> {
                result.add(x);
                printCartesianProduct(list, i + 1, result);
                result.remove(x);
            });
        }
    }
}
