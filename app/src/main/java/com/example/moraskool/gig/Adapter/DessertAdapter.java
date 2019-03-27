package com.example.moraskool.gig.Adapter;

/**
 * Created by moraskool on 05/16/17.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moraskool.gig.Model.Dessert;
import com.example.moraskool.gig.R;
import com.example.moraskool.gig.ViewGigActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DessertAdapter extends RecyclerView.Adapter<DessertAdapter.DessertVh> {

    private List<Dessert> desserts = new ArrayList<>();
    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private static String today;

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

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }


    // TODO: another placeholder stuff here
    @Override
    public DessertVh onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_dessert, parent, false);
        return new DessertAdapter.DessertVh(view);
    }

    @Override
    public void onBindViewHolder(DessertVh holder, final int position) {
        Dessert dessert = desserts.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , ViewGigActivity.class);
                view.getContext().startActivity(intent);
            }

        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                String curtUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final Query latestQuery = rootRef
                        .child("User Posts")
                        .child(curtUserId)
                        .child("Gig posts");
                latestQuery.getRef().removeValue();

                return true;
            }
        });
        holder.mName.setText(dessert.getName());
        holder.mDescription.setText(dessert.getDescription());
        holder.mFirstLetter.setText(String.valueOf(dessert.getFirstLetter()));
        //holder.mTimeStamp.setText(getTimeStamp(dessert.getTimestamp()));
        holder.mPrice.setText(String.valueOf(dessert.getAmount()));

    }

    public interface RecyclerViewItemClickListener {
        void onClickListenerForItem(int position);

    }
    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
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
        private TextView mTimeStamp;

        public DessertVh(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.txt_name);
            mPrice = (TextView) itemView.findViewById(R.id.txt_price);
            mDescription = (TextView) itemView.findViewById(R.id.txt_desc);
            mFirstLetter = (TextView) itemView.findViewById(R.id.txt_firstletter);
            mTimeStamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }
}