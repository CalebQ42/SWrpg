package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;

public class Item{
    //Version 1 0-2
    public String name;
    public String desc;
    public int count;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(name);
        tmp.add(desc);
        tmp.add(count);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 3:
                name = (String)tmp[0];
                desc = (String)tmp[1];
                count = (int)tmp[2];
        }
    }
}
