package com.viktor.framework.parsers;

import com.viktor.framework.Request;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BodyParserTest {

    @Test
    void DoBodyParserWorksWithFormUrlencoded() {
        Map<String, List<String>> form = new HashMap<>();
        Map<String,String> headers = new HashMap<>();

        headers.put("Content-Type", "application/x-www-form-urlencoded");
        byte[] body = "q=JavaAdvanced&v=16".getBytes(StandardCharsets.UTF_8);

        BodyParser bodyParser = new BodyParser();
        Request request = Request.builder().method("POST").path("http://localhost:9999/coursar/search?q=JavaPRO").headers(headers).body(body).build();
        bodyParser.bodyParsing(request, form);

        Map<String, List<String>> form2 = new HashMap<>();
        form2.put("q",List.of("JavaAdvanced"));
        form2.put("v",List.of("16"));
        boolean equals = form.equals(form2);
        assertTrue(equals);


    }

}