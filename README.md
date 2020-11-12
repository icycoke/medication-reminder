# Medication Reminder
Medication Reminder is mainly designed for two common scenarios of heart patients. First, patients with heart disease often need to add event reminders frequently to ensure that they do not miss their medication time or the date of going to the hospital. Second, when heart disease patients go out, they need to ensure that they carry relevant medicines with them. Medication Reminder can help the user easily add new event to Google Calendar. Furthermore, it can record the user's home location and use geo-fence to push notification when the user is leaving home.

![Welcome Interface](https://github.com/icycoke/img-repo/blob/main/medication-reminder/welcome.png)

*(The welcome interface is created by using an open-source framework [Welcome](https://github.com/icycoke/welcome-android))*

![User guide](https://github.com/icycoke/img-repo/blob/main/medication-reminder/guide1.png)
![User guide](https://github.com/icycoke/img-repo/blob/main/medication-reminder/guide2.png)

## Project Structure
![Medication Reminder Flowchart](https://github.com/icycoke/img-repo/blob/main/medication-reminder/medication_reminder_flowchart.png)

All source code files are under `com.icycoke.android.medication_reminder` package

The collected data file will be saved as `getFilesDir().getAbsolutePath() + "/home_location_history.csv"` which is `/data/data/com.icycoke.musicalpedometer/files/home_location_history.csv` as default

DAO layer files are under `com.icycoke.android.medication_reminder.persistence` package
