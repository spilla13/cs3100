package rsck.chalkboard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jacob.
 */
public class Course {
    private int ID;
    private String courseName;
    private String schoolName;
    private ArrayList<WeightedGrades> grades;

    public Course(JSONObject courseJSON, int user_ID, String token){
        ID = courseJSON.getInt("id");
        courseName = courseJSON.getString("name");
        schoolName = courseJSON.getString("school");

        grades = new ArrayList<>();

        loadGrades(user_ID, token);

    }

    //Getters.
    public int getID() {
        return ID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    //Loads all grades for this Course.
    public void loadGrades(int user_ID, String token){

        //TODO: add grades to course.
        //TODO: send JSON to query stuff.

        /*
        *  Calls: http://cs3100.brod.es:3100/get/grade/?token='token'&user='ID'
        *  sendString = "{ \"course_id\" = ID,}"
        *
        * */

        DjangoFunctions django = new DjangoFunctions();
        JSONObject response = django.access("grade", Integer.toString(user_ID), token);

        //Now grab all unique Cat_IDs for the course...
        if(response.getBoolean("success")){
            JSONArray data = response.getJSONArray("data");

            ArrayList<Integer> uniqueIDs = new ArrayList<Integer>();

            for(int i = 0; i < data.length(); i++) {

                JSONObject grade = data.getJSONObject(i);

                //TODO: SEND JSON to query Data
                /*
                * Calls: http://cs3100.brod.es:3100/get/homework/?token='token'&user='ID'
                * Where: {"id": 'grade.getInt("homework_id")'}
                */
                JSONObject homework = django.access("homework", Integer.toString(user_ID), token);
                JSONObject matchedHomework = homework.getJSONArray("data").getJSONObject(0);

                int categoryID = matchedHomework.getInt("category_id");

                //Category is unique, add to grades.
                if(!uniqueIDs.contains(categoryID)){
                    WeightedGrades newWeightedCat = new WeightedGrades(categoryID, user_ID, token);

                    grades.add(newWeightedCat);
                }
            }
        }

    }

    //returns grade as a decimal.
    //Multiply by 100 for percent.
    public float getCourseGrade(){
        float total = 0;
        
        for(WeightedGrades weightedGrades : grades)
            total += weightedGrades.weightedTotal();
            
        return total;
    }
}
