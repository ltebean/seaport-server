package io.ltebean.api.dto;

/**
 * Created by leo on 16/5/19.
 */
public class Response<T> {

    public int code = 200;

    public String message = "";

    public T data;

    public Response(T data) {
        this.data = data;
    }

    public Response() {
    }
}
