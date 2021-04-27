package com.igoreul.mostrepeated;

import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Controller("/3mostrepeat")
public class MostRepeatedController {

    private static final Logger LOG = LoggerFactory.getLogger(MostRepeatedController.class);

    private final List<String> FullList= Arrays.asList("aabcde", "aabchu",
            "uuickw", "uuhisoa",
            "uuisz3", "uuisk7",
            "aabloe", "uuhkol",
            "cccd", "cccc", "cccd9", "cccc",
            "uuhbx", "uuibxt9", "uuilok", "uuizxed");


    @Get(uri="/", produces="text/plain")
    public List<String> index() {
        LOG.info(FullList.toString());
        return returnTopRepeated(FullList, 2);
    }

    public List<String> returnTopRepeated(List<String> receiveStrings, Integer numbersOfElements){

        Map<String, Integer> mapeamentoDeStrings = new TreeMap<>();

        receiveStrings.forEach(
                str -> {
                    var first3Chars = str.substring(0, 3);
                    if(mapeamentoDeStrings.containsKey(first3Chars)){
                        Integer indexOfThisString = mapeamentoDeStrings.get(first3Chars);
                        var newIndex = indexOfThisString + 1;
                        mapeamentoDeStrings.put(first3Chars, newIndex);
                    }
                    else {
                        mapeamentoDeStrings.put(first3Chars, 1);
                    }
                }
        );

        Map<String, Integer> orderedMapByValues = sortByValue(mapeamentoDeStrings,false, numbersOfElements);
        Set<String> strings = orderedMapByValues.keySet();
        List<String> collect = strings.stream().collect(Collectors.toList());
        return collect;
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap, final boolean order,Integer elementsToReturn)
    {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));

        return list.stream()
                .limit(elementsToReturn)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }
}