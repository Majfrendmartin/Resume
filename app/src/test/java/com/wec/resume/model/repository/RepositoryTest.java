package com.wec.resume.model.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import retrofit2.Retrofit;

import static org.junit.Assert.*;

/**
 * Created by Pawel Raciborski on 14.02.2018.
 */
public class RepositoryTest {

    private Repository repository;

    @Mock
    private Context context;

    @Mock
    private SharedPreferences preferences;

    @Mock
    private Retrofit retrofit;

    @Mock
    private EventBus eventBus;

    private Gson gson = new Gson();

    @Before
    public void setUp() throws Exception {
        repository = new RepositoryImpl(context, preferences, retrofit, eventBus, gson);
    }

    @Test
    public void getBio() throws Exception {
    }

    @Test
    public void loadUpdatedResume() throws Exception {
    }

}