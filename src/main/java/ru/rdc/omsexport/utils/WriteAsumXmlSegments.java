package ru.rdc.omsexport.utils;

import ru.rdc.omsexport.asum_models.*;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

//Утилитный класс для создания сегментов ASUM-файла
public class WriteAsumXmlSegments {

    //Метод для создания свойства элемента
    public static void getElement(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
        writer.writeStartElement(name);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    //Метод создает тег vrachi
    public static void writeVrachi(XMLStreamWriter writer, Vrachi vrachi) throws XMLStreamException {
        writer.writeStartElement("vrachi");

        getElement(writer, "kod", vrachi.getKod());
        getElement(writer, "fio", vrachi.getFio());
        getElement(writer, "mcod", vrachi.getMcod());
        getElement(writer, "idmsp", vrachi.getIdmsp());
        getElement(writer, "spec", vrachi.getSpec());
        getElement(writer, "dost", vrachi.getDost());
        getElement(writer, "type", vrachi.getType());
        getElement(writer, "vers_spec", vrachi.getVers_spec());
        getElement(writer, "ss", vrachi.getSs());

        writer.writeEndElement();
    }

    public static void writeZap(XMLStreamWriter writer, Zap zap) throws XMLStreamException {
        writer.writeStartElement("zap");

        getElement(writer, "id", zap.getSluchList().get(0).getPacient().getNpolis());
        getElement(writer, "idlist", "1");
        getElement(writer, "pr_nov", "0");

        //тут возможно нужно подумать о том как сделать сортировку,
        // чтобы в asum файл записались данные о том иногородний пациент или нет на основе первого элемента из sluchList
        writePatient(writer, zap.getSluchList().get(0).getPacient());

        //Проходимся по случаям и создаем их
        for (Sluch sluch : zap.getSluchList()) {
            writeSluch(writer, sluch);
        }

        writer.writeEndElement();
    }

    //Создаем сегмент zglv
    public static void writeZglv(XMLStreamWriter writer, Zglv zglv) throws XMLStreamException {
        writer.writeStartElement("zglv");

        getElement(writer, "idlist", zglv.getIdlist());
        getElement(writer, "crc", zglv.getCrc());
        getElement(writer, "version", zglv.getVersion());
        getElement(writer, "data", zglv.getData());
        getElement(writer, "filename", zglv.getFilename());

        writer.writeEndElement();
    }

    //Метод записывает данные в сегмент pacient в XML
    public static void writePatient(XMLStreamWriter writer, Pacient pacient) throws XMLStreamException {
        writer.writeStartElement("pacient");

        getElement(writer, "vpolis", pacient.getVpolis());
        getElement(writer, "spolis", pacient.getSpolis());
        getElement(writer, "npolis", pacient.getNpolis());
        getElement(writer, "smo", pacient.getSmo());
        getElement(writer, "smo_ogrn", pacient.getSmo_ogrn());
        getElement(writer, "smo_ok", pacient.getSmo_ok());
        getElement(writer, "smo_nam", pacient.getSmo_nam());
        getElement(writer, "novor", pacient.getNovor());
        getElement(writer, "inogor", pacient.getInogor());
        getElement(writer, "fam", pacient.getFam().toUpperCase());
        getElement(writer, "im", pacient.getIm().toUpperCase());
        getElement(writer, "ot", pacient.getOt().toUpperCase());
        getElement(writer, "w", pacient.getW());
        getElement(writer, "dr", pacient.getDr());
        getElement(writer, "fam_p", pacient.getFam_p());
        getElement(writer, "im_p", pacient.getIm_p());
        getElement(writer, "ot_p", pacient.getOt_p());
        getElement(writer, "w_p", pacient.getW_p());
        getElement(writer, "dr_p", pacient.getDr_p());
        getElement(writer, "mr", pacient.getMr());
        getElement(writer, "doctype", pacient.getDoctype());
        getElement(writer, "docser", pacient.getDocser());
        getElement(writer, "docnum", pacient.getDocnum());
        getElement(writer, "snils", pacient.getSnils());
        getElement(writer, "okatog", pacient.getOkatog());
        getElement(writer, "okatop", pacient.getOkatop());
        getElement(writer, "comentz", pacient.getComentz());
        getElement(writer, "adres", pacient.getAdres());
        getElement(writer, "recid", pacient.getRecid());
        getElement(writer, "inv", pacient.getInv());
        getElement(writer, "mse", pacient.getMse());

        writer.writeEndElement();
    }

    public static void writeSluch(XMLStreamWriter writer, Sluch sluch) throws XMLStreamException {
        writer.writeStartElement("sluch");

        getElement(writer,"id", sluch.getId());
        getElement(writer,"idcase", sluch.getIdcase());
        getElement(writer,"mcod", sluch.getMcod());
        getElement(writer,"glpu", sluch.getGlpu());
        getElement(writer,"spolis", sluch.getSpolis());
        getElement(writer,"npolis", sluch.getNpolis());
        getElement(writer,"novor", sluch.getNovor());
        getElement(writer,"smo", sluch.getSmo());
        getElement(writer,"fam", sluch.getFam().toUpperCase());
        getElement(writer,"im", sluch.getIm().toUpperCase());
        getElement(writer,"ot", sluch.getOt().toUpperCase());
        getElement(writer,"w", sluch.getW());
        getElement(writer,"dr", sluch.getDr());
        getElement(writer,"fam_p", sluch.getFam_p().toUpperCase());
        getElement(writer,"im_p", sluch.getIm_p().toUpperCase());
        getElement(writer,"ot_p", sluch.getOt_p().toUpperCase());
        getElement(writer,"w_p", sluch.getW_p());
        getElement(writer,"dr_p", sluch.getDr_p());
        getElement(writer,"usl_ok", sluch.getUsl_ok());
        getElement(writer,"vidpom", sluch.getVidpom());
        getElement(writer,"npr_mo", sluch.getNpr_mo());
        getElement(writer,"npr_lpu", sluch.getNpr_lpu());
        getElement(writer,"order", sluch.getOrder());
        getElement(writer,"t_order", sluch.getT_order());
        getElement(writer,"podr", sluch.getPodr());
        getElement(writer,"profil", sluch.getProfil());
        getElement(writer,"det", sluch.getDet());
        getElement(writer,"nhistory", sluch.getNhistory());
        getElement(writer,"date_1", sluch.getDate_1());
        getElement(writer,"date_2", sluch.getDate_2());
        getElement(writer,"ds0", sluch.getDs0());
        getElement(writer,"ds1", sluch.getDs1());
        getElement(writer,"ds2", sluch.getDs2());
        getElement(writer,"code_mes1", sluch.getCode_mes1());
        getElement(writer,"code_mes2", sluch.getCode_mes2());
        getElement(writer,"rslt", sluch.getRslt());
        getElement(writer,"ishod", sluch.getIshod());
        getElement(writer,"prvs", sluch.getPrvs());
        getElement(writer,"iddokt", sluch.getIddokt());
        getElement(writer,"os_sluch", sluch.getOs_sluch());
        getElement(writer,"idsp", sluch.getIdsp());
        getElement(writer,"ed_col", sluch.getEd_col());
        getElement(writer,"kolusl", sluch.getKolusl());
        getElement(writer,"tarif", sluch.getTarif());
        getElement(writer,"sumv", sluch.getSumv());
        getElement(writer,"oplata", sluch.getOplata());
        getElement(writer,"sump", sluch.getSump());
        getElement(writer,"refreason", sluch.getRefreason());
        getElement(writer,"sank_mek", sluch.getSank_mek());
        getElement(writer,"sank_mee", sluch.getSank_mee());
        getElement(writer,"sank_ekmp", sluch.getSank_ekmp());
        getElement(writer,"comentz", sluch.getComentz());
        getElement(writer,"uid", sluch.getUid());
        getElement(writer,"inogor", sluch.getInogor());
        getElement(writer,"pr_nov", sluch.getPr_nov());
        getElement(writer,"otmonth", sluch.getOtmonth());
        getElement(writer,"otyear", sluch.getOtyear());
        getElement(writer,"disp", sluch.getDisp());
        getElement(writer,"vid_hmp", sluch.getVid_hmp());
        getElement(writer,"metod_hmp", sluch.getMetod_hmp());
        getElement(writer,"ds3", sluch.getDs3());
        getElement(writer,"vnov_m", sluch.getVnov_m());
        getElement(writer,"rslt_d", sluch.getRslt_d());
        getElement(writer,"vers_spec", sluch.getVers_spec());
        getElement(writer,"dopsch", sluch.getDopsch());
        getElement(writer,"tal_d", sluch.getTal_d());
        getElement(writer,"tal_p", sluch.getTal_p());
        getElement(writer,"vbr", sluch.getVbr());
        getElement(writer,"p_otk", sluch.getP_otk());
        getElement(writer,"nrisoms", sluch.getNrisoms());
        getElement(writer,"ds1_pr", sluch.getDs1_pr());
        getElement(writer,"ds4", sluch.getDs4());
        getElement(writer,"nazn", sluch.getNazn());
        getElement(writer,"naz_sp", sluch.getNaz_sp());
        getElement(writer,"naz_v", sluch.getNaz_v());
        getElement(writer,"naz_pmp", sluch.getNaz_pmp());
        getElement(writer,"naz_pk", sluch.getNaz_pk());
        getElement(writer,"pr_d_n", sluch.getPr_d_n());
        getElement(writer,"npr_date", sluch.getNpr_date());
        getElement(writer,"tal_num", sluch.getTal_num());
        getElement(writer,"ds2_pr", sluch.getDs2_pr());
        getElement(writer,"pr_ds2_n", sluch.getPr_ds2_n());
        getElement(writer,"c_zab", sluch.getC_zab());
        getElement(writer,"mse", sluch.getMse());
        getElement(writer,"vb_p", sluch.getVb_p());
        getElement(writer,"naz_usl", sluch.getNaz_usl());
        getElement(writer,"napr_date", sluch.getNapr_date());
        getElement(writer,"napr_mo", sluch.getNapr_mo());
        getElement(writer,"ds_onk", sluch.getDs_onk());
        getElement(writer,"npr_usl_ok", sluch.getNpr_usl_ok());
        getElement(writer,"wei", sluch.getWei());

        //Для всего списка услуг из случая создаем узлы usl
        for (Usl usl : sluch.getUslList()) {
            usl.setIdstrax(sluch.getId()); //При добавлении usl в sluch также для Usl добавляем idstrax
            writeUsl(writer, usl);
        }

        //Если в случае есть объекты по онко, добавляем их в ASUM файл в случай
        if (sluch.getCons() != null) {
            writeCons(writer, sluch.getCons());
        }

        if (sluch.getNapr() != null) {
            writeNapr(writer, sluch.getNapr());
        }

        if (sluch.getOnkSl() != null) {
            writeOnkSl(writer, sluch.getOnkSl());
        }

        writer.writeEndElement();
    }

    public static void writeUsl(XMLStreamWriter writer, Usl usl) throws XMLStreamException {
        writer.writeStartElement("usl");

        getElement(writer,"idstrax",usl.getIdstrax());
        getElement(writer,"idusl",usl.getIdusl());
        getElement(writer,"idserv",usl.getIdserv());
        getElement(writer,"spolis",usl.getSpolis());
        getElement(writer,"npolis",usl.getNpolis());
        getElement(writer,"glpu",usl.getGlpu());
        getElement(writer,"mcod",usl.getMcod());
        getElement(writer,"podr",usl.getPodr());
        getElement(writer,"profil",usl.getProfil());
        getElement(writer,"det",usl.getDet());
        getElement(writer,"date_in",usl.getDate_in());
        getElement(writer,"date_out",usl.getDate_out());
        getElement(writer,"ds",usl.getDs());
        getElement(writer,"code_usl",usl.getCode_usl());
        getElement(writer,"ed_col",usl.getEd_col());
        getElement(writer,"kol_usl",usl.getKol_usl());
        getElement(writer,"tarif",usl.getTarif());
        getElement(writer,"sumv_usl",usl.getSumv_usl());
        getElement(writer,"zak",usl.getZak());
        getElement(writer,"stand",usl.getStand());
        getElement(writer,"prvs",usl.getPrvs());
        getElement(writer,"code_md",usl.getCode_md());
        getElement(writer,"comentu",usl.getComentu());
        getElement(writer,"uid",usl.getUid());
        getElement(writer,"dopsch",usl.getDopsch());
        getElement(writer,"dir2",usl.getDir2());
        getElement(writer,"gr_zdorov",usl.getGr_zdorov());
        getElement(writer,"student",usl.getStudent());
        getElement(writer,"vid_vme",usl.getVid_vme());
        getElement(writer,"koefk",usl.getKoefk());
        getElement(writer,"pouh",usl.getPouh());
        getElement(writer,"otkaz2",usl.getOtkaz2());
        getElement(writer,"nazna4",usl.getNazna4());
        getElement(writer,"p_per",usl.getP_per());
        getElement(writer,"npl",usl.getNpl());
        getElement(writer,"idsh",usl.getIdsh());
        getElement(writer,"idsh2",usl.getIdsh2());
        getElement(writer,"dn",usl.getDn());
        getElement(writer,"ds_onk",usl.getDs_onk());
        getElement(writer,"p_cel",usl.getP_cel());
        getElement(writer,"profil_k",usl.getProfil_k());
        getElement(writer,"idsl",usl.getIdsl());
        getElement(writer,"muvr",usl.getMuvr());
        getElement(writer,"muvr_lpu",usl.getMuvr_lpu());
        getElement(writer,"date_usl",usl.getDate_usl());

        if (usl.getMrUslN() != null) {
            writeMrUslN(writer, usl.getMrUslN());
        }

        writer.writeEndElement();
    }

    //Записываем сегмент mr_usl_n
    public static void writeMrUslN(XMLStreamWriter writer, MrUslN item) throws XMLStreamException {
        writer.writeStartElement("mr_usl_n");

        getElement(writer, "idusl", item.getIdusl());
        getElement(writer, "mr_n", item.getMr_n());
        getElement(writer, "prvs_mr_n", item.getPrvs_mr_n());
        getElement(writer, "code_md_m", item.getCode_md_m());
        getElement(writer, "fio_md", item.getFio_md());

        writer.writeEndElement();
    }

    //Записываем сегмент onk_sl
    public static void writeOnkSl(XMLStreamWriter writer, OnkSl item) throws XMLStreamException {
        writer.writeStartElement("onk_sl");

        getElement(writer, "idusl", item.getIdusl());
        getElement(writer, "id", item.getId());
        getElement(writer, "ds1_t", item.getDs1_t());
        getElement(writer, "stad", item.getStad());
        getElement(writer, "onk_t", item.getOnk_t());
        getElement(writer, "onk_n", item.getOnk_n());
        getElement(writer, "onk_m", item.getOnk_m());
        getElement(writer, "mtstz", item.getMtstz());
        getElement(writer, "sod", item.getSod());
        getElement(writer, "k_fr", item.getK_fr());
        getElement(writer, "wei", item.getWei());
        getElement(writer, "hei", item.getHei());
        getElement(writer, "bsa", item.getBsa());

        writer.writeEndElement();
    }

    //Записываем сегмент napr
    public static void writeNapr(XMLStreamWriter writer, Napr item) throws XMLStreamException {
        writer.writeStartElement("napr");

        getElement(writer, "idusl", item.getIdusl());
        getElement(writer, "id", item.getId());
        getElement(writer, "napr_date", item.getNapr_date());
        getElement(writer, "napr_v", item.getNapr_v());
        getElement(writer, "met_issl", item.getMet_issl());
        getElement(writer, "napr_usl", item.getNapr_usl());
        getElement(writer, "napr_mo", item.getNapr_mo());

        writer.writeEndElement();
    }

    //Записываем сегмент cons
    public static void writeCons(XMLStreamWriter writer, Cons item) throws XMLStreamException {
        writer.writeStartElement("cons");

        getElement(writer, "idusl", item.getIdusl());
        getElement(writer, "id", item.getId());
        getElement(writer, "pr_cons", item.getPr_cons());
        getElement(writer, "dt_cons", item.getDt_cons());

        writer.writeEndElement();
    }
}
