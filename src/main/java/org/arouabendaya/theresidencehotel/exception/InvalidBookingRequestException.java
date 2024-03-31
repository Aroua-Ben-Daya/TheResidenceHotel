package org.arouabendaya.theresidencehotel.exception;

public class InvalidBookingRequestException extends RuntimeException{
    public  InvalidBookingRequestException (String message){
        super(message);
    }
}
