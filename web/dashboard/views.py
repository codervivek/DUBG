from django.shortcuts import render

# Create your views here.
from .forms import SignUpForm, StatusForm
from django.contrib.auth import login, authenticate
from django.shortcuts import render, redirect
from django.db.models import Max
from django.views import generic
from django.contrib.auth.models import User
from django.views.generic.edit import CreateView, UpdateView, DeleteView
from django.urls import reverse_lazy
from .models import Status, Type, Message


def signup(request):
    if request.method == 'POST':
        form = SignUpForm(request.POST)
        if form.is_valid():
            form.save()
            username = form.cleaned_data.get('username')
            raw_password = form.cleaned_data.get('password1')
            user = authenticate(username=username, password=raw_password)
            login(request, user)
            return redirect('type_create')
    else:
        form = SignUpForm()
    return render(request, 'signup.html', {'form': form})

def home(request):
    return render(request, 'index.html', {})

class StatusCreate(CreateView):
    model = Status
    form_class=StatusForm
    success_url = reverse_lazy( 'home')

class TypeCreate(CreateView):
    model = Type
    fields = ['user','status']
    success_url = reverse_lazy( 'home')

from django.core import serializers
from django.http import JsonResponse
import json

def allStatusAPI(request):
    all = json.loads(serializers.serialize("json", Status.objects.all()))
    return JsonResponse({"status":all})

def statusDetailAPI(request,pk):
    all = json.loads(serializers.serialize("json", [Status.objects.get(pk=pk),]))
    return JsonResponse({"s":all})

def allTypeAPI(request,pk):
    all = json.loads(serializers.serialize("json", Type.objects.exclude(user=request.GET.get('pk'))))
    return JsonResponse({"status":all})

def typeDetailAPI(request,pk):
    all = json.loads(serializers.serialize("json", [Type.objects.get(pk=pk),]))
    username = Type.objects.get(pk=pk).user.first_name +" "+Type.objects.get(pk=pk).user.last_name
    return JsonResponse({"s":all,"username":username})

def allMessageAPI(request):
    all = json.loads(serializers.serialize("json", Message.objects.exclude(user=request.GET.get('pk'))))
    return JsonResponse({"status":all})

def messageDetailAPI(request,pk):
    all = json.loads(serializers.serialize("json", [Message.objects.get(pk=pk),]))
    username = Message.objects.get(pk=pk).user.first_name +" "+Message.objects.get(pk=pk).user.last_name
    return JsonResponse({"status":all,"username":username})

def createStatus(request):
    if request.method=='POST':
        data = json.loads(request.body.decode("utf-8"))

def allUserAPI(request):
    all = json.loads(serializers.serialize("json", User.objects.all()))
    return JsonResponse({"user":all})

def userDetailAPI(request,pk):
    all = json.loads(serializers.serialize("json", [User.objects.get(pk=pk),]))
    return JsonResponse({"user":all})

from django.core.exceptions import ObjectDoesNotExist

# def statusUpdateAPI(request):
#     user = User.objects.get(pk=request.GET.get('pk'))
#     try:
#         user.location.latitude=request.GET.get('latitude')
#         user.location.longitude=request.GET.get('longitude')
#     except ObjectDoesNotExist:
#         location = Type.objects.create(user=request.user, latitude=request.GET.get('latitude'),longitude=request.GET.get('longitude'))
    
#     return JsonResponse({"success":True})


def locationCreateAPI(request):
    user = User.objects.get(pk=request.GET.get('pk'))
    user.location.latitude=request.GET.get('latitude')
    user.location.longitude=request.GET.get('longitude')
    return JsonResponse({"success":True})

def locationUpdateAPI(request):
    user = User.objects.get(pk=request.GET.get('pk'))
    user.location.latitude=request.GET.get('latitude')
    user.location.longitude=request.GET.get('longitude')
    user.location.save()
    print(request.GET)
    print(user.location.latitude)
    return JsonResponse({"success":True})

def messageCreateAPI(request):
    print(request.GET)
    message = Message.objects.create(user=User.objects.get(id=request.GET.get('pk'))
    ,latitude=request.GET.get('latitude'),longitude=request.GET.get('longitude')
    ,message=request.GET.get('message'))
    message.save()

    return JsonResponse({"success":True})

def statusCreateAPI(request):
    print(request.GET)
    status = Status.objects.create(latitude=request.GET.get('latitude'),longitude=request.GET.get('longitude'),name=request.GET.get('name'),people_stuck=request.GET.get('people_stuck'),people_injured=request.GET.get('people_injured'))

    return JsonResponse({"success":True})

# def messageCreateAPI(request):
#     print(request.GET)
#     message = Message.objects.create(user=User.objects.get(id=request.GET.get('pk'))
#     ,latitude=request.GET.get('latitude'),longitude=request.GET.get('longitude')
#     ,message=request.GET.get('message'))
#     message.save()

#     return JsonResponse({"success":True})