package rsck.chalkboard;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Assignment implements Parcelable {
    public int ID;
    public double pointsReceived;
    public double pointsPossible;
    public String name;

    public Assignment(JSONObject assignment, int user_ID, String token){
        try {
            ID = assignment.getInt("id");
            name = assignment.getString("name");
            pointsPossible = assignment.getDouble("points_possible");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        * Calls: http://cs3100.brod.es:3100/get/grade/?token='token'&user='ID'
        * Where: {"homework_id": 'assignment.getInt("id");'}
        */
        DjangoFunctions django = new DjangoFunctions();
        JSONObject query = new JSONObject();

        try {
            query.put("homework_id", assignment.getInt("id"));

            JSONObject gradeResponse = django.access("grade", Integer.toString(user_ID), token, query);
            JSONObject grade = gradeResponse.getJSONArray("data").getJSONObject(0);

            if(grade.has("points_received"))
                pointsReceived = grade.getDouble("points_received");
            else
                pointsReceived = 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Assignment(JSONObject assignment){

        try {
            ID = assignment.getInt("id");
            name = assignment.getString("name");
            pointsPossible = assignment.getDouble("pointspossible");
            pointsReceived = assignment.getDouble("pointsreceived");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    /*Needed Parcelable Declarations below here*/
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flag){
        out.writeInt(ID);
        out.writeDouble(pointsReceived);
        out.writeDouble(pointsPossible);
        out.writeString(name);
    }

    public Assignment(Parcel in) {
        ID = in.readInt();
        pointsReceived = in.readDouble();
        pointsPossible = in.readDouble();
        name = in.readString();
    }

    public static final Parcelable.Creator<Assignment> CREATOR = new Parcelable.Creator<Assignment>() {
        public Assignment createFromParcel(Parcel in) {
            return new Assignment(in);
        }

        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };
}
