package ru.nik.chatpr.exceptions;

public class ErrorInfo {
    private final String url;
    private final ru.ilnik.garage.exceptions.ErrorType type;
    private final String typeMessage;
    private final String[] details;

    public ErrorInfo(CharSequence url, ru.ilnik.garage.exceptions.ErrorType type, String typeMessage, String... details) {
        this.url = url.toString();
        this.type = type;
        this.typeMessage = typeMessage;
        this.details = details;
    }
}
