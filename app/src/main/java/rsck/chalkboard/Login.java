package rsck.chalkboard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;


public class Login extends Activity {

    private String[] serverType;

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
        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        //final TextView rando = (TextView) findViewById(R.id.randomText);

        serverType = getResources().getStringArray(R.array.server_choice);
        final Spinner serverSpinner = (Spinner) findViewById(R.id.server_select);

        //Here is the code that sets up the spinner
        ArrayAdapter<String> serverAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, serverType);
        serverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serverSpinner.setAdapter(serverAdapter);

        //Method to call the login function on the button press
        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            //Get the spinner value
            final String serverToast = String.valueOf(serverSpinner.getSelectedItem());
            //Make the toast
            Toast.makeText(getApplicationContext(), serverToast, Toast.LENGTH_SHORT).show();

            //Tells users they are logging in
            Toast.makeText(getApplicationContext(), "Authenticating",
                    Toast.LENGTH_SHORT).show();

            Thread t = new Thread(new Runnable() {
                public void run() {
                    //Call the login function and let it do the rest

                    String userName = String.valueOf(username.getText());
                    String passWord = String.valueOf(password.getText());
                    /*
                    Check if passwords match, if not, clear textboxes and ask user to input again
                    If they do, show chalk check mark and allow submission
                    */
                    User user = new User();
                    if(user.login(userName, passWord)) {
                        sendToHome(user);
                        //Finish the login activity and prevent users from going back
                        finish();
                    }
                    else
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Error on Login",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                }
            });
            t.start();

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignUp = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cs3100.brod.es:3100/register/"));
                startActivity(toSignUp);
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
    public void sendToHome(User user){
        /*If the user name is valid then send to homescreen */
        Intent nextIntent = new Intent(this, Home.class);
        //Send the username and password to the database and then next activity
        nextIntent.putExtra("user", user);

        //Start the activity (aka go to the next screen)
        startActivity(nextIntent);
    }
}
