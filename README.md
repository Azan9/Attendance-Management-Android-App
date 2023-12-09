# Attendance Management Android App

This attendance management android app allows the students themselves to mark their attendance while in class (using session based attendance where students have to be on the same WIFI network as the teacher 
and the teacher has to start a session on his/her phone) and the app also keeps track of their overall attendance record. The app was created using JAVA in Android Studio IDE and the backend for the app was created with Django. 
The project also contains an admin panel in the form of a Web App using which they can register/de-register student and teachers, view all the attendance record and also assign and manage the classes and schedule of the students and teachers.

![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/79f8179b-694c-42ed-8ccc-724f4de85968) &nbsp; &nbsp; ![Screenshot_20231209_170448](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/6f6be92b-66ef-48c4-a110-dc336cf328ca)






# Mobile App Features

The mobile application will be built for Android devices using the Android studio IDE. The 
application will have the following features:

* A login portal for students and teachers.
* Session based attendance (WIFI connection required).
* Students can view their attendance record. They can also view their timetable and 
schedule from the events calendar. 
* Students can view, update their profile, and change their password.
* Teachers can take the attendance of the students manually as well. They can 
also view the attendance record of the student in their class.
* Teachers can view their timetable and schedule from the events calendar. 
* Teachers can view, update their profile, and change their password.
* Students and Teacher will be receive notification before their classes are about 
  to start.


# Web Application features

The web application will be build using Django and will have only one user which is the 
admin. The web application will have the following features:

* Admin can log in to the web app. They can also change their password or reset it.
* Admin can view and edit the details of the students and teachers.
* Admin can view the attendance record of the students.
* Admin can register or de-register students and teachers from the system.
* Admin can assign and manage the classes and schedule of the students and 
  teachers.


# Installation Guide for the app

Open the project in android studio and run the program in an emulator or a mobile device. Upgrade gradle files if necessary.

# Installation Guide for the backend

Backend Packages Required**

Install the latest version of python. The required packages listed below are included the requirement.txt file in the MyUsers folder (which is included in this repository). 

* Django
* 
* django_rest_knox
* djangorestframework
* knox
* requests
* Pillow

To install a package, type -> ```pip install package_name``` in the command prompt.

For example, to install djangorestframework:
  
  ```pip install djangorestframework```

To install all the packages at once:

There is requirement.txt file in the MyUsers folder which you can use to install all the necessary packages for the backend. Run the command below in command prompt to install the packages.

```pip install -r /path/to/requirements.txt```


