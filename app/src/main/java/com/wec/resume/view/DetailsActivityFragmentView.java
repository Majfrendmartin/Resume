package com.wec.resume.view;


import android.support.annotation.NonNull;

import com.wec.resume.model.BaseItem;

import java.util.Collection;

public interface DetailsActivityFragmentView extends View {
    void showList(@NonNull Collection<BaseItem> items, int type);
}
