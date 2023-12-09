from urllib import request
from webbrowser import get
from xml.etree.ElementInclude import include
from django.db import router
from knox import views as knox_views
from django.urls import path, include, re_path
from django.conf import settings
from django.conf.urls.static import static
from rest_framework.routers import DefaultRouter

# from . import views
from users import views

router = DefaultRouter()
router.register(r'api/join_student_subjects', views.StudentSubjectJoinTable, basename="student_subjects")
router.register(r'api/join_teacher_subjects', views.TeacherSubjectJoinTable, basename="teacher_subjects")
router.register(r'api/student_profile', views.StudentProfileJoinTable, basename="student_profile")
router.register(r'api/teacher_profile', views.TeacherProfileJoinTable, basename="teacher_profile")
router.register(r'api/user_student_schedule', views.StudentScheduleJoinTable, basename="user_student_schedule")
router.register(r'api/user_teacher_schedule', views.TeacherScheduleJoinTable, basename="user_teacher_schedule")
router.register(r'api/user_students_schedule', views.StudentsScheduleJoinTable, basename="user_students_schedule")
router.register(r'api/user_teachers_schedule', views.TeachersScheduleJoinTable, basename="user_teachers_schedule")
router.register(r'api/class_list_all', views.ClassNameAllView, basename="class_list")
router.register(r'api/class_list', views.ClassNameView, basename="class_list")
router.register(r'api/class_list_student', views.ClassNameStudentView, basename="class_list_student")
router.register(r'api/student_attendance_list', views.StudentAttendanceListJoinTable, basename="student_attendance_list")
router.register(r'api/student_attendance_record', views.StudentAttendanceRecordJoinTable, basename="student_attendance_record")
router.register(r'api/student_attendance_list_record', views.StudentAttendanceListRecordJoinTable, basename="student_attendance_list_record")
router.register(r'api/student_subject_record', views.StudentSubjectAttendanceJoinTable, basename="student_attendancesubject_list_record")
router.register(r'api/student_subject_teacher_record', views.StudentSubjectTeacherAttendanceJoinTable, basename="student_subject_record")
router.register(r'api/session', views.SessionSerializerTable, basename="session_schedule")
router.register(r'api/session_classes', views.ClassSerializerTable, basename="session_classes")
router.register(r'api/student_att_list', views.CheckStudentAttendanceRecord, basename="att_records")


urlpatterns = [
    path('', views.index, name='index'),
    path('profile/', views.profile, name='profile'),  
    path('register/', views.registerUser, name='user_register'),      
    path('student/', views.registerStudent, name='add_manage_student'), 
    path('teacher/', views.registerTeacher, name='add_manage_teacher'), 
    path('courses/', views.addCourse, name='add_manage_course'), 
    path('subjects/', views.addSubject, name='add_manage_subject'), 
    path('course_subjects/', views.addCourseSubject, name='add_manage_course_subject'), 
    path('teacher_course/', views.addTeacherCourse, name='add_manage_course_teacher'), 
    path('student_course/', views.addStudentCourse, name='add_manage_course_student'), 
    path('classes/', views.addClass, name='add_manage_class'), 
    path('class_schedule/', views.addClassSchedule, name='add_manage_class_schedule'), 
    path('student_schedule/', views.addStudentSchedule, name='add_manage_student_schedule'), 
    path('teacher_schedule/', views.addTeacherSchedule, name='add_manage_teacher_chedule'), 
    path('student_attendance/', views.addStudentAttendance, name='add_manage_student_attendance'), 
    path('api/register/', views.RegisterAPI.as_view(), name='register'),
    path('api/users/<str:pk>', views.UserDetailsAPI.as_view(), name='userdetails'),
    path('api/login/', views.LoginAPI.as_view(), name='login'),
    path('api/logout/', knox_views.LogoutView.as_view(), name='logout'),
    path('api/logoutall/', knox_views.LogoutAllView.as_view(), name='logoutall'),
    path('api/students/', views.StudentAPI.as_view(), name='view_or_register_students'),
    path('api/students/<str:pk>/', views.StudentDetailsAPI.as_view(), name='student'),
    path('api/teachers/', views.TeacherAPI.as_view(), name='view_or_register_teachers'),
    path('api/teachers/<str:pk>/', views.TeacherDetailsAPI.as_view(), name='teacher'),
    path('api/courses/', views.CoursesAPI.as_view(), name='view_or_register_courses'),
    path('api/courses/<str:pk>/', views.CoursesDetailsAPI.as_view(), name='course'),
    path('api/subjects/', views.SubjectsAPI.as_view(), name='view_or_register_subject'),
    path('api/subjects/<str:pk>/', views.SubjectsDetailsAPI.as_view(), name='subject'),
    path('api/classes/', views.ClassesAPI.as_view(), name='view_or_register_classes'),
    path('api/classes/<str:pk>/', views.ClassesDetailsAPI.as_view(), name='class'),
    path('api/course_subjects/',views.CourseSubjectsAPI.as_view(), name='view_or_register_course_subjects'),
    path('api/course_subjects/<str:pk>/', views.CourseSubjectsDetailsAPI.as_view(), name='course_subjects'),
    path('api/class_schedules/', views.ClassScheduleAPI.as_view(), name='view_or_register_class_schedules'),
    path('api/class_schedules/<str:pk>/', views.ClassScheduleDetailsAPI.as_view(), name='class_schedule'),
    path('api/teacher_courses/', views.TeacherCourseAPI.as_view(), name='view_or_register_teacher_courses'),
    path('api/teacher_courses/<str:pk>/', views.TeacherCourseDetailsAPI.as_view(), name='teacher_course'),
    path('api/student_courses/', views.StudentCourseAPI.as_view(), name='view_or_register_student_courses'),
    path('api/student_courses/<str:pk>/', views.StudentCourseDetailsAPI.as_view(), name='student_course'),
    path('api/student_schedule/', views.StudentScheduleAPI.as_view(), name='view_or_register_student_schedules'),
    path('api/student_schedule/<str:pk>/', views.StudentScheduleDetailsAPI.as_view(), name='student_schedule'),
    path('api/teacher_schedule/', views.TeacherScheduleAPI.as_view(), name='view_or_register_teacher_schedules'),
    path('api/teacher_schedule/<str:pk>/', views.TeacherScheduleDetailsAPI.as_view(), name='teacher_schedule'),
    path('api/student_attendance/', views.StudentAttendanceAPI.as_view(), name='view_or_register_student_attendance'),
    path('api/student_attendance/<str:pk>/', views.StudentAttendanceDetailsAPI.as_view(), name='student_attendance'),
    path('', include(router.urls)),
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT) 



