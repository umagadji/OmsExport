package ru.rdc.omsexport.mek.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.rdc.omsexport.mek.models.Err;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Класс читает ERR из XML файла от ТФОМС
public class ReadMekErrForXML {
    public static ObservableList<Err> getMek(String filePath) {
        File xmlFile = new File(filePath);
        ObservableList<Err> rowList = null;

        if (xmlFile.exists()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;

            try {
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);
                document.getDocumentElement().normalize();
                NodeList nodeList = document.getElementsByTagName("ERR");

                rowList = FXCollections.observableArrayList();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    rowList.add(getMek(nodeList.item(i)));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Файл МЭК не найден");
        }

        return rowList;
    }

    private static Err getMek(Node node) {
        Err item = new Err();

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            item.setS_com(getTagValue("s_com", element));
            item.setNpolis(getTagValue("npolis", element));
            //Объединяем в одно поле ФИО
            item.setFio(getTagValue("surname", element) + " " + getTagValue("name", element) + " " + getTagValue("patronymic", element));
            item.setBirthDate(getTagValue("birthDate", element));
            item.setDate_in(getTagValue("date_in", element));
            item.setDate_out(getTagValue("date_out", element));
            item.setRefreason(getTagValue("refreason", element));
            item.setSumvUsl(getTagValue("sumvUsl", element));
            item.setCodeUsl(getTagValue("codeUsl", element));
            item.setSankSum(getTagValue("sankSum", element));
            item.setDiagnosis(getTagValue("diagnosis", element));
            item.setNameMO(getTagValue("nameMO", element));
            item.setDocCode(getTagValue("docCode", element));
            item.setNhistory(getTagValue("nhistory", element));
            item.setIdstrax(getTagValue("idstrax", element));
            item.setErrorCode(getTagValue("errorCode", element));
        }

        return item;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0).getFirstChild();
            if (node != null) {
                String value = node.getNodeValue();
                return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
            }
        }
        return "";
    }
}
