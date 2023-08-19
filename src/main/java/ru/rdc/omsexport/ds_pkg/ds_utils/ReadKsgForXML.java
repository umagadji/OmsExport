package ru.rdc.omsexport.ds_pkg.ds_utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.rdc.omsexport.ds_pkg.local_db_models.ItemKsg;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Класс читает КСГ из локальной БД
public class ReadKsgForXML {
    public static List<ItemKsg> getKSGForLocalDB(String filePath) {
        File xmlFile = new File(filePath);
        List<ItemKsg> rowList = null;

        if (xmlFile.exists()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;

            try {
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);
                document.getDocumentElement().normalize();
                NodeList nodeList = document.getElementsByTagName("ITEMKSG");

                rowList = new ArrayList<>();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    rowList.add(getKsg(nodeList.item(i)));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Локальный файл БД не найден");
        }

        return rowList;
    }

    private static ItemKsg getKsg(Node node) {
        ItemKsg itemKsg = new ItemKsg();

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            itemKsg.setKsg(getTagValue("KSG", element));
        }

        return itemKsg;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);

        if (node.getNodeValue().equals("'") || node.getNodeValue().equals("")) {
            return "";
        } else {
            return node.getNodeValue();
        }
    }
}
