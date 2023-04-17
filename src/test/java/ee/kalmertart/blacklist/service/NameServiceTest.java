package ee.kalmertart.blacklist.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NameServiceTest {

    private static final List<String> NOISE_WORDS = List.of("to", ".", "sir");

    private final NameService nameService = new NameService();

    @Test
    void process_noWordsToRemove_returnsOnlyGivenValue() {
        assertThat(nameService.process("Jaanus", NOISE_WORDS))
                .hasSize(1)
                .contains("jaanus");
    }

    @Test
    void process_noWordsToRemoveAndHasFourWordsInName_returnsNames() {
        assertThat(nameService.process("Jaanus Uku, Tamm Sild", NOISE_WORDS))
                .hasSize(24);
    }

    @Test
    void process_noWordsToRemove_returnsAllExpectedPermutationsOfNames() {
        assertThat(nameService.process("Jaanus Uku Tamm", NOISE_WORDS))
                .hasSize(6)
                .contains("jaanus uku tamm")
                .contains("jaanus tamm uku")
                .contains("uku jaanus tamm")
                .contains("uku tamm jaanus")
                .contains("tamm jaanus uku")
                .contains("tamm uku jaanus");
    }

    @Test
    void process_noWordsToRemoveAndFirstNameEqualsToLastName_returnsOnlyGivenValue() {
        assertThat(nameService.process("Jaanus Jaanus", NOISE_WORDS))
                .hasSize(1)
                .contains("jaanus jaanus");
    }

    @Test
    void process_removesNoiseWords_returnsPermutationOfNames() {
        assertThat(nameService.process("Sir. Jaanus", NOISE_WORDS))
                .hasSize(3)
                .contains("jaanus")
                .contains("sir. jaanus")
                .contains("jaanus sir.");
    }

    @Test
    void process_removesNoiseWord_returnsBothNames() {
        assertThat(nameService.process("Anton", NOISE_WORDS))
                .hasSize(2)
                .contains("anton")
                .contains("ann");
    }

    @Test
    void process_nameOnlyContainsNoiseWords_returnsUncleanName() {
        assertThat(nameService.process("Totosir", NOISE_WORDS))
                .hasSize(1)
                .contains("totosir");
    }
}
