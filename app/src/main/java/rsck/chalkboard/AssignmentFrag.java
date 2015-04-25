package rsck.chalkboard;


import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class AssignmentFrag extends Fragment{

    public static String[] NAMES =
            {
                    "Assignment 1",
                    "Assignment 2",
                    "Assignment 3"
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_frag, container, false);

        TextView categoryTitle = (TextView) view.findViewById(R.id.categoryTitle);
        TextView categoryGrade = (TextView) view.findViewById(R.id.categoryGrade);
        TextView categoryPercent = (TextView) view.findViewById(R.id.categoryPercent);

        //change the text here!
        categoryTitle.setText("Homework");
        categoryGrade.setText("A");
        categoryPercent.setText("100%");

        return view;
    }

    public static class assignmentList extends ListFragment{

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



}
