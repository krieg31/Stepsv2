package com.example.stepsv2;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;


public class MyValueFormatter implements IValueFormatter {
    private String[] mSuffix = new String[]{
            "м", "км", "mln","bln","trln"
    };
    private int mMaxLength = 5;
    private DecimalFormat mFormat;
    private String mText = "";

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###E00");
    }

    public MyValueFormatter(String appendix) {
        this();
        mText = appendix;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return makePretty(value) + mText;
    }

    public void setAppendix(String appendix) {
        this.mText = appendix;
    }

    public void setSuffix(String[] suffix) {
        this.mSuffix = suffix;
    }

    public void setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
    }

    private String makePretty(double number) {

        String r = mFormat.format(number);

        int numericValue1 = Character.getNumericValue(r.charAt(r.length() - 1));
        int numericValue2 = Character.getNumericValue(r.charAt(r.length() - 2));
        int combined = Integer.valueOf(numericValue2 + "" + numericValue1);

        r = r.replaceAll("E[0-9][0-9]", mSuffix[combined / 3]);

        while (r.length() > mMaxLength || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }

        return r;
    }

    public int getDecimalDigits() {
        return 0;
    }
}