package com.wec.resume.view;


import com.wec.resume.model.Social;

public interface MainActivityView extends View {

    void setTitle(String title);

    void setAvatar(String avatar);

    void showCouldNoteLoadDataErrorMessage();

    void hideSplashScreen();

    void showSplashScreen();

    void showAndEnableSocialButtons();

    void enableButtonByType(Social.Type type);

    void animateButton(Social.Type type, int position, boolean shouldShowButton);

    void setSocialButtonToSelected(boolean socialButtonSelected);
}
