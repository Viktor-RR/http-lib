package com.viktor.framework.parsers;

import com.viktor.framework.Request;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QueryParserTest {

    @Test
    void DoQueryParserWorksWithFormEncoded() {
        Map<String,String> headers = new HashMap<>();

        headers.put("Content-Type", "application/x-www-form-urlencoded");
        byte[] body = "q=JavaAdvanced&v=16".getBytes(StandardCharsets.UTF_8);

        QueryParser queryParser = new QueryParser();
        Request request = Request.builder().method("POST").path("http://localhost:9999/coursar/search?q=JavaPRO").headers(headers).body(body).build();
        Map<String, List<String>> query = queryParser.queryParser(request);

        Map<String, List<String>> query2= new HashMap<>();
        query2.put("q", List.of("JavaPRO"));
        boolean equals = query.equals(query2);
        assertTrue(equals);

    }

}