# Attendance Management Android App

This attendance management android app allows the students themselves to mark their attendance while in class (using session based attendance where students have to be on the same WIFI network as the teacher 
and the teacher has to start a session on his/her phone) and the app also keeps track of their overall attendance record. The app was created using JAVA in Android Studio IDE and the backend for the app was created with Django. 
The project also contains an admin panel in the form of a Web App using which they can register/de-register student and teachers, view all the attendance record and also assign and manage the classes and schedule of the students and teachers.

![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/36934ff8-4875-4442-aa65-7128d1ca716b) &nbsp; &nbsp; ![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/af02134b-7dc9-4adb-962f-e7e17d3c54a7) &nbsp; &nbsp; ![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/1fc91df4-2b04-4bc4-bc09-3c698d11a5a4) 

![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/6db11af9-1432-48da-9d3b-c2957ca287d9) &nbsp; &nbsp; ![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/17c61cb0-b0bc-4f7d-8ed9-4ed58ae33c70) &nbsp; &nbsp; ![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/cf6626d0-7a62-44c5-b085-e82736029887)


# Mobile App Features

The mobile application will be built for Android devices using the Android studio IDE. The 
application will have the following features:

* A login portal for students and teachers.
* Session based attendance (WIFI connection required and both student and teacher should have their devices connected to the same network).
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

Note: Change the IP address to your devices private IP address in the ApiClient.java class, HomeActivity.java and StudentListAdapter.java class. 
      You can look up your IP address by typing ```ipconfig``` in the command promt. Copy the IPv4 Address and replace the IP addresses in the three classes mentioned above or you can create one global variable and used it across all three classes.

# Installation Guide for the backend

_Note: The backend is functional but incomplete. Before inserting new data, refer to the ER diagram [here](https://github.com/Azan9/Attendance-Management-Android-App/blob/main/ER%20Diagram/ERD.png)._


**Backend Packages Required**

Install the latest version of python. The required packages listed below are included the requirement.txt file in the MyUsers folder (which is included in this repository). 

* Django
* django_rest_knox
* djangorestframework
* knox
* requests
* Pillow

To install a package, type -> ```pip install package_name``` in the command prompt.

For example, to install djangorestframework:
  
  ```pip install djangorestframework```

To install all the packages at once:

There is a requirement.txt file in the MyUsers folder which you can use to install all the necessary packages for the backend. Run the command below in command prompt to install the packages.

```pip install -r /path/to/requirements.txt```

To start the django server, run the following command:

```python manage.py runserver 0.0.0.0:8000```

To access the web app, You need to create a superuser first to be able to use the web app. Follow [this](https://docs.djangoproject.com/en/1.8/intro/tutorial02/) guide to learn how to create a superuser.

After creating the superuser, type the following the url of your browser to access the web app.

_127.0.0.1:8000_ to use the web app or _127.0.0.1:8000/admin_ if you want to use the default Django admin interface

# Screenshots of the web app

![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/1204af0c-8668-46aa-b887-f5cea71ddd24)

![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/f8f3ee2b-b97e-43cd-bbec-c61bf754e6c9) 

![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/1818491e-dfe6-431c-b544-3ba7fcbfaf42) 

![image](https://github.com/Azan9/Attendance-Management-Android-App/assets/43653409/ff9acc73-9368-4a95-91ff-9eea16a422fb)






