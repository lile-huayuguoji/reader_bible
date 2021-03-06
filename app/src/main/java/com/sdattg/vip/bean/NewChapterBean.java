package com.sdattg.vip.bean;

import java.util.HashMap;
import java.util.List;

/**
 *  20180716
 *  eg: NewCategoryBean{, name='00-创世记_jieshao',
 *                        path='/storage/emulated/0/lxgj/unzip/01-书库/01-旧约/01-创世记/00-创世记_jieshao',
 *                        parentPath='/storage/emulated/0/lxgj/unzip/01-书库/01-旧约/01-创世记',
 *                        paragraphCount='18',
 *                        paragraphHashMap='paragraphHashMap.toString()'}
 *
 */
public class NewChapterBean {
    public String paragraphIndex;
    public String paragraphContent;

    @Override
    public String toString() {
        return "NewSecondCategoryBean{" +
                ", paragraphIndex='" + paragraphIndex + '\'' +
                ", paragraphContent='" + paragraphContent + '\'' +
                '}';
    }
}
