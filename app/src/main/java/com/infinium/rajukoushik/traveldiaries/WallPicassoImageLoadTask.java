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

    static Context context;
    public WallPicassoImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {


        Target target = new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {


            }

            @Override
            public void onBitmapLoaded(Bitmap arg0, Picasso.LoadedFrom arg1) {

                imageView.setImageBitmap(arg0);

            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                // TODO Auto-generated method stub

            }
        };

        Picasso.with(context.getApplicationContext())
                .load(url)
                .into(target);
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        imageView.setImageBitmap(result);
    }

}
