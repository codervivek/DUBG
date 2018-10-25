from django.conf.urls import url

from . import views
from django.views.generic.base import RedirectView


urlpatterns = [
    url(r'^$', views.home, name='home'),
    url(r'^status/create/$', views.StatusCreate.as_view(), name='status_create'),
    url(r'^a/$', views.allStatusAPI, name='status_list'),
    url(r'^b/(?P<pk>\d+)$', views.statusDetailAPI, name='status_detail'),
    url(r'^users/$', views.allUserAPI, name='user_list'),
    url(r'^status/update$', views.statusUpdateAPI, name='status_update'),
    url(r'^type/create/$', views.TypeCreate.as_view(), name='type_create'),
]