/*
*    File Name  : CalendarController.java
*    Author     : Joseph Schell
*    Date       : 12/18/2018
*/
package scheduletrackerapp.view;

import scheduletrackerapp.model.MessageAlert;
import scheduletrackerapp.model.Appointment;
import scheduletrackerapp.model.DateTime;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class CalendarController
{
    private final ObservableList<Appointment> calendar = 
            FXCollections.observableArrayList();
    private final DateFormat DATE_FORMAT = 
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String today;
    private String tomorrow;
    private String reminder = "";
    private String userName = "";
    private Connection connection;
    private Stage stage;
    
    @FXML private TableView<Appointment> calendarTableView;
    @FXML private TableColumn<Appointment, String> startTableColumn;
    @FXML private TableColumn<Appointment, String> endTableColumn;
    @FXML private TableColumn<Appointment, String> titleTableColumn;
    @FXML private TableColumn<Appointment, String> typeTableColumn;
    @FXML private TableColumn<Appointment, String> customerTableColumn;
    @FXML private TableColumn<Appointment, String> consultantTableColumn;
    @FXML private ComboBox<String> viewComboBox;
    
    @FXML private void initialize() throws IOException, 
            ClassNotFoundException, ParseException
    {
        // Opted for Lambda expressions to set the appointment data to the cells of the table
        // This seemed like a more efficient way due to less lines of code for each cell.
        startTableColumn.setCellValueFactory(cellData -> cellData.getValue().startProperty());
        endTableColumn.setCellValueFactory(cellData -> cellData.getValue().endProperty());
        titleTableColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        typeTableColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        customerTableColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        consultantTableColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());

        viewComboBox.getItems().addAll("All", "Week", "Month");
        viewComboBox.getSelectionModel().select(0);

        Calendar cal = Calendar.getInstance();
        today = DATE_FORMAT.format(cal.getTime());
        cal.add(Calendar.MONTH, +1);
        tomorrow = DATE_FORMAT.format(cal.getTime());

        // Establish connection to Database server
        connectToDB();

        // Fill table with data
        fillTable(true);

        userName = LoginController.showUI(stage, connection);
        
        if("".equals(userName))
        {
            Platform.exit();
        } 
        else if(!reminder.equals(""))
        {
            MessageAlert.reminderAlert(reminder);
        }
    }

    /*
    *   Connects to company Databse using given credentials:
    *
    *   Username: U05tz8
    *   Password: 53688606536
    *   URL: jdbc:mysql://52.206.157.109/U05tz8
    *
    *   @param: none
    */
    private void connectToDB()
    {
        String username = "U05tz8";
        String password = "53688606536";
        String URL = "jdbc:mysql://52.206.157.109/U05tz8";
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, username, password);
        } 
        catch (ClassNotFoundException | SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    /*
    *   Fills calendar table with data from Database
    *
    *   @param: boolean init
    */
    private void fillTable(boolean init) throws ParseException
    {
        ResultSet resultSet = getAppointmentsFromDB();
        calendar.clear();
        
        try
        {
            while (resultSet.next())
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
                
                if(init)
                {
                    if(reminder.equals(""))
                    {
                        if(DateTime.checkFifteenMinutes(today, start))
                        {
                            String[] parts = start.split(" ");
                            reminder = "You have a meeting at " + parts[1] + " (Today!)";
                        }
                    }
                }
            }
            calendarTableView.setItems(calendar);             
        }
        catch(SQLException ex)
        {
            Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /*
    *   Fills calendar table with data from Database, after combo box selection
    *
    *   @param: boolean init
    */
    private void fillComboSelection(boolean init) throws ParseException
    {
        ResultSet resultSet = getAppointmentsFromDB();
        calendar.clear();
        
        try
        {
            while (resultSet.next())
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
                
                
                if(init)
                {
                    if(DateTime.checkWeek(today, start))
                    {
                        calendar.add(appointment);
                    }
                }
                else
                {
                    if(DateTime.checkMonth(today, start))
                    {
                        calendar.add(appointment);
                    }
                }
            }
            calendarTableView.setItems(calendar);             
        }
        catch(SQLException ex)
        {
            Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    *   Provides response for when the combo box is clicked.
    *
    *   @param: none
    */
    @FXML private void comboBoxClicked() throws ParseException
    {
        String boxSelection = viewComboBox.getValue();
        //int week = (Calendar.DAY_OF_YEAR + 7);
        //int month = (Calendar.MONTH + 1);
                
        if(null == boxSelection)
        {
            fillTable(true);
        } 
        else switch (boxSelection)
        {
            case "Month":
                calendar.clear();
                fillComboSelection(false);
                break;
            case "Week":
                calendar.clear();
                fillComboSelection(true);
                break;
            default:
                fillTable(true);
                break;
        }
    }
    
    /*
    *   Retrieves appointments from the Database
    *
    *   @param: none
    */
    private ResultSet getAppointmentsFromDB()
    {
        ResultSet resultSet = null;
        Statement statement;
        
        try
        {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT appointmentid, start, title, type, customerName, "
                    + "appointment.customerId, userName, appointment.userId, "
                    + "description, location, contact, url, end FROM appointment, "
                    + "customer, user WHERE appointment.userId = user.userid "
                    + "AND appointment.customerId = customer.customerid ORDER BY start");
        } 
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return resultSet;
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
    *   Deletes an appointment from the Database
    *
    *   @param: Appointment appointment
    */
    private void deleteAppointment(Appointment appointment)
    {
        PreparedStatement preparedStatement;
        
        try
        {
            preparedStatement = connection.prepareStatement("DELETE "
                    + "FROM appointment WHERE appointmentid =?");
            preparedStatement.setString(1, Integer.toString(appointment.getID()));
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
    private void handleButton(boolean delete) throws ParseException, 
            IOException, ClassNotFoundException
    {
        Appointment appointment = calendarTableView.getSelectionModel().getSelectedItem();

        if(appointment != null) 
        {
            if(delete)
            {
                deleteAppointment(appointment);
            }
            else
            {
                AppointmentController.showUI(stage, connection, appointment, 
                        "Edit Appointment", userName);
            }
            fillTable(false);
        } 
        else 
        {
            MessageAlert.noSelectionAlert("appointment");
        }
    }
    
    /*
    *   Provides response for when the Add button is clicked.
    *
    *   @param: none
    */
    @FXML private void addButtonClicked() throws IOException, 
            ClassNotFoundException, ParseException
    {
        AppointmentController.showUI(stage, connection, null, 
                "Add Appointment", userName);
        fillTable(false);
    }
    
    /*
    *   Provides response for when the Customer button is clicked.
    *
    *   @param: none
    */
    @FXML private void customerButtonClicked() throws IOException, 
            ClassNotFoundException, ParseException
    {
        CustomerController.showUI(stage, connection, userName);
        fillTable(false);
    }

    /*
    *   Provides response for when the Delete button is clicked.
    *
    *   @param: none
    */
    @FXML private void deleteButtonClicked() throws ParseException, 
            IOException, ClassNotFoundException
    {
        handleButton(true);
    }
    
    /*
    *   Provides response for when the Edit button is clicked.
    *
    *   @param: none
    */
    @FXML private void editButtonClicked() throws IOException, 
            ClassNotFoundException, ParseException
    {
        handleButton(false);
    }

    @FXML private void reportButtonClicked() throws IOException
    {
        ReportsController.showUI(stage, connection);
    }
}
