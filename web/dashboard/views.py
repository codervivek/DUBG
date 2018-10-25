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
from .models import Status, Type


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

def createStatus(request):
    if request.method=='POST':
        data = json.loads(request.body.decode("utf-8"))

def allUserAPI(request):
    all = json.loads(serializers.serialize("json", User.objects.all()))
    return JsonResponse({"user":all})

from django.core.exceptions import ObjectDoesNotExist

def statusUpdateAPI(request):
    user = User.objects.get(pk=request.GET.get('pk'))
    try:
        user.location.latitude=request.GET.get('latitude')
        user.location.longitude=request.GET.get('longitude')
    except ObjectDoesNotExist:
        location = Type.objects.create(user=request.user, latitude=request.GET.get('latitude'),longitude=request.GET.get('longitude'))
    
    return JsonResponse({"success":True})

