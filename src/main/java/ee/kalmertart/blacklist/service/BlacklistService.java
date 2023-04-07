package ee.kalmertart.blacklist.service;

import ee.kalmertart.blacklist.request.BlacklistCheckRequest;
import ee.kalmertart.blacklist.response.BlacklistCheckResponse;
import ee.kalmertart.blacklist.response.BlacklistResponseBuilder;
import ee.kalmertart.blacklist.service.calculator.LevenshteinDistanceCalculator;
import ee.kalmertart.blacklist.service.calculator.SimilarityPercentageCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlacklistService {

    private static final Logger log = LoggerFactory.getLogger(BlacklistService.class);

    @Value("${name.similarity.threshold.percentage}")
    private double nameSimilarityThresholdPercentage;

    private final FileReaderService fileReaderService;
    private final NameService nameService;
    private final SimilarityPercentageCalculator similarityPercentageCalculator;
    private final LevenshteinDistanceCalculator levenshteinDistanceCalculator;

    public BlacklistService(FileReaderService fileReaderService,
                            NameService nameService,
                            LevenshteinDistanceCalculator levenshteinDistanceCalculator,
                            SimilarityPercentageCalculator similarityPercentageCalculator) {
        this.fileReaderService = fileReaderService;
        this.nameService = nameService;
        this.levenshteinDistanceCalculator = levenshteinDistanceCalculator;
        this.similarityPercentageCalculator = similarityPercentageCalculator;
    }

    public BlacklistCheckResponse check(BlacklistCheckRequest request) {
        long start = System.currentTimeMillis();
        List<String> blackListedNames = fileReaderService.readWords(request.getBlacklistFile());
        if (blackListedNames.isEmpty()) {
            log.info("No names blacklisted");
            return BlacklistResponseBuilder.notBlacklisted("No names in blacklist");
        }

        List<String> noiseWords = fileReaderService.readWords(request.getNoiseWordsFile());
        List<String> namePermutations = nameService.process(request.getName(), noiseWords);

        for (String permutation : namePermutations) {
            BlacklistCheckResponse blacklistCheckResponse = checkFromBlacklist(permutation, blackListedNames);
            if (blacklistCheckResponse.isBlacklisted()) {
                long elapsed = System.currentTimeMillis() - start;
                log.info("Name '{}' is blacklisted, took {} ms", permutation, elapsed);
                return blacklistCheckResponse;
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        log.info("Name '{}' is not blacklisted, took {} ms", request.getName(), elapsed);
        return BlacklistResponseBuilder.notBlacklisted();
    }

    private BlacklistCheckResponse checkFromBlacklist(String name, List<String> blackListedNames) {
        for (String blacklistedName : blackListedNames) {
            log.info("Comparing '{}' with blacklisted name '{}'", name, blacklistedName);
            int levenshteinDistance = levenshteinDistanceCalculator.calculate(name, blacklistedName.toLowerCase());
            double similarityPercentage = similarityPercentageCalculator.calculate(name, levenshteinDistance);
            if (similarityPercentage >= nameSimilarityThresholdPercentage) {
                log.info("Name '{}' has similarity to blacklisted name '{}' with percentage of {}%",
                        name, blacklistedName, similarityPercentage);
                return BlacklistResponseBuilder.blacklisted("Name '" + name
                        + "' has similarity to blacklisted name '" + blacklistedName
                        + "' with percentage of " + similarityPercentage + "%");
            }
        }
        return BlacklistResponseBuilder.notBlacklisted();
    }
}
