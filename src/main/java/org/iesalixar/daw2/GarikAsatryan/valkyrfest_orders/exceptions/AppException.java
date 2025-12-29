package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.exceptions;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;

    public AppException(String messageKey, Object... args) {
        super(messageKey); // Esto ayuda a ver la llave en los logs de la consola
        this.messageKey = messageKey;
        this.args = args;
    }
}
