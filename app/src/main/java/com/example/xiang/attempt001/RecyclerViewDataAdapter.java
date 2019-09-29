package com.example.xiang.attempt001;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by xiang on 2017-05-17.
 */

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder>{
    private ArrayList<SquareSectionDataModel> dataList;
    private Context myContext;
    View myView;

    public RecyclerViewDataAdapter(Context context, ArrayList<SquareSectionDataModel> dataList, View myView)
    {
        this.dataList = dataList;
        this.myContext = context;
        this.myView = myView;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.square_list_item, null);
        v.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        ItemRowHolder myHolder = new ItemRowHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        ArrayList singleSectionItems = dataList.get(position).getAllItemsInSection();
        holder.squareTitle.setTypeface(HomeScreen.FrontBold);
        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(myContext, singleSectionItems, myView);
        holder.recycler_view_list.setHasFixedSize(true);
        holder.recycler_view_list.setLayoutManager(new LinearLayoutManager(myContext, LinearLayoutManager.HORIZONTAL,false));
        holder.recycler_view_list.setAdapter(itemListDataAdapter);
        holder.recycler_view_list.setNestedScrollingEnabled(false);

        holder.buttonMore.setOnClickListener(new View.OnClickListener() {
            private Context context = myView.getContext();
            @Override
            public void onClick(View v) {
                SelectDialog selectDialog = new SelectDialog();
                android.app.FragmentManager selectDialogFM = ((Activity)context).getFragmentManager();
                selectDialog.show(selectDialogFM,SelectDialog.TAG);
            }
        });

    }

    @Override
    public int getItemCount() {
        return(null!=dataList?dataList.size():0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder
    {
        protected TextView squareTitle;
        protected RecyclerView recycler_view_list;
        protected Button buttonMore;
        public ItemRowHolder(View view)
        {
            super(view);
            this.squareTitle=(TextView)view.findViewById(R.id.squareTitle);
            this.recycler_view_list = (RecyclerView)view.findViewById(R.id.recycler_view_list);
            this.buttonMore=(Button)view.findViewById(R.id.buttonMore);
        }

    }

}
