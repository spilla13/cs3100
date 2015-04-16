from django.conf.urls import patterns, include, url
from django.contrib import admin

admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'chalkboard.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^add/course/', 'tracker.views.addCourse'),
    url(r'^add/category/', 'tracker.views.addCategory'),
    url(r'^add/homework/', 'tracker.views.addHomework'),
    url(r'^add/grade/', 'tracker.views.addGrade'),

    url(r'^get/courses/', 'tracker.views.getCourses'),

    url('^', include('django.contrib.auth.urls', namespace="auth")), 
    url('^', include('registration.backends.default.urls')),
    url(r'', include('tokenapi.urls')),
)
