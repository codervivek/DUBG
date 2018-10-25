from django.contrib import admin

# Register your models here.
from .models import Type, Status, Message

admin.site.register(Type)
admin.site.register(Message)
admin.site.register(Status)
