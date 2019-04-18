package com.data.nycschools.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.data.nycschools.R;
import com.data.nycschools.adapter.SchoolListAdapter;
import com.data.nycschools.databinding.FragSchoolListBinding;
import com.data.nycschools.utils.Utils;


public class SchoolListFragment extends Fragment {

    FragmentInteractionListener listener;
    FragSchoolListBinding fragBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragBinding = DataBindingUtil.inflate(inflater, R.layout.frag_school_list, container, false);
        fragBinding.setViewModel(listener.getViewModel());
        return fragBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupAdapter();
        loadSchoolList();
    }

    private void setupAdapter() {
        RecyclerView recyclerView =  fragBinding.recyclerView;
        SchoolListAdapter adapter = new SchoolListAdapter(listener , listener.getViewModel().schools);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_decorator));
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(adapter);
    }

    private void loadSchoolList() {
        if(Utils.isConnected(getContext())) {
            listener.getViewModel().loadSchoolList();
        }
        else{
            listener.getViewModel().snackBarMessage.setValue(R.string.internet_not_connected);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            listener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface FragmentInteractionListener {
        SchoolViewModel getViewModel();
        void openSchoolDetail(int position);
    }
}
