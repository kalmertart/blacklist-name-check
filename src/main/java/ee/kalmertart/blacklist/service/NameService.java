package ee.kalmertart.blacklist.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NameService {

    private static final String EMPTY = "";
    private static final String SPACE = " ";

    public List<String> process(String inputName, List<String> noiseWords) {
        Set<String> names = new HashSet<>();
        names.add(inputName.toLowerCase());

        if (!noiseWords.isEmpty()) {
            String cleanName = removeNoiseWords(inputName.toLowerCase(), noiseWords);
            if (cleanName.length() > 0) {
                names.add(cleanName);
            }
        }

        return buildNamePermutations(names);
    }

    private String removeNoiseWords(String name, List<String> noiseWords) {
        for (String noiseWord : noiseWords) {
            if (name.contains(noiseWord.toLowerCase())) {
                name = name.replace(noiseWord, EMPTY);
            }
        }
        return name;
    }

    private List<String> buildNamePermutations(Set<String> names) {
        List<String> possibleNames = new ArrayList<>();
        for (String name : names) {
            List<String> nameParts = Arrays.asList(name.split(SPACE));
            if (nameParts.size() > 1) {
                possibleNames.addAll(getPermutations(nameParts));
            } else {
                possibleNames.addAll(nameParts);
            }
        }
        return possibleNames;
    }

    private Set<String> getPermutations(List<String> words) {
        Set<String> permutations = new HashSet<>();
        Collections.sort(words);

        do {
            permutations.add(asName(words));
        } while (nextPermutation(words));

        return permutations;
    }

    private boolean nextPermutation(List<String> words) {
        int startWordIndex = words.size() - 2;
        while (startWordIndex >= 0 && words.get(startWordIndex).compareTo(words.get(startWordIndex + 1)) >= 0) {
            startWordIndex--;
        }

        if (startWordIndex < 0) {
            return false;
        }

        int endWordIndex = words.size() - 1;
        while (words.get(endWordIndex).compareTo(words.get(startWordIndex)) <= 0) {
            endWordIndex--;
        }

        Collections.swap(words, startWordIndex, endWordIndex);
        Collections.reverse(words.subList(startWordIndex + 1, words.size()));
        return true;
    }

    private String asName(List<String> values) {
        StringBuilder name = new StringBuilder();
        for (String value : values) {
            name.append(value).append(SPACE);
        }
        return name.toString().trim();
    }
}
