from django.db import models
from django.contrib.auth.models import User
# Create your models here.

class Status(models.Model):
    postedby = models.ForeignKey(User, on_delete=models.CASCADE, help_text="a")
    latitude = models.DecimalField(max_digits=30, decimal_places=15)
    longitude = models.DecimalField(max_digits=30, decimal_places=15)
    num_people = models.PositiveIntegerField()
    num_injured = models.PositiveIntegerField()
    comments = models.TextField()
