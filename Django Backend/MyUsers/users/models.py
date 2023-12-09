from email.policy import default
from random import choices
from tabnanny import verbose
from tkinter import CASCADE
from django.db import models
from django.contrib.auth.models import AbstractUser
from django.forms import CharField
from django.utils.translation import gettext_lazy as _

USER_TYPE_CHOICES = (
     ('student', 'student'),
     ('teacher', 'teacher'),
     ('admin', 'admin'),
     )

# Create your models here.
class User(AbstractUser):   
    user_type = models.CharField(max_length=40,choices=USER_TYPE_CHOICES, default='student')
    
    

class Student(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    full_name = models.CharField(max_length=200)
    phone_number = models.IntegerField()
    gender = models.CharField(max_length=30)
    birthdate = models.DateField()
    profile_image = models.ImageField(upload_to='api/students/images/', default='api/students/images/user2.png')
    
    def __str__(self):
        return f'{"Student:"} {self.full_name}' 




class Teacher(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    full_name = models.CharField(max_length=200)
    phone_number = models.IntegerField()
    gender = models.CharField(max_length=30)
    birthdate = models.DateField()
    profile_image = models.ImageField(upload_to='api/teachers/images/', default='api/teachers/images/user2.png')

    def __str__(self):
        return f'{"Teacher:"} {self.full_name}'




class Course(models.Model):
    course_name = models.CharField(max_length=100)

    def __str__(self):
        return self.course_name



class Student_Course(models.Model):
    course_id = models.ForeignKey(Course, on_delete=models.CASCADE)
    student_id = models.ForeignKey(Student, on_delete=models.CASCADE)

    def __str__(self):
        return f'{"Course: "}{str(self.course_id) + ", "} {"Student Name: "} {str(self.student_id)}'



class Teacher_Course(models.Model):
    course_id = models.ForeignKey(Course, on_delete=models.CASCADE)
    teacher_id = models.ForeignKey(Teacher, on_delete=models.CASCADE)

    def __str__(self):
        return f'{"Course: "}{str(self.course_id) + ", "} {"Teacher Name:"} {str(self.teacher_id)}'




class Subject(models.Model):
    subject_name = models.CharField(max_length=100)
    
    def __str__(self):
        return self.subject_name
   

class Course_Subject(models.Model):
    course_id = models.ForeignKey(Course, on_delete=models.CASCADE)
    subject_id = models.ForeignKey(Subject, on_delete=models.CASCADE)

    def __str__(self):
        return f'{"Course:"}{str(self.course_id) + ", "} {"Subject: "} {str(self.subject_id)}'




class Classes(models.Model):
    class_name = models.CharField(max_length=100)

    def __str__(self):
        return self.class_name



class Class_Schedule(models.Model):
    class_id = models.ForeignKey(Classes, on_delete=models.CASCADE)
    subject_id = models.ForeignKey(Subject, on_delete=models.CASCADE)
    day = models.CharField(max_length=100)
    start_time = models.CharField(max_length=100)
    end_time = models.CharField(max_length=100)
    class_venue = models.CharField(max_length=100, default="")
    session_status = models.CharField(max_length=10, default="inactive")
    ssid = models.CharField(max_length=100, default="")

    def __str__(self):
        return f'{"Class: "}{str(self.class_id) + ", "} {"Subject: "} {str(self.subject_id)}'




class Student_Schedule(models.Model):
    student_id = models.ForeignKey(Student, on_delete=models.CASCADE)
    class_schedule_id = models.ForeignKey(Class_Schedule, on_delete=models.CASCADE) 

    def __str__(self):
        return f'{str(self.student_id) + ", "} {str(self.class_schedule_id)}'





class Teacher_Schedule(models.Model):
    teacher_id = models.ForeignKey(Teacher, on_delete=models.CASCADE)
    class_schedule_id = models.ForeignKey(Class_Schedule, on_delete=models.CASCADE) 

    def __str__(self):
        return f'{str(self.teacher_id) + ", "} {str(self.class_schedule_id)}'
        
  


class Student_Attendance(models.Model):
    student_id = models.ForeignKey(Student, on_delete=models.CASCADE)
    subject_id = models.ForeignKey(Subject, on_delete=models.CASCADE)
    att_date = models.DateField()
    att_status = models.CharField(max_length=30)

    def __str__(self):
        return str(self.student_id)




class UserSubject(models.Model):
    id = models.IntegerField(primary_key=True)
    user_id = models.IntegerField(null=True)
    full_name = models.CharField(max_length=200, default='Null')
    subject_name = models.CharField(max_length=100)
    subject_id = models.IntegerField(null=True)
    class Meta:
        verbose_name_plural = 'User Subject'



class StudentProfile(models.Model):
    id = models.IntegerField(primary_key=True)
    user_id = models.IntegerField()
    full_name = models.CharField(max_length=200)
    email = models.EmailField()
    phone_number = models.IntegerField()
    gender = models.CharField(max_length=30)
    birthdate = models.DateField()
    profile_image = models.ImageField(upload_to='api/students/images/', default='no image')




class TeacherProfile(models.Model):
    id = models.IntegerField(primary_key=True)
    user_id = models.IntegerField()
    full_name = models.CharField(max_length=200)
    email = models.EmailField()
    phone_number = models.IntegerField()
    gender = models.CharField(max_length=30)
    birthdate = models.DateField()
    profile_image = models.ImageField(upload_to='api/teachers/images/', default='no image')



class UserSchedule(models.Model):
    id = models.IntegerField(primary_key=True)
    user_id = models.IntegerField()
    full_name = models.CharField(max_length=100)
    subject_name = models.CharField(max_length=100)
    class_name = models.CharField(max_length=10)
    day = models.CharField(max_length=30)
    start_time = models.CharField(max_length=100)    
    end_time = models.CharField(max_length=100)
    class_venue = models.CharField(max_length=100, default="")


class AttendanceList(models.Model):
    id = models.IntegerField(primary_key=True)
    teacher_user_id = models.IntegerField()
    student_id = models.IntegerField()
    student_name = models.CharField(max_length=100)
    class_id = models.IntegerField(default=0)
    class_name = models.CharField(max_length=10, default="")
    profile_image = models.ImageField(upload_to='api/students/images/', default='no image')


class AttendanceRecord(models.Model):
    student_id = models.IntegerField()
    student_name = models.CharField(max_length=100)
    subject_name = models.CharField(max_length=100)    
    class_name = models.CharField(max_length=10, default="")
    att_date = models.DateField()
    att_status = models.CharField(max_length=10)


class AttendanceListRecord(models.Model):
    teacher_user_id = models.IntegerField()
    student_id = models.IntegerField()
    student_name = models.CharField(max_length=100)
    subject_name = models.CharField(max_length=100)    
    class_name = models.CharField(max_length=10, default="")
    att_date = models.DateField()
    att_status = models.CharField(max_length=10)



class StudentSubjectAttendance(models.Model):
    student_id = models.IntegerField()
    user_id = models.IntegerField()
    subject_name = models.CharField(max_length=100)
    present = models.CharField(max_length=10)
    absent = models.CharField(max_length=10)




