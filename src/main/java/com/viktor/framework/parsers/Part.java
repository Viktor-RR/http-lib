package com.viktor.framework.parsers;

import java.util.ArrayList;
import java.util.List;

public class Part {
    private List<String> values = new ArrayList<>();
    private  List<String> files = new ArrayList<>();

    public Part(String values, String files) {
        this.values.add(values);
        this.files.add(files);
    }


    public List<String> getValues() {
        return values;
    }

    public List<String> getFiles() {
        return files;
    }
}