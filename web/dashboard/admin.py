from django.contrib import admin

# Register your models here.
from .models import Type, Status

admin.site.register(Type)
admin.site.register(Status)
