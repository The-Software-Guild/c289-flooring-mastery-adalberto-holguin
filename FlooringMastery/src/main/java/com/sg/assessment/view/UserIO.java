package com.sg.assessment.view;

import java.io.IOException;
import java.math.BigDecimal;

public interface UserIO {

    void print(String message);

    String readString(String prompt);

    String readString(String prompt, int max);

    String readStringNoEmpty(String prompt);

    String readNames(String prompt);

    String readNamesAllowEmpty(String prompt);

    String formatCurrency(BigDecimal amount);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    int readIntAllowEmpty(String prompt, int min, int max);

    double readDouble(String prompt);

    double readDouble(String prompt, double min, double max);

    float readFloat(String prompt);

    float readFloat(String prompt, float min, float max);

    long readLong(String prompt);

    long readLong(String prompt, long min, long max);

    BigDecimal readBigDecimal(String prompt);

    BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max);

    BigDecimal readBigDecimalAllowEmpty(String prompt, BigDecimal min, BigDecimal max);
}
