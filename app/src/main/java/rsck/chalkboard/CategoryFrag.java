package rsck.chalkboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CategoryFrag extends android.app.Fragment {

    public static CategoryFrag newInstance(String text){
        CategoryFrag f = new CategoryFrag();

        Bundle b = new Bundle();
        b.putString("text", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_frag, container, false);

        TextView categoryTitle = (TextView) view.findViewById(R.id.categoryTitle);
        TextView categoryGrade = (TextView) view.findViewById(R.id.categoryGrade);
        TextView categoryPercent = (TextView) view.findViewById(R.id.categoryPercent);

        //change the text here!
        categoryTitle.setText(getArguments().getString("text"));
        categoryGrade.setText("A");
        categoryPercent.setText("100%");


        return view;
    }


}
