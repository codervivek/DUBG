from django.conf.urls import url

from . import views
from django.views.generic.base import RedirectView


urlpatterns = [
    url(r'^$', views.home, name='home'),
    url(r'^status/create/$', views.StatusCreate.as_view(), name='status_create'),
    url(r'^a/$', views.allStatusAPI, name='status_list'),
    url(r'^b/(?P<pk>\d+)$', views.statusDetailAPI, name='status_detail'),
    url(r'^type/$', views.allTypeAPI, name='type_list'),
    url(r'^type/(?P<pk>\d+)$', views.typeDetailAPI, name='type_detail'),
    url(r'^users/$', views.allUserAPI, name='user_list'),
    url(r'^users/(?P<pk>\d+)$', views.userDetailAPI, name='user_detail'),
    # url(r'^status/update$', views.statusUpdateAPI, name='status_update'),
    url(r'^type/create/$', views.TypeCreate.as_view(), name='type_create'),
    url(r'^message/$', views.allMessageAPI, name='message_list'),
    url(r'^message/(?P<pk>\d+)$', views.messageDetailAPI, name='message_detail'),
    url(r'^location/update$', views.locationUpdateAPI, name='location_update'),
    url(r'^message/create$', views.messageCreateAPI, name='message_create'),
    # url(r'^type/connect$', views.connectType, name='connect_type'),

]