package rsck.chalkboard;

import org.json.JSONObject;

public class Assignment {
    public double pointsReceived;
    public double pointsPossible;
    public int ID;
    public String name;

    public Assignment(JSONObject assignment, int user_ID, String token){
        ID = assignment.getInt("id");
        name = assignment.getString("name");
        pointsPossible = assignment.getDouble("points_possible");

        /*
        * Calls: http://cs3100.brod.es:3100/get/grade/?token='token'&user='ID'
        * Where: {"homework_id": 'assignment.getInt("id");'}
        */
        DjangoFunctions django = new DjangoFunctions();
        JSONObject query = new JSONObject();
        query.put("homework_id", assignment.getInt("id"));

        JSONObject gradeResponse = django.access("grade", Integer.toString(user_ID), token, query);
        JSONObject grade = gradeResponse.getJSONArray("data").getJSONObject(0);

        pointsReceived = grade.getDouble("points_received");
    }
    
}
