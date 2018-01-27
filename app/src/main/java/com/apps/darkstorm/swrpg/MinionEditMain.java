package com.apps.darkstorm.swrpg;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.sw.Minion;
import com.apps.darkstorm.swrpg.ui.SetupMinionAttr;

public class MinionEditMain extends Fragment {

    private OnMinionEditInteractionListener mListener;
    private Minion minion;

    public MinionEditMain() {
    }
    public static MinionEditMain newInstance(Minion minion) {
        MinionEditMain fragment = new MinionEditMain();
        fragment.minion = minion;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            if(!((SWrpg)getActivity().getApplication()).hasShortcut(minion))
                ((SWrpg)getActivity().getApplication()).addShortcut(minion,getActivity());
            else
                ((SWrpg)getActivity().getApplication()).updateShortcut(minion,getActivity());
        }
    }

    public static MinionEditMain newInstance(int ID) {
        MinionEditMain fragment = new MinionEditMain();
        fragment.minion = new Minion(ID);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View top = inflater.inflate(R.layout.fragment_minion_edit_main, container, false);
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.uni_fab);
        fab.hide();
        Handler handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(getActivity()!= null&& minion!=null) {
                    SetupMinionAttr.setup((LinearLayout) top.findViewById(R.id.main_lay), getActivity(), minion);
                    minion.showHideCards(top);
                }
            }
        };
        handle.sendEmptyMessage(0);
        top.setFocusableInTouchMode(true);
        top.requestFocus();
        return top;
    }

    public void onResume(){
        super.onResume();
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false) &&
                ((SWrpg)getActivity().getApplication()).gac != null){
            minion.startEditing(getActivity(),((SWrpg)getActivity().getApplication()).charsFold.getDriveId());
        }else{
            minion.startEditing(getActivity());
        }
    }

    public void onPause(){
        super.onPause();
        minion.stopEditing();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMinionEditInteractionListener) {
            mListener = (OnMinionEditInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMinionEditInteractionListener {
    }
}