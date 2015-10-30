package com.crapi.loader.cbr;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class ParseCBRAnswerHandler extends DefaultHandler {
    
    private static final String CHAR_CODE = "CharCode";
    private String code;
    private boolean getCharCode = false;
    private boolean charCodeFinded = false;
    private boolean getValue = false;
    private String parsedValue;
    
    ParseCBRAnswerHandler(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Param code is null.");
        }
        
        this.code = code.toUpperCase();
    }
    
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        
        //System.out.println(qName);
        if (CHAR_CODE.equals(qName)) {
            getCharCode = true;
        } 
        if ("Value".equals(qName) && charCodeFinded) {
            getValue = true;
        } 
    }
    
    @Override 
    public void characters(char[] ch, int start, int length) throws SAXException { 
        if (getCharCode) {
            String parsedCode = new String(ch, start, length);
            if (code.equals(parsedCode)) {
                charCodeFinded = true;
            }
            getCharCode = false;
        }
        
        if (getValue) {
            parsedValue = new String(ch, start, length);
            throw new com.crapi.loader.cbr.SAXTerminatorException();
        }
    }

    /**
     * @return the parsedValue
     */
    public String getParsedValue() {
        return parsedValue;
    }
}
