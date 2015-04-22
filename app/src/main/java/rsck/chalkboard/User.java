package rsck.chalkboard;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Jacob.
 */
public class User {
    private int ID;
    private String username;
    private String token;
    private ArrayList<Course> courses;

    public User(){
        courses = new ArrayList<Course>();
    }

    //Getters
    public String getUserName(){return username;}

    public String getToken(){return token;}

    /*
        This function authenticates a user with the server.

        If the user/pass combination is valid,
            it assigns the ID and Token, and returns true.

        If the user/pass combination is invalid,
            The function returns false.

        Pre:Neither Param should be Null
     */
    public boolean login(final String user, final String password){
        DjangoFunctions django =  new DjangoFunctions();
        JSONObject response = django.authenticate(user,password);
        boolean success = false;

        try {
            success = response.getBoolean("success");
            if (success){
                ID = response.getInt("user");
                token = response.getString("token");
                username = user;

                //TODO: getClasses, grades, etc. upon login.
                //loadCourses();
            } else {
                //TODO: Handle unsuccessfull login.
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return success;
    }


    /*
     * Pre: Neither Name should be NULL
     * Post: adds a new course to ArrayList courses.
     *
     * Returns: True on successful add.
     *          False on failed course add.
     */
    /*public boolean addCourse(String courseName, String schoolName) {
        DjangoFunctions django;

        JSONObject course = new JSONObject();

        course.put("name", courseName);
        course.put("school", schoolName);

        /*
        *  //TODO: Add Accessor Call (ADD)
        *  Calls: http://cs3100.brod.es:3100/add/course/?token='token'&user='ID'
        *//*
        JSONObject response; // = accessor.post(URL, course.toString());

        if(response.getBoolean("success")){
            JSONObject data = response.getJSONObject("data");
            Course newCourse = new Course(data, ID, token);
            courses.add(newCourse);
        }
        else{
            //TODO: Handle failed course add.
        }

        return response.getBoolean("success");
    }*/

    /*
        Loads all courses for the user from the server.

        Post: Adds courses to ArrayList courses.
     */
    public void loadCourses(){
        DjangoFunctions django = new DjangoFunctions();

        /*
        * Calls:
        * URL = http://cs3100.brod.es:3100/get/course/?token='token'&user='ID'
        * "{}"
        */
        JSONObject JSONQuery = new JSONObject();
        JSONObject response = django.access("course", Integer.toString(ID), token, JSONQuery);

        if(response.getBoolean("success")){
            JSONArray data = response.getJSONArray("data");

            for(int i = 0; i < data.length(); i++){
                JSONObject courseJSON = data.getJSONObject(i);

                Course newCourse = new Course(courseJSON, ID, token);

                courses.add(newCourse);
            }
        } else {
            //TODO: Handle Failed Course request.
        }
    }

}
