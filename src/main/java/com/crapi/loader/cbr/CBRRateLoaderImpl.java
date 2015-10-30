package com.crapi.loader.cbr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.crapi.loader.RateLoader;

public class CBRRateLoaderImpl implements RateLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(CBRRateLoaderImpl.class);
	private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
	private final static int TIMEOUT = 5000;
	
	private boolean absent;

	private InputStream getXMLStream(Date reqDate) throws IOException {
		String urlString = "http://www.cbr.ru/scripts/xml_daily.asp";
		if (reqDate != null) {
			urlString = urlString + "?date_req=" + FORMATTER.format(reqDate);
		}

		URL url = new URL(urlString);
		URLConnection urlc = url.openConnection();
		urlc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
		urlc.setDoInput(true);
		urlc.setDoOutput(false);
		urlc.setConnectTimeout(TIMEOUT);
		urlc.connect();

		return urlc.getInputStream();
	}

	private String getStrRateByCodeDate(String code, Date date) {
		ParseCBRAnswerHandler handler = null;
		try (InputStream in = getXMLStream(date)) {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();

			handler = new ParseCBRAnswerHandler(code);
			parser.parse(new InputSource(in), handler);
		} catch (Exception e) {
			if (e instanceof SAXTerminatorException) {
				return handler.getParsedValue();
			} else {
				LOGGER.error(e.getMessage(), e);
			}
		}
		
		return null;
	}

	@Override
	public double getRate(String code, Date date) {
		String strRateValue = (code == null || code.isEmpty()) ? null : getStrRateByCodeDate(code, date);

		if (strRateValue != null) {
			try {
				return Double.parseDouble(strRateValue.replaceAll(",", "."));
			} catch (NumberFormatException e) {
				LOGGER.error(e.getMessage(), e);
			}
		} else {
			absent = true;
		}
		return Double.NaN;
	}

	public boolean isAbsent() {
		return absent;
	}
}
