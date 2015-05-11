package com.emptypockets.spacemania.network.client.payloads;

import com.emptypockets.spacemania.console.Console;

/**
 * Created by jenfield on 11/05/2015.
 */
public class NotifyClientPayload extends ClientPayload {
    String message;

    @Override
    public void executePayload() {
        Console.println(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void reset() {
        super.reset();
        message = null;
    }
}