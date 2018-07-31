package com.et.physicalmorse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Et on 2018/7/31 10:11
 * email：x313371005@126.com
 */
public class FlashUtil {

    private FlashUtil(){}

    public static ArrayList<FlashBean> getFlashData(String data) {
        ArrayList<FlashBean> flashMap = new ArrayList<>();

        char[] chars = data.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == MorseUtil.dit) {
                flashMap.add(new FlashBean(true, 200));
                flashMap.add(new FlashBean(false, 200));
            } else if (chars[i] == MorseUtil.dah) {
                flashMap.add(new FlashBean(true, 400));
                flashMap.add(new FlashBean(false, 200));
            } else if (chars[i] == MorseUtil.split) {
                flashMap.add(new FlashBean(false, 500));
            }
        }

        return flashMap;
    }

    static class FlashBean {
        boolean flag;
        int time;

        public FlashBean (boolean flag, int time) {
            this.flag = flag;
            this.time = time;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
