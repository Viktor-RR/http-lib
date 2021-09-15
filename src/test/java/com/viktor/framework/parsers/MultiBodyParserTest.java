package com.viktor.framework.parsers;

import com.viktor.framework.Request;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiBodyParserTest {

    @Test
    void doMultiParseWorksWithMultiData() {
        Map<String,String> headers = new HashMap<>();
        String copyValue = "Java Advanced";
        byte[] copyData = "Some text for you.".getBytes(StandardCharsets.UTF_8);

        headers.put("Content-Type", "multipart/form-data; boundary=WebAppBoundary");
        var body = ("--WebAppBoundary\r\n" +
                "Content-Disposition: form-data; name=\"q\"\r\n" +
                "Content-Type: */*; charset=UTF-8\r\n" +
                "Content-Transfer-Encoding: 8bit\r\n\r\n" +
                "Java Advanced" +
                "\r\n--WebAppBoundary" +
                "\r\n" +
                "Content-Disposition: form-data; name=\"avatar\"; filename=\"samplee.txt\"" +
                "\r\nContent-Type: text/plain" +
                "\r\nContent-Transfer-Encoding: binary" +
                "\r\n\r\n" +
                "Some text for you." +
                "\r\n" +
                "--WebAppBoundary--" +
                "\r\n").getBytes(StandardCharsets.UTF_8);

        Request requestFinal = Request.builder().method("POST").path("http://localhost:9999/coursar/search?q=JavaPRO").headers(headers).body(body).build();
            MultiBodyParser multiBodyParser = new MultiBodyParser();
            Map<String, Part> multiMap = multiBodyParser.multiParser(requestFinal);

        Part part = multiMap.get("q");
        Part part2 = multiMap.get("avatar");
        assertEquals(part.getValue(),copyValue);
        assertArrayEquals(part2.getData(),copyData);
    }
}
