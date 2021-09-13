package com.viktor.framework.parsers;



import com.viktor.framework.Request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

public class BodyParser {
    public void bodyParsing (Request request, Map<String, List<String>> originalBodyMap) {
        String contentType = request.getHeaders().getOrDefault("Content-Type", "");
        if (!contentType.equals("application/x-www-form-urlencoded"))
            return;
            Map<String, List<String>> bodyCopyMap = Pattern.compile("&")
                    .splitAsStream(new String(request.getBody()))
                    .map(s -> Arrays.copyOf(s.split("=", 2), 2))
                    .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));
            originalBodyMap.putAll(bodyCopyMap);
        }

    private String decode(String encoded) {
        return Optional.ofNullable(encoded)
                .map(e -> URLDecoder.decode(e, StandardCharsets.UTF_8))
                .orElse(null);
    }
}
