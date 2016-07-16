package com.cycus.playcodeapp.Utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by Arun_Saini on 24-06-2016.
 */
public class FontOverride {
    public static void setDefaultFont(Context context, String oldFont, String newFont) throws NoSuchFieldException, IllegalAccessException {
        Typeface newTypeFace= Typeface.createFromAsset(context.getAssets(), CustomTypeFaces.BASE_FOLDER+CustomTypeFaces.ROBO_REGULAR);
        replaceFont(oldFont, newTypeFace);
    }
    protected static void replaceFont(String oldFont, Typeface newTypeFace) throws NoSuchFieldException, IllegalAccessException {
        Field field=Typeface.class.getDeclaredField(oldFont);
        field.setAccessible(true);
        field.set(null , newTypeFace);
    }
}
