package com.abhishek.paginationWithImageApi.service;

import android.content.Context;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import com.abhishek.paginationWithImageApi.R;
import com.abhishek.paginationWithImageApi.service.exception.NoInternetConnectionException;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.CancellationException;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

public class ApiHandler<T> {
    private Context context;
    private Handle<T> handle;
    private Call<T> call;

    public ApiHandler(Context context, Call<T> call, Handle<T> handle) {
        this.context = context;
        this.handle = handle;
        this.call = call;
    }

    public ApiHandler build() {
        call.enqueue(retrofitCallback(context, handle));
        return this;
    }

    // success
    private void success(Handle<T> handle, Response<T> response, Context context) {

        if (response.code() == 200) {
            handle.onSuccess(response.body());
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    // failure
    private void failure(Handle handle, Throwable t, Context context) {

        String message = context.getResources().getString(R.string.unable_to_perform_action);
        String log;

        if (t instanceof NoInternetConnectionException) {
            message = t.getMessage();
            log = message;
        } else if (t instanceof JSONException) {
            log = "JSON Exception";
        } else if (t instanceof CancellationException) {
            log = "Cancellation Exception";
        } else if (t instanceof HttpException) {
            log = "Http Exception";
        } else if (t instanceof NetworkOnMainThreadException) {
            log = "Network on Main thread Exception";
        } else if (t instanceof RuntimeException) {
            log = "Runtime Exception";
        } else if (t instanceof IOException) {
            log = "IO Exception";
        } else {
            log = "default case Exception";
        }

        log = t != null && t.getMessage() != null ? log + "  --> message : " + t.getMessage() : log;

        Error error = new Error();
        error.setMessage(message);
        error.setStatus(0);
        error.setSuccess(false);
    }

    // retrofit call back
    private retrofit2.Callback<T> retrofitCallback(Context context, Handle<T> handle) {
        return new retrofit2.Callback<T>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                success(handle, response, context);
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                failure(handle, t, context);
            }
        };
    }

    public interface Handle<T> {
        void onSuccess(T object);

        void onFail(Error object);
    }

    // Error Object
    public static class Error {
        @SerializedName("status")
        private Integer status;
        @SerializedName("success")
        private Boolean success;
        @SerializedName("message")
        private String message;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

}
