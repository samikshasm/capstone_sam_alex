package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by samikshasm on 7/24/17.
 */

public class MorningQS extends AppCompatActivity {

    private String userIDMA;
    private String startActivity1;
    public static final String startActivity = "main";
    private String oral;
    private String vaginal;
    private String anal;
    private String vaginalCondomStr = "NA";
    private String analCondomStr = "NA";
    private String vaginalConsentStr = "NA";
    private String analConsentStr= "NA";
    private String oralConsentStr= "NA";
    private DatabaseReference mDatabase;
    private String time;
    private String date;
    private String monoStr = "NA";
    private String friendStr = "NA";
    private String newStr = "NA";
    private String naStr = "NA";
    private Integer counter = -1;
    private Integer nightCount;
    private String drinkCost ="NA";
    private String group;
    private String broadcastInt = "none";
    private String stress_event = "NA";
    private String interpersonal = "NA";
    private String work = "NA";
    private String financial = "NA";
    private String health = "NA";
    private String trauma = "NA";
    private String stress_other = "NA";
    private String stress_value = "NA";
    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //initializes variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.morning_qs);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final TextView oralQS = (TextView) findViewById(R.id.oral_qs);
        final LinearLayout oralQSLayout = (LinearLayout) findViewById(R.id.oral_qs_layout);
        final TextView vaginalQS = (TextView) findViewById(R.id.vaginal_qs);
        final LinearLayout vaginalQSLayout = (LinearLayout) findViewById(R.id.vaginal_layout);
        final TextView analQS = (TextView) findViewById(R.id.anal_qs);
        final LinearLayout analQSLayout = (LinearLayout) findViewById(R.id.anal_layout);
        final TextView numPartQS = (TextView) findViewById(R.id.number_partners_qs);
        final LinearLayout numPartLayout = (LinearLayout) findViewById(R.id.number_partners_layout);
        final TextView typePartQS = (TextView) findViewById(R.id.type_partners_qs);
        final TextView typePartSelect = (TextView) findViewById(R.id.select);
        final LinearLayout typePartLayout = (LinearLayout) findViewById(R.id.partners_layout);



        Button submit = (Button) findViewById(R.id.submit);
        final ImageButton oralYes = (ImageButton) findViewById(R.id.oral_yes_button);
        final ImageButton oralNo = (ImageButton) findViewById(R.id.oral_no_button);
        final ImageButton vaginalYes = (ImageButton) findViewById(R.id.vaginal_yes_button);
        final ImageButton vaginalNo= (ImageButton) findViewById(R.id.vaginal_no_button);
        final ImageButton analYes = (ImageButton) findViewById(R.id.anal_yes_button);
        final ImageButton analNo = (ImageButton) findViewById(R.id.anal_no_button);
        final ImageButton vaginalCondomYes = (ImageButton) findViewById(R.id.vaginal_condom_yes_button);
        final ImageButton vaginalCondomNo = (ImageButton) findViewById(R.id.vaginal_condom_no_button);
        final ImageButton analCondomYes = (ImageButton) findViewById(R.id.anal_condom_yes_button);
        final ImageButton analCondomNo = (ImageButton) findViewById(R.id.anal_condom_no_button);
        //final ImageButton oralConsentYes = (ImageButton) findViewById(R.id.oral_consent_yes_button);
        //final ImageButton oralConsentNo = (ImageButton) findViewById(R.id.oral_consent_no_button);
        //final ImageButton vaginalConsentYes = (ImageButton) findViewById(R.id.vaginal_consent_yes_button);
        //final ImageButton vaginalConsentNo = (ImageButton) findViewById(R.id.vaginal_consent_no_button);
        //final ImageButton analConsentYes = (ImageButton) findViewById(R.id.anal_consent_yes_button);
        //final ImageButton analConsentNo = (ImageButton) findViewById(R.id.anal_consent_no_button);
        final TextView vaginalCondom = (TextView) findViewById(R.id.vaginal_condom_qs);
        //final TextView vaginalConsent = (TextView) findViewById(R.id.vaginal_consent_qs);
        final TextView analCondom = (TextView) findViewById(R.id.anal_condom_qs);
        //final TextView analConsent = (TextView) findViewById(R.id.anal_consent_qs);
        //final TextView oralConsent = (TextView) findViewById(R.id.oral_consent_qs);
        final LinearLayout vaginalCondomLayout = (LinearLayout) findViewById(R.id.vaginal_condom);
        //final LinearLayout vaginalConsentLayout = (LinearLayout) findViewById(R.id.vaginal_consent);
        //final LinearLayout analConsentLayout = (LinearLayout) findViewById(R.id.anal_consent) ;
        final LinearLayout analCondomLayout = (LinearLayout) findViewById(R.id.anal_condom);
        //final LinearLayout oralConsentLayout = (LinearLayout) findViewById(R.id.oral_consent);

        final ImageButton stress_yes = (ImageButton) findViewById(R.id.stress_q1_yes_button);
        final ImageButton stress_no = (ImageButton) findViewById(R.id.stress_q1_no_button);

        final TextView stress_type = (TextView) findViewById(R.id.stress_type_qs);
        final TextView stress_select = (TextView) findViewById(R.id.select_stress);
        final LinearLayout stressTypeLayout = (LinearLayout) findViewById(R.id.type_layout);
        final TextView value_stress_qs = (TextView) findViewById(R.id.value_stress_qs);
        final LinearLayout ratingLayout = (LinearLayout) findViewById(R.id.rating_layout);


        final CheckBox interpersonal_check = (CheckBox) findViewById(R.id.interpersonal);
        final CheckBox work_check = (CheckBox) findViewById(R.id.work);
        final CheckBox financial_check = (CheckBox) findViewById(R.id.financial);
        final CheckBox health_check = (CheckBox) findViewById(R.id.health);
        final CheckBox trauma_check = (CheckBox) findViewById(R.id.trauma);
        final CheckBox other_check = (CheckBox) findViewById(R.id.other_stress);

        final ImageButton stress_1 = (ImageButton) findViewById(R.id.stress_1);
        final ImageButton stress_2 = (ImageButton) findViewById(R.id.stress_2);
        final ImageButton stress_3 = (ImageButton) findViewById(R.id.stress_3);
        final ImageButton stress_4 = (ImageButton) findViewById(R.id.stress_4);
        final ImageButton stress_5 = (ImageButton) findViewById(R.id.stress_5);
        final ImageButton stress_6 = (ImageButton) findViewById(R.id.stress_6);
        final ImageButton stress_7 = (ImageButton) findViewById(R.id.stress_7);
        final ImageButton stress_8 = (ImageButton) findViewById(R.id.stress_8);
        final ImageButton stress_9 = (ImageButton) findViewById(R.id.stress_9);
        final ImageButton stress_10 = (ImageButton) findViewById(R.id.stress_10);




        //new stuff
        final Button add = (Button) findViewById(R.id.add_button);
        final Button subtract = (Button) findViewById(R.id.subtract_button);
        final TextView numPartner = (TextView) findViewById(R.id.partners_counter);
        final CheckBox monoPartner = (CheckBox) findViewById(R.id.monogamous_partner);
        final CheckBox friendPartner = (CheckBox) findViewById(R.id.friend_partner);
        final CheckBox newPartner = (CheckBox) findViewById(R.id.new_partner);
        final CheckBox naPartner = (CheckBox) findViewById(R.id.na_partner);

        final RadioGroup costGroup = (RadioGroup) findViewById(R.id.radioTotalCost);


        //gets shared preferences variable
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = mSharedPreferences.getString("user ID", "none");

        SharedPreferences mSharedPreferences2 = getSharedPreferences("Group", MODE_PRIVATE);
        group = mSharedPreferences2.getString("Group", "none");

        nightCount = getNightCount();

        broadcastInt = getIntent().getStringExtra("broadcast Int");
        if(broadcastInt != null){
            int notificationId = Integer.parseInt(broadcastInt);
            NotificationManager manager = (NotificationManager) MorningQS.this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                manager.deleteNotificationChannel(CHANNEL_ID);
            }

        }else{
            NotificationManager manager = (NotificationManager) MorningQS.this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(5);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                manager.deleteNotificationChannel(CHANNEL_ID);
            }
        }


        //creates all of the onClick listeners for all of the buttons for questions
        View.OnClickListener handler1 = new View.OnClickListener() {
            public void onClick(View view) {
              /*  if(group.equals("experimental")){
                    Log.e("morningQS", "experimental");
                    Intent switchToMorningReport = new Intent(MorningQS.this, MorningReport.class);
                    startActivity(switchToMorningReport);
                }
                else if(group.equals("control")){
                    startActivity1 = "start";
                    storeScreen(startActivity1);
                    storeNumDrinks(0);
                    Intent goToStart = new Intent(MorningQS.this, StartActivity.class);
                    goToStart.putExtra("Start Activity", startActivity);
                    startActivity(goToStart);
                    finish();
                }else if(group.equals("none")){
                    startActivity1 = "start";
                    storeScreen(startActivity1);
                    storeNumDrinks(0);
                    Intent goToStart = new Intent(MorningQS.this, StartActivity.class);
                    goToStart.putExtra("Start Activity", startActivity);
                    startActivity(goToStart);
                    finish();
                }*/

                writeOralToDB(oral);
                //writeOralConsentToDB(oralConsentStr);
                writeVaginalToDB(vaginal);
                writeVaginalCondomToDB(vaginalCondomStr);
                //writeVaginalConsentToDB(vaginalConsentStr);
                writeAnalToDB(anal);
                writeAnalCondomToDB(analCondomStr);
                //writeAnalConsentToDB(analConsentStr);
                writeNumPartners(counter);
                writeMonoPartner(monoStr);
                writeFriendPartner(friendStr);
                writeNewPartner(newStr);
                writeNAPartner(naStr);
                int selectedId = costGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton costButton;
                costButton = (RadioButton) findViewById(selectedId);
                drinkCost = costButton.getText().toString();
                writeCostTotaltoDB(drinkCost);
                writeStressEventToDB(stress_event);
                writeInterpersonal(interpersonal);
                writeWorkToDB(work);
                writeFinancialToDB(financial);
                writeHealthToDB(health);
                writeTraumaToDB(trauma);
                writeStressOtherToDB(stress_other);
                writeStressValueToDB(stress_value);
                finish();

                startActivity1 = "start";
                storeScreen(startActivity1);
                storeNumDrinks(0);
                Intent goToStart = new Intent(MorningQS.this, StartActivity.class);
                goToStart.putExtra("Start Activity", startActivity);
                startActivity(goToStart);
                finish();
            }
        };
        submit.setOnClickListener(handler1);

        stress_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stress_event="Yes";
                stress_no.setImageResource(R.drawable.no_button);
                stress_yes.setImageResource(R.drawable.yes_green);
                stress_type.setVisibility(View.VISIBLE);
                stress_select.setVisibility(View.VISIBLE);
                stressTypeLayout.setVisibility(View.VISIBLE);
                value_stress_qs.setVisibility(View.VISIBLE);
                ratingLayout.setVisibility(View.VISIBLE);
                oralQS.setVisibility(View.VISIBLE);
                oralQSLayout.setVisibility(View.VISIBLE);
                vaginalQS.setVisibility(View.VISIBLE);
                vaginalQSLayout.setVisibility(View.VISIBLE);
                analQS.setVisibility(View.VISIBLE);
                analQSLayout.setVisibility(View.VISIBLE);
                numPartQS.setVisibility(View.VISIBLE);
                numPartLayout.setVisibility(View.VISIBLE);
                typePartQS.setVisibility(View.VISIBLE);
                typePartSelect.setVisibility(View.VISIBLE);
                typePartLayout.setVisibility(View.VISIBLE);

            }
        });
        stress_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stress_event="No";
                stress_no.setImageResource(R.drawable.no_green_button);
                stress_yes.setImageResource(R.drawable.yes_button);
                stress_type.setVisibility(View.GONE);
                stress_select.setVisibility(View.GONE);
                stressTypeLayout.setVisibility(View.GONE);
                value_stress_qs.setVisibility(View.GONE);
                ratingLayout.setVisibility(View.GONE);
                oralQS.setVisibility(View.VISIBLE);
                oralQSLayout.setVisibility(View.VISIBLE);
                vaginalQS.setVisibility(View.VISIBLE);
                vaginalQSLayout.setVisibility(View.VISIBLE);
                analQS.setVisibility(View.VISIBLE);
                analQSLayout.setVisibility(View.VISIBLE);
                numPartQS.setVisibility(View.VISIBLE);
                numPartLayout.setVisibility(View.VISIBLE);
                typePartQS.setVisibility(View.VISIBLE);
                typePartSelect.setVisibility(View.VISIBLE);
                typePartLayout.setVisibility(View.VISIBLE);
            }
        });

        interpersonal_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (interpersonal_check.isChecked() == true) {
                    interpersonal_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    interpersonal = "yes";
                }else{
                    interpersonal_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                    interpersonal = "NA";
                }
            }
        });

        financial_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (financial_check.isChecked() == true) {
                    financial_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    financial = "yes";
                }else{
                    financial_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                    monoStr = "NA";
                }
            }
        });

        work_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (work_check.isChecked() == true) {
                    work_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    work = "yes";
                }else{
                    work_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                    work = "NA";
                }
            }
        });

        health_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (health_check.isChecked() == true) {
                    health_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    health = "yes";
                }else{
                    health_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                    health = "NA";
                }
            }
        });

        trauma_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (trauma_check.isChecked() == true) {
                    trauma_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    trauma = "yes";
                }else{
                    trauma_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                    trauma = "NA";
                }
            }
        });

        other_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (other_check.isChecked() == true) {
                    other_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    stress_other = "yes";
                }else{
                    other_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                    stress_other = "no";
                }
            }
        });

        stress_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stress_value = "1";
                stress_1.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_2.setBackgroundResource(R.drawable.pink_circle_open);
                stress_3.setBackgroundResource(R.drawable.pink_circle_open);
                stress_4.setBackgroundResource(R.drawable.pink_circle_open);
                stress_5.setBackgroundResource(R.drawable.pink_circle_open);
                stress_6.setBackgroundResource(R.drawable.pink_circle_open);
                stress_7.setBackgroundResource(R.drawable.pink_circle_open);
                stress_8.setBackgroundResource(R.drawable.pink_circle_open);
                stress_9.setBackgroundResource(R.drawable.pink_circle_open);
                stress_10.setBackgroundResource(R.drawable.pink_circle_open);


            }
        });
        stress_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stress_value = "2";
                stress_1.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_2.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_3.setBackgroundResource(R.drawable.pink_circle_open);
                stress_4.setBackgroundResource(R.drawable.pink_circle_open);
                stress_5.setBackgroundResource(R.drawable.pink_circle_open);
                stress_6.setBackgroundResource(R.drawable.pink_circle_open);
                stress_7.setBackgroundResource(R.drawable.pink_circle_open);
                stress_8.setBackgroundResource(R.drawable.pink_circle_open);
                stress_9.setBackgroundResource(R.drawable.pink_circle_open);
                stress_10.setBackgroundResource(R.drawable.pink_circle_open);

            }
        });
        stress_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stress_value = "3";
                stress_1.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_2.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_3.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_4.setBackgroundResource(R.drawable.pink_circle_open);
                stress_5.setBackgroundResource(R.drawable.pink_circle_open);
                stress_6.setBackgroundResource(R.drawable.pink_circle_open);
                stress_7.setBackgroundResource(R.drawable.pink_circle_open);
                stress_8.setBackgroundResource(R.drawable.pink_circle_open);
                stress_9.setBackgroundResource(R.drawable.pink_circle_open);
                stress_10.setBackgroundResource(R.drawable.pink_circle_open);

            }
        });
        stress_4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stress_value = "4";
                stress_1.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_2.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_3.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_4.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_5.setBackgroundResource(R.drawable.pink_circle_open);
                stress_6.setBackgroundResource(R.drawable.pink_circle_open);
                stress_7.setBackgroundResource(R.drawable.pink_circle_open);
                stress_8.setBackgroundResource(R.drawable.pink_circle_open);
                stress_9.setBackgroundResource(R.drawable.pink_circle_open);
                stress_10.setBackgroundResource(R.drawable.pink_circle_open);

            }
        });
        stress_5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stress_value = "5";
                stress_1.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_2.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_3.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_4.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_5.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_6.setBackgroundResource(R.drawable.pink_circle_open);
                stress_7.setBackgroundResource(R.drawable.pink_circle_open);
                stress_8.setBackgroundResource(R.drawable.pink_circle_open);
                stress_9.setBackgroundResource(R.drawable.pink_circle_open);
                stress_10.setBackgroundResource(R.drawable.pink_circle_open);

            }
        });
        stress_6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stress_value = "6";
                stress_1.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_2.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_3.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_4.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_5.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_6.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_7.setBackgroundResource(R.drawable.pink_circle_open);
                stress_8.setBackgroundResource(R.drawable.pink_circle_open);
                stress_9.setBackgroundResource(R.drawable.pink_circle_open);
                stress_10.setBackgroundResource(R.drawable.pink_circle_open);

            }
        });
        stress_7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stress_value = "7";
                stress_1.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_2.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_3.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_4.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_5.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_6.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_7.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_8.setBackgroundResource(R.drawable.pink_circle_open);
                stress_9.setBackgroundResource(R.drawable.pink_circle_open);
                stress_10.setBackgroundResource(R.drawable.pink_circle_open);

            }
        });
        stress_8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stress_value = "8";
                stress_1.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_2.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_3.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_4.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_5.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_6.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_7.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_8.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_9.setBackgroundResource(R.drawable.pink_circle_open);
                stress_10.setBackgroundResource(R.drawable.pink_circle_open);
            }
        });
        stress_9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stress_value = "9";
                stress_1.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_2.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_3.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_4.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_5.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_6.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_7.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_8.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_9.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_10.setBackgroundResource(R.drawable.pink_circle_open);
            }
        });
        stress_10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stress_value = "10";
                stress_1.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_2.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_3.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_4.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_5.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_6.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_7.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_8.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_9.setBackgroundResource(R.drawable.pink_circle_closed);
                stress_10.setBackgroundResource(R.drawable.pink_circle_closed);
            }
        });



        oralNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                oral = "no";
                oralNo.setImageResource(R.drawable.no_green_button);
                oralYes.setImageResource(R.drawable.yes_button);
                //oralConsent.setVisibility(View.GONE);
                //oralConsentLayout.setVisibility(View.GONE);
                //oralConsentNo.setVisibility(View.GONE);
                //oralConsentYes.setVisibility(View.GONE);

            }
        });

        oralYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                oral = "yes";
                oralNo.setImageResource(R.drawable.no_button);
                oralYes.setImageResource(R.drawable.yes_green);
                //oralConsent.setVisibility(View.VISIBLE);
                //oralConsentLayout.setVisibility(View.VISIBLE);
                //oralConsentNo.setVisibility(View.VISIBLE);
                //oralConsentYes.setVisibility(View.VISIBLE);

            }
        });

        /*oralConsentYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                oralConsentStr = "yes";
                oralConsentNo.setImageResource(R.drawable.no_button);
                oralConsentYes.setImageResource(R.drawable.yes_green);

            }
        });*/

        /*oralConsentNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                oralConsentStr = "no";
                oralConsentNo.setImageResource(R.drawable.no_green_button);
                oralConsentYes.setImageResource(R.drawable.yes_button);
            }
        });*/

        vaginalNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vaginal = "no";
                vaginalNo.setImageResource(R.drawable.no_green_button);
                vaginalYes.setImageResource(R.drawable.yes_button);
                vaginalCondom.setVisibility(View.GONE);
                vaginalCondomLayout.setVisibility(View.GONE);
                vaginalCondomYes.setVisibility(View.GONE);
                vaginalCondomNo.setVisibility(View.GONE);
                //vaginalConsentLayout.setVisibility(View.GONE);
                //vaginalConsent.setVisibility(View.GONE);
                //vaginalConsentYes.setVisibility(View.GONE);
                //vaginalConsentNo.setVisibility(View.GONE);

            }
        });

        vaginalYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vaginal = "yes";
                vaginalNo.setImageResource(R.drawable.no_button);
                vaginalYes.setImageResource(R.drawable.yes_green);
                vaginalCondom.setVisibility(View.VISIBLE);
                vaginalCondomLayout.setVisibility(View.VISIBLE);
                vaginalCondomYes.setVisibility(View.VISIBLE);
                vaginalCondomNo.setVisibility(View.VISIBLE);
                //vaginalConsentLayout.setVisibility(View.VISIBLE);
                //vaginalConsent.setVisibility(View.VISIBLE);
                //vaginalConsentYes.setVisibility(View.VISIBLE);
                //vaginalConsentNo.setVisibility(View.VISIBLE);

            }
        });

        vaginalCondomYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vaginalCondomStr = "yes";
                vaginalCondomNo.setImageResource(R.drawable.no_button);
                vaginalCondomYes.setImageResource(R.drawable.yes_green);

            }
        });

        vaginalCondomNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vaginalCondomStr = "no";
                vaginalCondomNo.setImageResource(R.drawable.no_green_button);
                vaginalCondomYes.setImageResource(R.drawable.yes_button);

            }
        });

        /*vaginalConsentNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vaginalConsentStr = "no";
                vaginalConsentNo.setImageResource(R.drawable.no_green_button);
                vaginalConsentYes.setImageResource(R.drawable.yes_button);

            }
        });*/

        /*vaginalConsentYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vaginalConsentStr = "yes";
                vaginalConsentNo.setImageResource(R.drawable.no_button);
                vaginalConsentYes.setImageResource(R.drawable.yes_green);

            }
        });*/

        analYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                anal = "yes";
                analNo.setImageResource(R.drawable.no_button);
                analYes.setImageResource(R.drawable.yes_green);
                //analConsent.setVisibility(View.VISIBLE);
                //analConsentLayout.setVisibility(View.VISIBLE);
                //analConsentYes.setVisibility(View.VISIBLE);
                //analConsentNo.setVisibility(View.VISIBLE);
                analCondomLayout.setVisibility(View.VISIBLE);
                analCondom.setVisibility(View.VISIBLE);
                analCondomYes.setVisibility(View.VISIBLE);
                analCondomNo.setVisibility(View.VISIBLE);

            }
        });

        analNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                anal = "no";
                analNo.setImageResource(R.drawable.no_green_button);
                analYes.setImageResource(R.drawable.yes_button);
                //analConsent.setVisibility(View.GONE);
                //analConsentLayout.setVisibility(View.GONE);
                //analConsentYes.setVisibility(View.GONE);
                //analConsentNo.setVisibility(View.GONE);
                analCondomLayout.setVisibility(View.GONE);
                analCondom.setVisibility(View.GONE);
                analCondomYes.setVisibility(View.GONE);
                analCondomNo.setVisibility(View.GONE);

            }
        });

        analCondomYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                analCondomStr = "yes";
                analCondomNo.setImageResource(R.drawable.no_button);
                analCondomYes.setImageResource(R.drawable.yes_green);

            }
        });

        analCondomNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                analCondomStr = "no";
                analCondomNo.setImageResource(R.drawable.no_green_button);
                analCondomYes.setImageResource(R.drawable.yes_button);

            }
        });

        /*analConsentNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                analConsentStr = "no";
                analConsentNo.setImageResource(R.drawable.no_green_button);
                analConsentYes.setImageResource(R.drawable.yes_button);

            }
        });*/

        /*analConsentYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                analConsentStr = "yes";
                analConsentNo.setImageResource(R.drawable.no_button);
                analConsentYes.setImageResource(R.drawable.yes_green);

            }
        });*/

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                counter++;
                if (counter == 0){
                    numPartner.setText("none");
                }else {
                    numPartner.setText(""+counter);
                }
            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (counter == 0){
                    subtract.setEnabled(false);
                    numPartner.setText("none");
                }else {
                    counter--;
                    numPartner.setText(""+counter);
                }
            }
        });


        monoPartner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (monoPartner.isChecked() == true) {
                    monoPartner.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    monoStr = "yes";
                }else{
                    monoPartner.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                    monoStr = "no";
                }
            }
        });

        friendPartner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (friendPartner.isChecked() == true) {
                    friendPartner.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    friendStr = "yes";
                }else{
                    friendPartner.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                    friendStr = "no";
                }
            }
        });

        newPartner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (newPartner.isChecked() == true) {
                    newPartner.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    newStr = "yes";
                }else{
                    newPartner.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                    newStr = "no";
                }
            }
        });

        naPartner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (naPartner.isChecked() == true) {
                    naPartner.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    naStr = "yes";
                }else{
                    naPartner.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                    naStr = "no";
                }
            }
        });

        String broadcastID = getIntent().getStringExtra("broadcast Int");
        if (broadcastID != null) {
            int notificationId = Integer.parseInt(broadcastID);
            NotificationManager manager = (NotificationManager) MorningQS.this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }
    }

    //function to store the screen
    private void storeScreen(String string) {
        SharedPreferences mSharedPreferences = getSharedPreferences("screen", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("currentScreen", string);
        mEditor.apply();
    }
    //function to store the number of drinks
    private void storeNumDrinks (Integer integer) {
        SharedPreferences mSharedPreferences = getSharedPreferences("numDrinks", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("numDrinks", integer);
        mEditor.apply();
    }


    //function to get current time
    public void getTime() {
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = dateFormat.format(currentDate);
    }
    public void writeStressValueToDB(String value){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("StressValue");
        mRef.setValue(value);
    }

    public void writeStressEventToDB(String stress_event){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("StressEvent");
        mRef.setValue(stress_event);
    }

    public void writeInterpersonal(String interpersonal){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("Interpersonal");
        mRef.setValue(interpersonal);
    }
    public void writeWorkToDB(String work){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("Work");
        mRef.setValue(work);
    }
    public void writeFinancialToDB(String financial){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("Financial");
        mRef.setValue(financial);
    }
    public void writeHealthToDB(String health){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("Health");
        mRef.setValue(health);
    }
    public void writeTraumaToDB(String trauma){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("Trauma");
        mRef.setValue(trauma);
    }
    public void writeStressOtherToDB(String stress_other){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("StressOther");
        mRef.setValue(stress_other);
    }

    public void writeCostTotaltoDB(String drink_cost){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("Cost_Total");
        mRef.setValue(drink_cost);

    }

    //functions to write all of the answers to the database
    public void writeNumPartners(int number){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("numPartners");
        mRef.setValue(number);
    }

    public void writeMonoPartner(String monoPartner){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("MonogamousPartner");
        mRef.setValue(monoPartner);
    }

    public void writeFriendPartner(String friendPartner){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("FriendPartner");
        mRef.setValue(friendPartner);
    }

    public void writeNewPartner(String newPartner){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("NewPartner");
        mRef.setValue(newPartner);
    }

    public void writeNAPartner(String naPartner){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("NAPartner");
        mRef.setValue(naPartner);
    }
    public void writeOralToDB (String oral) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("oral");
        mRef.setValue(oral);
    }

    public void writeOralConsentToDB(String oralConsent){
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("oralConsent");
        mRef.setValue(oralConsent);
    }

    public void writeVaginalToDB (String vaginal) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("vaginal");
        mRef.setValue(vaginal);
    }

    public void writeAnalToDB (String anal) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("anal");
        mRef.setValue(anal);
    }

    public void writeVaginalCondomToDB (String vaginalCondom) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("vaginalCondom");
        mRef.setValue(vaginalCondom);
    }

    public void writeVaginalConsentToDB (String vaginalConsent) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("vaginalConsent");
        mRef.setValue(vaginalConsent);
    }

    public void writeAnalCondomToDB (String analCondom) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("analCondom");
        mRef.setValue(analCondom);
    }

    public void writeAnalConsentToDB (String analConsent) {
        getTime();
        DatabaseReference mRef= mDatabase.child("Users").child("UID: "+userIDMA).child("Night Count: "+nightCount).child("MorningAnswers").child(time).child("analConsent");
        mRef.setValue(analConsent);
    }

    //gets nightCount shared preference variable
    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }
}