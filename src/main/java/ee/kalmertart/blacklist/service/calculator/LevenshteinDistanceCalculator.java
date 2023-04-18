package ee.kalmertart.blacklist.service.calculator;

import org.springframework.stereotype.Component;

@Component
public class LevenshteinDistanceCalculator {

    public int calculate(String name, String blacklistedName) {
        int[][] distances = new int[name.length() + 1][blacklistedName.length() + 1];

        for (int nameIndex = 0; nameIndex <= name.length(); nameIndex ++) {
            for (int blacklistedNameIndex = 0; blacklistedNameIndex <= blacklistedName.length(); blacklistedNameIndex ++) {
                if (nameIndex == 0) {
                    distances[nameIndex][blacklistedNameIndex] = blacklistedNameIndex;
                } else if (blacklistedNameIndex == 0) {
                    distances[nameIndex][blacklistedNameIndex] = nameIndex;
                } else {
                    int previousNameCost = distances[nameIndex - 1][blacklistedNameIndex] + 1;
                    int previousBlackListedCost = distances[nameIndex][blacklistedNameIndex - 1] + 1;
                    int previousMinimumCost = Math.min(previousNameCost, previousBlackListedCost);

                    int cost = name.charAt(nameIndex - 1) == blacklistedName.charAt(blacklistedNameIndex - 1) ? 0 : 1;
                    int previousWithCurrentCost = distances[nameIndex - 1][blacklistedNameIndex - 1] + cost;

                    distances[nameIndex][blacklistedNameIndex] = Math.min(previousMinimumCost, previousWithCurrentCost);
                }
            }
        }

        return distances[name.length()][blacklistedName.length()];
    }
}
