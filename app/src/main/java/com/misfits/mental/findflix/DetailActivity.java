package com.misfits.mental.findflix;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.squareup.picasso.Picasso;
import com.misfits.mental.findflix.omdbApiRetrofitService.searchService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_DETAIL = "movie_detail";
    public static final String IMAGE_URL = "image_url";
    private View view;
    private static String ImdbId;
    private static boolean select = false, liked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        select = false;
        liked = false;
        final searchService.Detail detail = getIntent().getParcelableExtra(MOVIE_DETAIL);
        final String imageUrl =  getIntent().getStringExtra(IMAGE_URL);
        final ImageView redCircle = (ImageView) findViewById(R.id.main_backdrop);
        ImdbId = detail.imdbID;
        SharedPreferences prefs = getSharedPreferences("PREF_2", MODE_PRIVATE);
        SharedPreferences prefs2 = getSharedPreferences("PREF_4", MODE_PRIVATE);
        Set<String> saves = new HashSet<>();
        Set<String> likes = new HashSet<>();
        saves = prefs.getStringSet("movie_pref", null);
        likes = prefs2.getStringSet("movie_like",null);
        if(saves!=null)
        {
          if(saves.contains(ImdbId))
          {
              ImageButton imageButton = (ImageButton)findViewById(R.id.bookmark);
              select = true;
              imageButton.setSelected(true);
          }
        }
        if(likes!=null)
        {
            if(likes.contains(ImdbId))
            {
                ImageButton imageButton = (ImageButton)findViewById(R.id.button_like);
                liked = true;
                imageButton.setSelected(true);
            }
        }
        if(imageUrl.equals(getResources().getString(R.string.default_poster)))
        {
            Picasso.with(this).load(R.drawable.backgrnd_small).resize(400,400).centerCrop().into((ImageView) findViewById(R.id.main_backdrop), new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    redCircle.getDrawable().setColorFilter(Color.rgb(240, 0, 16), PorterDuff.Mode.MULTIPLY);

                }

                @Override
                public void onError() {

                }
            });
            ImageView imageView = (ImageView) findViewById(R.id.front_thumbnail);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
            Glide.with(this).load(R.raw.image_placeholder).into(imageViewTarget);
        }
        else
        {
            Picasso.with(this).load(imageUrl).into((ImageView) findViewById(R.id.main_backdrop), new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    redCircle.getDrawable().setColorFilter(Color.rgb(240, 0, 16), PorterDuff.Mode.MULTIPLY);

                }

                @Override
                public void onError() {

                }
            });
            Picasso.with(this).load(imageUrl).into((ImageView) findViewById(R.id.front_thumbnail));
        }



        // set title for the appbar
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(detail.Title);
        if(detail.imdbRating.equals("N/A"))
            ((TextView) findViewById(R.id.imdb_rating)).setText(detail.imdbRating);
        else
            ((TextView) findViewById(R.id.imdb_rating)).setText(detail.imdbRating+"/10");
        boolean isFirst = true;
        ViewGroup parent = (ViewGroup) findViewById(R.id.gen_details);
        if (detail.Type.equals("series"))
        {
            if(!detail.Rated.equals("N/A"))
            {
                view = LayoutInflater.from(this).inflate(R.layout.generic_text, parent, false);
                TextView textView = (TextView) view.findViewById(R.id.default_text);
                textView.setText(detail.Rated);
                parent.addView(view);
                isFirst = false;
            }
            if(!detail.Runtime.equals("N/A"))
            {
                if(!isFirst)
                {
                    view = LayoutInflater.from(this).inflate(R.layout.generic_seperator, parent, false);
                    parent.addView(view);
                }
                view = LayoutInflater.from(this).inflate(R.layout.generic_text, parent, false);
                TextView textView1 = (TextView) view.findViewById(R.id.default_text);
                textView1.setText(detail.Runtime);
                parent.addView(view);
                isFirst = false;
            }
            if(!detail.Year.equals("N/A"))
            {
                if(!isFirst)
                {
                    view = LayoutInflater.from(this).inflate(R.layout.generic_seperator, parent, false);
                    parent.addView(view);
                }
                view = LayoutInflater.from(this).inflate(R.layout.generic_text, parent, false);
                TextView textView2 = (TextView) view.findViewById(R.id.default_text);
                textView2.setText(detail.Year);
                parent.addView(view);
            }
        }
        else if(detail.Type.equals("game"))
        {
            view = LayoutInflater.from(this).inflate(R.layout.generic_text, parent, false);
            TextView textView = (TextView) view.findViewById(R.id.default_text);
            textView.setText("Video Game");
            parent.addView(view);
            if(!detail.Released.equals("N/A"))
            {
                view = LayoutInflater.from(this).inflate(R.layout.generic_seperator, parent, false);
                parent.addView(view);
                view = LayoutInflater.from(this).inflate(R.layout.generic_text,parent, false);
                TextView textView1 = (TextView)view.findViewById(R.id.default_text);
                textView1.setText("Released "+detail.Released);
                parent.addView(view);
            }
        }
        else
        {
            if(!detail.Rated.equals("N/A"))
            {
                view = LayoutInflater.from(this).inflate(R.layout.generic_text, parent, false);
                TextView textView = (TextView) view.findViewById(R.id.default_text);
                textView.setText(detail.Rated);
                parent.addView(view);
                isFirst = false;
            }
            if(!detail.Runtime.equals("N/A"))
            {
                if(!isFirst)
                {
                    view = LayoutInflater.from(this).inflate(R.layout.generic_seperator, parent, false);
                    parent.addView(view);
                }
                view = LayoutInflater.from(this).inflate(R.layout.generic_text, parent, false);
                TextView textView1 = (TextView) view.findViewById(R.id.default_text);
                textView1.setText(detail.Runtime);
                parent.addView(view);
                isFirst = false;
            }
            if(!detail.Released.equals("N/A"))
            {
                if(!isFirst)
                {
                    view = LayoutInflater.from(this).inflate(R.layout.generic_seperator, parent, false);
                    parent.addView(view);
                }
                view = LayoutInflater.from(this).inflate(R.layout.generic_text, parent, false);
                TextView textView2 = (TextView) view.findViewById(R.id.default_text);
                textView2.setText(detail.Released);
                parent.addView(view);
            }
        }
        if(!detail.Genre.equals("N/A"))
        {
            ViewGroup parent1 = (ViewGroup) findViewById(R.id.overall_details);
            view = LayoutInflater.from(this).inflate(R.layout.generic_horizontal, parent1, false);
            parent1.addView(view);
            view = LayoutInflater.from(this).inflate(R.layout.generic_text, parent1, false);
            TextView textView = (TextView) view.findViewById(R.id.default_text);
            textView.setText(detail.Genre);
            parent1.addView(view);
        }
        if(!detail.Plot.equals("N/A"))
        {
            ViewGroup parent1 = (ViewGroup) findViewById(R.id.parentLinear);
            view = LayoutInflater.from(this).inflate(R.layout.generic_text, parent1, false);
            TextView textView = (TextView) view.findViewById(R.id.default_text);
            textView.setText(detail.Plot);
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(0, 16, 0, 0);
            view.setLayoutParams(params);
            parent1.addView(view);
        }
        if(!detail.Actors.equals("N/A"))
        {
            ViewGroup parent1 = (ViewGroup) findViewById(R.id.parentLinear);
            view = LayoutInflater.from(this).inflate(R.layout.generic_subdetail, parent1, false);
            TextView textView = (TextView) view.findViewById(R.id.default_heading);
            if(detail.Actors.contains(","))
                textView.setText("Stars");
            else
                textView.setText("Star");
            TextView textView2 = (TextView) view.findViewById(R.id.default_detail);
            textView2.setText(detail.Actors);
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(0, 24, 0, 0);
            view.setLayoutParams(params);
            parent1.addView(view);
        }
        if(!detail.Director.equals("N/A"))
        {
            ViewGroup parent1 = (ViewGroup) findViewById(R.id.parentLinear);
            view = LayoutInflater.from(this).inflate(R.layout.generic_subdetail, parent1, false);
            TextView textView = (TextView) view.findViewById(R.id.default_heading);
            if(detail.Director.contains(","))
                textView.setText("Directors");
            else
                textView.setText("Director");
            TextView textView2 = (TextView) view.findViewById(R.id.default_detail);
            textView2.setText(detail.Director);
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(0, 16, 0, 0);
            view.setLayoutParams(params);
            parent1.addView(view);
        }
        if(!detail.Writer.equals("N/A"))
        {
            ViewGroup parent1 = (ViewGroup) findViewById(R.id.parentLinear);
            view = LayoutInflater.from(this).inflate(R.layout.generic_subdetail, parent1, false);
            TextView textView = (TextView) view.findViewById(R.id.default_heading);
            if(detail.Writer.contains(","))
                textView.setText("Writers");
            else
                textView.setText("Writer");
            TextView textView2 = (TextView) view.findViewById(R.id.default_detail);
            textView2.setText(detail.Writer);
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(0, 16, 0, 0);
            view.setLayoutParams(params);
            parent1.addView(view);
        }
        if(!detail.Awards.equals("N/A"))
        {
            ViewGroup parent1 = (ViewGroup) findViewById(R.id.parentLinear);
            view = LayoutInflater.from(this).inflate(R.layout.generic_subdetail, parent1, false);
            TextView textView = (TextView) view.findViewById(R.id.default_heading);
            textView.setText(detail.Awards);
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(0, 16, 0, 0);
            view.setLayoutParams(params);
            parent1.addView(view);
        }
        /*((TextView) findViewById(R.id.grid_title)).setText(detail.Title);
        ((TextView) findViewById(R.id.grid_writers)).setText(detail.Writer);
        ((TextView) findViewById(R.id.grid_actors)).setText(detail.Actors);
        ((TextView) findViewById(R.id.grid_director)).setText(detail.Director);
        ((TextView) findViewById(R.id.grid_genre)).setText(detail.Genre);
        ((TextView) findViewById(R.id.grid_released)).setText(detail.Released);
        ((TextView) findViewById(R.id.grid_plot)).setText(detail.Plot);
        ((TextView) findViewById(R.id.grid_runtime)).setText(detail.Runtime);
        */

    }

    public void addWatchlist(View v)
    {
        SharedPreferences prefs = getSharedPreferences("PREF_2", MODE_PRIVATE);
        Set<String> saves = new HashSet<>();
        saves = prefs.getStringSet("movie_pref", null);
        if(select)
        {
            saves.remove(ImdbId);
            SharedPreferences.Editor editor = getSharedPreferences("PREF_2", MODE_PRIVATE).edit();
            editor.clear();
            editor.putStringSet("movie_pref", saves);
            editor.apply();
            ImageButton imageButton = (ImageButton)findViewById(R.id.bookmark);
            select = false;
            imageButton.setSelected(false);
            Toast.makeText(getApplicationContext(), "Removed from watchlist", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (saves == null) {
                Set<String> h = new HashSet<String>();
                h.add(ImdbId);
                SharedPreferences.Editor editor = getSharedPreferences("PREF_2", MODE_PRIVATE).edit();
                editor.clear();
                editor.putStringSet("movie_pref", h);
                editor.apply();
            } else {
                saves.add(ImdbId);
                SharedPreferences.Editor editor = getSharedPreferences("PREF_2", MODE_PRIVATE).edit();
                editor.clear();
                editor.putStringSet("movie_pref", saves);
                editor.apply();
            }
            ImageButton imageButton = (ImageButton)findViewById(R.id.bookmark);
            select = true;
            imageButton.setSelected(true);
            Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_LONG).show();
        }
    }
    public void addLike(View v)
    {
        SharedPreferences prefs2 = getSharedPreferences("PREF_4", MODE_PRIVATE);
        Set<String> likes = new HashSet<>();
        likes = prefs2.getStringSet("movie_like", null);
        if(liked)
        {
            likes.remove(ImdbId);
            SharedPreferences.Editor editor = getSharedPreferences("PREF_4", MODE_PRIVATE).edit();
            editor.clear();
            editor.putStringSet("movie_like", likes);
            editor.apply();
            ImageButton imageButton = (ImageButton)findViewById(R.id.button_like);
            liked = false;
            imageButton.setSelected(false);
        }
        else
        {
            if (likes == null) {
                Set<String> h = new HashSet<String>();
                h.add(ImdbId);
                SharedPreferences.Editor editor = getSharedPreferences("PREF_4", MODE_PRIVATE).edit();
                editor.clear();
                editor.putStringSet("movie_like", h);
                editor.apply();
            } else {
                likes.add(ImdbId);
                SharedPreferences.Editor editor = getSharedPreferences("PREF_4", MODE_PRIVATE).edit();
                editor.clear();
                editor.putStringSet("movie_like", likes);
                editor.apply();
            }
            ImageButton imageButton = (ImageButton)findViewById(R.id.button_like);
            liked = true;
            imageButton.setSelected(true);
        }
    }


}
