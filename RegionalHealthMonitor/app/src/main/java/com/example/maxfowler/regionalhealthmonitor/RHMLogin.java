package com.example.maxfowler.regionalhealthmonitor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ProgressBar;

public class RHMLogin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhmlogin);

        findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);

        Button login = (Button)findViewById(R.id.logButton);
        login.setOnClickListener(new LoginListener());
    }


    private class LoginListener implements OnClickListener{

        @Override
        public void onClick(View v) {

            if(validateInputs()) {
                findViewById(R.id.logPanel).setVisibility(View.INVISIBLE);


                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                //Act like login

                Intent i = new Intent(RHMLogin.this, RHMMenu.class);
                startActivity(i);



            }
        }
    }

    private boolean validateInputs(){

        boolean retVal = true;

        EditText logName = (EditText)findViewById(R.id.logName);
        EditText pass = (EditText)findViewById(R.id.logPass);

        String lns = logName.getText().toString();
        String pws = logName.getText().toString();

        if(!lns.contains("@")){
            logName.setError("This is not a valid email!");
            retVal = false;
        }
        if (!(pws.length() > 5)) {
            pass.setError("This password is too small.  It must be five or more characters");
            retVal = false;
        }



        return retVal;

    }
}
