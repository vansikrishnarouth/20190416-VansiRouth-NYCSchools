package com.data.nycschools.utils;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.data.nycschools.adapter.SchoolListAdapter;
import com.data.nycschools.entity.School;

import java.util.List;

/**
 * Used BindingAdapter to directly setup observable dataset
 * to the view components in XML
 */
public class ListBindings {

    @BindingAdapter("app:listItems")
    public static void setItems(RecyclerView recyclerView, List<School> schoolsList){
        SchoolListAdapter adapter = (SchoolListAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItems(schoolsList);
        }
    }
}
