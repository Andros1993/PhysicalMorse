package com.et.physicalmorse;

import java.util.HashMap;

/**
 * Created by Et on 2018/7/28 09:46
 * email：x313371005@126.com
 */
public class MorseUtil {

    private static HashMap<Character, String> dictionaries = new HashMap<Character, String>();
    private volatile static MorseUtil instance;
    public static final char dit = '.';
    public static final char dah = '-';
    public static final char split = '/';

    static {
        // Letters
        dictionaries.put('A', "01");
        dictionaries.put('B', "1000");
        dictionaries.put('C', "1010");
        dictionaries.put('D', "100");
        dictionaries.put('E', "0");
        dictionaries.put('F', "0010");
        dictionaries.put('G', "110");
        dictionaries.put('H', "0000");
        dictionaries.put('I', "00");
        dictionaries.put('J', "0111");
        dictionaries.put('K', "101");
        dictionaries.put('L', "0100");
        dictionaries.put('M', "11");
        dictionaries.put('N', "10");
        dictionaries.put('O', "111");
        dictionaries.put('P', "0110");
        dictionaries.put('Q', "1101");
        dictionaries.put('R', "010");
        dictionaries.put('S', "000");
        dictionaries.put('T', "1");
        dictionaries.put('U', "001");
        dictionaries.put('V', "0001");
        dictionaries.put('W', "011");
        dictionaries.put('X', "1001");
        dictionaries.put('Y', "1011");
        dictionaries.put('Z', "1100");
        // Numbers
        dictionaries.put('0', "11111");
        dictionaries.put('1', "01111");
        dictionaries.put('2', "00111");
        dictionaries.put('3', "00011");
        dictionaries.put('4', "00001");
        dictionaries.put('5', "00000");
        dictionaries.put('6', "10000");
        dictionaries.put('7', "11000");
        dictionaries.put('8', "11100");
        dictionaries.put('9', "11110");
        // Punctuation
        dictionaries.put('.', "010101");
        dictionaries.put(',', "110011");
        dictionaries.put('?', "001100");
        dictionaries.put('\'', "011110");
        dictionaries.put('!', "101011");
        dictionaries.put('/', "10010");
        dictionaries.put('(', "10110");
        dictionaries.put(')', "101101");
        dictionaries.put('&', "01000");
        dictionaries.put(':', "111000");
        dictionaries.put(';', "101010");
        dictionaries.put('=', "10001");
        dictionaries.put('+', "01010");
        dictionaries.put('-', "100001");
        dictionaries.put('_', "001101");
        dictionaries.put('"', "010010");
        dictionaries.put('$', "0001001");
        dictionaries.put('@', "011010");
        
    }

    private MorseUtil(){}

    public static MorseUtil getInstance () {
        if (instance != null) {
            synchronized (MorseUtil.class) {
                if (instance != null) {
                    instance = new MorseUtil();
                }
            }
        }

        return instance;
    }

    /**
     * encode the message with morse
     * @param message
     * @return return string base on '.' '-' '/'
     */
    public static String encode (String message) {
        if (message == null || "".equalsIgnoreCase(message)) {
            throw new IllegalArgumentException("message can not be null");
        }

        StringBuilder builder = new StringBuilder();
        message = message.toUpperCase();
        for (int i = 0; i < message.toCharArray().length; i++) { //分解每个字符
            char codePoint = message.toCharArray()[i];
            String word = dictionaries.get(codePoint); //根据key值获取对应的value值
            if (word != null) {
                builder.append(word.replace('0', dit).replace('1', dah)).append(split); //如果有值，则将0和1替换成.和-。并在每个字符结尾加上/
            }
        }
        return builder.toString();
    }

}
