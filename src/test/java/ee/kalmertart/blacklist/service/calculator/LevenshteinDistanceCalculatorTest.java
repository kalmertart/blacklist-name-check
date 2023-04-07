package ee.kalmertart.blacklist.service.calculator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LevenshteinDistanceCalculatorTest {

    private final LevenshteinDistanceCalculator calculator = new LevenshteinDistanceCalculator();

    @Test
    void calculate_noChangesNeeded_returnsDistanceZero() {
        assertThat(calculator.calculate("Jaanus", "Jaanus")).isZero();
    }

    @Test
    void calculate_onlyOneChangeNeededToBeSame_returnsDistanceOne() {
        assertThat(calculator.calculate("Putin", "Putiin")).isOne();
    }

    @Test
    void calculate_twoCharactersNeedsToBeChangedToBeSame_returnsDistanceTwo() {
        assertThat(calculator.calculate("Adele", "Adolf")).isEqualTo(2);
    }

    @Test
    void calculate_multipleChangesNeededToBeSame_returnsDistanceThatRequiresMoreChangesThanBlacklistedNameLength() {
        assertThat(calculator.calculate("MoneySentToPutinIfWordsNotAddedToNoiseListAndInputNotClean", "Putin"))
                .isGreaterThan("Putin".length());
    }
}
