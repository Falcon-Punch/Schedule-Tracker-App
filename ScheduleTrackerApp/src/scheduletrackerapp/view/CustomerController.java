/*
*    File Name  : CustomerController.java
*    Author     : Joseph Schell
*    Date       : 12/24/2018
*/
package scheduletrackerapp.view;

import scheduletrackerapp.ScheduleTrackerApp;
import scheduletrackerapp.model.MessageAlert;
import scheduletrackerapp.model.Customer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomerController
{
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private Connection connection;
    private String userName = "";
    private Stage stage;
    
    @FXML private TableView<Customer> tableView;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> address2Column;
    @FXML private TableColumn<Customer, String> cityColumn;
    @FXML private TableColumn<Customer, String> postalCodeColumn;
    @FXML private TableColumn<Customer, String> countryColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;

    @FXML private void initialize() throws IOException, ClassNotFoundException
    {
        // Opted for Lambda expressions to set the appointment data to the cells of the table
        // This seemed like a more efficient way due to less lines of code for each cell.
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        address2Column.setCellValueFactory(cellData -> cellData.getValue().address2Property());
        cityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        postalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().postalCodeProperty());
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
    }

    /*
    *   Grabs customer data from Database and enters the data into the cells
    *   of the TableView
    *
    *   @param: None
    */
    private void updateCustomers() throws ClassNotFoundException
    {
        ResultSet resultSet = getCustomersFromDB();
        customers.clear();
        
        try
        {
            while(resultSet.next())
            {
                int customerID = resultSet.getInt("customerid");
                String name = resultSet.getString("customerName");
                int addressID = resultSet.getInt("addressId");
                String address = resultSet.getString("address");
                String address2 = resultSet.getString("address2");
                String city = resultSet.getString("city");
                String postalCode = resultSet.getString("postalCode");
                String phone = resultSet.getString("phone");
                String country = resultSet.getString("country");
                Customer customer = new Customer(customerID, name, addressID, 
                        address, address2, city, postalCode, phone, country);
                customers.add(customer);
            }
            tableView.setItems(customers);
        } 
        catch(SQLException ex)
        {
            Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    *   Retrieves customers from the Database
    *
    *   @param: none
    */
    public ResultSet getCustomersFromDB() throws ClassNotFoundException
    {
        ResultSet resultSet = null;
        Statement statement;
        
        try
        {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT customerid, customerName, customer.addressId, "
                    + "address, address2, postalCode, phone, city, country "
                    + "FROM customer, address, city, country WHERE "
                    + "customer.addressId = address.addressid AND address.cityId "
                    + "= city.cityid AND city.countryId = country.countryid");
        } 
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return resultSet;
    }
    
    /*
    *   Shows all UI settings on the Customer stage
    *
    *   @param: Stage primaryStage
    *           Connection connection
    *           String userName
    */
    public static void showUI(Stage primaryStage, Connection connection, 
               String userName) throws IOException, ClassNotFoundException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleTrackerApp.class.getResource("view/Customer.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage stage = new Stage();
        stage.setTitle("Customer Database");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        stage.setScene(scene);

        CustomerController customerController = loader.getController();
        customerController.setStage(stage);
        customerController.setConnection(connection);
        customerController.setUserName(userName);
        customerController.updateCustomers();

        stage.showAndWait();
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
    *   Sets the username
    *
    *   @param: String userName
    */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    /*
    *   Deletes a customer from the Database
    *
    *   @param: Customer customer
    */
    private void deleteCustomer(Customer customer)
    {
        PreparedStatement preparedStatement;
        
        try
        {
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM customer WHERE customerid =?");
            preparedStatement.setString(1, Integer.toString(customer.getID()));
            preparedStatement.execute();
        } 
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /*
    *   Provides response for when a button is clicked. Provides options
    *   for actions based on boolean parameter
    *
    *   @param: boolean delete
    */
    private void handleButton(boolean delete) throws ClassNotFoundException, IOException
    {
        Customer customer = tableView.getSelectionModel().getSelectedItem();
        
        if(customer != null)
        {
            if(delete)
            {
                deleteCustomer(customer);
            }
            else
            {
                //EditCustomerController.showUI(stage, connection, 
                        //customer, "Modify Customer", userName);
            }
            updateCustomers();
        } 
        else
        {
            MessageAlert.noSelectionAlert("customer");
        }
    }
    
    /*
    *   Provides response for when the Add button is clicked.
    *
    *   @param: none
    */
    @FXML private void addButtonClicked() throws IOException, ClassNotFoundException
    {
        ///EditCustomerController.showUI(stage, connection, null, "Add Customer", userName);
        updateCustomers();
    }
     
    /*
    *   Provides response for when the Delete button is clicked.
    *
    *   @param: none
    */
    @FXML private void deleteButtonClicked() throws ClassNotFoundException, IOException
    {
        handleButton(true);
    }
    
    /*
    *   Provides response for when the Edit button is clicked.
    *
    *   @param: none
    */
    @FXML private void editButtonClicked() throws IOException, ClassNotFoundException
    {
        handleButton(false);
    }
}
