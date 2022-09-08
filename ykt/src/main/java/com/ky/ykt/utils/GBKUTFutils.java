package com.ky.ykt.utils;

/**
 * @ClassName GBKUTFutils
 * @Author yaoweijie
 * @Date 2022/9/6 18:25
 * @Version 1.0.0
 * @Description TODO
 **/
public class GBKUTFutils {

    /**
     * gbk转unicode
     * @param str
     * @return
     */
    public static String gbkToUnicode(String str) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char chr1 = (char) str.charAt(i);
            if ((chr1 & (0x00FF)) == chr1) {
                result.append(chr1);
                continue;
            }
            result.append("\\u" + Integer.toHexString((int) chr1));
        }
        return result.toString();
    }


    /**
     * unicode转gbk
     * @param dataStr
     * @return
     */
    public static String unicodeToGbk(String dataStr) {
        int index = 0;
        StringBuffer buffer = new StringBuffer();
        int li_len = dataStr.length();
        while (index < li_len) {
            if (index >= li_len - 1
                    || !"\\u".equals(dataStr.substring(index, index + 2))) {
                buffer.append(dataStr.charAt(index));
                index++;
                continue;
            }
            String charStr = "";
            charStr = dataStr.substring(index + 2, index + 6);
            char letter = (char) Integer.parseInt(charStr, 16);
            buffer.append(letter);
            index += 6;
        }
        return buffer.toString();
    }
}
