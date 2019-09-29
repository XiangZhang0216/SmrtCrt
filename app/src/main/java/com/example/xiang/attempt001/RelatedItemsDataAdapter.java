package com.example.xiang.attempt001;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by xiang on 2017-10-08.
 */

public class RelatedItemsDataAdapter extends RecyclerView.Adapter<RelatedItemsDataAdapter.SingleItemRowHolder> {

    private Product[] relatedProducts;
    private Context myContext;
    View myView;

    public RelatedItemsDataAdapter(Context context, Product[] relatedProducts, View myView)
    {
        this.relatedProducts = new Product[relatedProducts.length];
        for (int i  = 0; i< relatedProducts.length;i++)
        {
            this.relatedProducts[i] = relatedProducts[i];
        }
        this.myContext = context;
        this.myView = myView;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_item_single_card,null);
        v.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        SingleItemRowHolder holder = new SingleItemRowHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {
        Product singleProduct = relatedProducts[position];
        holder.relatedItemName.setText(singleProduct.getItemName());
        holder.relatedItemPrice.setText("$"+singleProduct.getPrice().toString());
        new DownloadImageTask(holder.relatedItemImage,holder.relatedItemProgress).execute(singleProduct.getImageLink());
    }

    @Override
    public int getItemCount() {
        return(null!=relatedProducts?relatedProducts.length:0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder
    {
        protected TextView relatedItemName;
        protected TextView relatedItemPrice;
        protected ImageView relatedItemImage;
        protected ProgressBar relatedItemProgress;
        protected int position;
        public SingleItemRowHolder(View view)
        {
            super(view);
            this.relatedItemName = (TextView)view.findViewById(R.id.relatedItemName);
            relatedItemName.setTypeface(HomeScreen.FrontBold);
            this.relatedItemPrice = (TextView)view.findViewById(R.id.relatedItemPrice);
            this.relatedItemImage = (ImageView)view.findViewById(R.id.relatedItemImage);
            this.relatedItemProgress = (ProgressBar)view.findViewById(R.id.relatedItemProgress);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });


        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;
        ProgressBar bmProgress;
        public DownloadImageTask(ImageView bmImage,ProgressBar bmProgress)
        {
            this.bmImage = bmImage;
            this.bmProgress = bmProgress;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIconll = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIconll = BitmapFactory.decodeStream(in);
            }
            catch (Exception e)
            {
                Log.e("Image Error", e.getMessage());
                e.printStackTrace();
            }
            return mIconll;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bmProgress.setVisibility(View.GONE);
        }
    }
}
