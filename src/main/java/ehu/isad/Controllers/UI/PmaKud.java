package ehu.isad.Controllers.UI;

import ehu.isad.App;
import ehu.isad.Controllers.DB.PmaDBKud;
import ehu.isad.Model.Pma;
import ehu.isad.Utils.MessageDigestForUrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;

import static ehu.isad.Utils.MessageDigestForUrl.getDigest;

public class PmaKud implements Initializable {

    private App app;

    @FXML
    private TextField urlTF;

    @FXML
    private Button checkBTN;

    @FXML
    private TableView<?> infoT;

    @FXML
    private TableColumn<?, ?> urlTC;

    @FXML
    private TableColumn<?, ?> md5TC;

    @FXML
    private TableColumn<?, ?> versionTC;

    @FXML
    private Label egoeraL;

    @FXML
    void onClick(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        //lortu md5
        URL url = new URL(urlTF.getText());
        System.out.println(urlTF.getText());
		InputStream is = url.openStream();
		MessageDigest md = MessageDigest.getInstance("MD5");
		String digest = MessageDigestForUrl.getDigest(is, md, 2048);

        //bilatu datubasean
        PmaDBKud pmaDBKud = PmaDBKud.getInstance();
        List<Pma> list = pmaDBKud.pmaInfoLortu(digest);
        txertatuTaulan(list);

    }

    private void txertatuTaulan(List<Pma> list){

    }


    public void setPmaApp(App pApp) {
        app=pApp;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
