package ru.rdc.omsexport.ds_pkg.ds_utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.rdc.omsexport.ds_pkg.ds_models.RowDS;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//ЧИТАЕМ входной XML ФАЙЛ ДЛЯ СТАЦИОНАРОВ
public class ReadXmlDs {

    public static List<RowDS> getRowsForXMLDS(String filePath) {
        File xmlFile = new File(filePath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        List<RowDS> rowDSList = null;

        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();
            //System.out.println("Root element: " + document.getDocumentElement().getNodeName());
            // получаем узлы с именем Row
            // теперь XML полностью загружен в память
            // в виде объекта Document
            NodeList nodeList = document.getElementsByTagName("ROW");

            rowDSList = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                rowDSList.add(getRow(nodeList.item(i)));
                //System.out.println(i + " строка считана");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rowDSList;
    }

    private static RowDS getRow(Node node) {
        RowDS rowDS = new RowDS();

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            rowDS.setNum(getTagValue("NUM", element));
            rowDS.setInvoicekeyid(getTagValue("INVOICEKEYID", element));
            rowDS.setPatientkeyid(getTagValue("PATIENTKEYID", element));
            rowDS.setRootvisitid(getTagValue("ROOTVISITID", element));
            rowDS.setVisitid(getTagValue("VISITID", element));
            rowDS.setPatservid(getTagValue("PATSERVID", element));
            rowDS.setAgrid(getTagValue("AGRID", element));
            rowDS.setFam(getTagValue("FAM", element));
            rowDS.setIm(getTagValue("IM", element));
            rowDS.setOt(getTagValue("OT", element));
            rowDS.setIs_male(getTagValue("IS_MALE", element));
            rowDS.setKol_usl(getTagValue("KOL_USL", element));
            rowDS.setPrvs(getTagValue("PRVS", element));
            rowDS.setBirthdate(getTagValue("BIRTHDATE", element));
            rowDS.setAddress(getTagValue("ADDRESS", element));
            rowDS.setSpolis(getTagValue("SPOLIS", element));
            rowDS.setNpolis(getTagValue("NPOLIS", element));
            rowDS.setSnpol(getTagValue("SNPOL", element));
            rowDS.setVpolis(getTagValue("VPOLIS", element));
            rowDS.setSmo(getTagValue("SMO", element));
            rowDS.setSmo_nam(getTagValue("SMO_NAM", element));
            rowDS.setSmo_ogrn(getTagValue("SMO_OGRN", element));
            rowDS.setSmo_ok(getTagValue("SMO_OK", element));
            rowDS.setNovor(getTagValue("NOVOR", element));
            rowDS.setFam_n(getTagValue("FAM_N", element));
            rowDS.setIm_n(getTagValue("IM_N", element));
            rowDS.setOt_n(getTagValue("OT_N", element));
            rowDS.setIs_male_n(getTagValue("IS_MALE_N", element));
            rowDS.setDat_rojd_n(getTagValue("DAT_ROJD_N", element));
            rowDS.setDat_in(getTagValue("DAT_IN", element));
            rowDS.setDat_out(getTagValue("DAT_OUT", element));
            rowDS.setDoctor_code(getTagValue("DOCTOR_CODE", element));
            rowDS.setDoctor(getTagValue("DOCTOR", element));
            rowDS.setMr(getTagValue("MR", element));
            rowDS.setN_otd(getTagValue("N_OTD", element));
            rowDS.setInogor(getTagValue("INOGOR", element));
            rowDS.setMkb_code(getTagValue("MKB_CODE", element));
            rowDS.setMkb_code_s(getTagValue("MKB_CODE_S", element));
            rowDS.setMkb_code_p(getTagValue("MKB_CODE_P", element));
            rowDS.setChar_zab(getTagValue("CHAR_ZAB", element));
            rowDS.setTarif(getTagValue("TARIF", element));
            rowDS.setCoeff(getTagValue("COEFF", element));
            rowDS.setMet_pr_kod(getTagValue("MET_PR_KOD", element));
            rowDS.setDoctype(getTagValue("DOCTYPE", element));
            rowDS.setDocser(getTagValue("DOCSER", element));
            rowDS.setDocnum(getTagValue("DOCNUM", element));
            rowDS.setDocorg(getTagValue("DOCORG", element));
            rowDS.setDocdate(getTagValue("DOCDATE", element));
            rowDS.setSnils(getTagValue("SNILS", element));
            rowDS.setUsl_ksg(getTagValue("USL_KSG", element));
            rowDS.setUsl(getTagValue("USL", element));
            rowDS.setUsl_note(getTagValue("USL_NOTE", element));
            rowDS.setSumv(getTagValue("SUMV", element));
            rowDS.setIbnumber(getTagValue("IBNUMBER", element));
            rowDS.setGosp(getTagValue("GOSP", element));
            rowDS.setIshod(getTagValue("ISHOD", element));
            rowDS.setResult_gosp(getTagValue("RESULT_GOSP", element));
            rowDS.setVidpom(getTagValue("VIDPOM", element));
            rowDS.setNpr_mo(getTagValue("NPR_MO", element));
            rowDS.setNpr_lpu(getTagValue("NPR_LPU", element));
            rowDS.setCodeotd(getTagValue("CODEOTD", element));
            rowDS.setProfil(getTagValue("PROFIL", element));
            rowDS.setProfil_kd(getTagValue("PROFIL_KD", element));
            rowDS.setPostuplenie(getTagValue("POSTUPLENIE", element));
            rowDS.setZakonch(getTagValue("ZAKONCH", element));
            rowDS.setKd(getTagValue("KD", element));
            rowDS.setDate_napr(getTagValue("DATE_NAPR", element));
            rowDS.setZno(getTagValue("ZNO", element));
            rowDS.setVr_spec(getTagValue("VR_SPEC", element));
            rowDS.setVr_spec_name(getTagValue("VR_SPEC_NAME", element));
            rowDS.setKslp_code(getTagValue("KSLP_CODE", element));
            rowDS.setKslp_text(getTagValue("KSLP_TEXT", element));
            rowDS.setKslp_koeff(getTagValue("KSLP_KOEFF", element));
            rowDS.setCrit_code(getTagValue("CRIT_CODE", element));
            rowDS.setCrit_text(getTagValue("CRIT_TEXT", element));
            rowDS.setOperac_code(getTagValue("OPERAC_CODE", element));
            rowDS.setOperac_text(getTagValue("OPERAC_TEXT", element));
            rowDS.setDocsnils(getTagValue("DOCSNILS", element));
        }

        return rowDS;
    }

    private static String getTagValue(String tag, Element element) {
        String rez = "";

        //Получаем узел SNPOL, чтобы в случае ошибки чтения и выпадения NullPointerException вывести инфо для какого полиса ошибка
        NodeList snpolNodeList = element.getElementsByTagName("SNPOL").item(0).getChildNodes();
        Node snpolNode = (Node) snpolNodeList.item(0);

        try {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node node = (Node) nodeList.item(0);

            if (node.getNodeValue().equals("'") || node.getNodeValue().equals("")) {
                return rez;
            } else {
                rez = node.getNodeValue();
                return rez;
            }
        } catch (NullPointerException ex) {
            AlertDialogUtils.showErrorAlert("Ошибка c теге " + tag, null, "Ошибка чтения данных по стационару для полиса " + snpolNode.getNodeValue());
        }
        return rez;

        /*стары вариант до 25.10.2023
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);

        if (node.getNodeValue().equals("'") || node.getNodeValue().equals("")) {
            return "";
        } else {
            return node.getNodeValue();
        }*/
    }
}
