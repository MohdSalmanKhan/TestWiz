package com.example.android.wizardpager.wizard.Interfaces;

public interface MvpPresenter<V extends MvpView> {

    public void attachView(V view);

    public void detachView(boolean retainInstance);
}