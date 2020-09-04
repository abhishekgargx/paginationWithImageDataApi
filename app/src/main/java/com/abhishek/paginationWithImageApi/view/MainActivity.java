package com.abhishek.paginationWithImageApi.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.paginationWithImageApi.R;
import com.abhishek.paginationWithImageApi.databinding.ActivityMainBinding;
import com.abhishek.paginationWithImageApi.model.Flicker;
import com.abhishek.paginationWithImageApi.model.Photo;
import com.abhishek.paginationWithImageApi.service.ApiHandler;
import com.abhishek.paginationWithImageApi.service.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Photo> dataList = new ArrayList<>();
    private SearchItemRecycleViewAdapter adapter;
    private long pageNumber = -1;
    private int limit = 30;
    private long availablePages = 0;
    private Handler searchQueryHandler;
    private Runnable searchQueryRunnable;
    private String searchQueryForApi = "";
    private ActivityMainBinding binding;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setProductsRecycleView();
        searchQueryHandler = new Handler();
        searchQueryRunnable = () -> {
            resetPagination();
            if (dataList.size() > 0) {
                scrollToTop();
            }
            performSearchOverQuery(searchQueryForApi);
            searchQueryHandler.removeCallbacks(searchQueryRunnable);
        };
        setSearchListener();
    }

    private void performSearchOverQuery(String query) {
        if (query != null && !query.isEmpty()) {
            doSearchApiCall(query);
        }
    }

    public void scrollToTop() {
        binding.recyclerView.scrollToPosition(0);
    }

    public void resetPagination() {
        availablePages = 0;
        pageNumber = -1;
    }

    private void setSearchListener() {
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQueryHandler.removeCallbacks(searchQueryRunnable);
                performSearchOverQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQueryForApi = newText;
                searchQueryHandler.removeCallbacks(searchQueryRunnable);
                searchQueryHandler.postDelayed(searchQueryRunnable, 1000);
                return false;
            }
        });
    }

    private void setProductsRecycleView() {
        // adapter
        adapter = new SearchItemRecycleViewAdapter(this, dataList);
        // recycle view type -> grid layout
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerView.setHasFixedSize(true);
        // set adapter
        binding.recyclerView.setAdapter(adapter);
        // item spacing
        binding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(3,
                getResources().getDimensionPixelSize(R.dimen.photo_grid_item_spacing), true));
        // scroll listener
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //Handle method to be invoked when the RecyclerView has been scrolled.
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    if (pageNumber < availablePages) {
                        performSearchOverQuery(searchQueryForApi);
                    }
                }
            }
        });
    }

    void doSearchApiCall(String query) {
        pageNumber++;

        new ApiHandler<>(this,
                RetrofitInstance.getNewUserApiService(this).getPhotos(String.valueOf(pageNumber), String.valueOf(limit), query),
                new ApiHandler.Handle<Flicker>() {
                    @Override
                    public void onSuccess(Flicker data) {
                        if (data.getPhotos() != null) {
                            if (data.getPhotos().getPhoto() != null && data.getPhotos().getPhoto().size() > 0) {
                                if (pageNumber == 0) {
                                    availablePages = data.getPhotos().getPages();
                                    dataList.clear();
                                }
                                hideKeyboard(MainActivity.this);
                                dataList.addAll(data.getPhotos().getPhoto());
                                adapter.notifyDataSetChanged();
                            } else {
                                pageNumber--;
                            }
                        } else {
                            pageNumber--;
                        }
                    }

                    @Override
                    public void onFail(ApiHandler.Error object) {

                    }
                }).build();
    }
}