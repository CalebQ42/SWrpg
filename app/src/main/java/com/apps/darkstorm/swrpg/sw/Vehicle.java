package com.apps.darkstorm.swrpg.sw;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.apps.darkstorm.swrpg.R;
import com.apps.darkstorm.swrpg.SWrpg;
import com.apps.darkstorm.swrpg.custvars.DriveSaveLoad;
import com.apps.darkstorm.swrpg.custvars.SaveLoad;
import com.apps.darkstorm.swrpg.sw.stuff.CriticalInjuries;
import com.apps.darkstorm.swrpg.sw.stuff.Weapons;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.util.Arrays;

public class Vehicle {
    //
    //  |  Version 1  |
    //  |             |
    //  V     0-19    V
    //
    public int ID;
    public String name = "";
    public int silhouette;
    public int speed;
    public int handling;
    public int armor;
    //0-Fore,1-Port,2-Starboard,3-Aft;
    public int[] defense = new int[4];
    public int totalDefense;
    public int hullTraumaThresh;
    public int hullTraumaCur;
    public int sysStressThresh;
    public int sysStressCur;
    public int encumCapacity;
    public int passengerCapacity;
    public int hp;
    public Weapons weapons = new Weapons();
    public CriticalInjuries crits = new CriticalInjuries();
    private boolean[] showCards = new boolean[6];
    public String desc = "";
    public String model = "";
    //
    //  ^                 ^
    //  |  Version 1 End  |
    //  |                 |
    //

    private boolean editing = false;
    private boolean saving = false;
    private String loc="";
    public boolean external = false;


    public Vehicle(){
        defense[1] = -1;
        defense[2] = -1;
        for (int i = 0;i<showCards.length;i++)
            showCards[i] = true;
    }
    public Vehicle(int ID){
        this.ID = ID;
        defense[1] = -1;
        defense[2] = -1;
        for (int i = 0;i<showCards.length;i++)
            showCards[i] = true;
    }
    public void setSilhouette(int sil){
        silhouette = sil;
        if (sil>4){
            if(defense[1]==-1)
                defense[1]=0;
            if(defense[2]==-1)
                defense[2] = 0;
        }else{
            if(defense[1]!=-1)
                defense[1] = -1;
            if(defense[2]!=-1)
                defense[2]=-1;
        }
    }
    public void stopEditing(){
        editing=false;
    }
    public void showHideCards(final View top){
        ((Switch)top.findViewById(R.id.basic_info_switch)).setChecked(showCards[0]);
        if (showCards[0])
            top.findViewById(R.id.basic_info_layout).setVisibility(View.VISIBLE);
        else
            top.findViewById(R.id.basic_info_layout).setVisibility(View.GONE);
        ((Switch)top.findViewById(R.id.basic_info_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showCards[0] = b;
                if (b)
                    top.findViewById(R.id.basic_info_layout).setVisibility(View.VISIBLE);
                else
                    top.findViewById(R.id.basic_info_layout).setVisibility(View.GONE);
            }
        });
        ((Switch)top.findViewById(R.id.defense_switch)).setChecked(showCards[1]);
        if (showCards[1])
            top.findViewById(R.id.defense_layout).setVisibility(View.VISIBLE);
        else
            top.findViewById(R.id.defense_layout).setVisibility(View.GONE);
        ((Switch)top.findViewById(R.id.defense_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showCards[1] = b;
                if (b)
                    top.findViewById(R.id.defense_layout).setVisibility(View.VISIBLE);
                else
                    top.findViewById(R.id.defense_layout).setVisibility(View.GONE);
            }
        });
        ((Switch)top.findViewById(R.id.damage_switch)).setChecked(showCards[2]);
        if (showCards[2])
            top.findViewById(R.id.damage_layout).setVisibility(View.VISIBLE);
        else
            top.findViewById(R.id.damage_layout).setVisibility(View.GONE);
        ((Switch)top.findViewById(R.id.damage_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showCards[2] = b;
                if (b)
                    top.findViewById(R.id.damage_layout).setVisibility(View.VISIBLE);
                else
                    top.findViewById(R.id.damage_layout).setVisibility(View.GONE);
            }
        });
        ((Switch)top.findViewById(R.id.weapons_show)).setChecked(showCards[3]);
        if (showCards[3])
            top.findViewById(R.id.weapons_main).setVisibility(View.VISIBLE);
        else
            top.findViewById(R.id.weapons_main).setVisibility(View.GONE);
        ((Switch)top.findViewById(R.id.weapons_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showCards[3] = b;
                if (b)
                    top.findViewById(R.id.weapons_main).setVisibility(View.VISIBLE);
                else
                    top.findViewById(R.id.weapons_main).setVisibility(View.GONE);
            }
        });
        ((Switch)top.findViewById(R.id.critical_injuries_show)).setChecked(showCards[4]);
        if (showCards[4])
            top.findViewById(R.id.critical_injuries_main).setVisibility(View.VISIBLE);
        else
            top.findViewById(R.id.critical_injuries_main).setVisibility(View.GONE);
        ((Switch)top.findViewById(R.id.critical_injuries_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showCards[4] = b;
                if (b)
                    top.findViewById(R.id.critical_injuries_main).setVisibility(View.VISIBLE);
                else
                    top.findViewById(R.id.critical_injuries_main).setVisibility(View.GONE);
            }
        });
        ((Switch)top.findViewById(R.id.desc_show)).setChecked(showCards[5]);
        if (showCards[5])
            top.findViewById(R.id.desc_main).setVisibility(View.VISIBLE);
        else
            top.findViewById(R.id.desc_main).setVisibility(View.GONE);
        ((Switch)top.findViewById(R.id.desc_show)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showCards[5] = b;
                if (b)
                    top.findViewById(R.id.desc_main).setVisibility(View.VISIBLE);
                else
                    top.findViewById(R.id.desc_main).setVisibility(View.GONE);
            }
        });
    }
    public Vehicle clone(){
        Vehicle tmp = new Vehicle();
        tmp.ID = ID;
        tmp.name = name;
        tmp.silhouette = silhouette;
        tmp.speed = speed;
        tmp.handling = handling;
        tmp.armor = armor;
        tmp.defense = defense.clone();
        tmp.totalDefense = totalDefense;
        tmp.hullTraumaCur = hullTraumaCur;
        tmp.hullTraumaThresh = hullTraumaThresh;
        tmp.sysStressCur = sysStressCur;
        tmp.sysStressThresh = sysStressThresh;
        tmp.encumCapacity = encumCapacity;
        tmp.passengerCapacity = passengerCapacity;
        tmp.hp = hp;
        tmp.weapons = weapons.clone();
        tmp.crits = crits.clone();
        tmp.showCards = showCards.clone();
        tmp.desc = desc;
        tmp.model = model;
        return tmp;
    }
    public void startEditing(final Activity main, final DriveId fold){
        if(external){
            startEditing(main);
        }else {
            if (!editing) {
                editing = true;
                AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        Vehicle old = Vehicle.this.clone();
                        if(((SWrpg)main.getApplication()).vehicFold!=null)
                            Vehicle.this.cloudSave(((SWrpg) main.getApplication()).gac, getFileId(main), false);
                        Vehicle.this.save(getFileLocation(main));
                        do {
                            if (!saving) {
                                saving = true;
                                if (!Vehicle.this.equals(old)) {
                                    if (!old.name.equals(Vehicle.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                        if (((SWrpg) main.getApplication()).hasShortcut(Vehicle.this)) {
                                            ((SWrpg) main.getApplication()).updateShortcut(Vehicle.this, main);
                                        } else {
                                            ((SWrpg) main.getApplication()).addShortcut(Vehicle.this, main);
                                        }
                                    }
                                    if(((SWrpg)main.getApplication()).vehicFold!=null)
                                        Vehicle.this.cloudSave(((SWrpg) main.getApplication()).gac, getFileId(main), false);
                                    Vehicle.this.save(getFileLocation(main));
                                    old = Vehicle.this.clone();
                                }
                                saving = false;
                            }
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (editing);
                        if (!saving) {
                            saving = true;
                            if (!Vehicle.this.equals(old)) {
                                if (!old.name.equals(Vehicle.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                                    if (((SWrpg) main.getApplication()).hasShortcut(Vehicle.this)) {
                                        ((SWrpg) main.getApplication()).updateShortcut(Vehicle.this, main);
                                    } else {
                                        ((SWrpg) main.getApplication()).addShortcut(Vehicle.this, main);
                                    }
                                }
                                if(((SWrpg)main.getApplication()).vehicFold!=null)
                                    Vehicle.this.cloudSave(((SWrpg) main.getApplication()).gac,
                                        getFileId(main), false);
                                Vehicle.this.save(getFileLocation(main));
                            }
                            saving = false;
                        }
                        return null;
                    }
                };
                async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }
    public void startEditing(final Activity main){
        if (!editing){
            editing = true;
            AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Vehicle old = Vehicle.this.clone();
                Vehicle.this.save(getFileLocation(main));
                do{
                    if (!saving) {
                        saving = true;
                        if (!Vehicle.this.equals(old)) {
                            if(!old.name.equals(Vehicle.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
                                if(((SWrpg)main.getApplication()).hasShortcut(Vehicle.this)) {
                                    ((SWrpg) main.getApplication()).updateShortcut(Vehicle.this, main);
                                }else{
                                    ((SWrpg)main.getApplication()).addShortcut(Vehicle.this,main);
                                }
                            }
                            Vehicle.this.save(getFileLocation(main));
                            old = Vehicle.this.clone();
                        }
                        saving = false;
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while(editing);
                if (!saving) {
                    saving = true;
                    if (!Vehicle.this.equals(old)) {
                        if(!old.name.equals(Vehicle.this.name) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
                            if(((SWrpg)main.getApplication()).hasShortcut(Vehicle.this)) {
                                ((SWrpg) main.getApplication()).updateShortcut(Vehicle.this, main);
                            }else{
                                ((SWrpg)main.getApplication()).addShortcut(Vehicle.this,main);
                            }
                        }
                        Vehicle.this.save(getFileLocation(main));
                    }
                    saving = false;
                }
                return null;
            }
        };
        async.execute();
    }
    }
    public void save(String filename){
        SaveLoad sl = new SaveLoad(filename);
        sl.addSave(ID);
        sl.addSave(name);
        sl.addSave(silhouette);
        sl.addSave(speed);
        sl.addSave(handling);
        sl.addSave(armor);
        sl.addSave(defense);
        sl.addSave(totalDefense);
        sl.addSave(hullTraumaCur);
        sl.addSave(hullTraumaThresh);
        sl.addSave(sysStressCur);
        sl.addSave(sysStressThresh);
        sl.addSave(encumCapacity);
        sl.addSave(passengerCapacity);
        sl.addSave(hp);
        sl.addSave(weapons.serialObject());
        sl.addSave(crits.serialObject());
        sl.addSave(showCards);
        sl.addSave(desc);
        sl.addSave(model);
        sl.save();
    }
    public void cloudSave(GoogleApiClient gac, DriveId fil, boolean async){
        if(fil != null){
            DriveSaveLoad sl = new DriveSaveLoad(fil);
            sl.setMime("swrpg/vhcl");
            sl.addSave(ID);
            sl.addSave(name);
            sl.addSave(silhouette);
            sl.addSave(speed);
            sl.addSave(handling);
            sl.addSave(armor);
            sl.addSave(defense);
            sl.addSave(totalDefense);
            sl.addSave(hullTraumaCur);
            sl.addSave(hullTraumaThresh);
            sl.addSave(sysStressCur);
            sl.addSave(sysStressThresh);
            sl.addSave(encumCapacity);
            sl.addSave(passengerCapacity);
            sl.addSave(hp);
            sl.addSave(weapons.serialObject());
            sl.addSave(crits.serialObject());
            sl.addSave(showCards);
            sl.addSave(desc);
            sl.addSave(model);
            sl.save(gac,async);
        }
    }
    public void reLoad(String filename){
        loc = filename;
        SaveLoad sl = new SaveLoad(filename);
        Object[] val = sl.load();
        System.out.println("Loading...");
        switch (val.length){
            case 20:
                model = (String)val[19];
                desc = (String)val[18];
                showCards = (boolean[])val[17];
                crits.loadFromObject(val[16]);
                weapons.loadFromObject(val[15]);
                hp = (int)val[14];
                passengerCapacity = (int)val[13];
                encumCapacity = (int)val[12];
                sysStressThresh = (int)val[11];
                sysStressCur = (int)val[10];
                hullTraumaThresh = (int)val[9];
                hullTraumaCur = (int)val[8];
                totalDefense = (int)val[7];
                defense = (int[])val[6];
                armor = (int)val[5];
                handling = (int)val[4];
                speed = (int)val[3];
                silhouette = (int)val[2];
                name = (String)val[1];
                String title = filename.substring(filename.lastIndexOf("/")+1);
                if (title.substring(0,title.indexOf(".")).equals(""))
                    ID = (int)val[0];
                else {
                    try {
                        ID = Integer.parseInt(title.substring(0, title.indexOf(".")));
                    }catch(java.lang.NumberFormatException ignored){
                        ID = (int)val[0];
                    }
                }
        }
    }
    public void reLoad(GoogleApiClient gac, DriveId fil){
        DriveSaveLoad sl = new DriveSaveLoad(fil);
        Object[] val = sl.load(gac);
        switch (val.length){
            case 20:
                model = (String)val[19];
                desc = (String)val[18];
                showCards = (boolean[])val[17];
                crits.loadFromObject(val[16]);
                weapons.loadFromObject(val[15]);
                hp = (int)val[14];
                passengerCapacity = (int)val[13];
                encumCapacity = (int)val[12];
                sysStressThresh = (int)val[11];
                sysStressCur = (int)val[10];
                hullTraumaThresh = (int)val[9];
                hullTraumaCur = (int)val[8];
                totalDefense = (int)val[7];
                defense = (int[])val[6];
                armor = (int)val[5];
                handling = (int)val[4];
                speed = (int)val[3];
                silhouette = (int)val[2];
                name = (String)val[1];
                String title = fil.asDriveFile().getMetadata(gac).await().getMetadata().getTitle();
                if (title.substring(0,title.indexOf(".")).equals(""))
                    ID = (int)val[0];
                else {
                    try {
                        ID = Integer.parseInt(title.substring(0, title.indexOf(".")));
                    }catch(java.lang.NumberFormatException ignored){
                        ID = (int)val[0];
                    }
                }
        }
    }
    public String getFileLocation(Activity main){
        if(main!= null) {
            String loc = ((SWrpg) main.getApplication()).prefs.getString(main.getString(R.string.local_location_key),
                    ((SWrpg) main.getApplication()).defaultLoc + "/SWShips");
            File location = new File(loc);
            if (!location.exists()) {
                if (!location.mkdir()) {
                    return "";
                }
            }
            String def = location.getAbsolutePath() + "/" + Integer.toString(ID) + ".vhcl";
            if(external)
                return this.loc;
            return def;
        }else{
            return "";
        }
    }
    public DriveId getFileId(Activity main){
        String name = Integer.toString(ID) + ".vhcl";
        DriveId fi = null;
        DriveApi.MetadataBufferResult res =
                ((SWrpg)main.getApplication()).vehicFold.queryChildren
                        (((SWrpg)main.getApplication()).gac,new Query.Builder().addFilter(
                Filters.eq(SearchableField.TITLE,name)).build()).await();
        for (Metadata met:res.getMetadataBuffer()){
            if (!met.isTrashed()){
                fi = met.getDriveId();
                break;
            }
        }
        res.release();
        if (fi == null){
            fi = ((SWrpg)main.getApplication()).vehicFold.createFile
                    (((SWrpg)main.getApplication()).gac,new MetadataChangeSet.Builder().setTitle(name).build(),null).await()
                    .getDriveFile().getDriveId();
        }
        return fi;
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Vehicle))
            return false;
        Vehicle in = (Vehicle)obj;
        return in.ID == ID && in.name.equals(name) && in.silhouette == silhouette && in.speed == speed && in.handling == handling
                && in.armor == armor && Arrays.equals(in.defense,defense) && totalDefense == in.totalDefense && in.hullTraumaCur == hullTraumaCur
                && in.hullTraumaThresh == hullTraumaThresh && in.sysStressCur == sysStressCur && in.sysStressThresh == sysStressThresh
                && in.encumCapacity == encumCapacity && in.passengerCapacity == passengerCapacity && in.hp == hp && in.weapons.equals(weapons)
                && in.crits.equals(crits) && Arrays.equals(in.showCards,showCards) && in.desc.equals(desc) && in.model.equals(model);
    }
    public void delete(final Activity main){
        File tmp = new File(getFileLocation(main));
        tmp.delete();
        if(((SWrpg)main.getApplication()).prefs.getBoolean(main.getString(R.string.google_drive_key),false)){
            AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    getFileId(main).asDriveResource().delete(((SWrpg)main.getApplication()).gac).await();
                    return null;
                }
            };
            async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ((SWrpg)main.getApplication()).deleteShortcut(this,main);
        }
    }
}
