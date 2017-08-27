package com.wec.resume.view;


import com.wec.resume.model.Social;

public interface MainActivityView extends View {

    void setAvatar(String avatar);

    void showCouldNoteLoadDataErrorMessage();

    void showSplashScreen(boolean showSplashScreen);

    void showAndEnableSocialButtons(boolean areAvailable);

    void enableButtonByType(Social.Type type);

    void animateButton(Social.Type type, int position, boolean shouldShowButton);

    void setSocialButtonToSelected(boolean socialButtonSelected);

    void navigateToURL(String url);
}
