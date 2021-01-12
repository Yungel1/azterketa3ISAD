package ehu.isad.Controllers.UI;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import src.Controllers.DB.TxanponDBKud;
import src.Main;
import src.Model.Txanpon;
import src.Model.TxanponInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class TxanponKud implements Initializable {

    private Main mainApp;

    @FXML
    private Button sartuBTN;

    @FXML
    private Button gordeBTN;

    @FXML
    private ComboBox<String> txanponCB;

    @FXML
    private TableView<TxanponInfo> txanponTable;

    @FXML
    private TableColumn<TxanponInfo, Integer> idTC;

    @FXML
    private TableColumn<TxanponInfo, String> txanponTC;

    @FXML
    private TableColumn<TxanponInfo, ?> noizTC;

    @FXML
    private TableColumn<TxanponInfo, Float> zenbatTC;

    @FXML
    private TableColumn<TxanponInfo, Float> bolumenaTC;

    @FXML
    private TableColumn<TxanponInfo, Image> portaeraTC;

    private ObservableList<TxanponInfo> txanponak;

    private List<TxanponInfo> txanponZer;

    @FXML
    void onClickSartu(ActionEvent event) throws IOException {
        Float[] zenbat=readFromUrl(txanponCB.getValue());
        txertatuTaulan(zenbat,txanponCB.getValue());
    }

    @FXML
    void onClickGorde(ActionEvent event) {
        TxanponDBKud.getInstance().taulaGorde(txanponZer);
        erakutsiTaula();
    }

    private Float[] readFromUrl(String txanpon) throws IOException {

        String inputLine;

        URL coinmarket = new URL("https://api.gdax.com/products/"
                + txanpon + "-eur/ticker");
        URLConnection yc = coinmarket.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        inputLine = in.readLine();
        in.close();

        Gson gson = new Gson();
        Txanpon txanpona = gson.fromJson(inputLine, Txanpon.class);

        return new Float[]{txanpona.getPrice(), txanpona.getVolume()};

    }

    private void txertatuTaulan(Float[] zenbat, String mota){
        List<TxanponInfo> txanponList = TxanponDBKud.getInstance().txanponInfoLortu();
        //Parte1
        int id;
        int pos=-1;
        TxanponInfo ti;
        int kont;
        boolean aurkitua;
        String mota1;
        for(int i=0;i<txanponList.size();i++){
            id=-1;
            ti=txanponList.get(i);
            mota1=ti.getMota();
            aurkitua=false;
            kont=0;
            while(kont<i){
                if(mota1.equals(txanponList.get(kont).getMota())&&txanponList.get(kont).getId()>id){
                    id=txanponList.get(kont).getId();
                    pos=kont;
                    aurkitua=true;
                }
                kont++;
            }
            if(aurkitua&&txanponList.get(pos).getZenbat()>ti.getZenbat()){
                ti.setPortaera(new Image("irudiak/down.png"));
            }
            else if(aurkitua&&txanponList.get(pos).getZenbat()<ti.getZenbat()){
                ti.setPortaera(new Image("irudiak/up.png"));
            }
            else{
                ti.setPortaera(new Image("irudiak/equal.png"));
            }
        }
        //Parte2
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ti=new TxanponInfo(txanponList.get(txanponList.size()-1).getId()+1,mota,dtf.format(now),zenbat[0],zenbat[1]);
        id=-1;
        pos=-1;
        aurkitua=false;
        for(int i=0;i<txanponList.size();i++){
            if(mota.equals(txanponList.get(i).getMota())&&txanponList.get(i).getId()>id){
                id=txanponList.get(i).getId();
                pos=i;
                aurkitua=true;
            }
        }
        if(aurkitua&&txanponList.get(pos).getZenbat()>ti.getZenbat()){
            ti.setPortaera(new Image("irudiak/down.png"));
        }
        else if(aurkitua&&txanponList.get(pos).getZenbat()<ti.getZenbat()){
            ti.setPortaera(new Image("irudiak/up.png"));
        }
        else{
            ti.setPortaera(new Image("irudiak/equal.png"));
        }

        txanponList.add(ti);
        txanponZer=txanponList;
        txanponak = FXCollections.observableArrayList(txanponList);


        txanponTable.setEditable(true);
        //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
        idTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        txanponTC.setCellValueFactory(new PropertyValueFactory<>("mota"));
        noizTC.setCellValueFactory(new PropertyValueFactory<>("noiz"));
        zenbatTC.setCellValueFactory(new PropertyValueFactory<>("zenbat"));
        bolumenaTC.setCellValueFactory(new PropertyValueFactory<>("bolumena"));

        portaeraTC.setCellValueFactory(new PropertyValueFactory<>("portaera"));

        portaeraTC.setCellFactory(p -> new TableCell<>() {
            public void updateItem(Image image, boolean empty) {
                if (image != null && !empty){
                    final ImageView imageview = new ImageView();
                    imageview.setFitHeight(25);
                    imageview.setFitWidth(40);
                    imageview.setImage(image);
                    setGraphic(imageview);
                    setAlignment(Pos.CENTER);
                    // tbData.refresh();
                }else{
                    setGraphic(null);
                    setText(null);
                }
            };
        });

        //add your data to the table here.
        txanponTable.setItems(txanponak);
    }

    public void setLiburuakApp(Main main) {
        mainApp=main;
    }

    public void sartuCB(){

        txanponCB.getItems().add("BTC");
        txanponCB.getItems().add("ETH");
        txanponCB.getItems().add("LTC");

        txanponCB.getSelectionModel().selectFirst();
    }

    public void erakutsiTaula(){
        List<TxanponInfo> txanponList = TxanponDBKud.getInstance().txanponInfoLortu();
        txanponZer=txanponList;
        int id;
        int pos=-1;
        TxanponInfo ti;
        int kont;
        boolean aurkitua;
        String mota;
        for(int i=0;i<txanponList.size();i++){
            id=-1;
            ti=txanponList.get(i);
            mota=ti.getMota();
            aurkitua=false;
            kont=0;
            while(kont<i){
                if(mota.equals(txanponList.get(kont).getMota())&&txanponList.get(kont).getId()>id){
                    id=txanponList.get(kont).getId();
                    pos=kont;
                    aurkitua=true;
                }
                kont++;
            }
            if(aurkitua&&txanponList.get(pos).getZenbat()>ti.getZenbat()){
                ti.setPortaera(new Image("irudiak/down.png"));
            }
            else if(aurkitua&&txanponList.get(pos).getZenbat()<ti.getZenbat()){
                ti.setPortaera(new Image("irudiak/up.png"));
            }
            else{
                ti.setPortaera(new Image("irudiak/equal.png"));
            }
        }
        txanponak = FXCollections.observableArrayList(txanponList);


        txanponTable.setEditable(true);
        //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
        idTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        txanponTC.setCellValueFactory(new PropertyValueFactory<>("mota"));
        noizTC.setCellValueFactory(new PropertyValueFactory<>("noiz"));
        zenbatTC.setCellValueFactory(new PropertyValueFactory<>("zenbat"));
        bolumenaTC.setCellValueFactory(new PropertyValueFactory<>("bolumena"));

        portaeraTC.setCellValueFactory(new PropertyValueFactory<>("portaera"));

        portaeraTC.setCellFactory(p -> new TableCell<>() {
            public void updateItem(Image image, boolean empty) {
                if (image != null && !empty){
                    final ImageView imageview = new ImageView();
                    imageview.setFitHeight(25);
                    imageview.setFitWidth(40);
                    imageview.setImage(image);
                    setGraphic(imageview);
                    setAlignment(Pos.CENTER);
                    // tbData.refresh();
                }else{
                    setGraphic(null);
                    setText(null);
                }
            };
        });

        //add your data to the table here.
        txanponTable.setItems(txanponak);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sartuCB();
        erakutsiTaula();
    }
}
