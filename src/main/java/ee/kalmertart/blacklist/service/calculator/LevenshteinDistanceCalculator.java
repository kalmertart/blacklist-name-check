package ee.kalmertart.blacklist.service.calculator;

import org.springframework.stereotype.Component;

@Component
public class LevenshteinDistanceCalculator {

    public int calculate(String name, String blacklistedName) {
        int[][] distance = new int[name.length() + 1][blacklistedName.length() + 1];

        for (int nameIndex = 0; nameIndex <= name.length(); nameIndex ++) {
            for (int blacklistedNameIndex = 0; blacklistedNameIndex <= blacklistedName.length(); blacklistedNameIndex ++) {
                if (nameIndex == 0) {
                    distance[nameIndex][blacklistedNameIndex] = blacklistedNameIndex;
                } else if (blacklistedNameIndex == 0) {
                    distance[nameIndex][blacklistedNameIndex] = nameIndex;
                } else {
                    int cost = name.charAt(nameIndex - 1) == blacklistedName.charAt(blacklistedNameIndex - 1) ? 0 : 1;
                    distance[nameIndex][blacklistedNameIndex] = Math.min(
                            Math.min(distance[nameIndex - 1][blacklistedNameIndex] + 1, distance[nameIndex][blacklistedNameIndex - 1] + 1),
                            distance[nameIndex - 1][blacklistedNameIndex - 1] + cost);
                }
            }
        }

        return distance[name.length()][blacklistedName.length()];
    }
}
