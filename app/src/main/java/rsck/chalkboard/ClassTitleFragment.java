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

import java.util.ArrayList;


public class ClassTitleFragment extends ListFragment{

    private ArrayAdapter<String> courseNames;
    private ArrayList<String> courseNamesList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        courseNamesList = new ArrayList<>();

        Bundle args = getArguments();
        if (args != null){
            ArrayList<Course> courses = args.getParcelableArrayList("courses");

            courseNamesList = new ArrayList<>();

            for (Course getCourse : courses)
                courseNamesList.add(getCourse.getCourseName());
        }

        courseNames = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_activated_1, courseNamesList){
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
        setListAdapter(courseNames);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    void showDetails(int index){
        Intent intent = new Intent(getActivity(), ClassOverView.class);

        Home homeActivity = (Home) getActivity();
        Course course = homeActivity.getUser().getCourse(index);

        intent.putExtra("index", index);
        intent.putExtra("course", course);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        Bundle bundle = intent.getExtras();
        Course course = bundle.getParcelable("course");

        Home homeActivity = (Home) getActivity();
        boolean found = homeActivity.getUser().updateCourse(course);

        if(found)
            homeActivity.updateTitleFragment();
    }

    public void addElement(String courseName){
        courseNames.add(courseName);
        courseNames.notifyDataSetChanged();
    }
}
