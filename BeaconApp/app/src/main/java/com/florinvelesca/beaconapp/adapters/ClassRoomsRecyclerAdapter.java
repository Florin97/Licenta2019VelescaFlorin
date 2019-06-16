package com.florinvelesca.beaconapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.florinvelesca.beaconapp.R;
import com.florinvelesca.beaconapp.interfaces.OnClassRoomSelected;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassRoomsRecyclerAdapter extends RecyclerView.Adapter<ClassRoomsRecyclerAdapter.ViewHolder> implements Filterable {
    List<String> searchedClassRooms;
    List<String> allClassRooms;
    Context context;
    private static OnClassRoomSelected onClassRoomSelected;

    public ClassRoomsRecyclerAdapter(List<String> classRooms, Context context,OnClassRoomSelected classRoomSelected) {
        this.searchedClassRooms = classRooms;
        this.allClassRooms = new ArrayList<>(classRooms);
        this.context = context;
        onClassRoomSelected = classRoomSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.classroom_item_view, viewGroup, false),onClassRoomSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String className = searchedClassRooms.get(i);
        viewHolder.className.setText(className);


    }

    @Override
    public int getItemCount() {
        return searchedClassRooms.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(allClassRooms);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(String classRoom : allClassRooms){
                    if(classRoom.toLowerCase().trim().contains(filterPattern)){
                        filteredList.add(classRoom);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values= filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            searchedClassRooms.clear();
            searchedClassRooms.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView className;
        OnClassRoomSelected onClassRoomSelected;
        public ViewHolder(@NonNull View itemView, OnClassRoomSelected onClassRoomSelected) {
            super(itemView);
            this.onClassRoomSelected = onClassRoomSelected;
            this.className = itemView.findViewById(R.id.text_view_class_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClassRoomSelected.onClassSelected(getAdapterPosition());
        }
    }
}
