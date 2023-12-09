from django.contrib import admin
from .models import User, Student, Teacher, Subject, Classes, Course, Class_Schedule, Student_Course, Teacher_Course, Student_Attendance, Student_Schedule, Teacher_Schedule, Course_Subject 


# Register your models here.
admin.site.register(User)
admin.site.register(Student)
admin.site.register(Teacher)
admin.site.register(Course)
admin.site.register(Subject)
admin.site.register(Classes)
admin.site.register(Course_Subject)
admin.site.register(Student_Course)
admin.site.register(Teacher_Course)
admin.site.register(Class_Schedule)
admin.site.register(Student_Schedule)
admin.site.register(Teacher_Schedule)
admin.site.register(Student_Attendance)
