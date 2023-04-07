package ee.kalmertart.blacklist.service.calculator;

import org.springframework.stereotype.Component;

@Component
public class SimilarityPercentageCalculator {

    public double calculate(String name, int levenshteinDistance) {
        double length = name.length();
        if (levenshteinDistance > length) {
            return 0;
        }
        return (length - levenshteinDistance) / length * 100d;
    }
}
