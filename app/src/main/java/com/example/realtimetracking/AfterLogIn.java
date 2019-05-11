package com.example.realtimetracking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AfterLogIn extends AppCompatActivity
{
    private static final int MY_PERMISSION_REQUEST_CODE = 1000;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FusedLocationProviderClient client;

    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_log_in);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Users");

        button=findViewById(R.id.button);
        textView=findViewById(R.id.textView);

        //initializing client data type to get location
        client= LocationServices.getFusedLocationProviderClient(this );

        /**
         asks user for permission for accessing location**/
        checkPermissionForLocation();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                /**
                 * checks if the location permissions are granted or not and only then proceeds
                 * **/

                if(ActivityCompat.checkSelfPermission(AfterLogIn.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(AfterLogIn.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
                {return;}

                    client.getLastLocation().addOnSuccessListener(AfterLogIn.this, new OnSuccessListener<Location>() {
                      @Override
                      public void onSuccess(Location location)
                      {
                            if(location!=null)
                            {
                                /**
                                 * Converts recieved cordinates into latitude and longitude*/
                                Double latitude=location.getLatitude();
                                Double longitude=location.getLongitude();
                                Double altitude=location.getAltitude();
                                String ans="Email id - "+firebaseUser.getEmail()+"\n"+"Latitude - "+latitude+"\n"
                                        +"Longitude - "+longitude+"\n"+"Altitude - "+altitude;
                                textView.setText(ans);

                                /**
                                 * Stores the information of user with its location on firebase database
                                 */
                                UserModel model=new UserModel(firebaseUser.getEmail(),""+latitude,""+longitude,""+altitude);
                                databaseReference.child(firebaseUser.getEmail().substring(0,6)).setValue(model)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                    Toast.makeText(AfterLogIn.this,"Sucess",Toast.LENGTH_SHORT).show();
                                                else
                                                    Toast.makeText(AfterLogIn.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                      }
                  });
            }
        });
    }

    /**
     * Gets permission promptly
     */
    private void checkPermissionForLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_CODE);
        }

    }
}
