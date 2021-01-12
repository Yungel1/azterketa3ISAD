package ehu.isad.Model;

public class Pma {

    private int id;
    private String version;
    private String md5;
    private int path;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Pma(int pId, String pVersion, String pMd5, int pPath){
        id=pId;
        version=pVersion;
        md5=pMd5;
        path=pPath;
    }

    public int getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getMd5() {
        return md5;
    }

    public int getPath() {
        return path;
    }
}
