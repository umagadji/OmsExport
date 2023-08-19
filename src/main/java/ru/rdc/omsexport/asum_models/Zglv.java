package ru.rdc.omsexport.asum_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Zglv {
    private String idlist;
    private String crc;
    private String version;
    private String data;
    private String filename;

}
