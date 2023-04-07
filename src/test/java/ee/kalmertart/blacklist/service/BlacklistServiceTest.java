package ee.kalmertart.blacklist.service;

import ee.kalmertart.blacklist.request.BlacklistCheckRequest;
import ee.kalmertart.blacklist.service.calculator.LevenshteinDistanceCalculator;
import ee.kalmertart.blacklist.service.calculator.SimilarityPercentageCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlacklistServiceTest {

    private static final File BLACKLIST_FILE = new File("blacklist_test_file");
    private static final File NOISE_WORDS_FILE = new File("noise_words_test_file");

    private static final String NAME_IN_BLACK_LIST = "Nimi";
    private static final List<String> BLACKLIST = List.of(NAME_IN_BLACK_LIST);
    private static final List<String> NOISE_WORDS = List.of("to");

    @Mock
    private FileReaderService fileReaderService;
    @Mock
    private NameService nameService;
    @Mock
    private LevenshteinDistanceCalculator levenshteinDistanceCalculator;
    @Mock
    private SimilarityPercentageCalculator similarityPercentageCalculator;
    @InjectMocks
    private BlacklistService blacklistService;

    @Test
    void check_noNamesInBlackList_returnsFalse() {
        when(fileReaderService.readWords(BLACKLIST_FILE)).thenReturn(Collections.emptyList());

        BlacklistCheckRequest request = new BlacklistCheckRequest("Nimi", BLACKLIST_FILE, NOISE_WORDS_FILE);

        assertThat(blacklistService.check(request).isBlacklisted()).isFalse();
        verify(fileReaderService, never()).readWords(NOISE_WORDS_FILE);
        verify(nameService, never()).process(any(), any());
        verify(levenshteinDistanceCalculator, never()).calculate(any(), any());
    }

    @Test
    void check_nameSimilarityIsHigherThanThreshold_returnsTrue() {
        String name = "Nimi";

        when(fileReaderService.readWords(BLACKLIST_FILE)).thenReturn(BLACKLIST);
        when(fileReaderService.readWords(NOISE_WORDS_FILE)).thenReturn(NOISE_WORDS);
        when(nameService.process(name, NOISE_WORDS)).thenReturn(List.of(name));
        when(levenshteinDistanceCalculator.calculate(name, NAME_IN_BLACK_LIST.toLowerCase())).thenReturn(0);
        when(similarityPercentageCalculator.calculate(name, 0)).thenReturn(100d);

        BlacklistCheckRequest request = new BlacklistCheckRequest(name, BLACKLIST_FILE, NOISE_WORDS_FILE);
        assertThat(blacklistService.check(request).isBlacklisted()).isTrue();
    }

    @Test
    void check_nameSimilarityIs_LowerThanThreshold_returnsFalse() {
        String name = "Nimi pole blacklistis";

        ReflectionTestUtils.setField(blacklistService, "nameSimilarityThresholdPercentage", 80d);
        when(fileReaderService.readWords(BLACKLIST_FILE)).thenReturn(BLACKLIST);
        when(fileReaderService.readWords(NOISE_WORDS_FILE)).thenReturn(NOISE_WORDS);
        when(nameService.process(name, NOISE_WORDS)).thenReturn(List.of(name));
        when(levenshteinDistanceCalculator.calculate(name, NAME_IN_BLACK_LIST.toLowerCase())).thenReturn(17);
        when(similarityPercentageCalculator.calculate(name, 17)).thenReturn(30d);

        BlacklistCheckRequest request = new BlacklistCheckRequest(name, BLACKLIST_FILE, NOISE_WORDS_FILE);
        assertThat(blacklistService.check(request).isBlacklisted()).isFalse();
    }
}
