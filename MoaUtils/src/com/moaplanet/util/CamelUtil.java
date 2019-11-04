// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CamelUtil.java

package com.moaplanet.util;


public class CamelUtil
{

    public CamelUtil()
    {
    }

    public static String convert2CamelCase(String underScore)
    {
        if(underScore.indexOf('_') < 0 && Character.isLowerCase(underScore.charAt(0)))
            return underScore;
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        int len = underScore.length();
        for(int i = 0; i < len; i++)
        {
            char currentChar = underScore.charAt(i);
            if(currentChar == '_')
                nextUpper = true;
            else
            if(nextUpper)
            {
                result.append(Character.toUpperCase(currentChar));
                nextUpper = false;
            } else
            {
                result.append(Character.toLowerCase(currentChar));
            }
        }

        return result.toString();
    }
}
