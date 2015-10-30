package com.crapi.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crapi.loader.RateLoader;
import com.crapi.loader.RateLoaderFactory;

@Controller
public class CRApi {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CRApi.class.getSimpleName());    
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    
    @Autowired
    MessageSource resources;

    @RequestMapping(value = {"/rate/{code}"}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getRate(@PathVariable(value = "code") String code) {
    	return getRateByDate(code, null);
    }
    
    @RequestMapping(value = {"/rate/{code}/{date}"}, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getRateByDate(@PathVariable(value = "code") String code, @PathVariable(value = "date") String date) {
  
        if (!Pattern.matches("\\w{3}", code)) { 
            return responseError(resources.getMessage("validate.wrongCode", null, null), HttpStatus.BAD_REQUEST);
        }
        
        Date rateDate = null;
        try {
        	rateDate = parseRateDate(date);
        } catch (ParseException e) {
            return responseError(resources.getMessage("validate.wrongDate", null, null), HttpStatus.BAD_REQUEST);
        }
        
        RateLoader rateLoader = RateLoaderFactory.createCBRRateLoader();
        double rate = rateLoader.getRate(code, rateDate);
        if (Double.isNaN(rate)) {
        	if (rateLoader.isAbsent()) {
        		return responseError(resources.getMessage("absentRate", null, null), HttpStatus.BAD_REQUEST);	
        	} else {
        		return responseError(null, HttpStatus.SERVICE_UNAVAILABLE);
        	}
        }
        
        return new ResponseEntity<String>(formatMessage(code, rateDate, rate), HttpStatus.OK);
    }

	private Date parseRateDate(String date) throws ParseException {
		Date rateDate;
		if (date == null) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 1); 
			rateDate = c.getTime();
		} else {
			rateDate = FORMATTER.parse(date);
		}
		return rateDate;
	}

    private String formatMessage(String code, Date rateDate, double rate) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("{\n");
        sb.append("\t\"code\": \"%s\",\n");
        sb.append("\t\"rate\": \"%.4f\",\n");
        sb.append("\t\"date\": \"%s\"\n");
        sb.append("}");
        String message = String.format(Locale.US, sb.toString(), code.toUpperCase(), rate, FORMATTER.format(rateDate));
        return message;
    }
    
    ResponseEntity<String> responseError(String errorMessage, HttpStatus status) {
        LOGGER.warn(errorMessage);
        return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    
}
