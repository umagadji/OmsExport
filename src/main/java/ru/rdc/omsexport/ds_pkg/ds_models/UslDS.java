package ru.rdc.omsexport.ds_pkg.ds_models;

//Класс описывает услугу
public class UslDS {
    private String idstrax;
    private String idusl;
    private String idserv;
    private String spolis;
    private String npolis;
    private String glpu;
    private String mcod;
    private String podr;
    private String profil;
    private String det;
    private String date_in;
    private String date_out;
    private String ds;
    private String code_usl;
    private String ed_col;
    private String kol_usl;
    private String tarif;
    private String sumv_usl;
    private String zak;
    private String stand;
    private String prvs;
    private String code_md;
    private String comentu;
    private String uid;
    private String dopsch;
    private String dir2;
    private String gr_zdorov;
    private String student;
    private String vid_vme;
    private String koefk;
    private String pouh;
    private String otkaz2;
    private String nazna4;
    private String p_per;
    private String npl;
    private String idsh;
    private String idsh2;
    private String dn;
    private String ds_onk;
    private String p_cel;
    private String profil_k;
    private String idsl;
    private String muvr;
    private String muvr_lpu;
    private String date_usl;

    //Содержит в себе врача. Кажется было сделано временно, но это не точно
    private VrachiDS vrachiDS;

    private CritDS critDS;
    private HrrgdDS hrrgdDS;
    private SlKoefDS slKoefDS;

    public UslDS() {
    }

    public SlKoefDS getSlKoef() {
        return slKoefDS;
    }

    public void setSlKoef(SlKoefDS slKoefDS) {
        this.slKoefDS = slKoefDS;
    }

    public HrrgdDS getHrrgd() {
        return hrrgdDS;
    }

    public void setHrrgd(HrrgdDS hrrgdDS) {
        this.hrrgdDS = hrrgdDS;
    }

    public CritDS getCrit() {
        return critDS;
    }

    public void setCrit(CritDS critDS) {
        this.critDS = critDS;
    }

    public VrachiDS getVrachi() {
        return vrachiDS;
    }

    public void setVrachi(VrachiDS vrachiDS) {
        this.vrachiDS = vrachiDS;
    }

    public String getIdstrax() {
        return idstrax;
    }

    public void setIdstrax(String idstrax) {
        this.idstrax = idstrax;
    }

    public String getIdusl() {
        return idusl;
    }

    public void setIdusl(String idusl) {
        this.idusl = idusl;
    }

    public String getIdserv() {
        return idserv;
    }

    public void setIdserv(String idserv) {
        this.idserv = idserv;
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

    public String getGlpu() {
        return glpu;
    }

    public void setGlpu(String glpu) {
        this.glpu = glpu;
    }

    public String getMcod() {
        return mcod;
    }

    public void setMcod(String mcod) {
        this.mcod = mcod;
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

    public String getDate_in() {
        return date_in;
    }

    public void setDate_in(String date_in) {
        this.date_in = date_in;
    }

    public String getDate_out() {
        return date_out;
    }

    public void setDate_out(String date_out) {
        this.date_out = date_out;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String getCode_usl() {
        return code_usl;
    }

    public void setCode_usl(String code_usl) {
        this.code_usl = code_usl;
    }

    public String getEd_col() {
        return ed_col;
    }

    public void setEd_col(String ed_col) {
        this.ed_col = ed_col;
    }

    public String getKol_usl() {
        return kol_usl;
    }

    public void setKol_usl(String kol_usl) {
        this.kol_usl = kol_usl;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public String getSumv_usl() {
        return sumv_usl;
    }

    public void setSumv_usl(String sumv_usl) {
        this.sumv_usl = sumv_usl;
    }

    public String getZak() {
        return zak;
    }

    public void setZak(String zak) {
        this.zak = zak;
    }

    public String getStand() {
        return stand;
    }

    public void setStand(String stand) {
        this.stand = stand;
    }

    public String getPrvs() {
        return prvs;
    }

    public void setPrvs(String prvs) {
        this.prvs = prvs;
    }

    public String getCode_md() {
        return code_md;
    }

    public void setCode_md(String code_md) {
        this.code_md = code_md;
    }

    public String getComentu() {
        return comentu;
    }

    public void setComentu(String comentu) {
        this.comentu = comentu;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDopsch() {
        return dopsch;
    }

    public void setDopsch(String dopsch) {
        this.dopsch = dopsch;
    }

    public String getDir2() {
        return dir2;
    }

    public void setDir2(String dir2) {
        this.dir2 = dir2;
    }

    public String getGr_zdorov() {
        return gr_zdorov;
    }

    public void setGr_zdorov(String gr_zdorov) {
        this.gr_zdorov = gr_zdorov;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getVid_vme() {
        return vid_vme;
    }

    public void setVid_vme(String vid_vme) {
        this.vid_vme = vid_vme;
    }

    public String getKoefk() {
        return koefk;
    }

    public void setKoefk(String koefk) {
        this.koefk = koefk;
    }

    public String getPouh() {
        return pouh;
    }

    public void setPouh(String pouh) {
        this.pouh = pouh;
    }

    public String getOtkaz2() {
        return otkaz2;
    }

    public void setOtkaz2(String otkaz2) {
        this.otkaz2 = otkaz2;
    }

    public String getNazna4() {
        return nazna4;
    }

    public void setNazna4(String nazna4) {
        this.nazna4 = nazna4;
    }

    public String getP_per() {
        return p_per;
    }

    public void setP_per(String p_per) {
        this.p_per = p_per;
    }

    public String getNpl() {
        return npl;
    }

    public void setNpl(String npl) {
        this.npl = npl;
    }

    public String getIdsh() {
        return idsh;
    }

    public void setIdsh(String idsh) {
        this.idsh = idsh;
    }

    public String getIdsh2() {
        return idsh2;
    }

    public void setIdsh2(String idsh2) {
        this.idsh2 = idsh2;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getDs_onk() {
        return ds_onk;
    }

    public void setDs_onk(String ds_onk) {
        this.ds_onk = ds_onk;
    }

    public String getP_cel() {
        return p_cel;
    }

    public void setP_cel(String p_cel) {
        this.p_cel = p_cel;
    }

    public String getProfil_k() {
        return profil_k;
    }

    public void setProfil_k(String profil_k) {
        this.profil_k = profil_k;
    }

    public String getIdsl() {
        return idsl;
    }

    public void setIdsl(String idsl) {
        this.idsl = idsl;
    }

    public String getMuvr() {
        return muvr;
    }

    public void setMuvr(String muvr) {
        this.muvr = muvr;
    }

    public String getMuvr_lpu() {
        return muvr_lpu;
    }

    public void setMuvr_lpu(String muvr_lpu) {
        this.muvr_lpu = muvr_lpu;
    }

    public String getDate_usl() {
        return date_usl;
    }

    public void setDate_usl(String date_usl) {
        this.date_usl = date_usl;
    }

    @Override
    public String toString() {
        return "Usl{" +
                "idstrax='" + idstrax + '\'' +
                ", idusl='" + idusl + '\'' +
                ", idserv='" + idserv + '\'' +
                ", spolis='" + spolis + '\'' +
                ", npolis='" + npolis + '\'' +
                ", glpu='" + glpu + '\'' +
                ", mcod='" + mcod + '\'' +
                ", podr='" + podr + '\'' +
                ", profil='" + profil + '\'' +
                ", det='" + det + '\'' +
                ", date_in='" + date_in + '\'' +
                ", date_out='" + date_out + '\'' +
                ", ds='" + ds + '\'' +
                ", code_usl='" + code_usl + '\'' +
                ", ed_col='" + ed_col + '\'' +
                ", kol_usl='" + kol_usl + '\'' +
                ", tarif='" + tarif + '\'' +
                ", sumv_usl='" + sumv_usl + '\'' +
                ", zak='" + zak + '\'' +
                ", stand='" + stand + '\'' +
                ", prvs='" + prvs + '\'' +
                ", code_md='" + code_md + '\'' +
                ", comentu='" + comentu + '\'' +
                ", uid='" + uid + '\'' +
                ", dopsch='" + dopsch + '\'' +
                ", dir2='" + dir2 + '\'' +
                ", gr_zdorov='" + gr_zdorov + '\'' +
                ", student='" + student + '\'' +
                ", vid_vme='" + vid_vme + '\'' +
                ", koefk='" + koefk + '\'' +
                ", pouh='" + pouh + '\'' +
                ", otkaz2='" + otkaz2 + '\'' +
                ", nazna4='" + nazna4 + '\'' +
                ", p_per='" + p_per + '\'' +
                ", npl='" + npl + '\'' +
                ", idsh='" + idsh + '\'' +
                ", idsh2='" + idsh2 + '\'' +
                ", dn='" + dn + '\'' +
                ", ds_onk='" + ds_onk + '\'' +
                ", p_cel='" + p_cel + '\'' +
                ", profil_k='" + profil_k + '\'' +
                ", idsl='" + idsl + '\'' +
                ", muvr='" + muvr + '\'' +
                ", muvr_lpu='" + muvr_lpu + '\'' +
                ", date_usl='" + date_usl + '\'' +
                '}';
    }
}
