package com.example.xiang.attempt001;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by xiang on 2017-05-17.
 */

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder>{
    private ArrayList<SingleSquareModel> itemsList;
    private Context myContext;
    View myView;
    String queryCmd;
    CompositeSubscription squareSubscription;
    public SectionListDataAdapter(Context context, ArrayList<SingleSquareModel> itemsList, View myView)
    {
        this.itemsList = itemsList;
        this.myContext = context;
        this.myView = myView;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.square_list_single_card,null);
        v.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        SingleItemRowHolder holder = new SingleItemRowHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {
        SingleSquareModel singleItem = itemsList.get(position);
        holder.tvTitle.setText(singleItem.getSquareName());
        Context imageContext = holder.itemImage.getContext();
        int id = imageContext.getResources().getIdentifier(singleItem.getSquareImageName(),"drawable",imageContext.getPackageName());
        holder.itemImage.setImageResource(id);
        holder.position=position;

    }

    @Override
    public int getItemCount() {
        return(null!=itemsList?itemsList.size():0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView tvTitle;
        protected ImageView itemImage;
        protected int position;
        public SingleItemRowHolder(View view)
        {
            super(view);
            this.tvTitle=(TextView)view.findViewById(R.id.tvTitle);
            tvTitle.setTypeface(HomeScreen.FrontBold);
            this.itemImage=(ImageView)view.findViewById(R.id.itemImage);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    queryCmd = "MU";
                    HomeScreen.returncycle = 10;

                    for(int c = 0; c<22;c++)
                    {

                        switch (position)
                        {
                            case 0:
                                queryCmd = "MU";
                                break;
                            case 1:
                                queryCmd = "MCa";
                                break;
                            case 2:
                                queryCmd = "MCb";
                                break;
                            case 3:
                                queryCmd = "MCc";
                                break;
                            case 4:
                                queryCmd = "MCd";
                                break;
                            case 5:
                                queryCmd = "MCe";
                                break;
                            case 6:
                                queryCmd = "MCf";
                                break;
                            case 7:
                                queryCmd = "MCg";
                                break;
                            case 8:
                                queryCmd = "MCh";
                                break;
                            case 9:
                                queryCmd = "MCi";
                                break;
                            case 10:
                                queryCmd = "MCj";
                                break;
                            case 11:
                                queryCmd = "MCk";
                                break;
                            case 12:
                                queryCmd = "MCl";
                                break;
                            case 13:
                                queryCmd = "MCm";
                                break;
                            case 14:
                                queryCmd = "MCn";
                                break;
                            case 15:
                                queryCmd = "MCo";
                                break;
                            case 16:
                                queryCmd = "MCp";
                                break;
                            case 17:
                                queryCmd = "MCq";
                                break;
                            case 18:
                                queryCmd = "MCr";
                                break;
                            case 19:
                                queryCmd = "MCs";
                                break;
                            case 20:
                                queryCmd = "MCt";
                                break;
                            case 21:
                                queryCmd = "MCu";
                                break;
                            default:
                                queryCmd = "MU";
                                break;
                        }
                    }

                    final ConnectivityManager conMgr = (ConnectivityManager)myView.getContext(). getSystemService(Context.CONNECTIVITY_SERVICE);
                    final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                    if(activeNetwork != null && activeNetwork.isConnected())
                    {
                        switch (queryCmd)
                        {
                            case  "MU":
                                HomeScreen.currentCategory="MRU";
                                break;
                            case "MCa":
                                HomeScreen.currentCategory="MRCa";
                                break;
                            case  "MCb":
                                HomeScreen.currentCategory="MRCb";
                                break;
                            case  "MCc":
                                HomeScreen.currentCategory="MRCc";
                                break;
                            case "MCd":
                                HomeScreen.currentCategory="MRCd";
                                break;
                            case  "MCe":
                                HomeScreen.currentCategory="MRCe";
                                break;
                            case  "MCf":
                                HomeScreen.currentCategory="MRCf";
                                break;
                            case  "MCg":
                                HomeScreen.currentCategory="MRCg";
                                break;
                            case  "MCh":
                                HomeScreen.currentCategory="MRCh";
                                break;
                            case  "MCi":
                                HomeScreen.currentCategory="MRCi";
                                break;
                            case "MCj":
                                HomeScreen.currentCategory="MRCj";
                                break;
                            case  "MCk":
                                HomeScreen.currentCategory="MRCk";
                                break;
                            case  "MCl":
                                HomeScreen.currentCategory="MRCl";
                                break;
                            case  "MCm":
                                HomeScreen.currentCategory="MRCm";
                                break;
                            case  "MCn":
                                HomeScreen.currentCategory="MRCn";
                                break;
                            case "MCo":
                                HomeScreen.currentCategory="MRCo";
                                break;
                            case  "MCp":
                                HomeScreen.currentCategory="MRCp";
                                break;
                            case  "MCq":
                                HomeScreen.currentCategory="MRCq";
                                break;
                            case "MCr":
                                HomeScreen.currentCategory="MRCr";
                                break;
                            case  "MCs":
                                HomeScreen.currentCategory="MRCs";
                                break;
                            case"MCt":
                                HomeScreen.currentCategory="MRCt";
                                break;
                            case"MCu":
                                HomeScreen.currentCategory="MRCu";
                                break;
                            default:
                                HomeScreen.currentCategory="MRU";
                                break;
                        }
                        HomeScreen.HomeScreenProgress = new ProgressDialog(myContext);
                        HomeScreen.HomeScreenProgress.setMessage(myContext.getString(R.string.searching_department)+tvTitle.getText()+"...");
                        HomeScreen.HomeScreenProgress.setCancelable(false);
                        HomeScreen.HomeScreenProgress.show();
                        squareSubscription = new CompositeSubscription();
                        QueryCondition queryCondition = new QueryCondition();
                        queryCondition.setConditionString(queryCmd);
                        queryCondition.setReturncycleint(888);
                        squareSubscription.add(ProductNetworkUtil.getProductRetrofit("Token").assortedProducts(queryCmd)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(this::handleProductListResponse, this::handleProductError));
                    }
                    else
                    {
                        Toast.makeText(myView.getContext(), R.string.internet_is_required_to_search_products,Toast.LENGTH_LONG).show();
                    }

                }
                private void handleProductListResponse(Product[] productList)
                {
                    ProductDataSource.storedArray = new Product[productList.length];
                    for (int c = 0; c< productList.length; c++)
                    {
                        ProductDataSource.storedArray[c] = productList[c];
                    }
                    HomeScreen.currentscrollersize = 0;
                    HomeFragment.LimitedOffersHeader.setText(myContext.getString(R.string.offered_deals)+ tvTitle.getText());
                    RecyclerView changeOffers = (RecyclerView)myView.findViewById(R.id.LimitedOffersRecyclerView);
                    changeOffers.setNestedScrollingEnabled(false);
                    HomeFragment.homeOfferAdapter = new OfferAdapter(myView.getContext(),productList,false);
                    changeOffers.setLayoutManager(new LinearLayoutManager(myView.getContext(),LinearLayoutManager.VERTICAL,false));
                    changeOffers.setAdapter(HomeFragment.homeOfferAdapter);
                    HomeScreen.HomeScreenProgress.dismiss();

                }
                private void handleProductError(Throwable error)
                {
                    HomeScreen.HomeScreenProgress.dismiss();
                    if(error instanceof HttpException)
                    {
                        Gson gson  = new GsonBuilder().create();
                        try
                        {
                            String errorBody = ((HttpException)error).response().errorBody().string();
                            Response response = gson.fromJson(errorBody,Response.class);
                            Snackbar.make(HomeScreen.contenthomescreen, R.string.internal_server_error,Snackbar.LENGTH_SHORT).show();

                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Snackbar.make(HomeScreen.contenthomescreen, R.string.network_error,Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
