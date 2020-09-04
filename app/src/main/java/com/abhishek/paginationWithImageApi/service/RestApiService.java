package com.abhishek.paginationWithImageApi.service;

import com.abhishek.paginationWithImageApi.model.Flicker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApiService {

    String apiKey = "3e7cc266ae2b0e0d78e279ce8e361736";
    String photosApiBaseURL = "services/rest/?format=json&nojsoncallback=1&method=flickr.photos.search&api_key=" + apiKey;

    @GET(photosApiBaseURL)
    Call<Flicker> getPhotos(@Query("page") String pageNumber,
                            @Query("per_page") String limit,
                            @Query("text") String text
    );

}
