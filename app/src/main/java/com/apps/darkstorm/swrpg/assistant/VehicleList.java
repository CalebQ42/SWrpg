package com.apps.darkstorm.swrpg.assistant;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apps.darkstorm.swrpg.assistant.load.DriveLoadVehicles;
import com.apps.darkstorm.swrpg.assistant.load.InitialConnect;
import com.apps.darkstorm.swrpg.assistant.load.LoadVehicles;
import com.apps.darkstorm.swrpg.assistant.sw.Vehicle;
import com.apps.darkstorm.swrpg.assistant.ui.cards.VehicleCard;
import com.apps.darkstorm.swrpg.assistant.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class VehicleList extends Fragment {
    private OnVehicleListInteractionListener mListener;
    Handler handle;
    ArrayList<Vehicle> vehicles = new ArrayList<>();

    public VehicleList() {}

    public static VehicleList newInstance() {
        VehicleList fragment = new VehicleList();
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
        return inflater.inflate(R.layout.general_list, container, false);
    }

    public void onViewCreated(final View top,Bundle saved){
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout)top.findViewById(R.id.swipe_refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadVehicles();
            }
        });
        if (((SWrpg)getActivity().getApplication()).prefs.getBoolean(getActivity().getString(R.string.ads_key),true)) {
            AdView ads = new AdView(getActivity());
            ads.setAdSize(AdSize.BANNER);
            LinearLayout.LayoutParams adLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            adLayout.weight = 0;
            adLayout.topMargin = (int)(5*getActivity().getResources().getDisplayMetrics().density);
            adLayout.gravity = Gravity.CENTER_HORIZONTAL;
            ads.setLayoutParams(adLayout);
            if(BuildConfig.DEBUG){
                ads.setAdUnitId(getActivity().getString(R.string.banner_test));
            }else {
                if (BuildConfig.APPLICATION_ID.equals("com.apps.darkstorm.swrpg"))
                    ads.setAdUnitId(getActivity().getString(R.string.free_banner_ad_id));
                else
                    ads.setAdUnitId(getActivity().getString(R.string.paid_banner_ad_id));
            }
            AdRequest adRequest = new AdRequest.Builder().addKeyword("Star Wars").build();
            ads.loadAd(adRequest);
            ((LinearLayout)top.findViewById(R.id.top_lay)).addView(ads,0);
        }
        final LinearLayout linLay = (LinearLayout)top.findViewById(R.id.main_lay);
        handle = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==20){
                    refresh.setRefreshing(true);
                }else if(msg.arg1==-20){
                    refresh.setRefreshing(false);
                }else if(msg.arg1==5){
                    if (getActivity() != null)
                        Snackbar.make(top,R.string.cloud_fail,Snackbar.LENGTH_LONG).show();
                }else if(msg.arg1==15){
                    if(getActivity()!=null)
                        Snackbar.make(top,R.string.still_loading,Snackbar.LENGTH_LONG).show();
                }
                if (msg.obj instanceof ArrayList){
                    ArrayList<Vehicle> chars = (ArrayList<Vehicle>)msg.obj;
                    if(!chars.equals(vehicles)||linLay.getChildCount()!=chars.size()) {
                        linLay.removeAllViews();
                        vehicles = chars;
                        for(Vehicle chara:chars){
                            if(getActivity()!=null)
                                linLay.addView(VehicleCard.getCard(getActivity(),linLay,chara,handle));
                        }
                    }
                }
                if (msg.obj instanceof Vehicle){
                    if (msg.arg1==-1){
                        int ind = vehicles.indexOf(msg.obj);
                        ((Vehicle)msg.obj).delete(getActivity());
                        if(ind != -1){
                            linLay.removeViewAt(ind);
                            vehicles.remove(ind);
                        }
                    }else{
                        if(!refresh.isRefreshing()) {
                            getFragmentManager().beginTransaction().replace(R.id.content_main,
                                    VehicleEdit.newInstance((Vehicle) msg.obj))
                                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                                            android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack("").commit();
                        }else{
                            Message out = handle.obtainMessage();
                            out.arg1=15;
                            handle.sendMessage(out);
                        }
                    }
                }
            }
        };
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.uni_fab);
        fab.setImageResource(R.drawable.add);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadVehicles lv = new LoadVehicles(getActivity());
                ArrayList<Integer> has = new ArrayList<>();
                for (Vehicle vh:lv.vehicles)
                    has.add(vh.ID);
                int id = 0;
                for(int i = 0;i<lv.vehicles.size();i++){
                    if(lv.vehicles.get(i).ID==id){
                        id++;
                        i = -1;
                    }
                }
                getFragmentManager().beginTransaction().replace(R.id.content_main, VehicleEdit.newInstance(id))
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,
                                android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("").commit();
            }
        });
    }

    public void onResume(){
        super.onResume();
        loadVehicles();
    }

    public void loadVehicles(){
        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Message dal = handle.obtainMessage();
                dal.arg1 = 20;
                handle.sendMessage(dal);
                if(ContextCompat.checkSelfPermission(VehicleList.this.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    while(((SWrpg)getActivity().getApplication()).askingPerm){
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(ContextCompat.checkSelfPermission(VehicleList.this.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)) {
                        int timeout = 0;
                        if(((SWrpg)getActivity().getApplication()).prefs.getBoolean(getString(R.string.google_drive_key),false)){
                            if(((SWrpg)getActivity().getApplication()).gac==null ||!((SWrpg)getActivity().getApplication()).gac.isConnected())
                                ((MainActivity)getActivity()).gacMaker();
                        }
                        while ((((SWrpg) getActivity().getApplication()).gac == null ||
                                !((SWrpg) getActivity().getApplication()).gac.isConnected()) && timeout < 50) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            timeout++;
                            if(getActivity()==null)
                                break;
                        }
                        if (getActivity()!= null) {
                            if (timeout < 50) {
                                if(((SWrpg)getActivity().getApplication()).driveFail){
                                    ((SWrpg)getActivity().getApplication()).driveFail = false;
                                    InitialConnect.connect(getActivity());
                                }
                                DriveLoadVehicles dlc = new DriveLoadVehicles(getActivity());
                                if (dlc.vehicles != null) {
                                    dlc.saveLocal(getActivity());
                                }
                            } else {
                                Message out = handle.obtainMessage();
                                out.arg1 = 5;
                                handle.sendMessage(out);
                            }
                        }
                    }
                    if (getActivity()!= null) {
                        LoadVehicles lc = new LoadVehicles(getActivity());
                        Message out = handle.obtainMessage();
                        out.obj = lc.vehicles;
                        handle.sendMessage(out);
                    }
                }
                Message out = handle.obtainMessage();
                out.arg1 = -20;
                handle.sendMessage(out);
                return null;
            }
        };
        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVehicleListInteractionListener) {
            mListener = (OnVehicleListInteractionListener) context;
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

    public interface OnVehicleListInteractionListener {
    }
}