package com.crapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.crapi.loader.RateLoader;
import com.crapi.loader.RateLoaderFactory;
import com.crapi.loader.cbr.CBRRateLoaderImpl;

public class TestCMRLoader extends Assert {
    
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void test() throws ParseException {
        RateLoader cbr = RateLoaderFactory.createCBRRateLoader();
        
        assertTrue(cbr instanceof CBRRateLoaderImpl);
        assertTrue(Double.isNaN(cbr.getRate(null, null)));
        assertTrue(Double.isNaN(cbr.getRate(null, new Date())));
        assertTrue(Double.isNaN(cbr.getRate("SSS", new Date())));
        assertTrue(Double.isNaN(cbr.getRate("SSS4", new Date())));
        assertTrue(Double.isNaN(cbr.getRate("A", new Date())));
        //assertTrue(Double.isNaN(cbr.getRate("USD", new Date())));
        assertFalse(Double.isNaN(cbr.getRate("USD", null)));
        assertFalse(Double.isNaN(cbr.getRate("USD", new Date())));
        assertFalse(Double.isNaN(cbr.getRate("USD", FORMATTER.parse("2010-10-01"))));
        //System.out.println("-"+cbr.getRate("USD", FORMATTER.parse("2001-01-23"))+"-");
        assertTrue(28.37 == cbr.getRate("USD", FORMATTER.parse("2001-01-23")));
        assertTrue(28.37 == cbr.getRate("usd", FORMATTER.parse("2001-01-23")));
        
    }
}
