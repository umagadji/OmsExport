package ru.rdc.omsexport.ds_pkg.ds_models;

//Класс описывает случай пациента
public class SluchDS {
    private String id;
    private String idcase;
    private String mcod;
    private String glpu;
    private String spolis;
    private String npolis;
    private String novor;
    private String smo;
    private String fam;
    private String im;
    private String ot;
    private String w;
    private String dr;
    private String fam_p;
    private String im_p;
    private String ot_p;
    private String w_p;
    private String dr_p;
    private String usl_ok;
    private String vidpom;
    private String npr_mo;
    private String order;
    private String t_order;
    private String podr;
    private String profil;
    private String det;
    private String nhistory;
    private String date_1;
    private String date_2;
    private String ds0;
    private String ds1;
    private String ds2;
    private String code_mes1;
    private String code_mes2;
    private String rslt;
    private String ishod;
    private String prvs;
    private String iddokt;
    private String os_sluch;
    private String idsp;
    private String ed_col;
    private String kolusl;
    private String tarif;
    private String sumv;
    private String oplata;
    private String sump;
    private String refreason;
    private String sank_mek;
    private String sank_mee;
    private String sank_ekmp;
    private String comentz;
    private String uid;
    private String inogor;
    private String pr_nov;
    private String otmonth;
    private String otyear;
    private String disp;
    private String vid_hmp;
    private String metod_hmp;
    private String ds3;
    private String vnov_m;
    private String rslt_d;
    private String vers_spec;
    private String dopsch;
    private String tal_d;
    private String tal_p;
    private String vbr;
    private String p_otk;
    private String nrisoms;
    private String ds1_pr;
    private String ds4;
    private String nazn;
    private String naz_sp;
    private String naz_v;
    private String naz_pmp;
    private String naz_pk;
    private String pr_d_n;
    private String npr_date;
    private String tal_num;
    private String ds2_pr;
    private String pr_ds2_n;
    private String c_zab;
    private String mse;
    private String vb_p;
    private String naz_usl;
    private String napr_date;
    private String napr_mo;
    private String ds_onk;
    private String npr_lpu;
    private String npr_usl_ok;
    private String wei;

    private int profil_k; //Профиль койки

    private int ed_kol_ds; //Количество койко-дней для стационаров

    private String snpol;

    //Случай содержит в себе услугу
    private UslDS uslDS;

    public UslDS getUsl() {
        return uslDS;
    }

    public String getSnPol() {
        return spolis + npolis;
    }

    public void setUsl(UslDS uslDS) {
        this.uslDS = uslDS;
    }

    private PacientDS pacientDS;

    public SluchDS() {}

    public String isInogor() {
        return inogor;
    }

    public int getProfil_k() {
        return profil_k;
    }

    public void setProfil_k(int profil_k) {
        this.profil_k = profil_k;
    }

    public int getEd_kol_ds() {
        return ed_kol_ds;
    }

    public void setEd_kol_ds(int ed_kol_ds) {
        this.ed_kol_ds = ed_kol_ds;
    }

    public PacientDS getPacient() {
        return pacientDS;
    }

    public void setPacient(PacientDS pacientDS) {
        this.pacientDS = pacientDS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdcase() {
        return idcase;
    }

    public void setIdcase(String idcase) {
        this.idcase = idcase;
    }

    public String getMcod() {
        return mcod;
    }

    public void setMcod(String mcod) {
        this.mcod = mcod;
    }

    public String getGlpu() {
        return glpu;
    }

    public void setGlpu(String glpu) {
        this.glpu = glpu;
    }

    public String getSpolis() {
        return spolis;
    }

    public void setSpolis(String spolis) {
        this.spolis = spolis;
    }

    public String getNpolis() {
        return npolis;
    }

    public void setNpolis(String npolis) {
        this.npolis = npolis;
    }

    public String getNovor() {
        return novor;
    }

    public void setNovor(String novor) {
        this.novor = novor;
    }

    public String getSmo() {
        return smo;
    }

    public void setSmo(String smo) {
        this.smo = smo;
    }

    public String getFam() {
        return fam;
    }

    public void setFam(String fam) {
        this.fam = fam;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public String getOt() {
        return ot;
    }

    public void setOt(String ot) {
        this.ot = ot;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getFam_p() {
        return fam_p;
    }

    public void setFam_p(String fam_p) {
        this.fam_p = fam_p;
    }

    public String getIm_p() {
        return im_p;
    }

    public void setIm_p(String im_p) {
        this.im_p = im_p;
    }

    public String getOt_p() {
        return ot_p;
    }

    public void setOt_p(String ot_p) {
        this.ot_p = ot_p;
    }

    public String getW_p() {
        return w_p;
    }

    public void setW_p(String w_p) {
        this.w_p = w_p;
    }

    public String getDr_p() {
        return dr_p;
    }

    public void setDr_p(String dr_p) {
        this.dr_p = dr_p;
    }

    public String getUsl_ok() {
        return usl_ok;
    }

    public void setUsl_ok(String usl_ok) {
        this.usl_ok = usl_ok;
    }

    public String getVidpom() {
        return vidpom;
    }

    public void setVidpom(String vidpom) {
        this.vidpom = vidpom;
    }

    public String getNpr_mo() {
        return npr_mo;
    }

    public void setNpr_mo(String npr_mo) {
        this.npr_mo = npr_mo;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getT_order() {
        return t_order;
    }

    public void setT_order(String t_order) {
        this.t_order = t_order;
    }

    public String getPodr() {
        return podr;
    }

    public void setPodr(String podr) {
        this.podr = podr;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getDet() {
        return det;
    }

    public void setDet(String det) {
        this.det = det;
    }

    public String getNhistory() {
        return nhistory;
    }

    public void setNhistory(String nhistory) {
        this.nhistory = nhistory;
    }

    public String getDate_1() {
        return date_1;
    }

    public void setDate_1(String date_1) {
        this.date_1 = date_1;
    }

    public String getDate_2() {
        return date_2;
    }

    public void setDate_2(String date_2) {
        this.date_2 = date_2;
    }

    public String getDs0() {
        return ds0;
    }

    public void setDs0(String ds0) {
        this.ds0 = ds0;
    }

    public String getDs1() {
        return ds1;
    }

    public void setDs1(String ds1) {
        this.ds1 = ds1;
    }

    public String getDs2() {
        return ds2;
    }

    public void setDs2(String ds2) {
        this.ds2 = ds2;
    }

    public String getCode_mes1() {
        return code_mes1;
    }

    public void setCode_mes1(String code_mes1) {
        this.code_mes1 = code_mes1;
    }

    public String getCode_mes2() {
        return code_mes2;
    }

    public void setCode_mes2(String code_mes2) {
        this.code_mes2 = code_mes2;
    }

    public String getRslt() {
        return rslt;
    }

    public void setRslt(String rslt) {
        this.rslt = rslt;
    }

    public String getIshod() {
        return ishod;
    }

    public void setIshod(String ishod) {
        this.ishod = ishod;
    }

    public String getPrvs() {
        return prvs;
    }

    public void setPrvs(String prvs) {
        this.prvs = prvs;
    }

    public String getIddokt() {
        return iddokt;
    }

    public void setIddokt(String iddokt) {
        this.iddokt = iddokt;
    }

    public String getOs_sluch() {
        return os_sluch;
    }

    public void setOs_sluch(String os_sluch) {
        this.os_sluch = os_sluch;
    }

    public String getIdsp() {
        return idsp;
    }

    public void setIdsp(String idsp) {
        this.idsp = idsp;
    }

    public String getEd_col() {
        return ed_col;
    }

    public void setEd_col(String ed_col) {
        this.ed_col = ed_col;
    }

    public String getKolusl() {
        return kolusl;
    }

    public void setKolusl(String kolusl) {
        this.kolusl = kolusl;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public String getSumv() {
        return sumv;
    }

    public void setSumv(String sumv) {
        this.sumv = sumv;
    }

    public String getOplata() {
        return oplata;
    }

    public void setOplata(String oplata) {
        this.oplata = oplata;
    }

    public String getSump() {
        return sump;
    }

    public void setSump(String sump) {
        this.sump = sump;
    }

    public String getRefreason() {
        return refreason;
    }

    public void setRefreason(String refreason) {
        this.refreason = refreason;
    }

    public String getSank_mek() {
        return sank_mek;
    }

    public void setSank_mek(String sank_mek) {
        this.sank_mek = sank_mek;
    }

    public String getSank_mee() {
        return sank_mee;
    }

    public void setSank_mee(String sank_mee) {
        this.sank_mee = sank_mee;
    }

    public String getSank_ekmp() {
        return sank_ekmp;
    }

    public void setSank_ekmp(String sank_ekmp) {
        this.sank_ekmp = sank_ekmp;
    }

    public String getComentz() {
        return comentz;
    }

    public void setComentz(String comentz) {
        this.comentz = comentz;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInogor() {
        return inogor;
    }

    public void setInogor(String inogor) {
        this.inogor = inogor;
    }

    public String getPr_nov() {
        return pr_nov;
    }

    public void setPr_nov(String pr_nov) {
        this.pr_nov = pr_nov;
    }

    public String getOtmonth() {
        return otmonth;
    }

    public void setOtmonth(String otmonth) {
        this.otmonth = otmonth;
    }

    public String getOtyear() {
        return otyear;
    }

    public void setOtyear(String otyear) {
        this.otyear = otyear;
    }

    public String getDisp() {
        return disp;
    }

    public void setDisp(String disp) {
        this.disp = disp;
    }

    public String getVid_hmp() {
        return vid_hmp;
    }

    public void setVid_hmp(String vid_hmp) {
        this.vid_hmp = vid_hmp;
    }

    public String getMetod_hmp() {
        return metod_hmp;
    }

    public void setMetod_hmp(String metod_hmp) {
        this.metod_hmp = metod_hmp;
    }

    public String getDs3() {
        return ds3;
    }

    public void setDs3(String ds3) {
        this.ds3 = ds3;
    }

    public String getVnov_m() {
        return vnov_m;
    }

    public void setVnov_m(String vnov_m) {
        this.vnov_m = vnov_m;
    }

    public String getRslt_d() {
        return rslt_d;
    }

    public void setRslt_d(String rslt_d) {
        this.rslt_d = rslt_d;
    }

    public String getVers_spec() {
        return vers_spec;
    }

    public void setVers_spec(String vers_spec) {
        this.vers_spec = vers_spec;
    }

    public String getDopsch() {
        return dopsch;
    }

    public void setDopsch(String dopsch) {
        this.dopsch = dopsch;
    }

    public String getTal_d() {
        return tal_d;
    }

    public void setTal_d(String tal_d) {
        this.tal_d = tal_d;
    }

    public String getTal_p() {
        return tal_p;
    }

    public void setTal_p(String tal_p) {
        this.tal_p = tal_p;
    }

    public String getVbr() {
        return vbr;
    }

    public void setVbr(String vbr) {
        this.vbr = vbr;
    }

    public String getP_otk() {
        return p_otk;
    }

    public void setP_otk(String p_otk) {
        this.p_otk = p_otk;
    }

    public String getNrisoms() {
        return nrisoms;
    }

    public void setNrisoms(String nrisoms) {
        this.nrisoms = nrisoms;
    }

    public String getDs1_pr() {
        return ds1_pr;
    }

    public void setDs1_pr(String ds1_pr) {
        this.ds1_pr = ds1_pr;
    }

    public String getDs4() {
        return ds4;
    }

    public void setDs4(String ds4) {
        this.ds4 = ds4;
    }

    public String getNazn() {
        return nazn;
    }

    public void setNazn(String nazn) {
        this.nazn = nazn;
    }

    public String getNaz_sp() {
        return naz_sp;
    }

    public void setNaz_sp(String naz_sp) {
        this.naz_sp = naz_sp;
    }

    public String getNaz_v() {
        return naz_v;
    }

    public void setNaz_v(String naz_v) {
        this.naz_v = naz_v;
    }

    public String getNaz_pmp() {
        return naz_pmp;
    }

    public void setNaz_pmp(String naz_pmp) {
        this.naz_pmp = naz_pmp;
    }

    public String getNaz_pk() {
        return naz_pk;
    }

    public void setNaz_pk(String naz_pk) {
        this.naz_pk = naz_pk;
    }

    public String getPr_d_n() {
        return pr_d_n;
    }

    public void setPr_d_n(String pr_d_n) {
        this.pr_d_n = pr_d_n;
    }

    public String getNpr_date() {
        return npr_date;
    }

    public void setNpr_date(String npr_date) {
        this.npr_date = npr_date;
    }

    public String getTal_num() {
        return tal_num;
    }

    public void setTal_num(String tal_num) {
        this.tal_num = tal_num;
    }

    public String getDs2_pr() {
        return ds2_pr;
    }

    public void setDs2_pr(String ds2_pr) {
        this.ds2_pr = ds2_pr;
    }

    public String getPr_ds2_n() {
        return pr_ds2_n;
    }

    public void setPr_ds2_n(String pr_ds2_n) {
        this.pr_ds2_n = pr_ds2_n;
    }

    public String getC_zab() {
        return c_zab;
    }

    public void setC_zab(String c_zab) {
        this.c_zab = c_zab;
    }

    public String getMse() {
        return mse;
    }

    public void setMse(String mse) {
        this.mse = mse;
    }

    public String getVb_p() {
        return vb_p;
    }

    public void setVb_p(String vb_p) {
        this.vb_p = vb_p;
    }

    public String getNaz_usl() {
        return naz_usl;
    }

    public void setNaz_usl(String naz_usl) {
        this.naz_usl = naz_usl;
    }

    public String getNapr_date() {
        return napr_date;
    }

    public void setNapr_date(String napr_date) {
        this.napr_date = napr_date;
    }

    public String getNapr_mo() {
        return napr_mo;
    }

    public void setNapr_mo(String napr_mo) {
        this.napr_mo = napr_mo;
    }

    public String getDs_onk() {
        return ds_onk;
    }

    public void setDs_onk(String ds_onk) {
        this.ds_onk = ds_onk;
    }

    public String getNpr_lpu() {
        return npr_lpu;
    }

    public void setNpr_lpu(String npr_lpu) {
        this.npr_lpu = npr_lpu;
    }

    public String getNpr_usl_ok() {
        return npr_usl_ok;
    }

    public void setNpr_usl_ok(String npr_usl_ok) {
        this.npr_usl_ok = npr_usl_ok;
    }

    public String getWei() {
        return wei;
    }

    public void setWei(String wei) {
        this.wei = wei;
    }

    @Override
    public String toString() {
        return "Sluch{" +
                "id='" + id + '\'' +
                ", idcase='" + idcase + '\'' +
                ", mcod='" + mcod + '\'' +
                ", glpu='" + glpu + '\'' +
                ", spolis='" + spolis + '\'' +
                ", npolis='" + npolis + '\'' +
                ", novor='" + novor + '\'' +
                ", smo='" + smo + '\'' +
                ", fam='" + fam + '\'' +
                ", im='" + im + '\'' +
                ", ot='" + ot + '\'' +
                ", w='" + w + '\'' +
                ", dr='" + dr + '\'' +
                ", fam_p='" + fam_p + '\'' +
                ", im_p='" + im_p + '\'' +
                ", ot_p='" + ot_p + '\'' +
                ", w_p='" + w_p + '\'' +
                ", dr_p='" + dr_p + '\'' +
                ", usl_ok='" + usl_ok + '\'' +
                ", vidpom='" + vidpom + '\'' +
                ", npr_mo='" + npr_mo + '\'' +
                ", order='" + order + '\'' +
                ", t_order='" + t_order + '\'' +
                ", podr='" + podr + '\'' +
                ", profil='" + profil + '\'' +
                ", det='" + det + '\'' +
                ", nhistory='" + nhistory + '\'' +
                ", date_1='" + date_1 + '\'' +
                ", date_2='" + date_2 + '\'' +
                ", ds0='" + ds0 + '\'' +
                ", ds1='" + ds1 + '\'' +
                ", ds2='" + ds2 + '\'' +
                ", code_mes1='" + code_mes1 + '\'' +
                ", code_mes2='" + code_mes2 + '\'' +
                ", rslt='" + rslt + '\'' +
                ", ishod='" + ishod + '\'' +
                ", prvs='" + prvs + '\'' +
                ", iddokt='" + iddokt + '\'' +
                ", os_sluch='" + os_sluch + '\'' +
                ", idsp='" + idsp + '\'' +
                ", ed_col='" + ed_col + '\'' +
                ", kolusl='" + kolusl + '\'' +
                ", tarif='" + tarif + '\'' +
                ", sumv='" + sumv + '\'' +
                ", oplata='" + oplata + '\'' +
                ", sump='" + sump + '\'' +
                ", refreason='" + refreason + '\'' +
                ", sank_mek='" + sank_mek + '\'' +
                ", sank_mee='" + sank_mee + '\'' +
                ", sank_ekmp='" + sank_ekmp + '\'' +
                ", comentz='" + comentz + '\'' +
                ", uid='" + uid + '\'' +
                ", inogor='" + inogor + '\'' +
                ", pr_nov='" + pr_nov + '\'' +
                ", otmonth='" + otmonth + '\'' +
                ", otyear='" + otyear + '\'' +
                ", disp='" + disp + '\'' +
                ", vid_hmp='" + vid_hmp + '\'' +
                ", metod_hmp='" + metod_hmp + '\'' +
                ", ds3='" + ds3 + '\'' +
                ", vnov_m='" + vnov_m + '\'' +
                ", rslt_d='" + rslt_d + '\'' +
                ", vers_spec='" + vers_spec + '\'' +
                ", dopsch='" + dopsch + '\'' +
                ", tal_d='" + tal_d + '\'' +
                ", tal_p='" + tal_p + '\'' +
                ", vbr='" + vbr + '\'' +
                ", p_otk='" + p_otk + '\'' +
                ", nrisoms='" + nrisoms + '\'' +
                ", ds1_pr='" + ds1_pr + '\'' +
                ", ds4='" + ds4 + '\'' +
                ", nazn='" + nazn + '\'' +
                ", naz_sp='" + naz_sp + '\'' +
                ", naz_v='" + naz_v + '\'' +
                ", naz_pmp='" + naz_pmp + '\'' +
                ", naz_pk='" + naz_pk + '\'' +
                ", pr_d_n='" + pr_d_n + '\'' +
                ", npr_date='" + npr_date + '\'' +
                ", tal_num='" + tal_num + '\'' +
                ", ds2_pr='" + ds2_pr + '\'' +
                ", pr_ds2_n='" + pr_ds2_n + '\'' +
                ", c_zab='" + c_zab + '\'' +
                ", mse='" + mse + '\'' +
                ", vb_p='" + vb_p + '\'' +
                ", naz_usl='" + naz_usl + '\'' +
                ", napr_date='" + napr_date + '\'' +
                ", napr_mo='" + napr_mo + '\'' +
                ", ds_onk='" + ds_onk + '\'' +
                ", npr_lpu='" + npr_lpu + '\'' +
                ", npr_usl_ok='" + npr_usl_ok + '\'' +
                ", wei='" + wei + '\'' +
                '}';
    }
}
