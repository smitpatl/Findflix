package com.misfits.mental.findflix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import com.inmobi.ads.*;
import com.inmobi.sdk.*;
import com.misfits.mental.findflix.Utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ImageView imageViewTriangle;
    private EditText editSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InMobiSdk.init(getApplicationContext(), "b5b16403bd604318aec56169bd083ab9");
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);
        editSearch = (EditText)findViewById(R.id.editText);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    startSearch();
                    handled = true;
                }
                return handled;

            }
        });
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        imageViewTriangle = (ImageView)findViewById(R.id.triangle_image);
        Picasso.with(getApplicationContext()).load(R.drawable.backgrnd).resize(width, height).centerCrop().into(imageViewTriangle);
    }
    private void startSearch() {
        if(CommonUtils.isNetworkAvailable(getApplicationContext())) {
            CommonUtils.hideSoftKeyboard(MainActivity.this);
            String movieTitle = editSearch.getText().toString().trim();
            if (!movieTitle.isEmpty()) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("searchTerm",movieTitle);
                startActivity(intent);
            }
            else
                Snackbar.make(imageViewTriangle,
                        getResources().getString(R.string.snackbar_title_empty),
                        Snackbar.LENGTH_LONG).show();
        } else {
            CommonUtils.hideSoftKeyboard(MainActivity.this);
            Snackbar.make(imageViewTriangle,
                    getResources().getString(R.string.network_not_available),
                    Snackbar.LENGTH_LONG).show();
        }
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.homemenu, popup.getMenu());
        popup.show();
    }
    public void doWatchlist(MenuItem item)
    {
        SharedPreferences prefs = getSharedPreferences("PREF_2", MODE_PRIVATE);
        Set<String> saves = new HashSet<>();
        saves = prefs.getStringSet("movie_pref", null);
        if(saves==null)
        {
            Toast.makeText(getApplicationContext(),"Your watchlist is empty",Toast.LENGTH_LONG).show();
        }
        else if (saves.size()==0)
        {
            Toast.makeText(getApplicationContext(),"Your watchlist is empty",Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, WatchlistActivity.class);
            startActivity(intent);
        }
    }
    public void doLike(MenuItem item)
    {
        SharedPreferences prefs2 = getSharedPreferences("PREF_4", MODE_PRIVATE);
        Set<String> likes = new HashSet<>();
        likes = prefs2.getStringSet("movie_like", null);
        if(likes==null)
        {
            Toast.makeText(getApplicationContext(),"You haven't liked any titles yet",Toast.LENGTH_LONG).show();
        }
        else if (likes.size()==0)
        {
            Toast.makeText(getApplicationContext(),"You haven't liked any titles yet",Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, LikesActivity.class);
            startActivity(intent);
        }
    }
    public void doContact(MenuItem item)
    {
        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
        startActivity(intent);
    }
    public void doAbout(MenuItem item)
    {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

}
