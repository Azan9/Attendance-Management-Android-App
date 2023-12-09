# import imp
from cgitb import text
from logging import raiseExceptions
from django.http import HttpResponse, JsonResponse
from requests import request
from rest_framework import generics, permissions, viewsets
from rest_framework.response import Response
from rest_framework.decorators import api_view
from django.contrib.auth.decorators import login_required
from knox.models import AuthToken
from .serializers import AttendanceRecordSerializer, AttendanceListRecordSerializer, StudentAttendanceListSerializer, StudentSubjectAttendanceSerializer, SubjectSerializer, ClassesSerializer, UserSerializer, RegisterSerializer, StudentSerializer, TeacherSerializer
from .serializers import CourseSerializer, SubjectSerializer, CourseSubjectSerializer, StudentAttendanceSerializer, StudentScheduleSerializer, TeacherScheduleSerializer
from .serializers import TeacherProfileSerializer, StudentProfileSerializer,  StudentCourseSerializer, TeacherCourseSerializer, ClassScheduleSerializer, ScheduleSerializer, UserSubjectSerializer
from .models import AttendanceList, AttendanceListRecord, UserSubject, User, Teacher, Student, Course, Subject, Course_Subject, Classes, Class_Schedule, UserSchedule
from .models import StudentSubjectAttendance, AttendanceRecord, Teacher_Schedule, Student_Schedule, Teacher_Course, Student_Course, Student_Attendance, TeacherProfile, StudentProfile
from django.shortcuts import render, redirect
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth import login
from rest_framework.generics import ListCreateAPIView, RetrieveUpdateDestroyAPIView
from rest_framework import permissions
from rest_framework.authtoken.serializers import AuthTokenSerializer
from knox.views import LoginView as KnoxLoginView
from django.shortcuts import get_object_or_404

from users import serializers

def index(request):    
    return render(request, 'index.html')


@login_required(login_url='http://127.0.0.1:8000/')
def profile(request):
    return render(request, 'profile.html')


def registerUser(request):
    return render(request, 'register_user.html')


def registerStudent(request):
    return render(request, 'student.html')


def registerTeacher(request):
    return render(request, 'teacher.html')


def addCourse(request):
    return render(request, 'course.html')


def addSubject(request):
    return render(request, 'subject.html')


def addCourseSubject(request):
    return render(request, 'course_subjects.html')


def addTeacherCourse(request):
    return render(request, 'teacher_course.html')


def addStudentCourse(request):
    return render(request, 'student_course.html')


def addClass(request):
    return render(request, 'classes.html')


def addClassSchedule(request):
    return render(request, 'class_schedule.html')


def addStudentSchedule(request):
    return render(request, 'student_schedule.html')


def addTeacherSchedule(request):
    return render(request, 'teacher_schedule.html')


def addStudentAttendance(request):
    return render(request, 'student_attendance.html')
    
        

# Register API
class RegisterAPI(ListCreateAPIView):
    serializer_class = RegisterSerializer
    queryset = User.objects.all() 
      
    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = serializer.save()
        return Response({
            "user": UserSerializer(user, context=self.get_serializer_context()).data,
            "token": AuthToken.objects.create(user)[1]
        })

class UserDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = UserSerializer
    queryset = User.objects.all()  


# Login API
class LoginAPI(KnoxLoginView):
    permission_classes = (permissions.AllowAny,)

    def post(self, request, format=None):
        serializer = AuthTokenSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        login(request, user)
        return Response({
            "id": user.id,
            "username": user.username,
            "user_type": user.user_type,
            "email": user.email,
            "password": user.password,
            "token": AuthToken.objects.create(user)[1]
        })
        

# Register or View Students API
class StudentAPI(ListCreateAPIView):
    serializer_class = StudentSerializer 
    queryset = Student.objects.all()



# Register View Teachers API
class TeacherAPI(ListCreateAPIView):
    serializer_class = TeacherSerializer
    queryset = Teacher.objects.all() 


# Register or view Courses API
class CoursesAPI(ListCreateAPIView):
    serializer_class = CourseSerializer
    queryset = Course.objects.all() 



# Register or view Subjects API
class SubjectsAPI(ListCreateAPIView):
    serializer_class = SubjectSerializer
    queryset = Subject.objects.all() 


# Register or view Course Subject API
class CourseSubjectsAPI(ListCreateAPIView):
    serializer_class = CourseSubjectSerializer
    queryset = Course_Subject.objects.all() 


# Register or view Classes API
class ClassesAPI(ListCreateAPIView):
    serializer_class = ClassesSerializer
    queryset = Classes.objects.all() 


# Register or view Class schedules API
class ClassScheduleAPI(ListCreateAPIView):
    serializer_class = ClassScheduleSerializer
    queryset = Class_Schedule.objects.all() 


# Register or view Student courses API
class StudentCourseAPI(ListCreateAPIView):
    serializer_class = StudentCourseSerializer
    queryset = Student_Course.objects.all() 


# Register or view Teacher courses API
class TeacherCourseAPI(ListCreateAPIView):
    serializer_class = TeacherCourseSerializer
    queryset = Teacher_Course.objects.all() 


# Register or view Students Attendance API
class StudentAttendanceAPI(ListCreateAPIView):
    serializer_class = StudentAttendanceSerializer
    queryset = Student_Attendance.objects.all() 


# Register or view Student class schedule API
class StudentScheduleAPI(ListCreateAPIView):
    serializer_class = StudentScheduleSerializer
    queryset = Student_Schedule.objects.all() 


# Register or view Teacher class schedule API
class TeacherScheduleAPI(ListCreateAPIView):
    serializer_class = TeacherScheduleSerializer
    queryset = Teacher_Schedule.objects.all() 


# Register or View Students API
class StudentDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = StudentSerializer 
    queryset = Student.objects.all()

    

# Register View Teachers API
class TeacherDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = TeacherSerializer
    queryset = Teacher.objects.all() 


# Register or view Courses API
class CoursesDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = CourseSerializer
    queryset = Course.objects.all() 



# Register or view Subjects API
class SubjectsDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = SubjectSerializer
    queryset = Subject.objects.all() 


# Register or view Course Subject API
class CourseSubjectsDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = CourseSubjectSerializer
    queryset = Course_Subject.objects.all() 


# Register or view Classes API
class ClassesDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = ClassesSerializer
    queryset = Classes.objects.all() 


# Register or view Class schedules API
class ClassScheduleDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = ClassScheduleSerializer
    queryset = Class_Schedule.objects.all() 


# Register or view Student courses API
class StudentCourseDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = StudentCourseSerializer
    queryset = Student_Course.objects.all() 


# Register or view Teacher courses API
class TeacherCourseDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = TeacherCourseSerializer
    queryset = Teacher_Course.objects.all() 


# Register or view Students Attendance API
class StudentAttendanceDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = StudentAttendanceSerializer
    queryset = Student_Attendance.objects.all() 


# Register or view Student class schedule API
class StudentScheduleDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = StudentScheduleSerializer
    queryset = Student_Schedule.objects.all() 


# Register or view Teacher class schedule API
class TeacherScheduleDetailsAPI(RetrieveUpdateDestroyAPIView):
    serializer_class = TeacherScheduleSerializer
    queryset = Teacher_Schedule.objects.all() 



# Student Subjects
class StudentSubjectJoinTable(viewsets.ModelViewSet):
    serializer_class = UserSubjectSerializer

    def get_queryset(self):
        query = UserSubject.objects.raw('Select users_student.id, users_student.user_id_id AS "user_id", users_student.full_name, users_subject.subject_name, users_subject.id AS "subject_id" FROM users_student  \
                                                JOIN users_student_course ON users_student.id = users_student_course.student_Id_id \
                                                JOIN users_course ON users_student_course.course_Id_id = users_course.id \
                                                JOIN users_course_subject ON users_course.id = users_course_subject.course_Id_id \
                                                JOIN users_subject ON users_course_subject.subject_Id_id = users_subject.id;')
        return query

    def retrieve(self, request, *args, **kwargs):
        queryset = UserSubject.objects.raw('Select DISTINCT users_student.id, users_student.user_id_id AS "user_id", users_student.full_name, users_subject.subject_name, users_subject.id AS "subject_id" FROM users_student  \
                                                JOIN users_student_course ON users_student.id = users_student_course.student_Id_id \
                                                JOIN users_course ON users_student_course.course_Id_id = users_course.id \
                                                JOIN users_course_subject ON users_course.id = users_course_subject.course_Id_id \
                                                JOIN users_subject ON users_course_subject.subject_Id_id = users_subject.id WHERE users_student.user_id_id = %s',[kwargs['pk']]) 

        serializer = UserSubjectSerializer(queryset, many = True)
        return Response(serializer.data)


# Teacher Subjects
class TeacherSubjectJoinTable(viewsets.ModelViewSet):
    serializer_class = UserSubjectSerializer

    def get_queryset(self):
        query = UserSubject.objects.raw('Select users_teacher.id, users_teacher.user_id_id AS "user_id", users_teacher.full_name, users_subject.subject_name, users_subject.id AS "subject_id" FROM users_teacher  \
                                                JOIN users_teacher_course ON users_teacher.id = users_teacher_course.teacher_Id_id \
                                                JOIN users_course ON users_teacher_course.course_Id_id = users_course.id \
                                                JOIN users_course_subject ON users_course.id = users_course_subject.course_Id_id \
                                                JOIN users_subject ON users_course_subject.subject_Id_id = users_subject.id;')
        return query

    def retrieve(self, request, *args, **kwargs):
        queryset = UserSubject.objects.raw('Select DISTINCT users_teacher.id, users_teacher.user_id_id AS "user_id", users_teacher.full_name, users_subject.subject_name, users_subject.id AS "subject_id" FROM users_teacher  \
                                                JOIN users_teacher_course ON users_teacher.id = users_teacher_course.teacher_Id_id \
                                                JOIN users_course ON users_teacher_course.course_Id_id = users_course.id \
                                                JOIN users_course_subject ON users_course.id = users_course_subject.course_Id_id \
                                                JOIN users_subject ON users_course_subject.subject_Id_id = users_subject.id WHERE users_teacher.user_id_id = %s',[kwargs['pk']]) 

        serializer = UserSubjectSerializer(queryset, many = True)
        return Response(serializer.data)


# Student Profile
class StudentProfileJoinTable(viewsets.ModelViewSet):
    serializer_class = StudentProfileSerializer

    def get_queryset(self):
        query = StudentProfile.objects.raw('Select users_student.id, users_student.full_name, users_user.email, users_student.phone_number, users_student.user_id_id AS "user_id", users_student.gender, users_student.birthdate, users_student.profile_image FROM users_user \
                                     JOIN users_student ON users_user.id = users_student.user_id_id;')
        return query
        

    def retrieve(self, request, *args, **kwargs):
        queryset = StudentProfile.objects.raw('Select users_student.id, users_student.full_name, users_user.email, users_student.phone_number, users_student.user_id_id AS "user_id", users_student.gender, users_student.birthdate, users_student.profile_image FROM users_user \
                                        JOIN users_student ON users_user.id = users_student.user_id_id where users_student.user_id_id = %s',[kwargs['pk']]) 

        serializer = StudentProfileSerializer(queryset, many = True)
        return Response(serializer.data)



# Teacher Profile
class TeacherProfileJoinTable(viewsets.ModelViewSet):
    serializer_class = TeacherProfileSerializer

    def get_queryset(self):
        query = TeacherProfile.objects.raw('Select users_teacher.id, users_teacher.full_name, users_user.email, users_teacher.phone_number, users_teacher.user_id_id AS "user_id", users_teacher.gender, users_teacher.birthdate, users_teacher.profile_image FROM users_user \
                                     JOIN users_teacher ON users_user.id = users_teacher.user_id_id;')
        return query
        

    def retrieve(self, request, *args, **kwargs):
        queryset = TeacherProfile.objects.raw('Select users_teacher.id, users_teacher.full_name, users_user.email, users_teacher.phone_number, users_teacher.user_id_id AS "user_id", users_teacher.gender, users_teacher.birthdate, users_teacher.profile_image FROM users_user \
                                        JOIN users_teacher ON users_user.id = users_teacher.user_id_id where users_teacher.user_id_id = %s',[kwargs['pk']]) 

        serializer = TeacherProfileSerializer(queryset, many = True)
        return Response(serializer.data)


# Teacher Schedule
class TeacherScheduleJoinTable(viewsets.ModelViewSet):
    serializer_class = ScheduleSerializer

    def get_queryset(self):
        query = UserSchedule.objects.raw('Select users_teacher.id, users_teacher.user_id_id AS "user_id", users_teacher.full_name, users_subject.subject_name, users_classes.class_name, users_class_schedule.day, users_class_schedule.start_time, users_class_schedule.end_time, users_class_schedule.class_venue FROM users_teacher \
                                            JOIN users_teacher_schedule ON users_teacher.id = users_teacher_schedule.teacher_Id_id \
                                            JOIN users_class_schedule ON users_teacher_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id;')
        return query
        

    def retrieve(self, request, *args, **kwargs):
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = UserSchedule.objects.raw('Select users_teacher.id, users_teacher.user_id_id AS "user_id", users_teacher.full_name, users_subject.subject_name, users_classes.class_name, users_class_schedule.day, users_class_schedule.start_time, users_class_schedule.end_time, users_class_schedule.class_venue FROM users_teacher \
                                            JOIN users_teacher_schedule ON users_teacher.id = users_teacher_schedule.teacher_Id_id \
                                            JOIN users_class_schedule ON users_teacher_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id WHERE users_teacher.user_id_id = %s AND users_class_schedule.day = %s',[params_list[0], params_list[1]]) 

        serializer = ScheduleSerializer(queryset, many = True)
        return Response(serializer.data)


# Student Schedule
class StudentScheduleJoinTable(viewsets.ModelViewSet):
    serializer_class = ScheduleSerializer

    def get_queryset(self):
        query = UserSchedule.objects.raw('Select users_student.id, users_student.user_id_id AS "user_id", users_student.full_name, users_subject.subject_name, users_classes.class_name, users_class_schedule.day, users_class_schedule.start_time, users_class_schedule.end_time, users_class_schedule.class_venue FROM users_student \
                                            JOIN users_student_schedule ON users_student.id = users_student_schedule.student_Id_id \
                                            JOIN users_class_schedule ON users_student_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id;') 
        return query
        

    def retrieve(self, request, *args, **kwargs):
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = UserSchedule.objects.raw('Select users_student.id, users_student.user_id_id AS "user_id", users_student.full_name, users_subject.subject_name, users_classes.class_name, users_class_schedule.day, users_class_schedule.start_time, users_class_schedule.end_time, users_class_schedule.class_venue FROM users_student \
                                            JOIN users_student_schedule ON users_student.id = users_student_schedule.student_Id_id \
                                            JOIN users_class_schedule ON users_student_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id WHERE users_student.user_id_id = %s AND users_class_schedule.day = %s',[params_list[0], params_list[1]]) 

        serializer = ScheduleSerializer(queryset, many = True)
        return Response(serializer.data)


# All class name for selected teacher
class ClassNameAllView(viewsets.ModelViewSet):
    serializer_class = StudentAttendanceListSerializer
    def get_queryset(self):
        query = AttendanceList.objects.raw('Select DISTINCT users_classes.id, users_classes.class_name FROM users_teacher \
                                            JOIN users_teacher_schedule ON users_teacher.id = users_teacher_schedule.teacher_Id_id \
                                            JOIN users_class_schedule ON users_teacher_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id;') 
        return query

    
    def retrieve(self, request, *args, **kwargs):         
        queryset = AttendanceList.objects.raw('Select DISTINCT users_classes.id, users_classes.class_name FROM users_teacher \
                                            JOIN users_teacher_schedule ON users_teacher.id = users_teacher_schedule.teacher_Id_id \
                                            JOIN users_class_schedule ON users_teacher_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id \
                                            WHERE users_teacher.user_id_id = %s ORDER BY users_classes.class_name', [kwargs['pk']]) 
        
        
        
        serializer = StudentAttendanceListSerializer(queryset, many = True)
        return Response(serializer.data)


# Unique class name for selected teacher
class ClassNameView(viewsets.ModelViewSet):
    serializer_class = StudentAttendanceListSerializer
    def get_queryset(self):
        query = AttendanceList.objects.raw('Select DISTINCT users_classes.id, users_classes.class_name FROM users_teacher \
                                            JOIN users_teacher_schedule ON users_teacher.id = users_teacher_schedule.teacher_Id_id \
                                            JOIN users_class_schedule ON users_teacher_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id;') 
        return query

    
    def retrieve(self, request, *args, **kwargs): 
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = AttendanceList.objects.raw('Select DISTINCT users_classes.id, users_classes.class_name FROM users_teacher \
                                            JOIN users_teacher_schedule ON users_teacher.id = users_teacher_schedule.teacher_Id_id \
                                            JOIN users_class_schedule ON users_teacher_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id \
                                            WHERE users_teacher.user_id_id = %s AND users_subject.subject_name = %s ORDER BY users_classes.class_name', [params_list[0], params_list[1]]) 
        
        
        
        serializer = StudentAttendanceListSerializer(queryset, many = True)
        return Response(serializer.data)


# Unique class name for selected student
class ClassNameStudentView(viewsets.ModelViewSet):
    serializer_class = StudentAttendanceListSerializer
    def get_queryset(self):
        query = AttendanceList.objects.raw('Select DISTINCT users_classes.id, users_classes.class_name FROM users_student \
                                            JOIN users_student_schedule ON users_student.id = users_student_schedule.student_Id_id \
                                            JOIN users_class_schedule ON users_student_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id;') 
        return query

    
    def retrieve(self, request, *args, **kwargs): 
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = AttendanceList.objects.raw('Select DISTINCT users_classes.id, users_classes.class_name FROM users_student \
                                            JOIN users_student_schedule ON users_student.id = users_student_schedule.student_Id_id \
                                            JOIN users_class_schedule ON users_student_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id \
                                            WHERE users_student.user_id_id = %s AND users_subject.subject_name = %s ORDER BY users_classes.class_name', [params_list[0], params_list[1]]) 
        
        
        
        serializer = StudentAttendanceListSerializer(queryset, many = True)
        return Response(serializer.data)


# Student List for attendance based on subject and class for the teacher
class StudentAttendanceListJoinTable(viewsets.ModelViewSet):
    serializer_class = StudentAttendanceListSerializer
    def get_queryset(self):
        query = AttendanceList.objects.raw('Select users_teacher.id, users_teacher.user_id_id AS "teacher_user_id" , users_subject.subject_name, users_classes.class_name,  users_student.id AS "student_id" ,users_student.full_name AS "student_name", users_student.profile_image \
                                                FROM users_class_schedule JOIN users_teacher_schedule ON users_class_schedule.id = users_teacher_schedule.class_schedule_id_id \
                                                JOIN users_student_schedule ON users_class_schedule.id = users_student_schedule.class_schedule_id_id \
                                                JOIN users_subject ON users_class_schedule.subject_id_id = users_subject.id \
                                                JOIN users_classes ON users_class_schedule.class_id_id = users_classes.id \
                                                JOIN users_student ON users_student_schedule.student_id_id = users_student.id \
                                                JOIN users_teacher ON users_teacher_schedule.teacher_id_id = users_teacher.id;') 
        return query
         
    
    def retrieve(self, request, *args, **kwargs):      
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = AttendanceList.objects.raw('Select users_teacher.id, users_teacher.user_id_id AS "teacher_user_id" , users_subject.subject_name, users_classes.class_name,  users_student.id AS "student_id" ,users_student.full_name AS "student_name", users_student.profile_image \
                                                FROM users_class_schedule JOIN users_teacher_schedule ON users_class_schedule.id = users_teacher_schedule.class_schedule_id_id \
                                                JOIN users_student_schedule ON users_class_schedule.id = users_student_schedule.class_schedule_id_id \
                                                JOIN users_subject ON users_class_schedule.subject_id_id = users_subject.id \
                                                JOIN users_classes ON users_class_schedule.class_id_id = users_classes.id \
                                                JOIN users_student ON users_student_schedule.student_id_id = users_student.id \
                                                JOIN users_teacher ON users_teacher_schedule.teacher_id_id = users_teacher.id where users_teacher.user_id_id = %s AND users_classes.class_name = %s and users_subject.subject_name =%s', [params_list[0], params_list[1], params_list[2]]) 
        
        
        
        serializer = StudentAttendanceListSerializer(queryset, many = True)
        return Response(serializer.data)


class StudentAttendanceRecordJoinTable(viewsets.ModelViewSet):
    serializer_class = AttendanceRecordSerializer

    def get_queryset(self):
        query = AttendanceList.objects.raw('select users_student.id, users_student_attendance.student_id_id AS "student_id", users_student.full_name AS "student_name", users_subject.id AS "subject_id", users_subject.subject_name, users_student_attendance.att_date, users_student_attendance.att_status \
                                                    FROM users_student \
                                                    JOIN users_student_attendance ON users_student.id = users_student_attendance.student_id_id \
                                                    JOIN users_subject ON users_student_attendance.subject_id_id = users_subject.id;') 
        return query

    
    def retrieve(self, request, *args, **kwargs):      
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = AttendanceRecord.objects.raw('select users_student.id, users_student_attendance.student_id_id AS "student_id", users_student.full_name AS "student_name", users_subject.id AS "subject_id", users_subject.subject_name, users_student_attendance.att_date, users_student_attendance.att_status \
                                                    FROM users_student \
                                                    JOIN users_student_attendance ON users_student.id = users_student_attendance.student_id_id \
                                                    JOIN users_subject ON users_student_attendance.subject_id_id = users_subject.id where users_subject.subject_name = %s AND users_student.id = %s', [params_list[0], params_list[1]]) 
        
        
        
        serializer = AttendanceRecordSerializer(queryset, many = True)
        return Response(serializer.data)


class StudentAttendanceListRecordJoinTable(viewsets.ModelViewSet):
    serializer_class = AttendanceListRecordSerializer

    def get_queryset(self):
        query = AttendanceList.objects.raw('Select users_teacher.id, users_teacher.user_id_id AS "teacher_user_id",  users_student.id AS "student_id", users_student.full_name AS "student_name", users_subject.subject_name, users_classes.class_name, \
                                                        users_student_attendance.att_date, users_student_attendance.att_status FROM users_student_attendance \
                                                        JOIN users_subject ON users_student_attendance.subject_id_id = users_subject.id \
                                                        JOIN users_student ON users_student_attendance.student_id_id = users_student.id \
                                                        JOIN users_class_schedule ON users_student_attendance.subject_id_id = users_class_schedule.subject_id_id \
                                                        JOIN users_classes ON users_class_schedule.class_id_id = users_classes.id \
                                                        JOIN users_teacher_schedule ON users_class_schedule.id = users_teacher_schedule.class_schedule_id_id \
                                                        JOIN users_teacher ON users_teacher_schedule.teacher_id_id = users_teacher.id;') 
        return query

    
    def retrieve(self, request, *args, **kwargs):      
        params = kwargs
        params_list = params['pk'].split(':') 
        queryset = AttendanceListRecord.objects.raw('Select users_teacher.id, users_teacher.user_id_id AS "teacher_user_id",  users_student.id AS "student_id", users_student.full_name AS "student_name", users_subject.subject_name, users_classes.class_name, \
                                                        users_student_attendance.att_date, users_student_attendance.att_status FROM users_student_attendance \
                                                        JOIN users_subject ON users_student_attendance.subject_id_id = users_subject.id \
                                                        JOIN users_student ON users_student_attendance.student_id_id = users_student.id \
                                                        JOIN users_class_schedule ON users_student_attendance.subject_id_id = users_class_schedule.subject_id_id \
                                                        JOIN users_classes ON users_class_schedule.class_id_id = users_classes.id \
                                                        JOIN users_teacher_schedule ON users_class_schedule.id = users_teacher_schedule.class_schedule_id_id \
                                                        JOIN users_teacher ON users_teacher_schedule.teacher_id_id = users_teacher.id where users_classes.class_name = %s AND users_subject.subject_name = %s AND users_student_attendance.att_date = %s', [params_list[0], params_list[1], params_list[2]]) 
        
        
        
        serializer = AttendanceListRecordSerializer(queryset, many = True)
        return Response(serializer.data)


# All Teacher Schedule
class TeachersScheduleJoinTable(viewsets.ModelViewSet):
    serializer_class = ScheduleSerializer

    def get_queryset(self):
        query = UserSchedule.objects.raw('Select users_teacher.id, users_teacher.user_id_id AS "user_id", users_teacher.full_name, users_subject.subject_name, users_classes.class_name, users_class_schedule.day, users_class_schedule.start_time, users_class_schedule.end_time, users_class_schedule.class_venue FROM users_teacher \
                                            JOIN users_teacher_schedule ON users_teacher.id = users_teacher_schedule.teacher_Id_id \
                                            JOIN users_class_schedule ON users_teacher_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id;')
        return query
        

    def retrieve(self, request, *args, **kwargs):
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = UserSchedule.objects.raw('Select users_teacher.id, users_teacher.user_id_id AS "user_id", users_teacher.full_name, users_subject.subject_name, users_classes.class_name, users_class_schedule.day, users_class_schedule.start_time, users_class_schedule.end_time, users_class_schedule.class_venue FROM users_teacher \
                                            JOIN users_teacher_schedule ON users_teacher.id = users_teacher_schedule.teacher_Id_id \
                                            JOIN users_class_schedule ON users_teacher_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id WHERE users_teacher.user_id_id = %s',[params_list[0]]) 

        serializer = ScheduleSerializer(queryset, many = True)
        return Response(serializer.data)


# All Student Schedule
class StudentsScheduleJoinTable(viewsets.ModelViewSet):
    serializer_class = ScheduleSerializer

    def get_queryset(self):
        query = UserSchedule.objects.raw('Select users_student.id, users_student.user_id_id AS "user_id", users_student.full_name, users_subject.subject_name, users_classes.class_name, users_class_schedule.day, users_class_schedule.start_time, users_class_schedule.end_time, users_class_schedule.class_venue FROM users_student \
                                            JOIN users_student_schedule ON users_student.id = users_student_schedule.student_Id_id \
                                            JOIN users_class_schedule ON users_student_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id;') 
        return query
        

    def retrieve(self, request, *args, **kwargs):
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = UserSchedule.objects.raw('Select users_student.id, users_student.user_id_id AS "user_id", users_student.full_name, users_subject.subject_name, users_classes.class_name, users_class_schedule.day, users_class_schedule.start_time, users_class_schedule.end_time, users_class_schedule.class_venue FROM users_student \
                                            JOIN users_student_schedule ON users_student.id = users_student_schedule.student_Id_id \
                                            JOIN users_class_schedule ON users_student_schedule.class_schedule_Id_id = users_class_schedule.id \
                                            JOIN users_classes ON users_class_schedule.class_Id_id = users_classes.id \
                                            JOIN users_subject ON users_class_schedule.subject_Id_id = users_subject.id WHERE users_student.user_id_id = %s',[params_list[0]]) 

        serializer = ScheduleSerializer(queryset, many = True)
        return Response(serializer.data)



class StudentSubjectAttendanceJoinTable(viewsets.ModelViewSet):
    serializer_class = StudentSubjectAttendanceSerializer

    def get_queryset(self):
        query = StudentSubjectAttendance.objects.raw('') 
        return query
        

    def retrieve(self, request, *args, **kwargs):
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = StudentSubjectAttendance.objects.raw('SELECT users_student.id, users_student_attendance.student_id_id AS "student_id", users_student.user_id_id AS "user_id", users_subject.subject_name, SUM(users_student_attendance.att_status = "Present") AS "present", SUM(users_student_attendance.att_status = "Absent") AS "absent" \
                                                            FROM users_student_attendance \
                                                            JOIN users_student ON users_student_attendance.student_id_id = users_student.id \
                                                            JOIN users_subject ON users_subject.id = users_student_attendance.subject_id_id \
                                                            where users_student.user_id_id = %s AND users_subject.subject_name = %s',[params_list[0], params_list[1]]) 

        serializer = StudentSubjectAttendanceSerializer(queryset, many = True)
        return Response(serializer.data)



class StudentSubjectTeacherAttendanceJoinTable(viewsets.ModelViewSet):
    serializer_class = StudentSubjectAttendanceSerializer

    def get_queryset(self):
        query = StudentSubjectAttendance.objects.raw('') 
        return query
        

    def retrieve(self, request, *args, **kwargs):
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = StudentSubjectAttendance.objects.raw('SELECT users_student.id, users_student_attendance.student_id_id AS "student_id", users_student.user_id_id AS "user_id", users_subject.subject_name, SUM(users_student_attendance.att_status = "Present") AS "present", SUM(users_student_attendance.att_status = "Absent") AS "absent" \
                                                            FROM users_student_attendance \
                                                            JOIN users_student ON users_student_attendance.student_id_id = users_student.id \
                                                            JOIN users_subject ON users_subject.id = users_student_attendance.subject_id_id \
                                                            where users_student_attendance.student_id_id = %s AND users_subject.subject_name = %s',[params_list[0], params_list[1]]) 

        serializer = StudentSubjectAttendanceSerializer(queryset, many = True)
        return Response(serializer.data)


class SessionSerializerTable(viewsets.ModelViewSet):
    serializer_class = ClassScheduleSerializer

    def get_queryset(self):
        query = Class_Schedule.objects.raw('SELECT * FROM users_class_schedule;') 
        return query

    
    def retrieve(self, request, *args, **kwargs):
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = Class_Schedule.objects.raw('SELECT users_class_schedule.id FROM users_class_schedule WHERE users_class_schedule.class_Id_id = %s \
                                                    AND users_class_schedule.subject_id_id = %s',[params_list[0], params_list[1]]) 

        serializer = ClassScheduleSerializer(queryset, many = True)
        return Response(serializer.data)


class ClassSerializerTable(viewsets.ModelViewSet):
    serializer_class = ClassesSerializer

    def get_queryset(self):
        query = Classes.objects.raw('SELECT * FROM users_classes;') 
        return query

    
    def retrieve(self, request, *args, **kwargs):
        params = kwargs
        params_list = params['pk'].split('-') 
        queryset = Classes.objects.raw('select users_classes.id from users_classes WHERE users_classes.class_name = %s',[params_list[0]]) 

        serializer = ClassesSerializer(queryset, many = True)
        return Response(serializer.data)

class CheckStudentAttendanceRecord(viewsets.ModelViewSet):
    serializer_class = StudentAttendanceSerializer

    def get_queryset(self):
        query = Student_Attendance.objects.raw('') 
        return query

    def retrieve(self, request, *args, **kwargs):
        params = kwargs
        params_list = params['pk'].split(':') 
        queryset = Student_Attendance.objects.raw('SELECT users_student_attendance.id, users_student_attendance.student_id_id, users_student_attendance.subject_id_id, users_student_attendance.att_date \
                                                            FROM users_student_attendance where users_student_attendance.student_id_id = %s \
                                                            AND users_student_attendance.subject_id_id = %s AND users_student_attendance.att_date = %s', [params_list[0], params_list[1], params_list[2]]) 

        serializer = StudentAttendanceSerializer(queryset, many = True)
        return Response(serializer.data)
