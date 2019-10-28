package com.moaplanet.gosing.utils;

import android.net.Uri;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class ObjectUtil {

    public static boolean checkNotNull(Integer _val){
        if(_val == null){
            return false;
        }
        return (_val >= 0);
    }

    public static boolean checkNotNull(Long _val){
        if(_val == null){
            return false;
        }
        return (_val >= 0);
    }

    public static boolean checkNotNull(String _val){
        if(_val == null){
            return false;
        }
        return (!_val.isEmpty());
    }

    public static boolean checkNotNull(Object _val){
        return _val != null;
    }

    public static boolean checkNotNull(Object[] _val){
        if(_val == null){
            return false;
        }
        return (_val.length > 0);
    }

    public static boolean checkNotNull(List _val){
        if(_val == null){
            return false;
        }
        return (!_val.isEmpty());
    }

    public static boolean checkNotNull(File _val){
        if(_val == null){
            return false;
        }
        return true;
    }

    public static boolean checkNotNull(Uri _val){
        if(_val == null){
            return false;
        }
        return true;
    }

    public static boolean checkListRange(List _val, int _idx){
        if(!ObjectUtil.checkNotNull(_val)){
            return false;
        }

        return (0 <= _idx && _idx < _val.size());
    }

    public static boolean checkArrRange(Object[] _val, int _idx){
        if(!ObjectUtil.checkNotNull(_val)){
            return false;
        }

        return (0 <= _idx && _idx < _val.length);
    }

    public static boolean checkEditTextRange(EditText _editText, int _idx){
        if(_editText == null){
            return false;
        }

        return (0 <= _idx && _idx < _editText.length());
    }

    public static boolean equals(String string1, String string2){
        if(!checkNotNull(string1) || !checkNotNull(string2)){
            return false;
        }
        return string1.equals(string2);
    }

    public static int convertStringToInt(String _numberStr){
        try {
            return Integer.parseInt(_numberStr);
        } catch (Exception e){
            Logger.e("convertStringToInt= " + _numberStr);
        }
        return 0;
    }

    public static <T> boolean isHashMap(HashMap<String, T> hashMap, String key){
        return hashMap.containsKey(key) && hashMap.get(key) != null;
    }

    public static String getHashMapVale(HashMap<String, String> hashMap, String key){
        try {
            return hashMap.get(key);
        } catch (Exception e) {
            Logger.e("getHashMapVale key= " + key);
        }
        return "";
    }

    public static boolean isStringIdx(String string, int idx){
        if(!ObjectUtil.checkNotNull(string)) {
            return false;
        }
        return 0 <= idx && idx < string.length();
    }

    /**
     * Return NVL ''
     * @param obj
     * @return
     */
    public static String nvl(Object obj) {

        String retValue = "";

        if (obj == null){
            return retValue;
        }else{
            retValue = (String) obj;
            if (retValue.equals("null")) {
                retValue = "";
            }
        }
        return retValue;
    }
}
