package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;
import java.util.Arrays;

public class Dutys{
    Duty[] d;
    public Dutys(){
        d = new Duty[0];
    }
    public void add(Duty du){
        d = Arrays.copyOf(d,d.length+1);
        d[d.length-1] = du;
    }
    public int remove(Duty dy){
        int i =-1;
        for (int j = 0;j<d.length;j++){
            if (d[j].equals(dy)){
                i = j;
                break;
            }
        }
        if (i!= -1) {
            Duty[] newD = new Duty[d.length - 1];
            for (int j = 0; j < i; j++)
                newD[j] = d[j];
            for (int j = i + 1; j < d.length; j++)
                newD[j - 1] = d[j];
            d = newD;
        }
        return i;
    }
    public Duty get(int i){
        return d[i];
    }
    public int size(){
        return d.length;
    }
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        for (Duty du:d){
            tmp.add(du.serialObject());
        }
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        ArrayList<Duty> out = new ArrayList<>();
        for (Object o:tmp){
            Duty du = new Duty();
            du.loadFromObject(o);
            out.add(du);
        }
        d = out.toArray(d);
    }
}
