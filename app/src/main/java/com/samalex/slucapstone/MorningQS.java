package com.samalex.slucapstone;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by samikshasm on 7/24/17.
 */

public class MorningQS extends AppCompatActivity {
    private int cycleWhichThisSurveyIsAbout = 0;
    private String userIDMA;
    private String startActivity1;
    public static final String startActivity = "main";
    private String oral;
    private String vaginal;
    private String anal;
    private String vaginalCondomStr = "NA";
    private String analCondomStr = "NA";
    private String vaginalConsentStr = "NA";
    private String analConsentStr = "NA";
    private String oralConsentStr = "NA";
    private DatabaseReference mDatabase;
    private String time;
    private String date;
    private String monoStr = "NA";
    private String friendStr = "NA";
    private String newStr = "NA";
    private String naStr = "NA";
    private Integer counter = -1;
    private String drinkCost = "NA";
    private String broadcastInt = "none";
    private String stress_event = "NA";
    private String interpersonal = "NA";
    private String work = "NA";
    private String financial = "NA";
    private String health = "NA";
    private String trauma = "NA";
    private String stress_other = "NA";
    private String stress_value = "NA";
    private List<String> typeStress = new ArrayList<>();

    private String whenStressOccurred = "NA";
    private String drinklastNight = "NA";
    private Integer drinksCounter = 0;
    private List<String> typesOfDrinks = new ArrayList<>();
    private String hangover = "NA";
    private String analVaginalSex = "NA";
    private String condom = "NA";
    private String partner = "NA";
    private String drugs = "NA";
    private List<String> typeDrugs = new ArrayList<>();
    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";

    private TextView stressRatingSlideBarLabel;

    private ImageButton lastNightDrinkYes;
    private ImageButton lastNightDrinkNo;
    private TextView lastNightDrinkYesText;
    private TextView lastNightDrinkNoText;
    private TextView number_drinks_qs;
    private LinearLayout number_drinks_qs_layout;
    private Button drinks_add;
    private Button drinks_subtract;
    private TextView drinks_counterText;
    private TextView drink_type;
    private TextView select_drink_type;
    private LinearLayout drink_type_layout;
    private CheckBox liquor;
    private CheckBox wine;
    private CheckBox beer;
    private TextView hangover_qs;
    private LinearLayout hangover_qs_layout;
    private TextView hangoverYesText;
    private ImageButton hangoverYes;
    private TextView hangoverNoText;
    private ImageButton hangoverNo;
    private TextView drugs_qs;
    private LinearLayout drugs_layout;
    private ImageButton drugs_yes;
    private TextView drugs_yesText;
    private ImageButton drugs_no;
    private TextView drugs_noText;
    private TextView typesOfDrugs_qs;
    private TextView select_drugs;
    private LinearLayout typesOfDrugs_layout;
    private CheckBox ritalin;
    private CheckBox adderall;
    private CheckBox oxyContin;
    private CheckBox vicodin;
    private CheckBox percocet;
    private CheckBox otherPrescriptionOpioid;
    private CheckBox xanax;
    private CheckBox valium;
    private CheckBox otherBenzo;
    private CheckBox marijuana;
    private CheckBox heroin;
    private CheckBox mdma;
    private CheckBox meth;
    private CheckBox otherDrug;
    private ImageButton analVaginalSexYes;
    private TextView analVaginalSexYesText;
    private ImageButton analVaginalSexNo;
    private TextView analVaginalSexNoText;
    private TextView condom_qs;
    private LinearLayout condom_layout;
    private ImageButton condom_yes;
    private TextView condom_yesText;
    private ImageButton condom_no;
    private TextView condom_noText;
    private TextView sexPartner_qs;
    private LinearLayout sexPartner_qs_layout;
    private RadioGroup partnerGroup;
    private TextView stressfulEventOccurance;
    private LinearLayout stressfulEventOccurance_layout;
    private RadioGroup stressGroup;
    private SeekBar seekBar;
    Button submit;
    private ImageButton stress_yes;
    private TextView stress_yesText;
    private ImageButton stress_no;
    private TextView stress_noText;
    private TextView stress_type;
    private TextView stress_select;
    private LinearLayout stressTypeLayout;
    private TextView value_stress_qs;
    private LinearLayout ratingLayout;
    private CheckBox interpersonal_check;
    private CheckBox work_check;
    private CheckBox financial_check;
    private CheckBox health_check;
    private CheckBox trauma_check;
    private CheckBox other_check;

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            stressRatingSlideBarLabel.setText("You rate your stress: " + progress + " out of 10");
            stress_value = progress + "";
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //initializes variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.morning_qs);
        storeScreen("morningQS");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        lastNightDrinkYes = (ImageButton) findViewById(R.id.drinkAlcoholQS_yes);
        lastNightDrinkNo = (ImageButton) findViewById(R.id.drinkAlcoholQS_no);
        lastNightDrinkYesText = (TextView) findViewById(R.id.drinkAlcoholQS_yesText);
        lastNightDrinkNoText = (TextView) findViewById(R.id.drinkAlcoholQS_noText);
        number_drinks_qs = (TextView) findViewById(R.id.number_drinks_qs);
        number_drinks_qs_layout = (LinearLayout) findViewById(R.id.number_drinks_qs_layout);
        drinks_add = (Button) findViewById(R.id.drinks_add_button);
        drinks_subtract = (Button) findViewById(R.id.drinks_subtract_button);
        drinks_counterText = (TextView) findViewById(R.id.drinks_counter);
        drink_type = (TextView) findViewById(R.id.drink_type);
        select_drink_type = (TextView) findViewById(R.id.select_drink_type);
        drink_type_layout = (LinearLayout) findViewById(R.id.drink_type_layout);
        liquor = (CheckBox) findViewById(R.id.liquor);
        wine = (CheckBox) findViewById(R.id.wine);
        beer = (CheckBox) findViewById(R.id.beer);
        hangover_qs = (TextView) findViewById(R.id.hangover_qs);
        hangover_qs_layout = (LinearLayout) findViewById(R.id.hangover_qs_layout);
        hangoverYesText = (TextView) findViewById(R.id.hangover_yesText);
        hangoverYes = (ImageButton) findViewById(R.id.hangover_yes);
        hangoverNoText = (TextView) findViewById(R.id.hangover_noText);
        hangoverNo = (ImageButton) findViewById(R.id.hangover_no);
        drugs_qs = (TextView) findViewById(R.id.drugs);
        drugs_layout = (LinearLayout) findViewById(R.id.drugs_layout);
        drugs_yes = (ImageButton) findViewById(R.id.drugs_yes);
        drugs_yesText = (TextView) findViewById(R.id.drugs_yesText);
        drugs_no = (ImageButton) findViewById(R.id.drugs_no);
        drugs_noText = (TextView) findViewById(R.id.drugs_noText);
        typesOfDrugs_qs = (TextView) findViewById(R.id.typesOfDrugs);
        select_drugs = (TextView) findViewById(R.id.select_drugs);
        typesOfDrugs_layout = (LinearLayout) findViewById(R.id.typesOfDrugs_layout);
        ritalin = (CheckBox) findViewById(R.id.ritalin);
        adderall = (CheckBox) findViewById(R.id.adderall);
        oxyContin = (CheckBox) findViewById(R.id.oxyContin);
        vicodin = (CheckBox) findViewById(R.id.vicodin);
        percocet = (CheckBox) findViewById(R.id.percocet);
        otherPrescriptionOpioid = (CheckBox) findViewById(R.id.otherPrescriptionOpioid);
        xanax = (CheckBox) findViewById(R.id.xanax);
        valium = (CheckBox) findViewById(R.id.valium);
        otherBenzo = (CheckBox) findViewById(R.id.otherBenzo);
        marijuana = (CheckBox) findViewById(R.id.marijuana);
        heroin = (CheckBox) findViewById(R.id.heroin);
        mdma = (CheckBox) findViewById(R.id.mdma);
        meth = (CheckBox) findViewById(R.id.meth);
        otherDrug = (CheckBox) findViewById(R.id.otherDrug);
        analVaginalSexYes = (ImageButton) findViewById(R.id.analVaginalSex_yes);
        analVaginalSexYesText = (TextView) findViewById(R.id.analVaginalSex_yesText);
        analVaginalSexNo = (ImageButton) findViewById(R.id.analVaginalSex_no);
        analVaginalSexNoText = (TextView) findViewById(R.id.analVaginalSex_noText);
        condom_qs = (TextView) findViewById(R.id.condom);
        condom_layout = (LinearLayout) findViewById(R.id.condom_layout);
        condom_yes = (ImageButton) findViewById(R.id.condom_yes);
        condom_yesText = (TextView) findViewById(R.id.condom_yesText);
        condom_no = (ImageButton) findViewById(R.id.condom_no);
        condom_noText = (TextView) findViewById(R.id.condom_noText);
        sexPartner_qs = (TextView) findViewById(R.id.sexPartner_qs);
        sexPartner_qs_layout = (LinearLayout) findViewById(R.id.sexPartner_qs_layout);
        partnerGroup = (RadioGroup) findViewById(R.id.radioGroupPartner);
        stressfulEventOccurance = (TextView) findViewById(R.id.stressfulEventOccurance);
        stressfulEventOccurance_layout = (LinearLayout) findViewById(R.id.stressfulEventOccurance_layout);
        stressGroup = (RadioGroup) findViewById(R.id.radioGroupStress);
        seekBar = (SeekBar) findViewById(R.id.stress_rating_slidebar);
        submit = (Button) findViewById(R.id.submit);
        stress_yes = (ImageButton) findViewById(R.id.stress_q1_yes_button);
        stress_yesText = (TextView) findViewById(R.id.stress_q1_yes_text);
        stress_no = (ImageButton) findViewById(R.id.stress_q1_no_button);
        stress_noText = (TextView) findViewById(R.id.stress_q1_no_text);
        stress_type = (TextView) findViewById(R.id.stress_type_qs);
        stress_select = (TextView) findViewById(R.id.select_stress);
        stressTypeLayout = (LinearLayout) findViewById(R.id.type_layout);
        value_stress_qs = (TextView) findViewById(R.id.value_stress_qs);
        ratingLayout = (LinearLayout) findViewById(R.id.rating_layout);
        interpersonal_check = (CheckBox) findViewById(R.id.interpersonal);
        work_check = (CheckBox) findViewById(R.id.work);
        financial_check = (CheckBox) findViewById(R.id.financial);
        health_check = (CheckBox) findViewById(R.id.health);
        trauma_check = (CheckBox) findViewById(R.id.trauma);
        other_check = (CheckBox) findViewById(R.id.other_stress);

        //gets shared preferences variable
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = mSharedPreferences.getString("user ID", "none");

        dismissMorningQuestionnaireNotification();

        ImageView appHeaderBar = findViewById(R.id.morningqs_app_header_bar);
        appHeaderBar.setOnClickListener(new View.OnClickListener() {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long userRawStartTime = getUserRawStartTime();
            String userRawStartDateStr = dateFormat.format(new Date(userRawStartTime));
            long userStartTime = getUserStartTime();
            String userStartDateStr = dateFormat.format(new Date(userStartTime));

            int currentCycle = CalculationUtil.updateAndGetCurrentCycle(getApplicationContext());
            long morningSurveyTimeInMillis = BoozymeterApplication.getNextMorningSurveyTimeInMillis(getUserStartTime(), cycleWhichThisSurveyIsAbout);
            String moringSurveyTime = dateFormat.format(new Date(morningSurveyTimeInMillis));
            long eveningSurveyTimeInMillis = BoozymeterApplication.getNextEveningRemonderTimeInMillis(getUserStartTime(), currentCycle);
            String eveningReminderTime = dateFormat.format(new Date(eveningSurveyTimeInMillis));

            @Override
            public void onClick(View view) {
                int currentCycle = CalculationUtil.updateAndGetCurrentCycle(getApplicationContext());
                InterventionDisplayData ui = CalculationUtil.getInterventionMap(getApplicationContext()).get(cycleWhichThisSurveyIsAbout);
                String liveReportFlag;
                String morningReportFlag;

                // TODO: investigate more which flow cause this null
                if (ui == null) {
                    liveReportFlag = "precomputed map is null";
                    morningReportFlag = "precomputed map is null";
                } else {
                    liveReportFlag = ui.isShowLiveReport() ? "Yes" : "No";
                    morningReportFlag = ui.isShowMorningReport() ? "Yes" : "No";
                }

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MorningQS.this, R.style.MyDialogTheme);
                builder.setTitle("Hidden Logs")
                        .setMessage("Username: " + userIDMA
                                        + "\nUser group: " + getGroup()
                                        + "\n"
                                        + "\nRaw start time: " + userRawStartDateStr
                                        + "\nCanonical start time (incl. cycle offset): " + userStartDateStr
                                        + "\n"
                                        + "\nThis is a morning survey for cycle " + (cycleWhichThisSurveyIsAbout + 1) + " **" // +1 to display as a one-based number
                                        + "\nCurrent Cycle: " + (currentCycle + 1) // +1 to display as a one-based number
                                        + "\n"
                                        + "\nLive report: " + liveReportFlag
                                        + "\nMorning report: " + morningReportFlag
//                                        + "\n"
//                                        + "\nNumber of Episodes: " + getNightCount()
//                                        + "\n"
//                                        + "\nNext morning Survey alarm will go off: " + moringSurveyTime
//                                        + "\nNext evening reminder will go off: " + eveningReminderTime
                                        + "\n\n--------- Settings ---------"
                                        + "\nNumber of cycles: " + BoozymeterApplication.NUM_CYCLES
                                        + "\nCycle length: " + (BoozymeterApplication.CYCLE_LENGTH / 1000 / 60 / 60) + " hr." + " ("
                                        + BoozymeterApplication.CYCLE_LENGTH / 1000 / 60 + " min)"
                                        + "\nCycle offset: " + (BoozymeterApplication.CYCLE_OFFSET / 1000 / 60 / 60) + " hr." + " ("
                                        + BoozymeterApplication.CYCLE_OFFSET / 1000 / 60 + " min)"
                                        + "\nEvening reminder offset: " + (BoozymeterApplication.EVENING_REMINDER_OFFSET / 1000 / 60 / 60) + " hr." + " ("
                                        + BoozymeterApplication.EVENING_REMINDER_OFFSET / 1000 / 60 + " min)"
                                        + "\nMorning survey offset: " + ((float) BoozymeterApplication.SURVEY_OFFSET / 1000 / 60 / 60) + " hr." + " ("
                                        + BoozymeterApplication.SURVEY_OFFSET / 1000 / 60 + " min)"
                                        + "\nIn-episode reminder offset: " + (BoozymeterApplication.IN_EPISODE_REMINDER_INTERVAL / 1000 / 60 / 60) + " hr." + " ("
                                        + BoozymeterApplication.IN_EPISODE_REMINDER_INTERVAL / 1000 / 60 + " min)"
                                        + "\n"
                        )
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(R.drawable.white_small_icon)
                        .show();
            }
        });


        //creates all of the onClick listeners for all of the buttons for questions
        View.OnClickListener handler1 = new View.OnClickListener() {
            public void onClick(View view) {
                if (!validateAnswers()) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(MorningQS.this, R.style.MyDialogTheme);
                    } else {
                        builder = new AlertDialog.Builder(MorningQS.this);
                    }
                    builder
//                            .setTitle("Try Again")
                            .setMessage("Please answer every question.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(R.drawable.white_small_icon)
                            .show();
                } else {

                    int nightCount = getNightCount();
                    cycleWhichThisSurveyIsAbout = CalculationUtil.updateAndGetCurrentCycle(getApplicationContext()) - 1; // we are reporting the previous cycle's drinks

                    writeOralToDB(oral, nightCount, cycleWhichThisSurveyIsAbout);
                    writeLastNightDrink(drinklastNight, nightCount, cycleWhichThisSurveyIsAbout);
                    writeNumDrinksToDB(drinksCounter, nightCount, cycleWhichThisSurveyIsAbout);
                    writeTypesDrinksToDB(TextUtils.join(",", typesOfDrinks), nightCount, cycleWhichThisSurveyIsAbout);
                    writeHangoverToDB(hangover, nightCount, cycleWhichThisSurveyIsAbout);
                    writeDrugsToDB(drugs, nightCount, cycleWhichThisSurveyIsAbout);
                    writeTypeDrugsToDB(TextUtils.join(",", typeDrugs), nightCount, cycleWhichThisSurveyIsAbout);
                    writeAnalVaginalToDB(analVaginalSex, nightCount, cycleWhichThisSurveyIsAbout);
                    writeCondomToDB(condom, nightCount, cycleWhichThisSurveyIsAbout);

                    if (analVaginalSex.equals("Yes")) {
                        int partnerSelectedId = partnerGroup.getCheckedRadioButtonId();
                        RadioButton partnerButton;
                        partnerButton = (RadioButton) findViewById(partnerSelectedId);
                        partner = partnerButton.getText().toString();
                        writePartnerToDB(partner, nightCount, cycleWhichThisSurveyIsAbout);

                    }
                    if (stress_event.equals("Yes")) {
                        int stressSelectedID = stressGroup.getCheckedRadioButtonId();
                        RadioButton stressButton;
                        stressButton = (RadioButton) findViewById(stressSelectedID);
                        whenStressOccurred = stressButton.getText().toString();
                        writeStressOccurranceToDB(whenStressOccurred, nightCount, cycleWhichThisSurveyIsAbout);
                    }

                    writeStressEventToDB(stress_event, nightCount, cycleWhichThisSurveyIsAbout);
                    writeTypeStressToDB(TextUtils.join(",", typeStress), nightCount, cycleWhichThisSurveyIsAbout);
                    writeStressValueToDB(stress_value, nightCount, cycleWhichThisSurveyIsAbout);
                    finish();


                    startNextActivity();
                }
            }
        };
        submit.setOnClickListener(handler1);

        View.OnClickListener onClickDrinkNo = new View.OnClickListener() {
            public void onClick(View view) {
                drinklastNight = "No";
                drinksCounter = 0;
                lastNightDrinkNo.setImageResource(R.drawable.no_green_button);
                lastNightDrinkYes.setImageResource(R.drawable.yes_button);
                lastNightDrinkNoText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
                lastNightDrinkYesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                number_drinks_qs.setVisibility(View.GONE);
                number_drinks_qs_layout.setVisibility(View.GONE);
                drink_type.setVisibility(View.GONE);
                select_drink_type.setVisibility(View.GONE);
                drink_type_layout.setVisibility(View.GONE);
                hangover_qs.setVisibility(View.GONE);
                hangover_qs_layout.setVisibility(View.GONE);
                drugs_qs.setVisibility(View.GONE);
                drugs_layout.setVisibility(View.GONE);
                if (drugs.equals("yes")) {
                    typesOfDrugs_layout.setVisibility(View.GONE);
                }
            }
        };
        lastNightDrinkNo.setOnClickListener(onClickDrinkNo);
        lastNightDrinkNoText.setOnClickListener(onClickDrinkNo);

        View.OnClickListener onClickDrinkYes = new View.OnClickListener() {
            public void onClick(View view) {
                drinklastNight = "Yes";
                lastNightDrinkNo.setImageResource(R.drawable.no_button);
                lastNightDrinkYes.setImageResource(R.drawable.yes_green);
                lastNightDrinkYesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
                lastNightDrinkNoText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                number_drinks_qs.setVisibility(View.VISIBLE);
                number_drinks_qs_layout.setVisibility(View.VISIBLE);
                drink_type.setVisibility(View.VISIBLE);
                select_drink_type.setVisibility(View.VISIBLE);
                drink_type_layout.setVisibility(View.VISIBLE);
                hangover_qs.setVisibility(View.VISIBLE);
                hangover_qs_layout.setVisibility(View.VISIBLE);
                drugs_qs.setVisibility(View.VISIBLE);
                drugs_layout.setVisibility(View.VISIBLE);
                if (drugs.equals("Yes")) {
                    typesOfDrugs_layout.setVisibility(View.VISIBLE);
                }
            }
        };
        lastNightDrinkYes.setOnClickListener(onClickDrinkYes);
        lastNightDrinkYesText.setOnClickListener(onClickDrinkYes);

        drinks_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                drinks_subtract.setEnabled(true);
                drinksCounter++;
                drinks_counterText.setText("" + drinksCounter);
            }
        });

        drinks_subtract.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                drinksCounter--;
                drinks_counterText.setText("" + drinksCounter);
                if (drinksCounter == 0) {
                    drinks_subtract.setEnabled(false);
                }
            }
        });

        liquor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (liquor.isChecked() == true) {
                    liquor.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typesOfDrinks.add("Liquor");
                } else {
                    liquor.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        wine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (wine.isChecked() == true) {
                    wine.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typesOfDrinks.add("Wine");
                } else {
                    wine.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        beer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (beer.isChecked() == true) {
                    beer.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typesOfDrinks.add("Beer");
                } else {
                    beer.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        View.OnClickListener onClickHangoverNo = new View.OnClickListener() {
            public void onClick(View view) {
                hangover = "No";
                hangoverNo.setImageResource(R.drawable.no_green_button);
                hangoverYes.setImageResource(R.drawable.yes_button);

                hangoverNoText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
                hangoverYesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
            }
        };
        hangoverNo.setOnClickListener(onClickHangoverNo);
        hangoverNoText.setOnClickListener(onClickHangoverNo);

        View.OnClickListener onClickHangoverYes = new View.OnClickListener() {
            public void onClick(View view) {
                hangover = "Yes";
                hangoverNo.setImageResource(R.drawable.no_button);
                hangoverYes.setImageResource(R.drawable.yes_green);

                hangoverNoText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                hangoverYesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
            }
        };
        hangoverYes.setOnClickListener(onClickHangoverYes);
        hangoverYesText.setOnClickListener(onClickHangoverYes);

        View.OnClickListener onClickDrugNo = new View.OnClickListener() {
            public void onClick(View view) {
                drugs = "No";
                drugs_no.setImageResource(R.drawable.no_green_button);
                drugs_yes.setImageResource(R.drawable.yes_button);
                drugs_noText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
                drugs_yesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                typesOfDrugs_qs.setVisibility(View.GONE);
                select_drugs.setVisibility(View.GONE);
                typesOfDrugs_layout.setVisibility(View.GONE);

            }
        };
        drugs_no.setOnClickListener(onClickDrugNo);
        drugs_noText.setOnClickListener(onClickDrugNo);

        View.OnClickListener onClickDrugYes = new View.OnClickListener() {
            public void onClick(View view) {
                drugs = "Yes";
                drugs_no.setImageResource(R.drawable.no_button);
                drugs_yes.setImageResource(R.drawable.yes_green);
                drugs_noText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                drugs_yesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
                typesOfDrugs_qs.setVisibility(View.VISIBLE);
                select_drugs.setVisibility(View.VISIBLE);
                typesOfDrugs_layout.setVisibility(View.VISIBLE);
            }
        };
        drugs_yes.setOnClickListener(onClickDrugYes);
        drugs_yesText.setOnClickListener(onClickDrugYes);

        ritalin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ritalin.isChecked() == true) {
                    ritalin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("Ritalin");
                } else {
                    ritalin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        adderall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (adderall.isChecked() == true) {
                    adderall.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("Adderall");
                } else {
                    adderall.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        oxyContin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (oxyContin.isChecked() == true) {
                    oxyContin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("OxyContin");
                } else {
                    oxyContin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        vicodin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (vicodin.isChecked() == true) {
                    vicodin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("Vicodin");
                } else {
                    vicodin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        percocet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (percocet.isChecked() == true) {
                    percocet.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("Percocet");
                } else {
                    percocet.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        otherPrescriptionOpioid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (otherPrescriptionOpioid.isChecked() == true) {
                    otherPrescriptionOpioid.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("OtherPrescripOpioid");
                } else {
                    otherPrescriptionOpioid.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        xanax.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (xanax.isChecked() == true) {
                    xanax.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("Xanax");
                } else {
                    xanax.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        valium.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (valium.isChecked() == true) {
                    valium.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("Valium");
                } else {
                    valium.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        otherBenzo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (otherBenzo.isChecked() == true) {
                    otherBenzo.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("OtherBenzo");
                } else {
                    otherBenzo.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        marijuana.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (marijuana.isChecked() == true) {
                    marijuana.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("Marijuana");
                } else {
                    marijuana.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        heroin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (heroin.isChecked() == true) {
                    heroin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("Heroin");
                } else {
                    heroin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        mdma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mdma.isChecked() == true) {
                    mdma.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("MDMA/Ecstasy");
                } else {
                    mdma.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        meth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (meth.isChecked() == true) {
                    meth.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("Methamphetamine");
                } else {
                    meth.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        otherDrug.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (otherDrug.isChecked() == true) {
                    otherDrug.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs.add("OtherDrug");
                } else {
                    otherDrug.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });


        View.OnClickListener onClickAnalVaginalSexNo = new View.OnClickListener() {
            public void onClick(View view) {
                analVaginalSex = "No";
                analVaginalSexNo.setImageResource(R.drawable.no_green_button);
                analVaginalSexYes.setImageResource(R.drawable.yes_button);
                analVaginalSexNoText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
                analVaginalSexYesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                condom_qs.setVisibility(View.GONE);
                condom_layout.setVisibility(View.GONE);
                sexPartner_qs.setVisibility(View.GONE);
                sexPartner_qs_layout.setVisibility(View.GONE);
            }
        };
        analVaginalSexNo.setOnClickListener(onClickAnalVaginalSexNo);
        analVaginalSexNoText.setOnClickListener(onClickAnalVaginalSexNo);

        View.OnClickListener onClickAnalVaginalSexYes = new View.OnClickListener() {
            public void onClick(View view) {
                analVaginalSex = "Yes";
                analVaginalSexNo.setImageResource(R.drawable.no_button);
                analVaginalSexYes.setImageResource(R.drawable.yes_green);
                analVaginalSexNoText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                analVaginalSexYesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
                condom_qs.setVisibility(View.VISIBLE);
                condom_layout.setVisibility(View.VISIBLE);
                sexPartner_qs.setVisibility(View.VISIBLE);
                sexPartner_qs_layout.setVisibility(View.VISIBLE);
            }
        };
        analVaginalSexYes.setOnClickListener(onClickAnalVaginalSexYes);
        analVaginalSexYesText.setOnClickListener(onClickAnalVaginalSexYes);

        View.OnClickListener onClickCondomNo = new View.OnClickListener() {
            public void onClick(View view) {
                condom = "No";
                condom_no.setImageResource(R.drawable.no_green_button);
                condom_yes.setImageResource(R.drawable.yes_button);

                condom_noText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
                condom_yesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
            }
        };
        condom_no.setOnClickListener(onClickCondomNo);
        condom_noText.setOnClickListener(onClickCondomNo);

        View.OnClickListener onClickCondomYes = new View.OnClickListener() {
            public void onClick(View view) {
                condom = "Yes";
                condom_no.setImageResource(R.drawable.no_button);
                condom_yes.setImageResource(R.drawable.yes_green);

                condom_noText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                condom_yesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
            }
        };
        condom_yes.setOnClickListener(onClickCondomYes);
        condom_yesText.setOnClickListener(onClickCondomYes);

        View.OnClickListener onClickStressYes = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stress_event = "Yes";
                stress_no.setImageResource(R.drawable.no_button);
                stress_yes.setImageResource(R.drawable.yes_green);
                stress_noText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                stress_yesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
                stress_type.setVisibility(View.VISIBLE);
                stress_select.setVisibility(View.VISIBLE);
                stressTypeLayout.setVisibility(View.VISIBLE);
                value_stress_qs.setVisibility(View.VISIBLE);
                stressfulEventOccurance.setVisibility(View.VISIBLE);
                stressfulEventOccurance_layout.setVisibility(View.VISIBLE);
                stressGroup.setVisibility(View.VISIBLE);
                ratingLayout.setVisibility(View.VISIBLE);

            }
        };
        stress_yes.setOnClickListener(onClickStressYes);
        stress_yesText.setOnClickListener(onClickStressYes);

        View.OnClickListener onClickStressNo = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stress_event = "No";
                stress_no.setImageResource(R.drawable.no_green_button);
                stress_yes.setImageResource(R.drawable.yes_button);
                stress_noText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.green));
                stress_yesText.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                stress_type.setVisibility(View.GONE);
                stress_select.setVisibility(View.GONE);
                stressTypeLayout.setVisibility(View.GONE);
                value_stress_qs.setVisibility(View.GONE);
                stressfulEventOccurance.setVisibility(View.GONE);
                stressfulEventOccurance_layout.setVisibility(View.GONE);
                stressGroup.setVisibility(View.GONE);
                ratingLayout.setVisibility(View.GONE);
            }
        };
        stress_no.setOnClickListener(onClickStressNo);
        stress_noText.setOnClickListener(onClickStressNo);


        interpersonal_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (interpersonal_check.isChecked() == true) {
                    interpersonal_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress.add("Interpersonal");
                } else {
                    interpersonal_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        financial_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (financial_check.isChecked() == true) {
                    financial_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress.add("Financial");
                } else {
                    financial_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        work_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (work_check.isChecked() == true) {
                    work_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress.add("Work");
                } else {
                    work_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        health_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (health_check.isChecked() == true) {
                    health_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress.add("Health");
                } else {
                    health_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        trauma_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (trauma_check.isChecked() == true) {
                    trauma_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress.add("Trauma");
                } else {
                    trauma_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        other_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (other_check.isChecked() == true) {
                    other_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress.add("Other");
                } else {
                    other_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        String broadcastID = getIntent().getStringExtra("broadcast Int");
        if (broadcastID != null) {
            int notificationId = Integer.parseInt(broadcastID);
            NotificationService.dismissNotification(this, notificationId);
            cycleWhichThisSurveyIsAbout = CalculationUtil.updateAndGetCurrentCycle(getApplicationContext()) - 1; // we are reporting the previous cycle's drinks
        }

        // set a change listener on the SeekBar
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();
        stressRatingSlideBarLabel = findViewById(R.id.stress_rating_text);
        stressRatingSlideBarLabel.setText("You rate your stress: " + progress + " out of 10");
        stress_value = progress + "";
    }

    private boolean validateAnswers() {
        if (drinklastNight.equals("NA")
                || analVaginalSex.equals("NA")
                || stress_event.equals("NA")) {
            return false;
        }

        if (drinklastNight.equals("Yes")) {
            if (hangover.equals("NA") || drugs.equals("NA")) {
                return false;
            }
        }
        if (analVaginalSex.equals("Yes")) {
            int partnerSelectedId = partnerGroup.getCheckedRadioButtonId();
            if (condom.equals("NA") || partnerSelectedId == -1) {
                return false;
            }
        }
        if (stress_event.equals("Yes")) {
            int stressSelectedID = stressGroup.getCheckedRadioButtonId();
            if (stressSelectedID == -1) {
                return false;
            }
        }
        return true;
    }

    private void startNextActivity() {
        InterventionMap interventionMap = CalculationUtil.getInterventionMap(getApplicationContext());
        InterventionDisplayData display = interventionMap.get(cycleWhichThisSurveyIsAbout);

        if (display.isShowMorningReport()) {
            goToMorningReportScreen();
        } else {
            goToStartScreen();
        }
    }

    private void goToStartScreen() {
        startActivity1 = "start";
        storeScreen(startActivity1);
        storeNumDrinks(0);
        Intent goToStart = new Intent(MorningQS.this, StartActivity.class);
        goToStart.putExtra("Start Activity", startActivity);
        startActivity(goToStart);
        finish();
    }

    private void goToMorningReportScreen() {
        startActivity1 = "morningReport";
        storeScreen(startActivity1);
        storeNumDrinks(0);
        Intent goToMorningReport = new Intent(MorningQS.this, MorningReport.class);
        goToMorningReport.putExtra("Start Activity", startActivity);
        startActivity(goToMorningReport);
        finish();
    }

    private void dismissMorningQuestionnaireNotification() {
        NotificationManager manager = (NotificationManager) MorningQS.this.getSystemService(Context.NOTIFICATION_SERVICE);
        broadcastInt = getIntent().getStringExtra("broadcast Int");
        int notificationId;
        if (broadcastInt == null) {
            notificationId = 5;
        } else {
            notificationId = Integer.parseInt(broadcastInt);
        }

        manager.cancel(notificationId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.deleteNotificationChannel(CHANNEL_ID);
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
    private void storeNumDrinks(Integer integer) {
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

    public void writeStressValueToDB(String value, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("12StressValue");
        mRef.setValue(value);
    }

    public void writeStressEventToDB(String stress_event, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("10StressEvent");
        mRef.setValue(stress_event);
    }

    public void writeStressOccurranceToDB(String whenStressOccurred, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("13StressTime");
        mRef.setValue(whenStressOccurred);
    }

    public void writeTypeStressToDB(String typeStress, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("11TypeStress");
        mRef.setValue(typeStress);
    }

    public void writeOralToDB(String oral, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("oral");
        mRef.setValue(oral);
    }

    public void writeLastNightDrink(String drinklastNight, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("1EpisodeLastNight");
        mRef.setValue(drinklastNight);
    }

    public void writeNumDrinksToDB(int drinksCounter, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("2NumberOfDrinks");
        mRef.setValue(drinksCounter);
    }

    public void writeTypesDrinksToDB(String typesOfDrinks, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("3TypesOfDrinks");
        mRef.setValue(typesOfDrinks);
    }


    public void writeHangoverToDB(String hangover, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("4Hangover");
        mRef.setValue(hangover);
    }

    public void writeDrugsToDB(String drugs, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("5Drugs");
        mRef.setValue(drugs);
    }

    public void writeTypeDrugsToDB(String typeDrugs, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("6TypeDrugs");
        mRef.setValue(typeDrugs);
    }

    public void writeAnalVaginalToDB(String analVaginalSex, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("7AnalVaginalSex");
        mRef.setValue(analVaginalSex);
    }

    public void writeCondomToDB(String condom, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("8UsedCondom");
        mRef.setValue(condom);
    }


    public void writePartnerToDB(String partner, int nightCount, int cycle) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Cycle: " + cycle)
                .child("MorningAnswers").child(time).child("9PartnerType");
        mRef.setValue(partner);
    }

    //gets nightCount shared preference variable
    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }

    private String getGroup() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Group", MODE_PRIVATE);
        String group = mSharedPreferences.getString("Group", "none");
        return group;
    }

    private long calculateEveningReminderTime() {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        long userStartTime = getUserStartTime();


        long reminderTime = userStartTime + BoozymeterApplication.EVENING_REMINDER_OFFSET;
        if (reminderTime < now) {
            reminderTime += BoozymeterApplication.CYCLE_LENGTH;
        }

        return reminderTime;
    }

    private Long getUserStartTime() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        Long time = mSharedPreferences.getLong("userStartTime", 0);
        return time;
    }

    private Long getUserRawStartTime() {
        SharedPreferences mSharedPreferences = getSharedPreferences("boozymeter", MODE_PRIVATE);
        Long time = mSharedPreferences.getLong("userRawStartTime", 0);
        return time;
    }
}