package com.apps.darkstorm.swrpg.StarWars.CharStuff;

import java.util.ArrayList;

public class Note{
    //Version 1 0-1
    public String title,note;
    public Object serialObject(){
        ArrayList<Object> tmp = new ArrayList<>();
        tmp.add(title);
        tmp.add(note);
        return tmp.toArray();
    }
    public void loadFromObject(Object obj){
        Object[] tmp = (Object[])obj;
        switch (tmp.length){
            case 2:
                title = (String)tmp[0];
                note = (String)tmp[1];
        }
    }
}
