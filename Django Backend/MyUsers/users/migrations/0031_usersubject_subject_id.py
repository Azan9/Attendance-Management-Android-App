# Generated by Django 4.0.3 on 2022-04-08 16:15

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0030_remove_usersubject_course_name'),
    ]

    operations = [
        migrations.AddField(
            model_name='usersubject',
            name='subject_id',
            field=models.IntegerField(null=True),
        ),
    ]
