package ehu.isad.Controllers.UI;

import ehu.isad.App;
import ehu.isad.Controllers.DB.PmaDBKud;
import ehu.isad.Model.Pma;
import ehu.isad.Utils.MessageDigestForUrl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;

public class PmaKud implements Initializable {

    private App app;

    @FXML
    private TextField urlTF;

    @FXML
    private Button checkBTN;

    @FXML
    private TableView<Pma> infoT;

    @FXML
    private TableColumn<Pma, String> urlTC;

    @FXML
    private TableColumn<Pma, String> md5TC;

    @FXML
    private TableColumn<Pma, String> versionTC;

    @FXML
    private Label egoeraL;

    private List<Pma> zerrenda;

    @FXML
    void onClick(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        //lortu md5
        URL url = new URL(urlTF.getText()+"/README");
		InputStream is = url.openStream();
		MessageDigest md = MessageDigest.getInstance("MD5");
		String digest = MessageDigestForUrl.getDigest(is, md, 2048);

        //bilatu datubasean
        PmaDBKud pmaDBKud = PmaDBKud.getInstance();
        List<Pma> list = pmaDBKud.pmaInfoLortu(digest,urlTF.getText());
        if(list.isEmpty()){
            egoeraL.setText("Ez da datubasean aurkitu");
            Pma pmaAux=new Pma(0,"",digest,0);
            pmaAux.setUrl(urlTF.getText());
            list.add(pmaAux);
        }
        else{
            egoeraL.setText("Datubasean zegoen");
        }
        if(zerrenda==null){
            zerrenda=list;
        }
        else{
            zerrenda.addAll(list);
        }
        txertatuTaulan();

    }

    private void txertatuTaulan(){

        ObservableList<Pma> zerrendaO = FXCollections.observableArrayList(zerrenda);

        infoT.setEditable(true);
        //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
        urlTC.setCellValueFactory(new PropertyValueFactory<>("url"));
        md5TC.setCellValueFactory(new PropertyValueFactory<>("md5"));
        versionTC.setCellValueFactory(new PropertyValueFactory<>("version"));

        infoT.setItems(zerrendaO);


    }


    public void setPmaApp(App pApp) {
        app=pApp;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
