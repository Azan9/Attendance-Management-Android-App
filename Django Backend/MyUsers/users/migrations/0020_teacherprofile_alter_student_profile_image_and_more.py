# Generated by Django 4.0.3 on 2022-04-04 20:11

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0019_studentprofile_profile_image'),
    ]

    operations = [
        migrations.CreateModel(
            name='TeacherProfile',
            fields=[
                ('id', models.IntegerField(primary_key=True, serialize=False)),
                ('user_id', models.IntegerField()),
                ('full_name', models.CharField(max_length=200)),
                ('email', models.EmailField(max_length=254)),
                ('phone_number', models.IntegerField()),
                ('gender', models.CharField(max_length=30)),
                ('birthdate', models.DateField()),
                ('profile_image', models.ImageField(default='no image', upload_to='api/teachers/images/')),
            ],
        ),
        migrations.AlterField(
            model_name='student',
            name='profile_image',
            field=models.ImageField(default='no image', upload_to='api/students/images/'),
        ),
        migrations.AlterField(
            model_name='teacher',
            name='profile_image',
            field=models.ImageField(default='no image', upload_to='api/teachers/images/'),
        ),
    ]
