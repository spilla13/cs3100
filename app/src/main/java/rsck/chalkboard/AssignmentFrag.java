package rsck.chalkboard;


import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AssignmentFrag extends ListFragment{


    public static String[] NAMES =
            {
                    "Class 1",
                    "Class 2",
                    "Class 3"
            };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // An ArrayAdapter connects the array to our ListView
        // getActivity() returns a Context so we have the resources needed
        // We pass a default list item text view to put the data in and the
        // array
        ArrayAdapter<String> connectArrayToListView = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_activated_1,NAMES){
            //This lets you customize the why the list is displayed like font, color, etc.
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/chalk_font.ttf");
                TextView v = (TextView)super.getView(position, convertView, parent);
                v.setTextColor(Color.WHITE);
                v.setTextSize(20);
                v.setTypeface(font);
                return v;
            }
        };
        setListAdapter(connectArrayToListView);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    void showDetails(int index){
        Intent intent = new Intent();
        intent.setClass(getActivity(), ClassOverView.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

}



