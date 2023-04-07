package ee.kalmertart.blacklist.service.calculator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimilarityPercentageCalculatorTest {

    private final SimilarityPercentageCalculator calculator = new SimilarityPercentageCalculator();

    private static final String TEST_NAME = "12345";

    @Test
    void calculate_levenshteinDistanceIsZero_similarityPercentageIsHundred() {
        assertThat(calculator.calculate(TEST_NAME, 0)).isEqualTo(100.0);
    }

    @Test
    void calculate_levenshteinDistanceIsOne_similarityPercentageIs80() {
        assertThat(calculator.calculate(TEST_NAME, 1)).isEqualTo(80.0);
    }

    @Test
    void calculate_levenshteinDistanceIsBiggerThanInputLength_similarityPercentageIsZero() {
        assertThat(calculator.calculate(TEST_NAME, 8)).isZero();
    }
}
