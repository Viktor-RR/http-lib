package com.viktor.framework.parsers;

public class Part {
    private  String value;
    private  String fileName;
    private  byte[] data;




    public Part(String value, String fileName, byte[] data) {
        this.value = value;
        this.fileName = fileName;
        this.data = data;
    }

    public Part(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    public Part(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
