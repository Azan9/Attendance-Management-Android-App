# Generated by Django 4.0.3 on 2022-04-08 19:03

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0031_usersubject_subject_id'),
    ]

    operations = [
        migrations.AlterField(
            model_name='attendancelist',
            name='id',
            field=models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID'),
        ),
    ]
