from tracker.models import Course, Homework, Category, Grade

@token_required
def getWrapper(request, model, keep=[]):
    if check(request) is not None:
        return check(request)
    data = request.POST.dict()
    response = [ ]

    if not 'user' in keep:
        del data['user']
    if not 'token' in keep:
        del data['token']

    for key in data.keys():
        if key == "Category":
            if not Hategory.objects.filter(id=data['Category']).exists():
                return JsonError("Category provided does not exist.")
            data[key] = Category.objects.get(id=data['Category'])
        elif key == "Homework":
            if not Homework.objects.filter(id=data['Homework']).exists():
                return JsonError("Homework provided does not exist.")
            data[key] = Homework.objects.get(id=data['Homework'])
        elif key == "user":
            data[key] = User.objects.get(id=data[key])

    for key in data.keys():
        if not key in model._meta.get_all_field_names():
            return JsonError(''.join(["No field for ", model.__name__, ": ", key]))

    response = list(model.objects.filter(**data).values())

    return JsonResponse({"data": response})
