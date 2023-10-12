# Airplane Application

___
An application designed for airline companies and flight passengers for **a better organization of flights**. It will
allow passengers to book their flights with ease and send them reminders when the flight is near. The passengers will
also receive updates regarding **gate changes and flight information**.

### Why is this project of interest to you?

Since I love travelling, I have travelled many times in my life. And so, I would like to try designing my own airplane
reservation system as it will allow me to better understand the mechanisms required for allowing passengers to book
flight and how airline companies decide on flight schedules *(the part involving airline companies will be implemented
in future phases)*.

## User Stories

___
- As a user, I want to be able to register for an account.
- As a user, I want to be able to book a flight and add it to my list of booked flight.
- As a user, I want to be able to see my flight information.
- As a user, I want to be able to cancel my flight and remove it from my list of booked flight.
- As a user, I want to be able to log back into my account after quitting the program.
- As a user, I want to be able to save all my booked flights and be able to see it after quitting the program.


Sample logs:
```
Sun Mar 27 16:15:33 PDT 2022
Loaded passenger Christie Leung from file.

Sun Mar 27 16:20:04 PDT 2022
Added new passenger Christie Leung to accounts database.

Sun Mar 27 16:20:13 PDT 2022
Added Flight AC246 to Passenger Christie Leung to passenger's booked flights.

Sun Mar 27 16:20:17 PDT 2022
Removed Flight AC246 to Passenger Christie Leung from passenger's booked flights.

Sun Mar 27 16:21:11 PDT 2022
Removed passenger Christie Leung from accounts database.
```

- The first log shows that the passenger has been loaded from the JSON file. 
- The second log shows that a new passenger has been added to account.
- The third log shows that a flight has been added to a passenger's list of booked flights.
- The fourth log shows that a flight has been removed from a passenger's list of booked flights.
- The fifth log shows that a passenger has been removed from account.

If nothing shows up on the console log, it would mean that there are no registered passengers in the json file and that the user has not signed up for an account when opening the application.

Looking at my UML Diagram, I think that having all the ui pages to have accounts or flight schedule is not a good idea. Instead of passing around account and flight schedule within the pages, I could use a similar approach as EventLog where a Singleton Design Pattern is implemented. This would make sense for flight schedules and accounts being there would only always be one list of accounts or flight schedules. 

I could also change Account and FlightSchedule to have a similar implementation of EventLog where it is an Iterable of either passenger or flight. This makes sense because both are a list of passengers or flights at the moment.

All changes for future:
- Change FlightSchedule class to implement Singleton Design Pattern
- Change Account class to implement Singleton Design Pattern
- Have flight schedule implement Iterable<Flight>
- Have Account implement Iterable<Passenger>

