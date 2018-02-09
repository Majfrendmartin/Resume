package com.wec.resume.presenter.utils;

import static android.webkit.URLUtil.isValidUrl;

/**
 * Created by Pawel Raciborski on 09.02.2018.
 */

public class UrlValidatorImpl implements UrlValidator {
    @Override
    public boolean isUrlValid(String url) {
        return isValidUrl(url);
    }
}
