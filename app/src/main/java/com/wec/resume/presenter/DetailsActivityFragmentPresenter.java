package com.wec.resume.presenter;


import com.wec.resume.model.Section.SectionType;
import com.wec.resume.view.DetailsActivityFragmentView;

public interface DetailsActivityFragmentPresenter extends FragmentPresenter<DetailsActivityFragmentView> {
    void onItemClicked(int position);

    void setType(SectionType type);
}
