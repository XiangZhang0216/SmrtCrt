package com.example.xiang.attempt001;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by xiang on 2017-05-14.
 */

public class HomeFragment extends Fragment{
    View myView;
    ArrayList<SquareSectionDataModel> allSampleData;
    public static TextView LimitedOffersHeader;
    public static RelativeLayout emptyOffersLayout;
    public static RecyclerView homeOffers;
    public static ListenerScrollView homebigscroll;
    public static RelativeLayout homefragmentrelativelayout;
    public static ProgressBar scrollLoadingProgress;
    public static OfferAdapter homeOfferAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home, container, false);


        allSampleData = new ArrayList<SquareSectionDataModel>();
        createDummyData();
        RecyclerView homeSquareBar = (RecyclerView)myView.findViewById(R.id.homeSquareBar);
        LimitedOffersHeader = (TextView)myView.findViewById(R.id.LimitedOffersHeader);
        LimitedOffersHeader.setTypeface(HomeScreen.FrontBold);
        emptyOffersLayout = (RelativeLayout)myView.findViewById(R.id.emptyofferlayout);
        homebigscroll=(ListenerScrollView)myView.findViewById(R.id.homebigscroll);
        homefragmentrelativelayout = (RelativeLayout)myView.findViewById(R.id.homefragmentrelativelayout);
        scrollLoadingProgress = (ProgressBar)myView.findViewById(R.id.scrollLoadingProgress);
        RecyclerViewDataAdapter rAdapter = new RecyclerViewDataAdapter(getActivity(), allSampleData,myView);
        homeSquareBar.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        homeSquareBar.setAdapter(rAdapter);
        homeOffers = (RecyclerView)myView.findViewById(R.id.LimitedOffersRecyclerView);
        homeOffers.setNestedScrollingEnabled(false);

        Product placeholder = new Product();
        placeholder.setItemName("hello");
        Product[] phA = new Product[1];
        phA[0] = placeholder;
        ProductDataSource.scrollLoading = true;

        int checkLength = ProductDataSource.storedArray.length;
        if(checkLength>=10)
        {
            HomeScreen.returncycle = checkLength;
        }
        else
        {
            HomeScreen.returncycle = ProductDataSource.storedArray.length;
        }

        if(HomeScreen.currentCategory==null || HomeScreen.currentCategory == "") {
            HomeScreen.currentCategory = "MRU";
        }

        if(!HomeScreen.currentCategory.contains("MRU") || !HomeScreen.currentCategory.equals("") || !(HomeScreen.currentCategory==null))
        {
            switch (HomeScreen.currentCategory)
            {
                case "MRCa":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.animals_amp_pet_supplies));
                    break;
                case "MRCb":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.apparel_amp_accessories));
                    break;
                case "MRCc":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.arts_amp_entertainment));
                    break;
                case "MRCd":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.baby_amp_toddler));
                    break;
                case "MRCe":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.business_amp_industrial));
                    break;
                case "MRCf":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.cameras_amp_optics));
                    break;
                case "MRCg":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.electronics));
                    break;
                case "MRCh":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.food_beverages_amp_tobacco));
                    break;
                case "MRCi":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.furniture));
                    break;
                case "MRCj":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.hardware));
                    break;
                case "MRCk":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.health_amp_beauty));
                    break;
                case "MRCl":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.home_amp_garden));
                    break;
                case "MRCm":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.luggage_amp_bags));
                    break;
                case "MRCn":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.mature));
                    break;
                case "MRCo":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.media));
                    break;
                case "MRCp":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.office_supplies));
                    break;
                case "MRCq":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.religious_amp_ceremonial));
                    break;
                case "MRCr":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.software));
                    break;
                case "MRCs":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.sporting_goods));
                    break;
                case "MRCt":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.toys_amp_games));
                    break;
                case "MRCu":
                    LimitedOffersHeader.setText(getString(R.string.searching_department)+getString(R.string.vehicles_amp_parts));
                    break;

            }
        }

        homeOfferAdapter = new OfferAdapter(getActivity(),ProductDataSource.storedArray,false);
        homeOffers.setLayoutManager( new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        homeOffers.setAdapter(homeOfferAdapter);

        //Scrolling
        homeOffers.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (ProductDataSource.scrollLoading)
                {
                    return;
                }
                else if (!recyclerView.canScrollVertically(1))
                {
                    onScrolledToBottom();
                }
            }
            public void onScrolledToBottom()
            {

            }
        });

        return myView;
    }


    public void createDummyData()
    {


        SquareSectionDataModel dm = new SquareSectionDataModel();
        dm.setSquareHeaderTitle(getString(R.string.category));
        ArrayList<SingleSquareModel> singleSquare = new ArrayList<SingleSquareModel>();
        for (int i=0; i<=21; i++)
        {
            if (i==0)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.all), "URL"+0, "all"));
            }
            if (i==1)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.animals_amp_pet_supplies), "URL"+1, "a"));
            }
            if (i==2)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.apparel_amp_accessories), "URL"+2, "b"));
            }
            if (i==3)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.arts_amp_entertainment), "URL"+3, "c"));
            }
            if (i==4)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.baby_amp_toddler), "URL"+4, "d"));
            }
            if (i==5)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.business_amp_industrial), "URL"+5, "e"));
            }
            if (i==6)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.cameras_amp_optics), "URL"+6, "f"));
            }
            if (i==7)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.electronics), "URL"+7, "g"));
            }
            if (i==8)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.food_beverages_amp_tobacco), "URL"+8, "h"));
            }
            if (i==9)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.furniture), "URL"+9, "i"));
            }
            if (i==10)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.hardware), "URL"+10, "j"));
            }
            if (i==11)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.health_amp_beauty), "URL"+11, "k"));
            }
            if (i==12)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.home_amp_garden), "URL"+12, "l"));
            }
            if (i==13)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.luggage_amp_bags), "URL"+13, "m"));
            }
            if (i==14)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.mature), "URL"+14, "n"));
            }
            if (i==15)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.media), "URL"+15, "o"));
            }
            if (i==16)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.office_supplies), "URL"+16, "p"));
            }
            if (i==17)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.religious_amp_ceremonial), "URL"+17, "q"));
            }
            if (i==18)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.software), "URL"+18, "r"));
            }
            if (i==19)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.sporting_goods), "URL"+19, "s"));
            }
            if (i==20)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.toys_amp_games), "URL"+20, "t"));
            }
            if (i==21)
            {
                singleSquare.add(new SingleSquareModel(getString(R.string.vehicles_amp_parts), "URL"+21, "u"));
            }
        }
        dm.setAllItemsInSection(singleSquare);
        allSampleData.add(dm);
    }

}
