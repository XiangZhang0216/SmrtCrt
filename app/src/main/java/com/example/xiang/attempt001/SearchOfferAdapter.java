package com.example.xiang.attempt001;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

import java.io.InputStream;

/**
 * Created by xiang on 2017-09-30.
 */

public class SearchOfferAdapter extends RecyclerView.Adapter<SearchOfferAdapter.SearchOfferViewHolder>{

        Context context;

        Product[] data;
        LayoutInflater inflater;
        boolean fromScan;
public SearchOfferAdapter(Context context, Product[] data, boolean scanner)
        {
        this.context = context;
        this.data =data;
        inflater = LayoutInflater.from(context);
        fromScan = scanner;

        }

    @Override
    public SearchOfferAdapter.SearchOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.offer_list_item, parent, false);
       SearchOfferViewHolder offerViewHolder = new SearchOfferViewHolder(view);
        return offerViewHolder;
    }



    @Override
    public void onBindViewHolder(SearchOfferViewHolder holder, int position) {
        holder.Position = position;
        holder.offerText.setText(data[position].getItemName());
        holder.offerInfo.setText(data[position].getCategory());
        holder.offerPrice.setText("$"+data[position].getPrice());
        if(position == data.length-1)
        {
            ProductDataSource.scrollLoading = false;
        }

        new DownloadImageTask(holder.offerImage,holder.offerProgress).execute(data[position].getImageLink());
    }

    @Override
public int getItemCount() {
        if(data.length==0)
        {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)SearchConstraintContentFragment.SearchRecyclerView.getLayoutParams();
        marginLayoutParams.setMargins(0,0,0,0);
        SearchConstraintContentFragment.SearchRecyclerView.setLayoutParams(marginLayoutParams);
        SearchConstraintContentFragment.emptySearchLayout.setVisibility(View.VISIBLE);
        SearchConstraintContentFragment.searchProgress.setVisibility(View.INVISIBLE);
        SearchFragment.currentSearchScrollerSize = 0;

        }
        if (data.length!=0)
        {
        SearchFragment.currentSearchScrollerSize = data.length;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)SearchConstraintContentFragment.SearchRecyclerView.getLayoutParams();
        marginLayoutParams.setMargins(0,0,0,80);
        SearchConstraintContentFragment.SearchRecyclerView.setLayoutParams(marginLayoutParams);
        SearchConstraintContentFragment.emptySearchLayout.setVisibility(View.GONE);
        }

        return data.length;
        }

class SearchOfferViewHolder extends RecyclerView.ViewHolder{
    int Position;
    TextView offerText;
    TextView offerInfo;
    TextView offerPrice;
    ImageView offerImage;
    ProgressBar offerProgress;
    public SearchOfferViewHolder(View view)
    {
        super(view);
        offerText = (TextView)view.findViewById(R.id.offer_textview);
        offerInfo = (TextView)view.findViewById(R.id.offer_info);
        offerPrice = (TextView)view.findViewById(R.id.offer_price) ;
        offerImage = (ImageView)view.findViewById(R.id.offer_imageview);
        offerProgress = (ProgressBar)view.findViewById(R.id.offerimageprogress);

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                final ConnectivityManager conMgr = (ConnectivityManager) v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                if(activeNetwork != null && activeNetwork.isConnected()) {
                    Intent gotoDetails = new Intent(v.getContext(), OfferDetail.class);
                    Bundle offerDetailsBundle = new Bundle();
                    offerDetailsBundle.putSerializable("DetailObject", data[Position]);
                    gotoDetails.putExtras(offerDetailsBundle);
                    context.startActivity(gotoDetails);
                }
                else
                {
                    Snackbar.make(v, v.getContext().getString(R.string.no_internet),Snackbar.LENGTH_SHORT).show();
                }

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
        bmProgress.setVisibility(View.GONE);
        bmImage.setImageBitmap(result);
    }
}
    public void add(Product product,int productPosition)
    {
        data= (Product[]) ArrayUtils.addAll(data,product);
        notifyItemInserted(productPosition);
    }

}
