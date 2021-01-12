package ehu.isad.Controllers.DB;

import ehu.isad.Model.Eskaneoa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class CMSDBKud {

    private String[] cmsak = new String[4];

    private static final CMSDBKud instance = new CMSDBKud();

    public static CMSDBKud getInstance() {
        return instance;
    }

    private CMSDBKud() {
        cmsak[0] = "WordPress";
        cmsak[1] = "Joomla";
        cmsak[2] = "phpMyAdmin";
        cmsak[3] = "Drupal";
    }

    public List<Eskaneoa> eskaneoInfoLortu() {
        List<Eskaneoa> emaitza = new ArrayList<>();
        DBKudeatzaileSQLITE dbkud = DBKudeatzaileSQLITE.getInstantzia();
        String cmsMota="";
        for (int i=0;i<cmsak.length;i++) {
            if (i == cmsak.length-1) {
                cmsMota = cmsMota+" s.string LIKE '%" + cmsak[i] + "%'";
            } else {
                cmsMota = cmsMota+" s.string LIKE '%" + cmsak[i] + "%' OR";
            }
        }
        String cmsNOTmota=cmsMota.replace("OR","AND");
        cmsNOTmota=cmsNOTmota.replace("LIKE","NOT LIKE");

        String query = "select s.string,s.version,t.target,t.LastUpdate from targets as t " +
                "LEFT JOIN scans as s ON s.target_id = t.target_id where ("+cmsMota+")" +
                " GROUP BY(t.target_id) union select 'unknown 0',s.version,t.target,t.LastUpdate " +
                "from targets as t " +
                "LEFT JOIN scans as s ON s.target_id = t.target_id where ("+cmsNOTmota+")" +
                " GROUP BY(t.target_id)";
        ResultSet rs = dbkud.execSQL(query);

        try {
            while (rs.next()) {

                String cmsVersion = rs.getString("string");
                boolean aurkitua=false;
                int auk=0;
                while(!aurkitua && auk< cmsak.length){
                    if(cmsVersion.indexOf(cmsak[auk])!=-1) {
                        cmsVersion = cmsVersion.substring(cmsVersion.indexOf(cmsak[auk]));
                        aurkitua=true;
                    }
                    auk++;
                }

                String url = rs.getString("target");
                String[] banatuta = cmsVersion.split(" ");
                String cms = banatuta[0];
                String version = banatuta[1];
                String data = rs.getString("LastUpdate");
                LocalDate date = LocalDate.parse(data);
                Eskaneoa eskaneo = new Eskaneoa(url,cms,version,date);
                emaitza.add(eskaneo);
            }
        }catch (SQLException e){
            System.err.println(e);
        }


        return emaitza;
    }
    public void dataEguneratu(String pUrl, LocalDate pData){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedString = pData.format(formatter);

        DBKudeatzaileSQLITE dbkud = DBKudeatzaileSQLITE.getInstantzia();
        String query = "UPDATE targets SET LastUpdate='"+formattedString+"' WHERE target LIKE'%"+pUrl+"%';";
        dbkud.execSQL(query);

    }
}
