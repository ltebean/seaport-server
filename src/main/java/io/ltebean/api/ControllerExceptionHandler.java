package io.ltebean.api;


import io.ltebean.api.dto.Response;
import io.ltebean.api.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by leo on 16/6/7.
 */
@ControllerAdvice(basePackages = "io.ltebean.api")
public class ControllerExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Response handleUnAuthorzied() {
        Response response = new Response();
        response.code = HttpStatus.UNAUTHORIZED.value();
        response.message = "unauthorized";
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleError() {
        return Response.error("server error");
    }
}
