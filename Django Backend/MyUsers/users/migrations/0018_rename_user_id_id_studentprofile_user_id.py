# Generated by Django 4.0.3 on 2022-04-04 12:21

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0017_rename_user_id_studentprofile_user_id_id'),
    ]

    operations = [
        migrations.RenameField(
            model_name='studentprofile',
            old_name='user_id_id',
            new_name='user_id',
        ),
    ]
