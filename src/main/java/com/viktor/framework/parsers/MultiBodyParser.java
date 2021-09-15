package com.viktor.framework.parsers;

import com.viktor.framework.Request;
import com.viktor.framework.guava.Bytes;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MultiBodyParser {

    public Map<String, Part> multiParser(Request request) {
        Map<String, Part> originalMap = new HashMap<>();
        final String REGEX = "\"(.+?)\"";;
        final byte[] CRLF = new byte[]{'\r', '\n'};
        final byte[] CRLFCRLF = new byte[]{'\r', '\n', '\r', '\n'};

        var contentType = request.getHeaders().getOrDefault("Content-Type", "");
        if (!contentType.contains("boundary")) {
            return null;
        }

        var headers = request.getHeaders();
        var boundaryNotFinal = headers.getOrDefault("Content-Type", "");
        var boundaryFinal = "--" + boundaryNotFinal.substring(boundaryNotFinal.indexOf("=") + 1);

        var fullBodyString = new String(request.getBody());
        var partsOfBodyByBoundary = Arrays.stream(fullBodyString.split(boundaryFinal))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        var flag = partsOfBodyByBoundary.remove(partsOfBodyByBoundary.size() - 1);
        var lastIndexWithFlag = partsOfBodyByBoundary.remove(partsOfBodyByBoundary.size() - 1).concat(flag);
        partsOfBodyByBoundary.add(lastIndexWithFlag);

        for (String stringWithFile : partsOfBodyByBoundary) {
            if (stringWithFile.endsWith(flag)) {
                var finalStringWithFile = stringWithFile.substring(0, stringWithFile.length() - flag.length()).trim();
                int index = Bytes.indexOf(finalStringWithFile.getBytes(StandardCharsets.UTF_8), CRLFCRLF, 0, finalStringWithFile.length()) + CRLF.length;
                var tempParams = new String(finalStringWithFile.getBytes(StandardCharsets.UTF_8), 0, index).trim();
                var file = finalStringWithFile.replace(tempParams,"").trim();
                var keyName = "";
                var filename = "";
                var matcher = Pattern.compile(REGEX).matcher(tempParams);
                while (matcher.find(0)) {
                    keyName = matcher.group();
                    break;
                }
                while (matcher.find()) {
                    filename = matcher.group();
                }
                var finalKey = keyName.replaceAll("\"", "");
                var finalFilename= filename.replaceAll("\"", "");
                originalMap.put(finalKey, new Part(finalFilename, file.getBytes(StandardCharsets.UTF_8)));

            } else {
                var lastIndex = Bytes.indexOf(stringWithFile.getBytes(StandardCharsets.UTF_8), CRLFCRLF,0,stringWithFile.length()) + CRLFCRLF.length;
                var tempParams = new String(stringWithFile.getBytes(StandardCharsets.UTF_8),0,lastIndex);
                var value = stringWithFile.replace(tempParams,"").trim();
                String finalParam = "";
                var matcher = Pattern.compile(REGEX).matcher(tempParams);
                while (matcher.find()) {
                    finalParam = matcher.group();
                }
                var lastParam = finalParam.replaceAll("\"", "");
                originalMap.put(lastParam, new Part(value));

            }
        }   return originalMap;
    }

}

