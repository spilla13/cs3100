package rsck.chalkboard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jacob.
 */
public class WeightedGrades {
    private int ID;
    private String name;
    private double weight;
    private ArrayList<Assignment> assignments;

    public WeightedGrades(int cat_ID, int user_ID, String token){
        assignments = new ArrayList<Assignment>();
        ID = cat_ID;

        /*
        * Calls: http://cs3100.brod.es:3100/get/category/?token='token'&user='ID'
        * Where: {"id":ID}
        */
        DjangoFunctions django = new DjangoFunctions();
        JSONObject query = new JSONObject();
        query.put("id", ID);

        JSONObject response = django.access("category", Integer.toString(user_ID), token);//, query);
        JSONObject category = response.getJSONArray("data").getJSONObject(0);

        weight = category.getDouble("weight");
        name = category.getString("name");

        loadAssignments(user_ID, token);
    }

    public void setWeight(double newWeight){weight = newWeight;}

    public double weightedTotal(){
        return weight * unweightedTotal();
    }

    public float unweightedTotal(){
        return pointsReceived() / pointsPossible();
    }

    public float pointsPossible(){
        float total = 0;

        for(Assignment assignment : assignments)
            total += assignment.pointsPossible;

        return total;
    }

    public float pointsReceived(){
        float total = 0;

        for(Assignment assignment : assignments)
            total += assignment.pointsReceived;

        return total;
    }

    public void loadAssignments(int user_ID, String token){
        DjangoFunctions django = new DjangoFunctions();

        /*
        * Calls: http://cs3100.brod.es:3100/get/homework/?token='token'&user='ID'
        * Where: {"category_id": 'ID'}
        */
        JSONObject query = new JSONObject();
        query.put("category_id", ID);

        JSONObject hwResponse = django.access("homework", Integer.toString(user_ID), token, query);
        JSONArray homeworks = hwResponse.getJSONArray("data");

        for(int i = 0; i < homeworks.length(); i++){
            JSONObject assignment = homeworks.getJSONObject(i);

            //TODO: Add to Assignments.
            Assignment newAssignemt = new Assignment(assignment, user_ID, token);

            assignments.add(newAssignemt);
        }
    }
}
