package candyshop;

import entity.Candy;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button btSave;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfPrice;
    @FXML
    private TableView<Candy> table;
    @FXML
    private TableColumn<Candy, Integer> colId;
    @FXML
    private TableColumn<Candy, String> colName;
    @FXML
    private TableColumn<Candy, Double> colPrice;
    @FXML
    private Button btDelete;
    @FXML
    private Button btClose;
    
    
    @FXML
    private void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) btClose.getScene().getWindow();
        stage.close();
    }
    

    @FXML
    private void saveButtonAction(ActionEvent event) {

        if (tfName.getText().trim().isEmpty() && tfPrice.getText().trim().isEmpty()) {
            Exception e = new Exception();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Enter the required date!");
            alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.toString())));
            alert.showAndWait();
        } else {
            Session s = HibernateUtil.getsession();
            Candy c = new Candy();
            c.setName(tfName.getText());
            c.setPrice(Double.parseDouble(tfPrice.getText()));
            s.save(c);
            updateTable();
        }
    }
    
    
    @FXML
    private void deleteButtonAction(ActionEvent event) {
        
        try {
            Candy selectedrow = table.getSelectionModel().getSelectedItem();

            Session s = HibernateUtil.getsession();
            Transaction tr = s.beginTransaction();
            Candy c1 = (Candy) s.get(Candy.class, selectedrow.getId());
            s.delete(c1);
            tr.commit();
            updateTable();
            
        } catch (RuntimeException ex) {

            Exception e = new Exception();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Select data to delete!");
            alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.toString())));
            alert.showAndWait();

        }
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        updateTable();

    }

    public void updateTable() {
        colId.setCellValueFactory(new PropertyValueFactory<Candy, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Candy, String>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<Candy, Double>("price"));
        table.setItems(getCandy());
    }

    public ObservableList<Candy> getCandy() {

        ObservableList<Candy> list = FXCollections.observableArrayList();
        Session s = HibernateUtil.getsession();
        List<Candy> listing = s.createCriteria(Candy.class).list();
        for (Candy cn : listing) {
            list.add(cn);
        }
        return list;

    }
 
}
