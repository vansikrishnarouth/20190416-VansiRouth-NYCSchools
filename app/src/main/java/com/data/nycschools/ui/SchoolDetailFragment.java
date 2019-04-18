package com.data.nycschools.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.data.nycschools.R;
import com.data.nycschools.databinding.FragSchoolDetailBinding;
import com.data.nycschools.entity.School;
import com.data.nycschools.utils.Constants;
import com.data.nycschools.utils.Utils;

public class SchoolDetailFragment extends Fragment {

    FragmentInteractionListener listener;
    FragSchoolDetailBinding fragBinding;
    int position;
    static String ARG_PARAM1 = "param1";

    public static Fragment newInstance(int position) {
        SchoolDetailFragment frag = new SchoolDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragBinding = DataBindingUtil.inflate(inflater, R.layout.frag_school_detail, container, false);
        position = getArguments().getInt(ARG_PARAM1, -1);
        if(position != -1) {
            School school = listener.getViewModel().individualSchool(position);
            fragBinding.setSchool(school);
        }
        fragBinding.setWebsiteClickListener(websiteClick);
        fragBinding.setPhoneClickListener(phoneClick);
        fragBinding.setViewModel(listener.getViewModel());
        return fragBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadSatScoresIfNotAvailable();
    }

    private void loadSatScoresIfNotAvailable() {
        if(listener.getViewModel().individualSchool(position).satScores == null){
            if (Utils.isConnected(getContext())) {
                listener.getViewModel().loadSatScoresFor(position);
            } else {
                listener.getViewModel().snackBarMessage.setValue(R.string.internet_not_connected);
            }
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

    private View.OnClickListener websiteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            School school = listener.getViewModel().individualSchool(position);
            if(school.website != null) {
                try {
                    //Prefix with http:// if not present, else ActivityChooser could not
                    //parse the Uri and show right apps
                    school.website = (school.website.startsWith("http://")) ? school.website : "http://" + school.website;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(school.website));
                    Intent chooserIntent = Intent.createChooser(intent , "Open with");
                    startActivity(chooserIntent);
                }
                catch (ActivityNotFoundException e){
                    e.printStackTrace();
                    Utils.showLongToast(getContext(), getString(R.string.msg_no_support_apps));
                }
                catch (Exception e){
                    e.printStackTrace();
                    Utils.showLongToast(getContext(), getString(R.string.msg_unexpected_error));
                }
            }
            else{
                Utils.showLongToast(getContext(), getString(R.string.msg_website_not_available));
            }
        }
    };

    private View.OnClickListener phoneClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            School school = listener.getViewModel().individualSchool(position);
            if(school.phone_number != null) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.CALL_PHONE},
                            Constants.REQUEST_CALL);
                } else {
                    initiateCall(school.phone_number);
                }
            }
            else{
                Utils.showLongToast(getContext(), getString(R.string.msg_phone_not_available));
            }
        }
    };

    private void initiateCall(String number){
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+number));
            startActivity(intent);
        }
        catch (ActivityNotFoundException e){
            e.printStackTrace();
            Utils.showLongToast(getContext(), getString(R.string.msg_no_support_apps));
        }
        catch (Exception e){
            e.printStackTrace();
            Utils.showLongToast(getContext(), getString(R.string.msg_unexpected_error));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.REQUEST_CALL && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            initiateCall(listener.getViewModel().individualSchool(position).phone_number);
        }
        else{
            Utils.showLongToast(getContext(), getString(R.string.grant_call_permission_msg));
        }
    }

    interface FragmentInteractionListener {
        SchoolViewModel getViewModel();
    }
}
