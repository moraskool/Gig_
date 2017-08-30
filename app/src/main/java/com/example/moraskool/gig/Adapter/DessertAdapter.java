package com.example.moraskool.gig.Adapter;

/**
 * Created by moraskool on 05/16/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moraskool.gig.Model.Dessert;
import com.example.moraskool.gig.R;

import java.util.ArrayList;
import java.util.List;


public class DessertAdapter extends RecyclerView.Adapter<DessertAdapter.DessertVh> {


    private List<Dessert> desserts = new ArrayList<>();
    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;

    private Context context;

     // TODO: placeholder stuff here

    @Override
    public int getItemViewType(int position) {
        if (desserts.isEmpty()) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        } else {
            return VIEW_TYPE_OBJECT_VIEW;
        }
    }
    public DessertAdapter(Context context,List<Dessert> desserts) {
        this.context = context;
        this.desserts = desserts;

         desserts = Dessert.prepareDesserts(
                context.getResources().getStringArray(R.array.dessert_names),
                context.getResources().getStringArray(R.array.dessert_descriptions),
                context.getResources().getStringArray(R.array.dessert_amounts));
    }


    // TODO: another placeholder stuff here
    @Override
    public DessertVh onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_dessert, parent, false);
        return new DessertAdapter.DessertVh(view);
    }

    @Override
    public void onBindViewHolder(DessertVh holder, int position) {
        Dessert dessert = desserts.get(position);

        holder.mName.setText(dessert.getName());
        holder.mDescription.setText(dessert.getDescription());
        holder.mFirstLetter.setText(String.valueOf(dessert.getFirstLetter()));
        holder.mPrice.setText(String.valueOf(dessert.getAmount()));

    }

    @Override
    public int getItemCount() {
        // if nothing, return null,
        // else return the number of items in the list
        return desserts == null ? 0 : desserts.size();
    }

    public static class DessertVh extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mPrice;
        private TextView mDescription;
        private TextView mFirstLetter;

        public DessertVh(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.txt_name);
            mPrice = (TextView) itemView.findViewById(R.id.txt_price);
            mDescription = (TextView) itemView.findViewById(R.id.txt_desc);
            mFirstLetter = (TextView) itemView.findViewById(R.id.txt_firstletter);
        }
    }
}
