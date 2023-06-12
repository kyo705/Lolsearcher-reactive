package com.lolsearcher.reactive.match;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.params.provider.Arguments;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Stream;

public class MatchSetup {


    protected static Stream<Arguments> invalidRequest() {

        /* puuId, count, queueId, championId */

        return Stream.of(
                Arguments.of(
                        " " /* invalid */,
                        20,
                        420,
                        43
                ),
                Arguments.of(
                        "1234567890123456789012345678901234567890123456789012345678901234567890123456789" /* 79 글자, invalid */,
                        20,
                        420,
                        43
                ),
                Arguments.of(
                        "puuid1",
                        -5 /* invalid */,
                        420,
                        43
                ),
                Arguments.of(
                        "puuid1",
                        0 /* invalid */,
                        420,
                        43
                ),
                Arguments.of(
                        "puuid1",
                        20,
                        450 /* invalid */,
                        43
                ),
                Arguments.of(
                        "puuid1",
                        20,
                        420,
                        -100 /* invalid */
                )
        );
    }

    protected static String getValidMatchIds() {

        return "[\"KR_6534295851\"]";
    }

    protected static String getValidMatch() throws IOException {

        FileInputStream fis = new FileInputStream("src/test/resources/ValidMatch.txt");

        return IOUtils.toString(fis, "UTF-8");

    }

    protected static String getInvalidMatch() throws IOException {

        FileInputStream fis = new FileInputStream("src/test/resources/InvalidMatch.txt");

        return IOUtils.toString(fis, "UTF-8");

    }
}
