package communication.rest;

import lombok.Getter;

import javax.ws.rs.core.Response;

@Getter
public class ResponseConfiguration {
    private Response.Status status;
    private String type;
    private Object entity;


}
