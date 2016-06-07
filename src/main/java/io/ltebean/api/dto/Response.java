package io.ltebean.api.dto;

/**
 * Created by leo on 16/5/19.
 */
public class Response {

    public int code = 200;

    public String message = "";

    public Object data;

    public Response(Object data) {
        this.data = data;
    }

    public Response() {
    }

    public static Response error(String message) {
        Response response = new Response();
        response.code = 500;
        response.message = message;
        return response;
    }
}
