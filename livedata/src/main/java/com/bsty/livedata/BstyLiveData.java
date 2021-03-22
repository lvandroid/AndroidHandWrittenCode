package com.bsty.livedata;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

public class BstyLiveData<T> {
    //持有的数据保存对象
    private T mData = null;
    //观察者的map
    private List<ObserverWrapper> mObsevers = new ArrayList<>();
    //数据持有类的数据版本
    private int mVersion = -1;

    public void postValue(T value) {
        //保存传进来的参数
        this.mData = value;
        mVersion++;
        //遍历所有观察者
        dispatchingValue();
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        //先判断传进来的组件生命周期
        if (owner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        //封装传进来的观察者
        ObserverWrapper observerWrapper = new ObserverWrapper();
        observerWrapper.observer = observer;
        observerWrapper.myLifeCycleBound = new MyLifeCycleBound();
        observerWrapper.lifecycle = owner.getLifecycle();
        //给组件的生命周期方法设置回调接口
        observerWrapper.lifecycle.addObserver(observerWrapper.myLifeCycleBound);
        mObsevers.add(observerWrapper);
    }

    private void dispatchingValue() {
        for (ObserverWrapper observer : mObsevers) {
            toChange(observer);
        }
    }

    private void toChange(ObserverWrapper observer) {
        if (observer.lifecycle.getCurrentState() != Lifecycle.State.RESUMED) {
            return;
        }
        if (observer.mLastVersion >= mVersion) {
            return;
        }
        observer.observer.onChanged(this.mData);
    }

    /**
     * 对观察者的封装类
     */
    private class ObserverWrapper {
        //观察者
        Observer<T> observer;
        //观察者的版本号
        private int mLastVersion = -1;
        //
        Lifecycle lifecycle;
        //绑定生命周期的回调接口
        MyLifeCycleBound myLifeCycleBound;
    }

    /**
     * 写了一个生命周期的观察者
     */
    @SuppressLint("RestrictedApi")
    class MyLifeCycleBound implements GenericLifecycleObserver {

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (source.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                remove(source.getLifecycle());
            }
            if (mData != null) {
                dispatchingValue();
            }
        }

        private void remove(Lifecycle lifecycle) {
            for (ObserverWrapper observerWrapper : mObsevers) {
                if (observerWrapper.lifecycle == lifecycle) {
                    observerWrapper.lifecycle.removeObserver(observerWrapper.myLifeCycleBound);
                    mObsevers.remove(observerWrapper);
                }
            }
        }
    }
}
