package com.nico.rsshub.views;

import android.content.Context;
import android.widget.RadioButton;

import com.nico.rsshub.modeles.Category;

/**
 * Created by Nico on 23/12/2016.
 */

public class CategoryRadioButton extends RadioButton {

    private Category category;

    public CategoryRadioButton(Context context,Category category) {
        super(context);
        this.category = category;
        this.setText(category.getValue());
    }

    public Category getCategory() {
        return category;
    }
}
