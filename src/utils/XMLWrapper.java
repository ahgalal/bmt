package utils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLWrapper {
	protected DefaultHandler handler;
	private SAXParserFactory factory;
	protected SAXParser saxParser;
	public XMLWrapper() throws ParserConfigurationException, SAXException {
		factory = SAXParserFactory.newInstance();
		saxParser = factory.newSAXParser();
	}
	
	public void parseXMLFile(String filePath){
		handler = new DefaultHandler() {
			
		};
	}
}
