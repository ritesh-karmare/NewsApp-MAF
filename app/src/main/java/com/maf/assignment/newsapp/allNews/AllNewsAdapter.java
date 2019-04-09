package com.maf.assignment.newsapp.allNews;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.maf.assignment.newsapp.R;
import com.maf.assignment.newsapp.network.responseModel.ArticleData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Ritesh on 04/09/2019.
 */

class AllNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<ArticleData> allNewsList;
    private Context context;

    AllNewsAdapter(Context context) {
        this.context = context;
        allNewsList = new ArrayList<>();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
            return new AllNewsViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof AllNewsViewHolder) {
            populateItemRows((AllNewsViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return allNewsList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return allNewsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    // Populate item with news headline and image
    private void populateItemRows(AllNewsViewHolder holder, final int position) {

        final ArticleData newsArticle = allNewsList.get(position);

        holder.tvNewsHeadline.setText(newsArticle.getTitle());

        Glide.with(context)
                .load(newsArticle.getUrlToImage())
                .apply(new RequestOptions()
                        .error(R.drawable.ic_launcher_background)
                        .placeholder(R.drawable.ic_launcher_background))
                .into(holder.ivBanners);

        holder.cvNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToNewsPage(allNewsList.get(position).getUrl());
            }
        });

    }

    // OnClick of news item, redirect to new's source url
    private void redirectToNewsPage(String newsUrl) {
        try {

            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .addDefaultShareMenuItem()
                    .setToolbarColor(context.getResources()
                            .getColor(R.color.colorPrimary))
                    .setShowTitle(true)
                    .build();

            customTabsIntent.launchUrl(context, Uri.parse(newsUrl));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
    }

    /*
    Helper methods
     */
    void addAll(List<ArticleData> allNewsList) {
        this.allNewsList.addAll(allNewsList);
    }

    void reset() {
        this.allNewsList.clear();
    }

    List<ArticleData> getList() {
        return allNewsList;
    }
}