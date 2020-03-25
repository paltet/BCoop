package com.bcoop.bcoop.ui.prize;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PrizeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PrizeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is prize fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}