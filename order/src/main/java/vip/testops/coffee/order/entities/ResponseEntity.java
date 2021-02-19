package vip.testops.coffee.order.entities;

import lombok.Data;

@Data
public class ResponseEntity<T> {
    private int code;
    private String message;
    private T data;
}
