package com.misfits.mental.findflix;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.misfits.mental.findflix.omdbApiRetrofitService.SavedLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.misfits.mental.findflix.Utils.CommonUtils;
import com.misfits.mental.findflix.omdbApiRetrofitService.searchService;

public class WatchlistActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<searchService.ResultWithDetail>{


    private RecyclerView mMovieListRecyclerView;
    private MovieRecyclerViewAdapter mMovieAdapter;
    private ArrayList<String> mMovieTitle;
    private ProgressBar mProgressBar;

    private static final int LOADER_ID = 1;

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        InMobiInterstitial interstitial = new InMobiInterstitial(getApplicationContext(), 1469330729738L, new InMobiInterstitial.InterstitialAdListener() {
            @Override
            public void onAdRewardActionCompleted(InMobiInterstitial ad, Map rewards) {}
            @Override
            public void onAdDisplayed(InMobiInterstitial ad) {}
            @Override
            public void onAdDismissed(InMobiInterstitial ad) {}
            @Override
            public void onAdInteraction(InMobiInterstitial ad, Map params) {}
            @Override
            public void onAdLoadSucceeded(final InMobiInterstitial ad) {
                ad.show();
            }
            @Override
            public void onAdLoadFailed(InMobiInterstitial ad, InMobiAdRequestStatus requestStatus) {}
            @Override
            public void onUserLeftApplication(InMobiInterstitial ad){}
        });
        interstitial.load();

        SharedPreferences prefs = getSharedPreferences("PREF_2", MODE_PRIVATE);
        Set movieSaves = prefs.getStringSet("movie_pref", null);
        ArrayList<String> list = new ArrayList<>(movieSaves);
        mMovieTitle = list;
        mMovieListRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mMovieAdapter = new MovieRecyclerViewAdapter(null);
        mMovieListRecyclerView.setAdapter(mMovieAdapter);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_column_count), StaggeredGridLayoutManager.VERTICAL);
        mMovieListRecyclerView.setItemAnimator(null);
        // Attach the layout manager to the recycler view
        mMovieListRecyclerView.setLayoutManager(gridLayoutManager);
        getSupportLoaderManager().enableDebugLogging(true);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_spinner);
        startSearch();


    }

    @Override
    public void onResume()
    {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("PREF_2", MODE_PRIVATE);
        Set movieSaves = prefs.getStringSet("movie_pref", null);
        ArrayList<String> list = new ArrayList<>(movieSaves);
        mMovieTitle = list;
        startSearch();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("mMovieTitle", mMovieTitle);
        outState.putInt("progress_visibility",mProgressBar.getVisibility());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        int progress_visibility= savedInstanceState.getInt("progress_visibility");
        // if the progressBar was visible before orientation-change
        if(progress_visibility == View.VISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        // init the loader, so that the onLoadFinished is called
        mMovieTitle = savedInstanceState.getStringArrayList("mMovieTitle");
        if (mMovieTitle != null) {
            Bundle args = new Bundle();
            args.putStringArrayList("movieTitle", mMovieTitle);
            getSupportLoaderManager().initLoader(LOADER_ID, args, this);
        }
    }

    @Override
    public Loader<searchService.ResultWithDetail> onCreateLoader(int id, Bundle args) {
        return new SavedLoader(WatchlistActivity.this, args.getStringArrayList("movieTitle"));
    }

    @Override
    public void onLoadFinished(Loader<searchService.ResultWithDetail> loader, searchService.ResultWithDetail resultWithDetail) {
        mProgressBar.setVisibility(View.GONE);
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
        if(resultWithDetail.getResponse().equals("True")) {
            mMovieAdapter.swapData(resultWithDetail.getMovieDetailList());
        } else {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.snackbar_title_not_found),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(WatchlistActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onLoaderReset(Loader<searchService.ResultWithDetail> loader) {
        mMovieAdapter.swapData(null);
    }

    public class MovieRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

        private List<searchService.Detail> mValues;

        public MovieRecyclerViewAdapter(List<searchService.Detail> items) {
            mValues = items;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_movie, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final searchService.Detail detail = mValues.get(position);
            final String title = detail.Title;
            final String imdbId = detail.imdbID;
            final String director = detail.Director;
            final String year = detail.Year;
            holder.mTitleView.setText(title);
            holder.mYearView.setText(year);
            if(detail.Type.equals("series"))
            {
                holder.mDirectorView.setText("TV Series");
            }
            else if(detail.Type.equals("game"))
            {
                holder.mDirectorView.setText("Video Game");
            }
            else
            {
                holder.mDirectorView.setText(director);
            }
            final String imageUrl;
            if (! detail.Poster.equals("N/A")) {
                imageUrl = detail.Poster;
            } else {
                // default image if there is no poster available
                imageUrl = getResources().getString(R.string.default_poster);
            }
            holder.mThumbImageView.layout(0, 0, 0, 0); // invalidate the width so that glide wont use that dimension
            Picasso.with(WatchlistActivity.this).load(imageUrl).into(holder.mThumbImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WatchlistActivity.this, DetailActivity.class);
                    // Pass data object in the bundle and populate details activity.
                    intent.putExtra(DetailActivity.MOVIE_DETAIL, detail);
                    intent.putExtra(DetailActivity.IMAGE_URL, imageUrl);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(WatchlistActivity.this,
                                    holder.mThumbImageView, "poster");
                    startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mValues == null) {
                return 0;
            }
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mYearView;
            public final TextView mDirectorView;
            public final ImageView mThumbImageView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.movie_title);
                mYearView = (TextView) view.findViewById(R.id.movie_year);
                mThumbImageView = (ImageView) view.findViewById(R.id.thumbnail);
                mDirectorView = (TextView) view.findViewById(R.id.movie_director);
            }

        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
        }

        public void swapData(List<searchService.Detail> items) {
            if(items != null) {
                mValues = items;
                notifyDataSetChanged();

            } else {
                mValues = null;
            }
        }
    }

    private void startSearch() {
        if(CommonUtils.isNetworkAvailable(getApplicationContext()))
        {
                Bundle args = new Bundle();
                args.putStringArrayList("movieTitle", mMovieTitle);
                getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                mProgressBar.setVisibility(View.VISIBLE);
                mMovieListRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.network_not_available),
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(WatchlistActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

}

