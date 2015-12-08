package com.example.maxfowler.regionalhealthmonitor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;

/**
 * This is the Login View Controller, which handles the user login, new user making, and
 * password recovery
 */
public class RHMLogin extends Activity{

    private String lns;
    private String pws;

    private RHMLogin ref = this;


    @Override
    /**
     * Creates the login view
     */
    protected void onCreate(Bundle savedInstanceState) {
        RHMDataCenter.initLookUpTables();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhmlogin);

        findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);

        Button login = (Button)findViewById(R.id.logButton);
        login.setOnClickListener(new LoginListener());
    }

    /**
     * This is a login listener which checks to see if a user can be logged in and then
     * does
     */
    private class LoginListener implements OnClickListener{

        @Override
        public void onClick(View v) {

            if(validateInputs() && login()) {
                findViewById(R.id.logPanel).setVisibility(View.INVISIBLE);


                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                //Act like login

                Intent i = new Intent(RHMLogin.this, RHMMenu.class);
                ((RHMAppData)ref.getApplication()).setUser(new RHMUser(lns, pws));

                startActivity(i);



            }
        }
    }

    /**
     * This validates the inputs, based on the email having a @ and the password being 3 or more characters
     * @return
     */
    private boolean validateInputs(){

        boolean retVal = true;

        EditText logName = (EditText)findViewById(R.id.logName);
        EditText pass = (EditText)findViewById(R.id.logPass);

        lns = logName.getText().toString();
        pws = pass.getText().toString();

        if(!lns.contains("@")){
            logName.setError("This is not a valid email!");
            retVal = false;
        }
        if (!(pws.length() >= 3)) {
            pass.setError("This password is too small.  It must be three or more characters");
            retVal = false;
        }



        return retVal;

    }

    /**
     * Login uses RHMDataCenter to attempt a login and reports an error on failure
     * @return
     */
    public boolean login(){
       boolean retVal = RHMDataCenter.loginResults(lns, pws);
        if(!retVal){
            EditText pass = (EditText)findViewById(R.id.logPass);
            pass.setError("Username or password doesn't exist!");
        }
        return retVal;
    }

    /**
     * Adds a user!
     * @param v
     */
    public void addUser(View v){
        handleUser(0, "Add User");
    }

    /**
     * Updates a password.
     * @param v
     */
    public void forgotPwd(View v){
        handleUser(1, "New Password");
    }

    /**
     * Just an inbetween method call to build the alert for a new user/password update
     * @param choice
     * @param title
     */
    public void handleUser(int choice, String title){
        buildAlert(title, choice);

    }

    /**
     * Build an alert dialog for new user/password update needs
     * @param title
     * @param choice
     */
    public void buildAlert(String title, int choice){


        final int c = choice;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        LayoutInflater li = this.getLayoutInflater();
        final View diagView = li.inflate(R.layout.rhmuseralert,null);
        builder.setView(diagView);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface diag, int which) {
                EditText et = ((EditText) diagView.findViewById(R.id.eh));
                EditText pt = ((EditText) diagView.findViewById(R.id.pwdh));

                String em = et.getText().toString();
                String pwd = pt.getText().toString();

                if (em != null && pwd != null) {
                    if (c == 0) {
                        RHMDataCenter.addNewUser(em, pwd);
                    } else if (c == 1) {
                        RHMDataCenter.editExistingUser(em, pwd);
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface diag, int which) {
                diag.cancel();
            }
        });

        builder.show();


    }

}


