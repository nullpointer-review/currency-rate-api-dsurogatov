package com.crapi.loader;

import com.crapi.loader.cbr.CBRRateLoaderImpl;

public class RateLoaderFactory {

    public static RateLoader createCBRRateLoader() {
        return new CBRRateLoaderImpl();
    }
}
