package com.synchrony.synchrony.model;

public class DataRequest {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataRequest{" +
                "data='" + data + '\'' +
                '}';
    }
}

