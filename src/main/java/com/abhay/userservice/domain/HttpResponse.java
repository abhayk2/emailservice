package com.abhay.userservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpResponse {
    protected String timeStamp;
    protected int statusCode;
    protected String message;
    protected HttpStatus status;
    protected String developerMessage;
    protected String path;
    protected String requestMethod;
    protected Map<?,?> data ;
}
