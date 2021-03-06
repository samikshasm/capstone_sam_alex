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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

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
    private Integer nightCount;
    private String drinkCost = "NA";
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
    private String typeStress = "";

    private String whenStressOccurred = "NA";
    private String drinklastNight = "NA";
    private Integer drinksCounter = 0;
    private String typesOfDrinks = "";
    private String hangover = "NA";
    private String analVaginalSex = "NA";
    private String condom = "NA";
    private String partner = "NA";
    private String drugs = "NA";
    private String typeDrugs = "";
    public static final String CHANNEL_ID = "com.samalex.slucapstone.ANDROID";

    private TextView stressRatingSlideBarLabel;

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

        final ImageButton lastNightDrinkYes = (ImageButton) findViewById(R.id.drinkAlcoholQS_yes);
        final ImageButton lastNightDrinkNo = (ImageButton) findViewById(R.id.drinkAlcoholQS_no);
        final TextView lastNightDrinkYesText = (TextView) findViewById(R.id.drinkAlcoholQS_yesText);
        final TextView lastNightDrinkNoText = (TextView) findViewById(R.id.drinkAlcoholQS_noText);

        final TextView number_drinks_qs = (TextView) findViewById(R.id.number_drinks_qs);
        final LinearLayout number_drinks_qs_layout = (LinearLayout) findViewById(R.id.number_drinks_qs_layout);
        final Button drinks_add = (Button) findViewById(R.id.drinks_add_button);
        final Button drinks_subtract = (Button) findViewById(R.id.drinks_subtract_button);
        final TextView drinks_counterText = (TextView) findViewById(R.id.drinks_counter);

        final TextView drink_type = (TextView) findViewById(R.id.drink_type);
        final TextView select_drink_type = (TextView) findViewById(R.id.select_drink_type);
        final LinearLayout drink_type_layout = (LinearLayout) findViewById(R.id.drink_type_layout);
        final CheckBox liquor = (CheckBox) findViewById(R.id.liquor);
        final CheckBox wine = (CheckBox) findViewById(R.id.wine);
        final CheckBox beer = (CheckBox) findViewById(R.id.beer);

        final TextView hangover_qs = (TextView) findViewById(R.id.hangover_qs);
        final LinearLayout hangover_qs_layout = (LinearLayout) findViewById(R.id.hangover_qs_layout);
        final TextView hangoverYesText = (TextView) findViewById(R.id.hangover_yesText);
        final ImageButton hangoverYes = (ImageButton) findViewById(R.id.hangover_yes);
        final TextView hangoverNoText = (TextView) findViewById(R.id.hangover_noText);
        final ImageButton hangoverNo = (ImageButton) findViewById(R.id.hangover_no);

        final TextView drugs_qs = (TextView) findViewById(R.id.drugs);
        final LinearLayout drugs_layout = (LinearLayout) findViewById(R.id.drugs_layout);
        final ImageButton drugs_yes = (ImageButton) findViewById(R.id.drugs_yes);
        final TextView drugs_yesText = (TextView) findViewById(R.id.drugs_yesText);
        final ImageButton drugs_no = (ImageButton) findViewById(R.id.drugs_no);
        final TextView drugs_noText = (TextView) findViewById(R.id.drugs_noText);

        final TextView typesOfDrugs_qs = (TextView) findViewById(R.id.typesOfDrugs);
        final TextView select_drugs = (TextView) findViewById(R.id.select_drugs);
        final LinearLayout typesOfDrugs_layout = (LinearLayout) findViewById(R.id.typesOfDrugs_layout);
        final CheckBox ritalin = (CheckBox) findViewById(R.id.ritalin);
        final CheckBox adderall = (CheckBox) findViewById(R.id.adderall);
        final CheckBox oxyContin = (CheckBox) findViewById(R.id.oxyContin);
        final CheckBox vicodin = (CheckBox) findViewById(R.id.vicodin);
        final CheckBox percocet = (CheckBox) findViewById(R.id.percocet);
        final CheckBox otherPrescriptionOpioid = (CheckBox) findViewById(R.id.otherPrescriptionOpioid);
        final CheckBox xanax = (CheckBox) findViewById(R.id.xanax);
        final CheckBox valium = (CheckBox) findViewById(R.id.valium);
        final CheckBox otherBenzo = (CheckBox) findViewById(R.id.otherBenzo);
        final CheckBox marijuana = (CheckBox) findViewById(R.id.marijuana);
        final CheckBox heroin = (CheckBox) findViewById(R.id.heroin);
        final CheckBox mdma = (CheckBox) findViewById(R.id.mdma);
        final CheckBox meth = (CheckBox) findViewById(R.id.meth);
        final CheckBox otherDrug = (CheckBox) findViewById(R.id.otherDrug);

        final ImageButton analVaginalSexYes = (ImageButton) findViewById(R.id.analVaginalSex_yes);
        final TextView analVaginalSexYesText = (TextView) findViewById(R.id.analVaginalSex_yesText);
        final ImageButton analVaginalSexNo = (ImageButton) findViewById(R.id.analVaginalSex_no);
        final TextView analVaginalSexNoText = (TextView) findViewById(R.id.analVaginalSex_noText);

        final TextView condom_qs = (TextView) findViewById(R.id.condom);
        final LinearLayout condom_layout = (LinearLayout) findViewById(R.id.condom_layout);
        final ImageButton condom_yes = (ImageButton) findViewById(R.id.condom_yes);
        final TextView condom_yesText = (TextView) findViewById(R.id.condom_yesText);
        final ImageButton condom_no = (ImageButton) findViewById(R.id.condom_no);
        final TextView condom_noText = (TextView) findViewById(R.id.condom_noText);

        final TextView sexPartner_qs = (TextView) findViewById(R.id.sexPartner_qs);
        final LinearLayout sexPartner_qs_layout = (LinearLayout) findViewById(R.id.sexPartner_qs_layout);
        final RadioGroup partnerGroup = (RadioGroup) findViewById(R.id.radioGroupPartner);


        final TextView stressfulEventOccurance = (TextView) findViewById(R.id.stressfulEventOccurance);
        final LinearLayout stressfulEventOccurance_layout = (LinearLayout) findViewById(R.id.stressfulEventOccurance_layout);
        final RadioGroup stressGroup = (RadioGroup) findViewById(R.id.radioGroupStress);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.stress_rating_slidebar);

        Button submit = (Button) findViewById(R.id.submit);

        final ImageButton stress_yes = (ImageButton) findViewById(R.id.stress_q1_yes_button);
        final TextView stress_yesText = (TextView) findViewById(R.id.stress_q1_yes_text);
        final ImageButton stress_no = (ImageButton) findViewById(R.id.stress_q1_no_button);
        final TextView stress_noText = (TextView) findViewById(R.id.stress_q1_no_text);

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

        //gets shared preferences variable
        SharedPreferences mSharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE);
        userIDMA = mSharedPreferences.getString("user ID", "none");

        SharedPreferences mSharedPreferences2 = getSharedPreferences("Group", MODE_PRIVATE);
        group = mSharedPreferences2.getString("Group", "none");

        nightCount = getNightCount();

        dismissMorningQuestionnaireNotification();

        //creates all of the onClick listeners for all of the buttons for questions
        View.OnClickListener handler1 = new View.OnClickListener() {
            public void onClick(View view) {
                writeOralToDB(oral);
                writeLastNightDrink(drinklastNight);
                writeNumDrinksToDB(drinksCounter);
                writeTypesDrinksToDB(typesOfDrinks);
                writeHangoverToDB(hangover);
                writeDrugsToDB(drugs);
                writeTypeDrugsToDB(typeDrugs);
                writeAnalVaginalToDB(analVaginalSex);
                writeCondomToDB(condom);

                if (analVaginalSex == "Yes") {
                    int partnerSelectedId = partnerGroup.getCheckedRadioButtonId();
                    RadioButton partnerButton;
                    partnerButton = (RadioButton) findViewById(partnerSelectedId);
                    partner = partnerButton.getText().toString();
                    writePartnerToDB(partner);

                }
                if (stress_event == "Yes") {
                    int stressSelectedID = stressGroup.getCheckedRadioButtonId();
                    RadioButton stressButton;
                    stressButton = (RadioButton) findViewById(stressSelectedID);
                    whenStressOccurred = stressButton.getText().toString();
                    writeStressOccurranceToDB(whenStressOccurred);
                }

                writeStressEventToDB(stress_event);
                writeTypeStressToDB(typeStress);
                writeStressValueToDB(stress_value);
                finish();

                // The "none" and "control" group will not see the Morning Summary Report
                if (group.equals("control") || group.equals("none")) {
                    startActivity1 = "start";
                    storeScreen(startActivity1);
                    storeNumDrinks(0);
                    Intent goToStart = new Intent(MorningQS.this, StartActivity.class);
                    goToStart.putExtra("Start Activity", startActivity);
                    startActivity(goToStart);
                    finish();
                } else if (group.equals("experimental")) {
                    startActivity1 = "morningReport";
                    storeScreen(startActivity1);
                    storeNumDrinks(0);
                    Intent goToStart = new Intent(MorningQS.this, StartActivity.class);
                    goToStart.putExtra("Start Activity", startActivity);
                    startActivity(goToStart);
                    finish();
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
                if (drugs.equals("yes")) {
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
                    typesOfDrinks += "Liquor ";
                } else {
                    liquor.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        wine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (wine.isChecked() == true) {
                    wine.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typesOfDrinks += "Wine ";
                } else {
                    wine.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        beer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (beer.isChecked() == true) {
                    beer.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typesOfDrinks += "Beer ";
                } else {
                    beer.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        View.OnClickListener onClickHangoverNo = new View.OnClickListener() {
            public void onClick(View view) {
                hangover = "no";
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
                hangover = "yes";
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
                drugs = "no";
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
                drugs = "yes";
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
                    typeDrugs += "Ritalin ";
                } else {
                    ritalin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        adderall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (adderall.isChecked() == true) {
                    adderall.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "Adderall ";
                } else {
                    adderall.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        oxyContin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (oxyContin.isChecked() == true) {
                    oxyContin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "OxyContin ";
                } else {
                    oxyContin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        vicodin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (vicodin.isChecked() == true) {
                    vicodin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "Vicodin ";
                } else {
                    vicodin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        percocet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (percocet.isChecked() == true) {
                    percocet.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "Percocet ";
                } else {
                    percocet.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        otherPrescriptionOpioid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (otherPrescriptionOpioid.isChecked() == true) {
                    otherPrescriptionOpioid.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "OtherPrescripOpioid ";
                } else {
                    otherPrescriptionOpioid.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        xanax.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (xanax.isChecked() == true) {
                    xanax.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "Xanax ";
                } else {
                    xanax.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        valium.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (valium.isChecked() == true) {
                    valium.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "Valium ";
                } else {
                    valium.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        otherBenzo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (otherBenzo.isChecked() == true) {
                    otherBenzo.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "OtherBenzo ";
                } else {
                    otherBenzo.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        marijuana.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (marijuana.isChecked() == true) {
                    marijuana.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "Marijuana ";
                } else {
                    marijuana.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        heroin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (heroin.isChecked() == true) {
                    heroin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "Heroin ";
                } else {
                    heroin.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        mdma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mdma.isChecked() == true) {
                    mdma.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "MDMA/Ecstasy ";
                } else {
                    mdma.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        meth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (meth.isChecked() == true) {
                    meth.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "Methamphetamine ";
                } else {
                    meth.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });
        otherDrug.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (otherDrug.isChecked() == true) {
                    otherDrug.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeDrugs += "OtherDrug ";
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
                    typeStress += "Interpersonal ";
                } else {
                    interpersonal_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        financial_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (financial_check.isChecked() == true) {
                    financial_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress += "Financial ";
                } else {
                    financial_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        work_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (work_check.isChecked() == true) {
                    work_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress += "Work ";
                } else {
                    work_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        health_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (health_check.isChecked() == true) {
                    health_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress += "Health ";
                } else {
                    health_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        trauma_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (trauma_check.isChecked() == true) {
                    trauma_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress += "Trauma ";
                } else {
                    trauma_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        other_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (other_check.isChecked() == true) {
                    other_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.pink));
                    typeStress += "Other ";
                } else {
                    other_check.setTextColor(ContextCompat.getColor(MorningQS.this, R.color.white));
                }
            }
        });

        String broadcastID = getIntent().getStringExtra("broadcast Int");
        if (broadcastID != null) {
            int notificationId = Integer.parseInt(broadcastID);
            NotificationManager manager = (NotificationManager) MorningQS.this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
        }

        // set a change listener on the SeekBar
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();
        stressRatingSlideBarLabel = findViewById(R.id.stress_rating_text);
        stressRatingSlideBarLabel.setText("You rate your stress: " + progress + " out of 10");
        stress_value = progress + "";
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

    public void writeStressValueToDB(String value) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("12StressValue");
        mRef.setValue(value);
    }

    public void writeStressEventToDB(String stress_event) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("10StressEvent");
        mRef.setValue(stress_event);
    }

    public void writeStressOccurranceToDB(String whenStressOccurred) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("13StressTime");
        mRef.setValue(whenStressOccurred);
    }

    public void writeTypeStressToDB(String typeStress) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("11TypeStress");
        mRef.setValue(typeStress);
    }

    public void writeInterpersonal(String interpersonal) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("Interpersonal");
        mRef.setValue(interpersonal);
    }

    public void writeWorkToDB(String work) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("Work");
        mRef.setValue(work);
    }

    public void writeFinancialToDB(String financial) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("Financial");
        mRef.setValue(financial);
    }

    public void writeHealthToDB(String health) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("Health");
        mRef.setValue(health);
    }

    public void writeTraumaToDB(String trauma) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("Trauma");
        mRef.setValue(trauma);
    }

    public void writeStressOtherToDB(String stress_other) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("StressOther");
        mRef.setValue(stress_other);
    }

    public void writeCostTotaltoDB(String drink_cost) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("Cost_Total");
        mRef.setValue(drink_cost);

    }

    //functions to write all of the answers to the database
    public void writeNumPartners(int number) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("numPartners");
        mRef.setValue(number);
    }

    public void writeMonoPartner(String monoPartner) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("MonogamousPartner");
        mRef.setValue(monoPartner);
    }

    public void writeFriendPartner(String friendPartner) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("FriendPartner");
        mRef.setValue(friendPartner);
    }

    public void writeNewPartner(String newPartner) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("NewPartner");
        mRef.setValue(newPartner);
    }

    public void writeNAPartner(String naPartner) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("NAPartner");
        mRef.setValue(naPartner);
    }

    public void writeOralToDB(String oral) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("oral");
        mRef.setValue(oral);
    }

    public void writeLastNightDrink(String drinklastNight) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("1EpisodeLastNight");
        mRef.setValue(drinklastNight);
    }

    public void writeNumDrinksToDB(int drinksCounter) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("2NumberOfDrinks");
        mRef.setValue(drinksCounter);
    }

    public void writeTypesDrinksToDB(String typesOfDrinks) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("3TypesOfDrinks");
        mRef.setValue(typesOfDrinks);
    }


    public void writeHangoverToDB(String hangover) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("4Hangover");
        mRef.setValue(hangover);
    }

    public void writeDrugsToDB(String drugs) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("5Drugs");
        mRef.setValue(drugs);
    }

    public void writeTypeDrugsToDB(String typeDrugs) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("6TypeDrugs");
        mRef.setValue(typeDrugs);
    }

    public void writeAnalVaginalToDB(String analVaginalSex) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("7AnalVaginalSex");
        mRef.setValue(analVaginalSex);
    }

    public void writeCondomToDB(String condom) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("8UsedCondom");
        mRef.setValue(condom);
    }


    public void writePartnerToDB(String partner) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("9PartnerType");
        mRef.setValue(partner);
    }

    public void writeVaginalToDB(String vaginal) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("vaginal");
        mRef.setValue(vaginal);
    }

    public void writeAnalToDB(String anal) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("anal");
        mRef.setValue(anal);
    }

    public void writeVaginalCondomToDB(String vaginalCondom) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("vaginalCondom");
        mRef.setValue(vaginalCondom);
    }

    public void writeVaginalConsentToDB(String vaginalConsent) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("vaginalConsent");
        mRef.setValue(vaginalConsent);
    }

    public void writeAnalCondomToDB(String analCondom) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("analCondom");
        mRef.setValue(analCondom);
    }

    public void writeAnalConsentToDB(String analConsent) {
        getTime();
        DatabaseReference mRef = mDatabase.child("Users").child("UID: " + userIDMA).child("Night Count: " + nightCount).child("MorningAnswers").child(time).child("analConsent");
        mRef.setValue(analConsent);
    }

    //gets nightCount shared preference variable
    private Integer getNightCount() {
        SharedPreferences mSharedPreferences = getSharedPreferences("Night Count", MODE_PRIVATE);
        Integer nightCount = mSharedPreferences.getInt("night counter", 0);
        return nightCount;
    }
}