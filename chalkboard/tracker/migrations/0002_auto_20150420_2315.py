# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('tracker', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='homework',
            name='weight',
        ),
        migrations.AddField(
            model_name='category',
            name='weight',
            field=models.FloatField(default=1),
            preserve_default=True,
        ),
    ]
