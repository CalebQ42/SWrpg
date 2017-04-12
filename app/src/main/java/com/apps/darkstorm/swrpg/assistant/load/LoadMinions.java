package com.apps.darkstorm.swrpg.assistant.load;

import android.app.Activity;

import com.apps.darkstorm.swrpg.assistant.SWrpg;
import com.apps.darkstorm.swrpg.assistant.sw.Minion;
import com.apps.darkstorm.swrpg.assistant.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class LoadMinions {
    public ArrayList<Minion> minions;
    public ArrayList<Date> lastMod;
    public LoadMinions(Activity main){
        minions = new ArrayList<>();
        lastMod = new ArrayList<>();
        if(main!= null) {
            File fold = new File(((SWrpg) main.getApplication()).prefs.getString(main.getString(R.string.local_location_key), ((SWrpg) main.getApplication()).defaultLoc));
            if (!fold.exists()) {
                if (!fold.mkdir()) {
                    return;
                }
            }
            File[] chars = fold.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".minion")||name.endsWith(".minion.bak");
                }
            });
            outer:
            for (File fil : chars) {
                if(fil.getName().endsWith(".minion")){
                    for(File file:chars){
                        if(Objects.equals(file.getName(), fil.getName() + ".bak")) {
                            fil.delete();
                            continue outer;
                        }
                    }
                }
                Minion tmp = new Minion();
                tmp.reLoad(fil.getAbsolutePath());
                minions.add(tmp);
                lastMod.add(new Date(fil.lastModified()));
                if(fil.getName().endsWith(".bak")){
                    tmp.save(tmp.getFileLocation(main));
                    fil.delete();
                }
            }
        }
    }
}