package com.example.superheroes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> implements Filterable {
    private Context sContext;
    private List<HeroItem> sList;
    private List<HeroItem> sListAll;
    private OnItemClickListener sListener;

    @Override
    public Filter getFilter() {
        return HeroFilter;
    }

    private Filter HeroFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HeroItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(sListAll);
            } else {
                for (HeroItem item : sListAll) {
                    if (item.getName().toLowerCase().contains(constraint.toString().toLowerCase().trim()) || Integer.toString(item.getID()).contains(constraint.toString().toLowerCase().trim())) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sList.clear();
            sList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        sListener = listener;
    }

    public HeroAdapter(Context context, List<HeroItem> HeroList) {
        sContext = context;
        sList = HeroList;
        sList = new ArrayList<>(HeroList);
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(sContext).inflate(R.layout.hero_item, parent, false);
        return new HeroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        HeroItem currentItem = sList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String creatorName = currentItem.getName();
        int id = currentItem.getID();
        holder.sTVName.setText(creatorName);
        holder.sTVID.setText("ID: " + id);
        Picasso.with(sContext).load(imageUrl).fit().centerInside().into(holder.sImageView);
    }

    @Override
    public int getItemCount() {
        return sList.size();
    }


    public class HeroViewHolder extends RecyclerView.ViewHolder{
        public ImageView sImageView;
        public TextView sTVName;
        public TextView sTVID;



        public HeroViewHolder(@NonNull View itemView) {
            super(itemView);
            sImageView = itemView.findViewById(R.id.image_view);
            sTVName = itemView.findViewById(R.id.text_view_name);
            sTVID = itemView.findViewById(R.id.text_view_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            sListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
