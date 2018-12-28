# Schedule Tracker App

Java application that tracks scheduled appointments for a company.

## Situation:
You are working for a software company that has been contracted to develop a scheduling desktop user interface application. The contract is with a global consulting organization that conducts business in multiple languages and has main offices in all over the world. The consulting organization has provided a MySQL database that your application must pull data from. The database is used for other systems and therefore its structure cannot be modified.

## Features requested by company:

1) Create a log-in form that can determine the user’s location and translate log-in and error control messages.

2) Provide the ability to add, update, and delete customer records in the database, including name, address, and phone number.

3) Provide the ability to add, update, and delete appointments, capturing the type of appointment and a link to the specific customer record in the database.

4) Provide the ability to filter the the calendar to view appointments within a week or within a month.

5) Provide the ability to automatically adjust appointment times based on user time zones and daylight savings time.

6) Write exception controls to prevent each of the following. You may use the same mechanism of exception control more than once, but you must incorporate at least two different mechanisms of exception control.</br>
• scheduling an appointment outside business hours</br>
• scheduling overlapping appointments</br>
• entering nonexistent or invalid customer data</br>
• entering an incorrect username and password</br>

7) Write two or more lambda expressions to make your program more efficient, justifying the use of each lambda expression with an in-line comment.
 
8) Write code to provide an alert if there is an appointment within 15 minutes of the user’s log-in.

9) Provide the ability to generate each of the following reports:</br>
• number of appointment types by month</br>
• the schedule for each consultant</br>
• one additional report of your choice</br>

10) Provide the ability to track user activity by recording timestamps for user log-ins in a .txt file. Each new record should be appended to the log file, if the file already exists.

## Screenshots: <br/>
### Login Page: <br/>
![login](https://user-images.githubusercontent.com/24645219/50427413-39b9c480-085c-11e9-9bb3-6010488f041a.jpg)

### Appointment Schedule Main Page:
![calendar](https://user-images.githubusercontent.com/24645219/50427410-39212e00-085c-11e9-845f-4d9005a09b18.jpg)

### Customer Database:
![customers](https://user-images.githubusercontent.com/24645219/50427411-39212e00-085c-11e9-9a40-c2902a00ee1f.jpg)

### Modify Customer Prompt:
![edit](https://user-images.githubusercontent.com/24645219/50427412-39b9c480-085c-11e9-8fbd-db71c5d28286.jpg)

### Reports Page:
![reports](https://user-images.githubusercontent.com/24645219/50427414-39b9c480-085c-11e9-8b72-ac0e3e33c2fb.jpg)

### Add New Appointment Prompt:
![add](https://user-images.githubusercontent.com/24645219/50427409-39212e00-085c-11e9-99a8-88187c54c4ff.jpg)
