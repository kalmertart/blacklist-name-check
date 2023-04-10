# Blacklist Name Matching

Application is built as Spring web application.

Web application accepts name check at http://localhost:8111/blacklist 
Requires `POST` request and body is simple `String` value.

Internally blacklist service takes 3 arguments: name, blacklist file and noise words file. 
When request is done via controller, then internal files are sent to service to make it easier to test.
All blacklisted names are defined in `src/main/resources/names.txt`
and noise words are defined in `src/main/resources/noise_words.txt`

Input name is cleaned using noise words file and then makes permutations of given input by splitting input value by space. 
Permutation generation logic: https://en.wikipedia.org/wiki/Permutation 

Each name permutation value is compared with blacklist values using Levenshtein distance: 
https://en.wikipedia.org/wiki/Levenshtein_distance

Based on distance value is calculated similarity percentage.
If similarity percentage is higher than `name.similarity.threshold.percentage` value defined in `application.properties`,
then blacklist check returns that given name is blacklisted.
