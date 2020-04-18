package com.biocrypt;

public class Share {
    private final int number;
    private final String data;

    public Share(int number, String data) {
        this.number = number;
        this.data = data;
    }

    public int getNumber() {
        return number;
    }

    public String getData() {
        return data;
    }
}
