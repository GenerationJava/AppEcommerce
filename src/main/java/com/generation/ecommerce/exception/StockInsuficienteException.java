package com.generation.ecommerce.exception;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException (String mensaje) {
        super(mensaje);
    }
}
