package com.crapi.loader;

import java.util.Date;

public interface RateLoader {

    double getRate(String code, Date date);
    boolean isAbsent();
}
