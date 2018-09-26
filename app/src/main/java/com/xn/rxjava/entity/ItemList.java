package com.xn.rxjava.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xn068074 on 2018/9/17.
 */

public class ItemList implements Serializable{
    public String name;
    public int count;
    public List<Item> itemList;
}
