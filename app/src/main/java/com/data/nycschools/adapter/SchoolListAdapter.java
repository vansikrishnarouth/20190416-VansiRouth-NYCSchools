package com.data.nycschools.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.data.nycschools.databinding.SchoolItemBinding;
import com.data.nycschools.entity.School;
import com.data.nycschools.ui.SchoolListFragment;

import java.util.List;

public class SchoolListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<School> items;
    private SchoolListFragment.FragmentInteractionListener listener;


    public class DataViewHolder extends RecyclerView.ViewHolder{
        private SchoolItemBinding binding;

        public DataViewHolder(SchoolItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public SchoolItemBinding getBinding() {
            return binding;
        }
    }

    public SchoolListAdapter(SchoolListFragment.FragmentInteractionListener listener, List<School> itemsList) {
        this.listener = listener;
        items = itemsList;
    }

    @Override
    public int getItemCount() {
        return (items == null)?0:items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SchoolItemBinding binding = SchoolItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DataViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ((DataViewHolder) viewHolder).getBinding().setSchool(items.get(position));
        ((DataViewHolder) viewHolder).getBinding().setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.openSchoolDetail(position);
            }
        });
    }

    public void setItems(List<School> itemsList) {
        items = itemsList;
        notifyDataSetChanged();
    }

}
