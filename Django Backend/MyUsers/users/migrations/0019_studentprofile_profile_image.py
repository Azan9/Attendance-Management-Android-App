# Generated by Django 4.0.3 on 2022-04-04 18:34

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0018_rename_user_id_id_studentprofile_user_id'),
    ]

    operations = [
        migrations.AddField(
            model_name='studentprofile',
            name='profile_image',
            field=models.ImageField(default='no image', upload_to='api/students/images/'),
        ),
    ]
