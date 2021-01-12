package ehu.isad.Controllers.DB;

import ehu.isad.Model.Pma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PmaDBKud {

    private static final PmaDBKud instance = new PmaDBKud();

    public static PmaDBKud getInstance() {
        return instance;
    }

    public List<Pma> pmaInfoLortu(String pMd5,String pUrl) {
        String query = "select idCMS,version,md5,path from checksums where md5='"+pMd5+"'";
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        ResultSet rs = dbKudeatzaile.execSQL(query);

        List<Pma> emaitza = new ArrayList<>();
        try {
            while (rs.next()) {
                int idCMS = rs.getInt("idCMS");
                String version = rs.getString("version");
                String md5 = rs.getString("md5");
                int path = rs.getInt("path");
                Pma pma = new Pma(idCMS, version, md5, path);
                pma.setUrl(pUrl);
                emaitza.add(pma);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return emaitza;
    }
    public void sartuDB(Pma pma){
        String query = "insert into checksums(version,md5,path) values('"+pma.getVersion()+"'+'"+pma.getMd5()+"0"+")";
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        dbKudeatzaile.execSQL(query);
    }
}
