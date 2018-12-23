/*
*    File Name  : SchedulerTrackerApp.java
*    Author     : Joseph Schell
*    Date       : 12/20/2018
*/
package scheduletrackerapp;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
//import scheduletrackerapp.view.CalendarController;

public class ScheduleTrackerApp extends Application
{
    private Stage primaryStage;
    private BorderPane calendarDisplay;

    public Stage getStage()
    {
        return primaryStage;
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Schedule Tracker App");
        displayCalendar();
    }

    /*
    *   Calls loader to load calendar GUI and sets the scene on the primary stage
    *
    *   @param: None
    */
    private void displayCalendar() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleTrackerApp.class.getResource("view_controller/Calendar.fxml"));
        calendarDisplay = (BorderPane) loader.load();

        CalendarController calendarController = loader.getController();
        calendarController.setStage(primaryStage);

        Scene scene = new Scene(calendarDisplay);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
