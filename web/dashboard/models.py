from django.db import models
from django.contrib.auth.models import User
# Create your models here.

class Status(models.Model):
    postedby = models.ForeignKey(User, on_delete=models.CASCADE, help_text="a")
    latitude = models.DecimalField(max_digits=30, decimal_places=15, help_text="Enter your Latitude position")
    longitude = models.DecimalField(max_digits=30, decimal_places=15, help_text="Enter your Longitude position")
    people_stuck = models.PositiveIntegerField(help_text="Enter number of People stuck")
    people_injured = models.PositiveIntegerField(help_text="Enter number of People injured")
    name = models.CharField(max_length=100,default="None")

class Type(models.Model):
    user=models.OneToOneField(User, related_name="location", on_delete=models.CASCADE, help_text="a")
    latitude = models.DecimalField(max_digits=30, decimal_places=15,blank=True, null=True)
    longitude = models.DecimalField(max_digits=30, decimal_places=15,blank=True, null=True)
    TYPE = (
        ('v', 'Volunteer'),
        ('r', 'Rescuer'),
    )
    status = models.CharField(
        max_length=1,
        choices=TYPE,
        default='v',
        help_text='Select your category',
    )
    engaged=models.OneToOneField(Status, related_name="rescuer", help_text="a",blank=True, null=True)

class Message(models.Model):
    user=models.ForeignKey(User, related_name="message", on_delete=models.CASCADE, help_text="a")
    latitude = models.DecimalField(max_digits=30, decimal_places=15,blank=True, null=True)
    longitude = models.DecimalField(max_digits=30, decimal_places=15,blank=True, null=True)
    message = models.TextField()