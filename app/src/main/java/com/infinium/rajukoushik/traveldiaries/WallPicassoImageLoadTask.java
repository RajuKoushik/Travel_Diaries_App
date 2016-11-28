package com.infinium.rajukoushik.traveldiaries;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rajukoushik on 28/11/16.
 */

public class WallPicassoImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;
    private  Context context;


    public WallPicassoImageLoadTask(String url, ImageView imageView, Context context) {
        this.url = url;
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

        try
        {
            Picasso.with(context).load(url).fit().into(imageView);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        imageView.setImageBitmap(result);
    }

}
