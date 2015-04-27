package rsck.chalkboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ClassOverView extends Activity implements AddCategory.Communicator, Details.Communicator {
    private Course course;
    public static final int ADD_CAT_CODE = 2;
    public static final int ADD_HW_CODE = 1;
    public static final int CAT_FRAG_ID =  1;
    public static final int MOD_HW_CODE = 3;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.class_overview);

        Bundle bundle = getIntent().getExtras();
        course = bundle.getParcelable("course");


        //Calls the fragment category dynamically
        LinearLayout fragContainer = (LinearLayout) findViewById(R.id.categoryMain);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        ll.setId(CAT_FRAG_ID);

        ArrayList<WeightedGrades> courseGrades = course.getGrades();

        for(final WeightedGrades grades : courseGrades)
            getFragmentManager().beginTransaction()
                    .add(ll.getId(), CategoryFrag.newInstance(grades),
                            Integer.toString(grades.getID())).commit();

        //if(courseGrades.size() > 0)
            fragContainer.addView(ll);

        //Change the font of text
        //The font path
        String chalkFontPath = "fonts/chalk_font.ttf";
        String robotoFontPath = "fonts/roboto_light.ttf";

        Button sendAddAssignmentClick = (Button) findViewById(R.id.addAssignmentButton);
        Button sendCategoryClick = (Button) findViewById(R.id.addCategoryButton);
        Button sendHomeClick = (Button) findViewById(R.id.returnHomeButton);

        //Connect the text view
        //TextView cardTitles = (TextView) findViewById(R.id.card_title_id);
        TextView currentGradeText = (TextView) findViewById(R.id.Current_grade_text);
        TextView currentGrade = (TextView) findViewById(R.id.current_grade);
        TextView courseTitle = (TextView)  findViewById(R.id.courseTitle);


        //Make the new typeface (font)
        Typeface tf = Typeface.createFromAsset(getAssets(), chalkFontPath);
        //Typeface rl = Typeface.createFromAsset(getAssets(), robotoFontPath);

        courseTitle.setText(course.getCourseName()); //set the name of the course
        updateGrade();

        //Set the new typeface (font)
        currentGradeText.setTypeface(tf);
        currentGrade.setTypeface(tf);
        courseTitle.setTypeface(tf);
        sendAddAssignmentClick.setTypeface(tf);
        sendHomeClick.setTypeface(tf);
        sendCategoryClick.setTypeface(tf);
        //cardTitles.setTypeface(rl);

        sendAddAssignmentClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddAssignmentClick();
            }
        });

        sendHomeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeClick();
            }
        });

    }

    //This should refresh the page when the user finishes the add calls
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        final double pointsReceived;
        final double pointsPossible;
        final String assignmentName;
        final int catID;
        final Assignment assignmentToReplace;
        WeightedGrades cat = new WeightedGrades();


        if(resultCode == RESULT_OK) {
            if (requestCode == ADD_HW_CODE) {
                pointsReceived = intent.getDoubleExtra("pointsReceived", 0);
                pointsPossible = intent.getDoubleExtra("pointsPossible", 0);
                assignmentName = intent.getStringExtra("assignmentName");
                catID = intent.getIntExtra("catID", 0);

                Thread t = new Thread(new Runnable() {
                    public void run() {
                        final int id = course.addHomeworkToCategory(pointsReceived, pointsPossible,
                                                        assignmentName, catID);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                CategoryFrag contFrag = (CategoryFrag) getFragmentManager()
                                        .findFragmentByTag(Integer.toString(catID));

                                contFrag.add(course.getAssignmentByID(id));

                                updateGrade();
                            }
                        });
                    }
                });
                t.start();
            }
            else if (requestCode == MOD_HW_CODE){ //TODO: Add modify Fucntionality

                assignmentToReplace = intent.getParcelableExtra("assignment");
                //catID = intent.getIntExtra("catID", 0);

                ArrayList<WeightedGrades> grades = course.getGrades();
                for(WeightedGrades weightedGrades : grades)
                {
                    if(weightedGrades.getAssignmentByID(assignmentToReplace.ID) != null)
                        cat = weightedGrades;
                }

                final int originalCatID = cat.getID();
                final String originalCatName = cat.getName();



                Thread t = new Thread(new Runnable() {
                    public void run() {
                        course.replaceAssignment(assignmentToReplace);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CategoryFrag contFrag = (CategoryFrag) getFragmentManager()
                                        .findFragmentByTag(Integer.toString(originalCatID));

                                contFrag.remove(assignmentToReplace);
                                contFrag.add(assignmentToReplace);
                                updateGrade();
                            }
                        });
                    }
                });
                t.start();
            }
        }
    }

    protected void onAddAssignmentClick() {
        ArrayList <WeightedGrades> weightedGrades = course.getGrades();
        if (weightedGrades.size() > 0) {
            Intent AddAssignment = new Intent(this, AddAssignment.class);
            AddAssignment.putParcelableArrayListExtra("weightedGrades", weightedGrades);
            startActivityForResult(AddAssignment, ADD_HW_CODE);
        }
        else{
            AlertDialog.Builder alertNoCategory = new AlertDialog.Builder(context);

            //setTitle
            alertNoCategory.setTitle("No Category");
            //set dialog message
            alertNoCategory.setMessage("Please add a Category");
            alertNoCategory.setPositiveButton("Thank You!",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // if this button is clicked, close dialog
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertNoCategory.create();
            alertDialog.show();
        }
    }

    protected void onAddCategoryClick() {
        Intent AddCategory = new Intent(this, AddCategory.class);
        startActivityForResult(AddCategory, ADD_CAT_CODE);
    }

    protected void onHomeClick() {
            Intent intent = new Intent();
            intent.putExtra("course", course);
            setResult(RESULT_OK, intent);
            finish();
    }

    public void showDialog(View v) {
        FragmentManager manager = getFragmentManager();
        AddCategory myDialog = new AddCategory();
        myDialog.show(manager, "meow");
    }

    public void showCategory(View v){
        FragmentManager manager = getFragmentManager();
        AddCategory myDialog = new AddCategory();
        myDialog.show(manager,"meow");
    }

    public void onCategoryMessage(final String categoryName, final String categoryWeight){
        //set the name and weight here !!Take out the TOAST!!

        if(categoryName.length() >=4) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    double weight = Double.valueOf(categoryWeight) / 100;
                    final int newCatID = course.addWeightedCategory(categoryName, weight);
                    final WeightedGrades newGrades = course.getCatWithID(newCatID);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getFragmentManager().beginTransaction()
                                    .add(CAT_FRAG_ID, CategoryFrag.newInstance(newGrades), Integer.toString(newCatID)).commit();
                            updateGrade();
                        }
                    });
                }
            });
            t.start();
        }
        else
        {
            AlertDialog.Builder alertNoCategory = new AlertDialog.Builder(context);

            //setTitle
            alertNoCategory.setTitle("Invalid Name!");
            //set dialog message
            alertNoCategory.setMessage("Must be (4-100) characters!");
            alertNoCategory.setPositiveButton("Thank You!",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // if this button is clicked, close dialog
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertNoCategory.create();
            alertDialog.show();
        }
    }



    public void onDetailsMessage(final int assignmentID, String method){
        //set the name and weight here !!Take out the TOAST!!
        if(method == "modify"){
           Intent Modify = new Intent(this, AssignmentModify.class);
            Modify.putExtra("assignment", course.getAssignmentByID(assignmentID));
            Modify.putExtra("weightedGrades", course.getGrades());
           startActivityForResult(Modify, MOD_HW_CODE);


        }else if(method == "delete"){
            Thread t = new Thread(new Runnable() {
                public void run() {
                final int catID = course.removeAssignmentID(assignmentID);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CategoryFrag contFrag = (CategoryFrag) getFragmentManager()
                                .findFragmentByTag(Integer.toString(catID));

                        AssignmentFrag oldFrag = (AssignmentFrag) contFrag.getChildFragmentManager()
                                .findFragmentByTag(Integer.toString(assignmentID));

                        if(oldFrag != null)
                            contFrag.getChildFragmentManager().beginTransaction().remove(oldFrag).commit();

                        contFrag.updateGrade();

                        updateGrade();
                    }
                });
                }
            });
            t.start();
        }
    }


    public void onBackPressed(){

        Intent intent = new Intent();
        intent.putExtra("course", course);
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    private void updateGrade(){
        TextView currentGrade = (TextView) findViewById(R.id.current_grade);

        double percentGrade = course.getCourseGrade()*100/course.getWeightTotal();
        String sPercentGrade = new BigDecimal(percentGrade).round(new MathContext(4, RoundingMode.HALF_UP)).toString();

        currentGrade.setText(sPercentGrade + "%");
    }

    public Course getCourse(){return course;}
}
