package com.abhishek.paginationWithImageApi.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.paginationWithImageApi.R;
import com.abhishek.paginationWithImageApi.databinding.PhotoItemBinding;
import com.abhishek.paginationWithImageApi.model.Photo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Locale;

public class SearchItemRecycleViewAdapter extends RecyclerView.Adapter<SearchItemRecycleViewAdapter.ViewHolder> {

    private List<Photo> searchProductItemList;
    private Context context;


    public SearchItemRecycleViewAdapter(Context context, List<Photo> searchProductItemList) {
        this.searchProductItemList = searchProductItemList;
        this.context = context;
    }

    public static boolean isStringValid(String str) {
        if (str != null) {
            str = str.trim();
            return !str.isEmpty()
                    && !str.equalsIgnoreCase("null")
                    && !str.equalsIgnoreCase("na")
                    && !str.equalsIgnoreCase("None");
        }
        return false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhotoItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.photo_item, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo item = searchProductItemList.get(position);
        // load image to image view
        // https://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg
        String imageUrl = String.format(Locale.US,
                "https://farm%s.static.flickr.com/%s/%s_%s.jpg",
                item.getFarm(), item.getServer(), item.getId(), item.getSecret());
        loadImageFromUrl(imageUrl, holder.binding.imageView, context);
    }

    // load image from url into image view
    private void loadImageFromUrl(String url, ImageView imageView, Context context) {

        if (context == null || !isStringValid(url)) {
            return;
        }
        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions().timeout(30000))
                .load(url)
                .error(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_error, context.getTheme()))
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return searchProductItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PhotoItemBinding binding;

        // custom constructor for binding
        public ViewHolder(PhotoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
