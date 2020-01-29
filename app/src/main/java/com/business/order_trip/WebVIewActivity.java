package com.business.order_trip;

import android.content.Intent;
import android.icu.util.RangeValueIterator;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.models.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class WebVIewActivity extends AppCompatActivity {
    private WebView mWebView;
    String product_name = "", price = "", image_url = "", ok_price, price0, price00, price1, price11;
    String id,weight, category, country, city, destination, end_date, details, tax, total_price, quantity, sender_id, delive_id, trip_id, from, to, timestamp, imagePath, status;
    int social_type;
    String webUrl;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        auth = FirebaseAuth.getInstance();

        mWebView = (WebView) findViewById(R.id.webView1);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        // Enable responsive layout
        mWebView.getSettings().setUseWideViewPort(true);

        // Zoom out if the content width is greater than the width of the veiwport
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebViewClient(new myWebViewClient());
        mWebView.loadUrl("https://www.amazon.com/");
    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            webUrl = url;
//            Toast.makeText(WebVIewActivity.this, webUrl, Toast.LENGTH_LONG).show();

            ImageButton button = findViewById(R.id.scraping);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    getWebsite(url);
                }
            });
            return true;
        }
    }
    private void getWebsite(String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try{
                    Document doc = Jsoup.connect(url).get();
                    Element content = doc.getElementById("productTitle");
                    Element content_price = doc.getElementById("priceblock_ourprice");
                    Element content_url = doc.getElementById("landingImage");
                    if(content_price != null){
                        price = content_price.text();
                    }
                    if(content != null){
                        product_name = content.text();
                    }
                    if(content_url != null){
                        image_url = content_url.attr("data-old-hires");
                    }
                } catch (IOException e){
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(product_name != "" && price != ""){//&& image_url != ""
                            Store();
                            Intent intent = new Intent(WebVIewActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("order_id", id);
                            startActivity(intent);
                        }else{
                            Toast.makeText(WebVIewActivity.this, "Order failed,  Try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).start();

    }
    private void Store() {
        String[] a = price.split("-");
        if (a.length ==1){

            price0 = a[0];
            ok_price = price0.substring(1);
        }else{
            price00 = a[0].substring(1);
            price11 = a[1].substring(2);
            ok_price = String.format("%.2f", (Double.parseDouble(price00) + Double.parseDouble(price11))/2);
        }

        id = UUID.randomUUID().toString();
        weight = "";
        category = "";
        country = "";
        city = "";
        destination = "";
        end_date = "";
        details = "";
        tax = "";
        total_price = "";
        quantity = "";
        delive_id = "";
        trip_id = "";
        from = "";
        to = "";
        sender_id = auth.getUid();
        social_type = 1;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        timestamp = sdf.format(cal.getTime());
        imagePath = "null";
        status = "No delived offer yet";
        OrderModel order = new OrderModel(social_type ,id, weight, category, country, city, destination, end_date, product_name, details, image_url, ok_price, tax, total_price, quantity, sender_id, delive_id, trip_id, from, to, timestamp, imagePath, status);

        FirebaseDatabase.getInstance().getReference("Orders").child(id)
                .setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }else {
                    Toast.makeText(WebVIewActivity.this,  "Order failed, Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
