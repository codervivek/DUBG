from django.conf.urls import url

from . import views
from django.views.generic.base import RedirectView


urlpatterns = [
    url(r'^$', views.home, name='home'),
]