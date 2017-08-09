package com.wec.resume.view;


import android.support.annotation.NonNull;

import com.wec.resume.model.BaseItem;
import com.wec.resume.model.ModalPair;

import java.util.List;

public interface DetailsActivityFragmentView extends View {
    void showList(@NonNull List<ModalPair<BaseItem, Boolean>> items, int type);

    void showItemDetails(int position, boolean visibility);
}
