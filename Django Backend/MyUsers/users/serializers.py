from dataclasses import fields
from pyexpat import model
from rest_framework import serializers
# from django.contrib.auth.models import User
from .models import AttendanceList, AttendanceListRecord, AttendanceRecord, Teacher, User, Student, Course, Subject, Course_Subject, Classes, Class_Schedule, Teacher_Schedule, Student_Schedule
from .models import StudentSubjectAttendance, Teacher_Course, Student_Course, Student_Attendance, UserSubject, StudentProfile, TeacherProfile, UserSchedule
from django.contrib.auth import authenticate



# User Serializer
class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username', 'email', 'password', 'user_type', 'is_superuser', 'is_staff')

    def update(self, instance, validated_data):
        user = super().update(instance, validated_data)

        try:
            user.set_password(validated_data['password'])
            user.save()
        except KeyError:
            pass

        return user


# Register Serializer
class RegisterSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username', 'email', 'password', 'user_type', 'is_superuser', 'is_staff')
        # extra_kwargs = {'password': {'write_only': True}}

    def create(self, validated_data):
        user = User.objects.create_user(self.validated_data['username'], 
        email = self.validated_data['email'], 
        password = self.validated_data['password'],
        user_type = self.validated_data['user_type'],
        is_superuser = self.validated_data['is_superuser'],
        is_staff = self.validated_data['is_staff'])
        return user
            

# Student Serializer
class StudentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Student
        fields = ('id', 'user_id', 'full_name', 'phone_number', 'gender', 'birthdate', 'profile_image')



# Register Teacher Serializer
class RegisterTeacherSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Teacher   
        fields = ('id', 'user_id', 'full_name', 'phone_number', 'gender','birthdate', 'profile_image')


# Teacher Serializer
class TeacherSerializer(serializers.ModelSerializer):
    class Meta:
        model = Teacher
        fields = ('id', 'user_id', 'full_name', 'phone_number', 'gender', 'birthdate', 'profile_image')



# Course Serializer
class CourseSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Course   
        fields = ('id', 'course_name')



# Subject Serializer
class SubjectSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Subject   
        fields = ('id', 'subject_name')


# Course Subject Serializer
class CourseSubjectSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Course_Subject   
        fields = ('id', 'course_id', 'subject_id')


# Classes Serializer
class ClassesSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Classes   
        fields = ('id', 'class_name')


# Teacher Course Serializer
class TeacherCourseSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Teacher_Course   
        fields = ('id', 'course_id', 'teacher_id')


# Student Course Serializer
class StudentCourseSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Student_Course   
        fields = ('id', 'course_id', 'student_id')


# Class Schedule Serializer
class ClassScheduleSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Class_Schedule
        fields = ('id', 'class_id', 'subject_id', 'day', 'start_time', 'end_time', 'class_venue', 'session_status', 'ssid')



# Student Class Schedule Serializer
class StudentScheduleSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Student_Schedule   
        fields = ('id', 'student_id', 'class_schedule_id')



# Teacher Class Schedule Serializer
class TeacherScheduleSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Teacher_Schedule   
        fields = ('id', 'teacher_id', 'class_schedule_id')



# Student Attendance Serializer
class StudentAttendanceSerializer(serializers.ModelSerializer):
    class Meta:     
        model = Student_Attendance   
        fields = ('id', 'student_id', 'subject_id', 'att_date', 'att_status')



class UserSubjectSerializer(serializers.ModelSerializer):
    class Meta: 
        model = UserSubject
        fields = ('id', 'user_id', 'full_name', 'subject_name', 'subject_id')


class StudentProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = StudentProfile
        fields = ('id', 'user_id', 'full_name', 'email', 'phone_number', 'gender', 'birthdate', 'profile_image')


class TeacherProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = TeacherProfile
        fields = ('id', 'user_id', 'full_name', 'email', 'phone_number', 'gender', 'birthdate', 'profile_image')



class ScheduleSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserSchedule
        fields = ('id', 'user_id', 'full_name', 'subject_name', 'class_name', 'day', 'start_time', 'end_time', 'class_venue')   


class StudentAttendanceListSerializer(serializers.ModelSerializer):
    class Meta:
        model = AttendanceList
        fields = ('id', 'teacher_user_id', 'student_id', 'student_name', 'class_id', 'class_name', 'profile_image')


class AttendanceRecordSerializer(serializers.ModelSerializer):
    class Meta:
        model = AttendanceRecord
        fields = ('id', 'student_id', 'student_name', 'subject_name', 'att_date', 'att_status')


class AttendanceListRecordSerializer(serializers.ModelSerializer):
    class Meta:
        model = AttendanceListRecord
        fields = ('id', 'teacher_user_id', 'student_id', 'student_name', 'subject_name', 'class_name', 'att_date', 'att_status')

 
class StudentSubjectAttendanceSerializer(serializers.ModelSerializer):
    class Meta:
        model = StudentSubjectAttendance
        fields = ('id', 'student_id', 'user_id', 'subject_name', 'present', 'absent')
