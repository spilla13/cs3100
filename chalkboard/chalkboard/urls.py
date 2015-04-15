from django.conf.urls import patterns, include, url
from django.contrib import admin

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'chalkboard.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^auth/', 'tracker.views.authenticate'),
    url(r'^add/course/', 'tracker.views.addCourse'),
    url(r'^add/homework/', 'tracker.views.addHomework'),
    url(r'^add/grade/', 'tracker.views.addGrade'),

    url('^', include('django.contrib.auth.urls', namespace="auth")), 
    url('^', include('registration.backends.default.urls')),
)
