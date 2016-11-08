package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;
import java.util.Arrays;

public class ForcePowers{
    ForcePower[] fp;
    public ForcePowers(){
        fp = new ForcePower[0];
    }
    public void add(ForcePower f){
        fp = Arrays.copyOf(fp,fp.length+1);
        fp[fp.length-1] = f;
    }
    public int remove(ForcePower f){
        int i =-1;
        for (int j = 0;j<fp.length;j++){
            if (fp[j].equals(f)){
                i = j;
                break;
            }
        }
        if (i!=-1) {
            ForcePower[] newFp = new ForcePower[fp.length - 1];
            for (int j = 0; j < i; j++)
                newFp[j] = fp[j];
            for (int j = i + 1; j < fp.length; j++)
                newFp[j - 1] = fp[j];
            fp = newFp;
        }
        return i;
    }
    public ForcePower get(int i){
        return fp[i];
    }
    public int size(){
        return fp.length;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (ForcePower f:fp){
            tmp.add(f.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<ForcePower> out = new ArrayList<>();
        for (Object o:tmp){
            ForcePower f = new ForcePower();
            f.loadFromObject(o);
            out.add(f);
        }
        fp = out.toArray(fp);
    }
}
