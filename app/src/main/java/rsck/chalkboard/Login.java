package rsck.chalkboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Login extends Activity {
    //Basically a constructor for when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets what layout it will display
        setContentView(R.layout.activity_login);
        //Connect the EditText boxes, buttons and textviews to the code using their xml id
        final EditText username = (EditText) findViewById(R.id.userName);
        final EditText password = (EditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        //final TextView rando = (TextView) findViewById(R.id.randomText);


        //Method to call the login function on the button press
        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Call the login function and let it do the rest

                String userName = String.valueOf(username.getText());
                String passWord = String.valueOf(password.getText());
                sendToHome(v, userName, passWord);
                //Finish the login activity and prevent users from going back
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add things to the action bar (if there is one)
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //The login function for the login page
    public void sendToHome(View view, String x, String y){
        /*If the user name is valid then send to homescreen */
        Intent sendToHome = new Intent(this, Home.class);
        //Send the username and password to the database and then next activity
        sendToHome.putExtra("theUser", x);
        sendToHome.putExtra("password", y);
        //Start the activity (aka go to the next screen)
        startActivity(sendToHome);
    }
}
