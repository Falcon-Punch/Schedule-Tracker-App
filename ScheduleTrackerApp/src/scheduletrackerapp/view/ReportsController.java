/*
*    File Name  : ReportsController.java
*    Author     : Joseph Schell
*    Date       : 12/23/2018
*/
package scheduletrackerapp.view;

import scheduletrackerapp.ScheduleTrackerApp;
import scheduletrackerapp.model.Appointment;
import scheduletrackerapp.model.DateTime;
import scheduletrackerapp.model.Type;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReportsController
{
    private Connection connection;
    private Stage stage;

    @FXML private TableView tableView;
    @FXML private TableColumn tableColumn1;
    @FXML private TableColumn tableColumn2;
    @FXML private TableColumn tableColumn3;
    @FXML private TableColumn tableColumn4;
    @FXML private TableColumn tableColumn5;
    @FXML private TableColumn tableColumn6;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<String> personComboBox;
    @FXML private Label personLabel;


    @FXML private void initialize() throws IOException, ClassNotFoundException
    {
        typeComboBox.getItems().addAll("Appointments by Month", 
                "Consultant Schedule", "Customer Schedule");
        typeComboBox.getSelectionModel().select(0);
        personComboBox.setDisable(true);
        personLabel.setDisable(true);
    }
    
    /*
    *   Fills Reports table with data from Database
    *
    *   @param: boolean consultant
    */
    private void fillTable(boolean consultant) 
            throws ClassNotFoundException, ParseException
    {
        tableColumn1.setText("Start");
        tableColumn2.setText("End");
        tableColumn3.setText("Title");
        tableColumn4.setText("Type");
        tableColumn5.setText("Customer");
        tableColumn6.setText("Consultant");

        tableColumn1.setCellValueFactory(new PropertyValueFactory<>("start"));
        tableColumn2.setCellValueFactory(new PropertyValueFactory<>("end"));
        tableColumn3.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableColumn4.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumn5.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tableColumn6.setCellValueFactory(new PropertyValueFactory<>("userName"));

        ObservableList<Appointment> calendar = FXCollections.observableArrayList();
        String query;
        
        if(consultant)
        {
            query = "SELECT appointmentid, start, title, type, customerName, "
                    + "appointment.customerId, userName, appointment.userId, "
                    + "description, location, contact, url, end FROM appointment, "
                    + "customer, user WHERE userName='" + personComboBox.getValue() 
                    + "' AND appointment.userId = user.userid AND appointment.customerId "
                    + "= customer.customerid ORDER BY start";
        } 
        else
        {
            query = "SELECT appointmentid, start, title, type, customerName, "
                    + "appointment.customerId, userName, appointment.userId, "
                    + "description, location, contact, url, end FROM appointment, "
                    + "customer, user WHERE customerName='" + personComboBox.getValue() 
                    + "' AND appointment.userId = user.userid AND appointment.customerId "
                    + "= customer.customerid ORDER BY start";
        }
        
        ResultSet resultSet = getDataFromDB(query);

        try
        {
            while(resultSet.next())
            {
                int appointmentID = resultSet.getInt("appointmentid");
                String start = DateTime.setDateLocal(resultSet.getString("start"));
                String title = resultSet.getString("title");
                String type = resultSet.getString("type");
                String customerName = resultSet.getString("customerName");
                int customerID = resultSet.getInt("customerId");
                String username = resultSet.getString("userName");
                int userId = resultSet.getInt("userId");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                String contact = resultSet.getString("contact");
                String URL = resultSet.getString("url");
                String end = DateTime.setDateLocal(resultSet.getString("end"));
                Appointment appointment = new Appointment(appointmentID, start, 
                        title, type, customerName, customerID, username, userId, 
                        description, location, contact, URL, end);
                calendar.add(appointment);
            }
            tableView.setItems(calendar);
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    *   Fills combo boxes with options for user to select when creating new report
    *
    *   @param: String type
    *           String query
    *           String columnName
    */    
    private void fillComboBox(String type, String query, String columnName) 
            throws ClassNotFoundException
    {
        personLabel.setText(type);
        personLabel.setDisable(false);
        personComboBox.setDisable(false);
        ResultSet resultSet = getDataFromDB(query);
        ArrayList<String> list = new ArrayList<>();
        
        try
        {
            while(resultSet.next())
            {
                String item = resultSet.getString(columnName);
                list.add(item);
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        personComboBox.getItems().clear();
        personComboBox.getItems().addAll(list);
        personComboBox.getSelectionModel().select(0);
    }

    /*
    *   Retrieves data from the Database
    *
    *   @param: String query
    */
    private ResultSet getDataFromDB(String query) throws ClassNotFoundException
    {
        ResultSet resultSet = null;
        Statement statement;
        
        try
        {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } 
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        
        return resultSet;
    }
    
    /*
    *   Provides response for when the Generate button is clicked.
    *
    *   @param: none
    */
    @FXML private void generateButtonClicked() throws ClassNotFoundException, ParseException
    {
        String type = typeComboBox.getValue();
        
        switch(type)
        {
            case "Appointments by Month":
                appointmentsByMonth();
                break;
            case "Consultant Schedule":
                consultantSchedule();
                break;
            case "Customer Schedule":
                customerSchedule();
                break;
        }
    }
    
    /*
    *   Provides response for when the Combo Box button is clicked.
    *
    *   @param: none
    */
    @FXML private void comboBoxClicked() throws ClassNotFoundException
    {
        String type = typeComboBox.getValue();
        
        switch(type)
        {
            case "Appointments by Month":
                personComboBox.setDisable(true);
                personLabel.setDisable(true);
                break;
            case "Consultant Schedule":
                fillComboBox("Consultant", "SELECT username FROM user", 
                        "username");
                break;
            case "Customer Schedule":
                fillComboBox("Customer", "SELECT customerName FROM customer", 
                        "customerName");
                break;
        }
    }

    /*
    *   Sets the connection
    *
    *   @param: Connection connection
    */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }
    
    /*
    *   Sets the stage
    *
    *   @param: Stage stage
    */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }
    
    /*
    *   Filters appointments by month and displays the data
    *
    *   @param: none
    */
    private void appointmentsByMonth() throws ClassNotFoundException
    {
        ObservableList<Type> table = FXCollections.observableArrayList();
        ResultSet resultSet = getDataFromDB("SELECT type,COUNT(*) as count "
                + "FROM appointment GROUP BY type");
        tableColumn1.setText("Type");
        tableColumn2.setText("Count");
        tableColumn3.setText("");
        tableColumn4.setText("");
        tableColumn5.setText("");
        tableColumn6.setText("");

        tableColumn1.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumn2.setCellValueFactory(new PropertyValueFactory<>("count"));
        tableColumn3.setCellValueFactory(new PropertyValueFactory<>(""));
        tableColumn4.setCellValueFactory(new PropertyValueFactory<>(""));
        tableColumn5.setCellValueFactory(new PropertyValueFactory<>(""));
        tableColumn6.setCellValueFactory(new PropertyValueFactory<>(""));

        try
        {
            while(resultSet.next())
            {
                String type = resultSet.getString("type");
                int count = resultSet.getInt("count");

                Type type2 = new Type(count, type);
                table.add(type2);
            }
            tableView.setItems(table);
        } 
        catch(SQLException ex)
        {
            Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    *   Filters appointments by specific consultants and displays the data
    *
    *   @param: none
    */
    private void consultantSchedule() throws ClassNotFoundException, ParseException
    {
        fillTable(true);
    }
    
    /*
    *   Filters appointments by specific customers and displays the data
    *
    *   @param: none
    */
    private void customerSchedule() throws ClassNotFoundException, ParseException
    {
        fillTable(false);
    }
    
    /*
    *   Shows all UI settings on the Reports stage
    *
    *   @param: Stage primaryStage
    *           Connection connection
    */
    public static void showUI(Stage primaryStage, 
            Connection connection) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleTrackerApp.class.getResource("view/Reports.fxml"));
        BorderPane page = (BorderPane) loader.load();

        Stage stage = new Stage();
        stage.setTitle("Generate Reports");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        stage.setScene(scene);

        ReportsController reportsController = loader.getController();
        reportsController.setStage(stage);
        reportsController.setConnection(connection);

        stage.showAndWait();
    }
}
