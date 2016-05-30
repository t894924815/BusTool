package com.aki.bustool.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aki.bustool.activities.MainActivity;
import com.aki.bustool.R;

/**
 * Created by chunr on 2016/4/29.
 */
public class MineFragment extends Fragment {

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,null);

        mainActivity = (MainActivity) getActivity();
        mainActivity.switchIco(3);
        return  view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivity.switchIco(33);
    }
}
