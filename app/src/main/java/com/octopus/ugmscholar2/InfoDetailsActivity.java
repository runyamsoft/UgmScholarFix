package com.octopus.ugmscholar2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

import layout.BookmarkedFragment;

public class InfoDetailsActivity extends AppCompatActivity {

    TextView title;
    ImageView img;
    WebView wv;
    ProgressBar pb;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_details);
        wv = (WebView)findViewById(R.id.webview);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        final Intent intent;
        intent = getIntent();
        title= (TextView) findViewById(R.id.title);
        //desc = (TextView) findViewById(R.id.desc);
        img = (ImageView) findViewById(R.id.img);
        pb = (ProgressBar)findViewById(R.id.progressBar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.getStringExtra("title");
                AdminSqliteHelper sqlite = new AdminSqliteHelper(InfoDetailsActivity.this, "management", null, 1);
                SQLiteDatabase db = sqlite.getWritableDatabase();
                ContentValues registry = new ContentValues();
                registry.put("title", intent.getStringExtra("title"));
                registry.put("imgURL", intent.getStringExtra("img"));
                registry.put("tgl", intent.getStringExtra("date"));
                registry.put("author", intent.getStringExtra("author"));
                registry.put("directURL", intent.getStringExtra("url"));
                db.insert("bookmark", null, registry);
                db.close();
                Toast.makeText(InfoDetailsActivity.this, "Data berhasil ditambahkan ", Toast.LENGTH_SHORT).show();

            }
        });

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                pb.setVisibility(View.VISIBLE);
                pb.setProgress(0);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pb.setProgress(100);
                pb.setVisibility(View.GONE);
                wv.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('wrap-topnav1')[0].style.display='none'; " +
                        "document.getElementsByClassName('wrap-topnav')[0].style.display='none';" +
                        "})()");
            }
        });
        wv.loadUrl("http://ditmawa.ugm.ac.id/" + intent.getStringExtra("url"));
    }
    public void back(View view){
        super.onBackPressed();
    }


}
