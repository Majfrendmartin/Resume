package com.wec.resume.view;


import com.wec.resume.model.BaseResumeItem;

import java.util.Collection;

public interface MainActivityFragmentView extends View {
    void showList(Collection<BaseResumeItem> strings);
}
