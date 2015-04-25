package rsck.chalkboard;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Jacob.
 */
public class User implements Parcelable{
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

    public ArrayList<Course> getCourses() {return courses;}

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
            }
                //TODO: Handle unsuccessfull login.
        }
        catch (Exception e){
            e.printStackTrace();
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
    public boolean addCourse(String courseName, String schoolName) {
        DjangoFunctions django = new DjangoFunctions();

        JSONObject course = new JSONObject();

        try {
            course.put("name", courseName);
            course.put("school", schoolName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        *
        *  Calls: http://cs3100.brod.es:3100/add/course/?token='token'&user='ID'
        */
        JSONObject response = django.add("course", Integer.toString(ID),token,course);

        Boolean success = false;
        try {
            success = response.getBoolean("success");
            if (success) {
                JSONObject data = response.getJSONObject("data");
                course.put("id", data.getInt("id"));
                Course newCourse = new Course(course, ID, token);
                courses.add(newCourse);
            } else {
                //TODO: Handle failed course add.
            }
        }   catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
        Loads all courses for the user from the server.

        Post: Adds courses to ArrayList courses.
     */
    public void load(){
        DjangoFunctions django = new DjangoFunctions();

        JSONObject gradeQuery = new JSONObject();
        gradeQuery.put("user_id",ID);
        JSONObject gradeRes = django.access("grade", Integer.toString(ID), token ,gradeQuery);
        JSONArray gradeDat = gradeRes.getJSONArray("data");
        ArrayList<Integer> myCourses = new ArrayList<>();

        for(int i = 0; i < gradeDat.length(); i++) {
            Integer thisUser = gradeDat.getJSONObject(i).getInt("user_id");
            if(thisUser == ID) {
                Integer thisCourseID = gradeDat.getJSONObject(i).getInt("course_id");
                if (!myCourses.contains(thisCourseID))
                    myCourses.add(thisCourseID);
            }
        }

        /*
        * Calls:
        * URL = http://cs3100.brod.es:3100/get/course/?token='token'&user='ID'
        * "{}"
        */
        JSONObject JSONQuery = new JSONObject();
        JSONObject courseRes = django.access("course", Integer.toString(ID), token, JSONQuery);
        try {
            if(courseRes.getBoolean("success")){
                JSONArray courseData = courseRes.getJSONArray("data");

                for(int i = 0; i < courseData.length(); i++){
                    int courseID = courseData.getJSONObject(i).getInt("id");
                    if(myCourses.contains(courseID)) {
                        JSONObject courseJSON = courseData.getJSONObject(i);

                        Course newCourse = new Course(courseJSON, ID, token);

                        courses.add(newCourse);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*Needed Parcelable Declarations below here*/
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flag){
        out.writeInt(ID);
        out.writeString(username);
        out.writeString(token);
        out.writeTypedList(courses);
    }

    public User(Parcel in) {
        this();

        ID = in.readInt();
        username = in.readString();
        token = in.readString();
        in.readTypedList(courses, Course.CREATOR);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
