package com.abhishek.paginationWithImageApi.service.exception;

import java.io.IOException;

public class NoInternetConnectionException extends IOException {
    @Override
    public String getMessage() {
        return "No Internet Connection";
    }
}
