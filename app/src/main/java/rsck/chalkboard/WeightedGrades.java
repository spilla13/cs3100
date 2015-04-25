package rsck.chalkboard;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by Jacob.
 */
public class WeightedGrades implements Parcelable{
    private int ID;
    private String name;
    private double weight;
    private ArrayList<Assignment> assignments;
    private int user_ID;
    private String token;

    public WeightedGrades(){assignments = new ArrayList<>();}

    public WeightedGrades(JSONObject data, int user_ID, String token){
        this.user_ID = user_ID;
        this.token = token;

        try {
            ID = data.getInt("id");
            name = data.getString("name");
            weight = data.getDouble("weight");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assignments = new ArrayList<>();
    }

    public WeightedGrades(int cat_ID, int user_ID, String token){
        this.assignments = new ArrayList<Assignment>();
        this.ID = cat_ID;
        this.user_ID = user_ID;
        this.token = token;

        /*
        * Calls: http://cs3100.brod.es:3100/get/category/?token='token'&user='ID'
        * Where: {"id":ID}
        */
        DjangoFunctions django = new DjangoFunctions();
        JSONObject query = new JSONObject();

        try {
            query.put("id", ID);

            JSONObject response = django.access("category", Integer.toString(user_ID), token, query);
            JSONObject category = response.getJSONArray("data").getJSONObject(0);

            weight = category.getDouble("weight");
            name = category.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loadAssignments();
    }

    public ArrayList<Assignment> getAssignments(){return assignments;}

    public String getName(){return name;}

    public Double getWeight(){return weight;}

    public int getID(){return ID;}

    public String getLetterGrade(){
        double total = weightedTotal();
        String letterGrade;

        if(total > .9)
            letterGrade = "A";
        else if(total > .8)
            letterGrade = "B";
        else if(total > .7)
            letterGrade = "C";
        else if(total > .6)
            letterGrade = "D";
        else
            letterGrade = "F";

        return letterGrade;
    }

    public double weightedTotal(){
        return weight * unweightedTotal();
    }

    public double unweightedTotal(){
        return pointsReceived() / pointsPossible();
    }

    public float pointsPossible(){
        float total = 0;

        for(Assignment assignment : assignments)
            total += assignment.pointsPossible;

        if(total == 0)
            total = 1;

        return total;
    }

    public float pointsReceived(){
        float total = 0;

        for(Assignment assignment : assignments)
            total += assignment.pointsReceived;

        return total;
    }

    public void loadAssignments(){
        DjangoFunctions django = new DjangoFunctions();

        /*
        * Calls: http://cs3100.brod.es:3100/get/homework/?token='token'&user='ID'
        * Where: {"category_id": 'ID'}
        */
        JSONObject query = new JSONObject();

        try {
            query.put("category_id", ID);

            JSONObject hwResponse = django.access("homework", Integer.toString(user_ID), token, query);
            JSONArray homeworks = hwResponse.getJSONArray("data");

            for(int i = 0; i < homeworks.length(); i++){
                JSONObject assignment = homeworks.getJSONObject(i);

                //TODO: Add to Assignments.
                Assignment newAssignment = new Assignment(assignment, user_ID, token);
                assignments.add(newAssignment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean addAssignment(double pointsReceived, double pointsPossible, String name,
                              int cat, int course){
        DjangoFunctions django = new DjangoFunctions();
        JSONObject homework = new JSONObject();
        JSONObject grade = new JSONObject();
        Boolean success = false;

        //prepare and add to homework table.
        try{
            homework.put("name", name);
            homework.put("points_possible", pointsPossible);
            homework.put("categoryid", cat);
        }catch(JSONException e) {
            e.printStackTrace();
        }

        JSONObject response = django.add("homework", Integer.toString(user_ID), token, homework);

        try{
            success = response.getBoolean("success");
        }catch(JSONException e){
            e.printStackTrace();
        }


        //prepare and add to course table
        try {
            if(success) {
                JSONObject data = response.getJSONObject("data");
                grade.put("courseid", course);
                grade.put("homeworkid", data.getInt("id"));
                grade.put("pointsreceived", pointsReceived);
                response = django.add("grade", Integer.toString(user_ID), token, grade);

                if(response.getBoolean("success")){
                    homework.put("id", data.getInt("id"));
                    homework.put("pointsreceived", pointsReceived);

                    Assignment newAssignment = new Assignment(homework);
                    assignments.add(newAssignment);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }


        return success;
    }


    /*Needed Parcelable Declarations below here*/
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flag){
        out.writeInt(ID);
        out.writeString(name);
        out.writeDouble(weight);
        out.writeTypedList(assignments);
        out.writeInt(user_ID);
        out.writeString(token);
    }

    public WeightedGrades(Parcel in) {
        this();

        ID = in.readInt();
        name = in.readString();
        weight = in.readDouble();
        in.readTypedList(assignments, Assignment.CREATOR);
        user_ID = in.readInt();
        token = in.readString();
    }

    public static final Parcelable.Creator<WeightedGrades> CREATOR = new Parcelable.Creator<WeightedGrades>() {
        public WeightedGrades createFromParcel(Parcel in) {
            return new WeightedGrades(in);
        }

        public WeightedGrades[] newArray(int size) {
            return new WeightedGrades[size];
        }
    };

}
