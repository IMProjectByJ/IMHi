package com.example.star.imhi.DAO.pojo;
import java.io.Serializable;

/**
 * Created by d c on 2018/1/5.
 */

public class ItemModel implements Serializable {

    public static final int CHAT_A = 1001;
    public static final int CHAT_B = 1002;
    public int type;
    public Object object;

    public ItemModel(int type, Object object) {
        this.type = type;
        this.object = object;
    }
}

