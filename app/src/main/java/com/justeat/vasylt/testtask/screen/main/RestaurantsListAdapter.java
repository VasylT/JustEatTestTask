package com.justeat.vasylt.testtask.screen.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.justeat.vasylt.testtask.R;
import com.justeat.vasylt.testtask.model.CuisineType;
import com.justeat.vasylt.testtask.model.ImageUrl;
import com.justeat.vasylt.testtask.model.Restaurant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasyl Tkachov on 26.06.2018.
 */
class RestaurantsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Picasso         picasso;
    private final AdapterCallback adapterCallback;

    private List<Restaurant> objects = new ArrayList<>();

    RestaurantsListAdapter(Context context, AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
        picasso = Picasso.with(context);
    }

    public interface AdapterCallback {
        void onListItemClick(View view);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_restaurant, parent, false);
        view.setOnClickListener(adapterCallback::onListItemClick);

        return new RestaurantViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RestaurantViewHolder viewHolder = (RestaurantViewHolder) holder;
        Restaurant item = objects.get(position);

        viewHolder.tvTitle.setText(item.getName());
        viewHolder.tvScore.setText(String.valueOf(item.getRating()));

        List<ImageUrl> imageUrlList = item.getImageUrls();
        if (imageUrlList != null && imageUrlList.size() != 0) {
            String url = imageUrlList.get(0).getUrl();
            picasso.load(url)
                    .fit()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(viewHolder.tvImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Image successfully loaded from cache
                        }

                        @Override
                        public void onError() {
                            // Load image from web
                            picasso.load(url)
                                    .fit()
                                    .error(R.drawable.ic_restaurant)
                                    .into(viewHolder.tvImage);
                        }
                    });
        }

        for (CuisineType cuisineType : item.getCuisineTypes()) {
            if (!TextUtils.isEmpty(viewHolder.vCuisine.getText())) {
                viewHolder.vCuisine.append(", ");
            }
            viewHolder.vCuisine.append(cuisineType.getName());
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setItems(List<Restaurant> items) {
        if (objects != null) {
            objects = items;
            notifyDataSetChanged();
        }
    }

    public void clear() {
        objects.clear();
        notifyDataSetChanged();
    }

    public Restaurant getItem(int position) {
        return objects.get(position);
    }

    private class RestaurantViewHolder extends RecyclerView.ViewHolder {

        final ImageView tvImage;
        final TextView  tvTitle;
        final TextView  tvScore;
        final TextView  vCuisine;

        RestaurantViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.restaurant_title);
            tvImage = itemView.findViewById(R.id.restaurant_image);
            tvScore = itemView.findViewById(R.id.restaurant_score);
            vCuisine = itemView.findViewById(R.id.cuisine);
        }
    }
}