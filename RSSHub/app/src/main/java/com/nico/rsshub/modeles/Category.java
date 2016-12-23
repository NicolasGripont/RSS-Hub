package com.nico.rsshub.modeles;

import com.nico.rsshub.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Nico on 20/12/2016.
 */

public enum Category {
    SPORT(R.string.sport),
    COMPUTING(R.string.computing),
    ECONOMY(R.string.economy),
    SCIENCES(R.string.sciences),
    CULTURE(R.string.culture),
    ECOLOGY(R.string.ecology),
    MULTIMEDIA(R.string.multimedia),
    INFORMATION(R.string.information),
    OTHER(R.string.other);

    private final int value;

    private Category(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
