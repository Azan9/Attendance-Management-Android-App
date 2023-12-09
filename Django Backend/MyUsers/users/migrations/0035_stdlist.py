# Generated by Django 4.0.3 on 2022-04-09 10:36

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0034_attendancelist_class_name'),
    ]

    operations = [
        migrations.CreateModel(
            name='StdList',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('full_name', models.CharField(max_length=200)),
                ('phone_number', models.IntegerField()),
                ('gender', models.CharField(max_length=30)),
                ('birthdate', models.DateField()),
                ('profile_image', models.ImageField(default='no image', upload_to='api/teachers/images/')),
                ('class_schedule_Id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='users.class_schedule')),
                ('teacher_Id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='users.teacher')),
                ('user_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
    ]
