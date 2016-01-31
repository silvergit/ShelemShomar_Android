package arp.dolphinsoft.lidora.shelemcounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_start_game;
    EditText edit_first_group_name, edit_second_group_name;
    CheckBox check_double_negative;
    Spinner spinner_game_type;
    TextView txt_validation;

    ArrayList<String> game_type_list;

    public static final String my_preferences = "my_prefs";
    public static final String pref_language = "lang_key";
//    public static final String pref_first_group_name = "first_group_name_key";
//    public static final String pref_second_group_name = "second_group_name_key";
//    public static final String pref_game_type = "game_type_key";
//    public static final String pref_double_negative = "double_negative_key";

    String lang = "fa";

    SharedPreferences shared_preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getMyPreferences();

        setContentView(R.layout.activity_main);

        initialize();

        btn_start_game.setOnClickListener(this);

    }

    private void getMyPreferences() {
        shared_preferences = getSharedPreferences(my_preferences, MODE_PRIVATE);

        if (shared_preferences.contains(pref_language)) {
            changeLanguage(shared_preferences.getString(pref_language, "fa"));
        }
//
//        if (shared_preferences.contains(pref_first_group_name)) {
//            edit_first_group_name.setText(shared_preferences.getString(pref_first_group_name, ""));
//        }
//
//        if (shared_preferences.contains(pref_second_group_name)) {
//            edit_first_group_name.setText(shared_preferences.getString(pref_second_group_name, ""));
//        }
//
//        if (shared_preferences.contains(pref_double_negative)) {
//            if (shared_preferences.getString(pref_first_group_name, "").contains("1")) {
//                check_double_negative.setChecked(true);
//            } else {
//                check_double_negative.setChecked(false);
//            }
//        }
    }

    private void setMyPreferences() {
//        String double_negative = "0";
//        String game_type = "165";
//        String first_group_name = null;
//        String second_group_name = null;
//
//        first_group_name = edit_first_group_name.getText().toString();
//        second_group_name = edit_second_group_name.getText().toString();
//
//        if (check_double_negative.isChecked() == true) {
//            double_negative = "1";
//        }

        //game_type = game_type_list.get(spinner_game_type.getSelectedItemPosition());
        SharedPreferences.Editor editor = shared_preferences.edit();
        editor.putString(pref_language, lang);
//        editor.putString(pref_first_group_name, first_group_name);
//        editor.putString(pref_second_group_name, second_group_name);
//        editor.putString(pref_double_negative, double_negative);
        //editor.putString(pref_game_type, game_type);

        editor.commit();
    }

    private void changeLanguage(String lang) {
        String languageToLoad  = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void initialize() {
        btn_start_game          = (Button)      findViewById(R.id.btn_start_game);
        edit_first_group_name   = (EditText)    findViewById(R.id.edit_first_group_name);
        edit_second_group_name  = (EditText)    findViewById(R.id.edit_second_group_name);
        check_double_negative   = (CheckBox)    findViewById(R.id.check_double_negative);
        spinner_game_type       = (Spinner)     findViewById(R.id.spinner_game_type);
        txt_validation          = (TextView)    findViewById(R.id.txt_validation);

        game_type_list = new ArrayList<String>();

        game_type_list.add("165");
        game_type_list.add("185");
        game_type_list.add("200");
        game_type_list.add("225");
        game_type_list.add("230");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, game_type_list);

        spinner_game_type.setAdapter(adapter);
        spinner_game_type.setSelection(0);

        txt_validation.setVisibility(View.INVISIBLE);

        check_double_negative.setChecked(false);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_start_game) {

            if(edit_first_group_name.getText().toString().equals("")) {
                txt_validation.setVisibility(View.VISIBLE);
                edit_first_group_name.requestFocus();
            } else if(edit_second_group_name.getText().toString().equals("")) {
                txt_validation.setVisibility(View.VISIBLE);
                edit_second_group_name.requestFocus();
            } else {
                int double_negative = 0;
                int game_type = 165;
                String first_group_name = null;
                String second_group_name = null;

                first_group_name = edit_first_group_name.getText().toString();
                second_group_name = edit_second_group_name.getText().toString();

                if (check_double_negative.isChecked() == true) {
                    double_negative = 1;
                }

                game_type = Integer.parseInt(game_type_list.get(
                        spinner_game_type.getSelectedItemPosition()));

                Intent intent = new Intent(this, GameChartActivity.class);

                intent.putExtra("double_negative", double_negative);
                intent.putExtra("game_type", game_type);
                intent.putExtra("first_group_name", first_group_name);
                intent.putExtra("second_group_name", second_group_name);

                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnu_about:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                //alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setTitle(R.string.about_title);
                alertDialogBuilder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                LayoutInflater inflater = this.getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.lay_about_me, null);
                alertDialogBuilder.setView(dialogView);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

            case R.id.mnu_help:
                Toast.makeText(this, R.string.comming_soon, Toast.LENGTH_SHORT).show();
                break;

            case R.id.mnu_theme:
                Toast.makeText(this, R.string.comming_soon, Toast.LENGTH_SHORT).show();
                break;

            case R.id.mnu_lang:
                AlertDialog.Builder alertDialogBuilder_lang = new AlertDialog.Builder(this);

                //alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                alertDialogBuilder_lang.setCancelable(true);
                alertDialogBuilder_lang.setTitle(R.string.about_title);
                alertDialogBuilder_lang.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                LayoutInflater inflater_lang = this.getLayoutInflater();

                View dialogView_lang = inflater_lang.inflate(R.layout.lay_alter_language, null);
                alertDialogBuilder_lang.setView(dialogView_lang);


                RadioButton radio_lang_eng = (RadioButton) dialogView_lang.findViewById(R.id.radio_lang_eng);
                RadioButton radio_lang_fa = (RadioButton) dialogView_lang.findViewById(R.id.radio_lang_fa);

                if (lang.contains("fa")) {
                    radio_lang_fa.setChecked(true);
                } else {
                    radio_lang_eng.setChecked(true);
                }

                radio_lang_eng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lang = "en";
                        setMyPreferences();
                    }
                });

                radio_lang_fa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lang = "fa";
                        setMyPreferences();
                    }
                });

                AlertDialog alertDialog_lang = alertDialogBuilder_lang.create();
                alertDialog_lang.show();
                break;

            case R.id.mnu_exit:
                this.finish();
                System.exit(0);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
