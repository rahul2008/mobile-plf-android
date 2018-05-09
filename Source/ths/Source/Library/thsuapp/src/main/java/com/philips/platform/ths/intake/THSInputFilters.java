package com.philips.platform.ths.intake;

import android.text.InputFilter;
import android.text.Spanned;
@SuppressWarnings({ "rawtypes", "unchecked"})
class THSInputFilters<T extends Number & Comparable<? super T>> implements InputFilter {

    private T min, max;

    THSInputFilters(T min, T max) {
        this.min = min;
        this.max = max;
    }

    /*public THSInputFilters(String min, String max) {
        this.min = Double.parseDouble(min);
        this.max = Double.parseDouble(max);
    }*/

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            if(min instanceof Double && max instanceof Double) {
                Double input = Double.parseDouble(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            }
            if(min instanceof Integer && max instanceof Integer){
                Integer input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            }


        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(T a, T b, Comparable c) {

        return b.compareTo(a) > 0 ? c.compareTo(a) >= 0 && c.compareTo(b) <= 0 : c.compareTo(b) >= 0 && c.compareTo(a) <= 0;
        //return b > a ? c >= a && c <= b : c >= b && c <= a;
    }

}