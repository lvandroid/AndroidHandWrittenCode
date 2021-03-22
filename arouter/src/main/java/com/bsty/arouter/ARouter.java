package com.bsty.arouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 * 中间人
 */
public class ARouter {
    private static ARouter aRouter = new ARouter();

    //装载了所有Activity的类对象
    private Map<String, Class<? extends Activity>> maps;
    //上下文
    private Context context;

    private ARouter() {
        maps = new HashMap<>();
    }

    public void init(Context context) {
        this.context = context;
        List<String> classNameList = getClassName("com.bsty.util");
        for (String className : classNameList) {
            try {
                Class<?> aClass = Class.forName(className);
                //判断这个类是否是IRouter的子类
                if (IRouter.class.isAssignableFrom(aClass)) {
                    IRouter iRouter = (IRouter) aClass.newInstance();
                    iRouter.putActivity();
                }
            } catch (Exception e) {

            }
        }
    }

    private List<String> getClassName(String packageName) {

        List<String> classList = new ArrayList<>();
        String path = null;
        try {
            path = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir;
            DexFile dexFile = new DexFile(path);
            Enumeration entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String name = (String) entries.nextElement();
                if (name.contains(packageName)) {
                    classList.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    public static ARouter getInstance() {
        return aRouter;
    }


    public void jumpActivity(String key, Bundle bundle) {
        Class<? extends Activity> activityClass = maps.get(key);
        if (activityClass != null) {
            Intent intent = new Intent().setClass(context, activityClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }

    public void addActivity(String key, Class<? extends Activity> clazz) {
        if (key != null && clazz != null && !maps.containsKey(key)) {
            maps.put(key, clazz);
        }
    }

}
