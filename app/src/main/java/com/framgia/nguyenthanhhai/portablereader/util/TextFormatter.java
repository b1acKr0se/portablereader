package com.framgia.nguyenthanhhai.portablereader.util;

public class TextFormatter {
    public static String removeMultipleLineBreaks(String source) {
        return source.trim().replaceAll("[\r\n]+", "\n\n");
    }
}
