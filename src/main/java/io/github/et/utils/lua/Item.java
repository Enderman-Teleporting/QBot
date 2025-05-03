package io.github.et.utils.lua;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Item {
    private String name;
    private ArrayList<Class> classes=new ArrayList<Class>();
    private boolean nullable;

    public Item(String name,boolean nullable,String ... classes) throws ClassNotFoundException {
        this.name=name;
        this.nullable=nullable;
        for(String c:classes){
            this.classes.add(Class.forName(c));
        }
    }

}
