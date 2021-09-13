package com.viktor.framework.parsers;

import com.viktor.framework.Request;
import com.viktor.framework.guava.Bytes;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MultiBodyParser {

    public void MultiParsing(Request request, Map<String, Part> originalMap) {
        final String REGEX = "\"(.+?)\"";
        final byte[] CRLF = new byte[]{'\r', '\n'};
        final byte[] CRLFCRLF = new byte[]{'\r', '\n', '\r', '\n'};

        String contentType = request.getHeaders().getOrDefault("Content-Type", "");
        if (!contentType.startsWith("multipart/form-data"))
            return;

        Map<String, String> headers = request.getHeaders();
        String boundaryNotFinal = headers.getOrDefault("Content-Type", "");
        String boundaryFinal = "--" + boundaryNotFinal.substring(boundaryNotFinal.indexOf("=") + 1);

        String fullBodyString = new String(request.getBody());
        List<String> partsOfBodyByBoundary = Arrays.stream(fullBodyString.split(boundaryFinal))
                .map(String::trim)
                .filter(s -> s.startsWith("Content-Disposition"))
                .collect(Collectors.toList());
        for (String stringWithFile : partsOfBodyByBoundary) {
            if (stringWithFile.contains("Content-Transfer-Encoding: binary")) {
                int index = Bytes.indexOf(stringWithFile.getBytes(StandardCharsets.UTF_8), CRLFCRLF, 0, stringWithFile.length()) + CRLF.length;
                String tempParam = new String(stringWithFile.getBytes(StandardCharsets.UTF_8), 0, index).trim();
                String file = new String(stringWithFile.getBytes(StandardCharsets.UTF_8), index, stringWithFile.length()).trim();
                String finalParam = "";
                Matcher matcher = Pattern.compile(REGEX).matcher(tempParam);
                while (matcher.find()) {
                    finalParam = matcher.group();
                }
                originalMap.put(finalParam, new Part("",file));

            } else {
                int lastIndex = Bytes.indexOf(stringWithFile.getBytes(StandardCharsets.UTF_8),CRLFCRLF,0,stringWithFile.length()) + CRLFCRLF.length;
                String tempParam = new String(stringWithFile.getBytes(StandardCharsets.UTF_8),0,lastIndex);
                String value = stringWithFile.replace(tempParam,"").trim();
                String finalParam = "";
                Matcher matcher = Pattern.compile(REGEX).matcher(tempParam);
                while (matcher.find()) {
                    finalParam = matcher.group();
                }
                originalMap.put(finalParam, new Part(value,""));

            }
        }
    }
}

