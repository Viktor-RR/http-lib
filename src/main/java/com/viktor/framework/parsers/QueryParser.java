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

public class QueryParser {
    public Map<String,List<String>> queryParser (Request request) {
        return Pattern.compile("&")
                 .splitAsStream(request.getPath().substring(request.getPath().indexOf("?") + 1))
                 .map(s -> Arrays.copyOf(s.split("=", 2), 2))
                 .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));

    }
    private String decode(String encoded) {
        return Optional.ofNullable(encoded)
                .map(e -> URLDecoder.decode(e, StandardCharsets.UTF_8))
                .orElse(null);
    }

}
