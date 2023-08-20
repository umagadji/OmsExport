package ru.rdc.omsexport.ds_pkg.ds_utils;

import ru.rdc.omsexport.asum_models.Zglv;
import ru.rdc.omsexport.ds_pkg.ds_models.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

//Утилитный класс для создания сегментов ASUM-файла по стационару
public class WriteAsumXmlSegmentsDS {

    //Метод для создания свойства элемента
    public static void getElement(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
        writer.writeStartElement(name);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    public static void writeZap(XMLStreamWriter writer, ZapDS zap) throws XMLStreamException {
        writer.writeStartElement("zap");

        getElement(writer, "id", zap.getSluchList().get(0).getPacientDS().getNpolis());
        getElement(writer, "idlist", "1");
        getElement(writer, "pr_nov", "0");

        //тут возможно нужно подумать о том как сделать сортировку,
        // чтобы в asum файл записались данные о том иногородний пациент или нет на основе первого элемента из sluchList
        writePatient(writer, zap.getSluchList().get(0).getPacientDS());

        //Проходимся по случаям и создаем их
        for (SluchDS sluch : zap.getSluchList()) {
            writeSluch(writer, sluch);
        }

        writer.writeEndElement();
    }



    //Метод создает тег zglv
    public static void writeZglv(XMLStreamWriter writer, Zglv zglv) throws XMLStreamException {
        writer.writeStartElement("zglv");

        getElement(writer, "idlist", zglv.getIdlist());
        getElement(writer, "crc", zglv.getCrc());
        getElement(writer, "version", zglv.getVersion());
        getElement(writer, "data", zglv.getData());
        getElement(writer, "filename", zglv.getFilename());

        writer.writeEndElement();
    }

    //Метод создает тег vrachi
    public static void writeVrachi(XMLStreamWriter writer, VrachiDS vrachi) throws XMLStreamException {
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

    //Метод записывает данные в сегмент pacient в XML
    public static void writePatient(XMLStreamWriter writer, PacientDS pacient) throws XMLStreamException {
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
        getElement(writer, "docdate", pacient.getDocdate());
        getElement(writer, "docorg", pacient.getDocorg());

        writer.writeEndElement();
    }

    public static void writeSluch(XMLStreamWriter writer, SluchDS sluch) throws XMLStreamException {
        writer.writeStartElement("sluch");

        getElement(writer,"id", sluch.getId());
        getElement(writer,"idcase", sluch.getIdcase());
        getElement(writer,"mcod", sluch.getMcod());
        getElement(writer,"glpu", sluch.getGlpu());
        getElement(writer,"spolis", sluch.getSpolis());
        getElement(writer,"npolis", sluch.getNpolis());
        getElement(writer,"novor", sluch.getNovor());
        getElement(writer,"smo", sluch.getSmo());
        getElement(writer,"fam", sluch.getFam());
        getElement(writer,"im", sluch.getIm());
        getElement(writer,"ot", sluch.getOt());
        getElement(writer,"w", sluch.getW());
        getElement(writer,"dr", sluch.getDr());
        getElement(writer,"fam_p", sluch.getFam_p());
        getElement(writer,"im_p", sluch.getIm_p());
        getElement(writer,"ot_p", sluch.getOt_p());
        getElement(writer,"w_p", sluch.getW_p());
        getElement(writer,"dr_p", sluch.getDr_p());
        getElement(writer,"usl_ok", sluch.getUsl_ok());
        getElement(writer,"vidpom", sluch.getVidpom());
        getElement(writer,"npr_mo", sluch.getNpr_mo());
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
        getElement(writer,"npr_lpu", sluch.getNpr_lpu());
        getElement(writer,"npr_usl_ok", sluch.getNpr_usl_ok());
        getElement(writer,"wei", sluch.getWei());

        if (sluch.getUslDS() != null) {
            writeUsl(writer, sluch.getUslDS());
        }

        writer.writeEndElement();
    }

    public static void writeUsl(XMLStreamWriter writer, UslDS usl) throws XMLStreamException {
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

        if (usl.getSlKoefDS() != null) {
            writeSlKoef(writer, usl.getSlKoefDS());
        }

        if (usl.getCritDS() != null) {
            writeCrit(writer, usl.getCritDS());
        }

        if (usl.getHrrgdDS() != null) {
            writeHrrgd(writer, usl.getHrrgdDS());
        }

        writer.writeEndElement();
    }

    //Метод создает тег crit
    public static void writeCrit(XMLStreamWriter writer, CritDS crit) throws XMLStreamException {
        writer.writeStartElement("crit");

        getElement(writer,"idusl", crit.getIdusl());
        getElement(writer,"crit", crit.getCrit());
        getElement(writer,"cname", crit.getCname());

        writer.writeEndElement();
    }

    //Метод создает тег hrrgd
    public static void writeHrrgd(XMLStreamWriter writer, HrrgdDS hrrgd) throws XMLStreamException {
        writer.writeStartElement("hrrgd");

        getElement(writer,"idusl", hrrgd.getIdusl());
        getElement(writer,"date_o", hrrgd.getDate_o());
        getElement(writer,"hkod", hrrgd.getHkod());
        getElement(writer,"name_o", hrrgd.getName_o());
        getElement(writer,"notksg", hrrgd.getNotksg());
        getElement(writer,"price", hrrgd.getPrice());
        getElement(writer,"idvidvme", hrrgd.getIdvidvme());

        writer.writeEndElement();
    }

    //Метод создает тег SlKoef
    public static void writeSlKoef(XMLStreamWriter writer, SlKoefDS sl_koef) throws XMLStreamException {
        writer.writeStartElement("sl_koef");

        getElement(writer,"idusl", sl_koef.getIdusl());
        getElement(writer,"idsl", sl_koef.getIdsl());
        getElement(writer,"z_sl", sl_koef.getZ_sl());
        getElement(writer,"id", sl_koef.getId());
        getElement(writer,"name_sl", sl_koef.getName_sl());

        writer.writeEndElement();
    }
}
