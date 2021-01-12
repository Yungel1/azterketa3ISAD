package ehu.isad.Model;


import java.time.LocalDate;

public class Eskaneoa {

    private String url;
    private String cms;
    private String version;
    private LocalDate lastUpdate;

    public Eskaneoa(String pUrl, String pCms, String pVersion,LocalDate pData){
        url=pUrl;
        cms=pCms;
        version=pVersion;
        lastUpdate=pData;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getUrl() {
        return url;
    }

    public String getCms() {
        return cms;
    }

    public String getVersion() {
        return version;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastupdated(LocalDate ld) {
    }
}
