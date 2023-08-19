package ru.rdc.omsexport.utils;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLFormatter {
    public static String formatXML(String xml) throws TransformerException {

        // write data to xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        // alternative
        //Transformer transformer = SAXTransformerFactory.newInstance().newTransformer();

        // pretty print by indention
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        // add standalone="yes", add line break before the root element
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

      /*transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
              "-//W3C//DTD XHTML 1.0 Transitional//EN");

      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
              "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");*/

        // ignore <?xml version="1.0" encoding="UTF-8"?>
        //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        StreamSource source = new StreamSource(new StringReader(xml));
        StringWriter output = new StringWriter();
        transformer.transform(source, new StreamResult(output));

        return output.toString();
    }
}
