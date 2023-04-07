package ee.kalmertart.blacklist.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NameServiceTest {

    private static final List<String> NOISE_WORDS = List.of("to", ".", "sir");

    private final NameService cleaner = new NameService();

    @Test
    void clean_noWordsToRemove_returnsOnlyGivenValue() {
        assertThat(cleaner.process("Jaanus", NOISE_WORDS))
                .hasSize(1)
                .contains("jaanus");
    }

    @Test
    void clean_noWordsToRemoveAndHasFourWordsInName_returnsNames() {
        assertThat(cleaner.process("Jaanus Uku, Tamm Sild", NOISE_WORDS))
                .hasSize(24);
    }

    @Test
    void clean_noWordsToRemove_returnsAllExpectedPermutationsOfNames() {
        assertThat(cleaner.process("Jaanus Uku Tamm", NOISE_WORDS))
                .hasSize(6)
                .contains("jaanus uku tamm")
                .contains("jaanus tamm uku")
                .contains("uku jaanus tamm")
                .contains("uku tamm jaanus")
                .contains("tamm jaanus uku")
                .contains("tamm uku jaanus");
    }

    @Test
    void clean_noWordsToRemoveAndFirstNameEqualsToLastName_returnsOnlyGivenValue() {
        assertThat(cleaner.process("Jaanus Jaanus", NOISE_WORDS))
                .hasSize(1)
                .contains("jaanus jaanus");
    }

    @Test
    void clean_removesNoiseWords_returnsPermutationOfNames() {
        assertThat(cleaner.process("Sir. Jaanus", NOISE_WORDS))
                .hasSize(3)
                .contains("jaanus")
                .contains("sir. jaanus")
                .contains("jaanus sir.");
    }

    @Test
    void clean_removesNoiseWord_returnsBothNames() {
        assertThat(cleaner.process("Anton", NOISE_WORDS))
                .hasSize(2)
                .contains("anton")
                .contains("ann");
    }

    @Test
    void clean_nameOnlyContainsNoiseWords_returnsUncleanName() {
        assertThat(cleaner.process("Totosir", NOISE_WORDS))
                .hasSize(1)
                .contains("totosir");
    }
}
