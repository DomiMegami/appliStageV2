package dominique.fr.myapplikejv;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import modeles.User;
import requests.RecupInfo_ModifProfilRequest;
import requests.RecupPseudoRequest;
import requests.UpdateProfilRequest;
import utilitaires.UserSessionManager;

public class ProfilModifActivity extends AppCompatActivity {



    /*--------variables -------*/
    Toolbar toolbar;
    TextView textViewPref;

    UserSessionManager session;

    //liste de toutes les categories
    ArrayList<String> liste_categ_tab;
    //user
    User user_connecte=new User();
    //chekbox
    CheckBox checkBox;
    CheckBox chk_cat_1;
    CheckBox chk_cat_2;
    CheckBox chk_cat_3;
    CheckBox chk_cat_4;

    EditText et_new_pseudo;

    ArrayList<Integer> listPreferencesId = new ArrayList<>();

/*--------------------------------*/

/*----------------- onCreate ---------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_modif);
    /*------Toolbar----------*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*-----------Session -------*/
        session = new UserSessionManager(getApplicationContext());

        /*-----------intent--------*/
        Intent intent = getIntent();
        user_connecte = (User)intent.getSerializableExtra("user_connecte");

        /*--------ciblage des éléments de l'Activity-------*/
        final EditText etPseudo = (EditText) findViewById(R.id.et_modif_pseudo); //pseudo
        final TextView tv_aff_email = (TextView) findViewById(R.id.tv_aff_email); //email
        final TextView tv_aff_mdp = (TextView)findViewById(R.id.tv_aff_mdp);//mdp
        final LinearLayout tv_aff_categories = (LinearLayout)findViewById(R.id.tv_aff_categories); // liste de toutes les categories


        /*------- recup donnees de session ------*/
        HashMap<String, Integer> userFromSession = session.getUserDetails();
        //récup de l'id
        final int idFromSession = userFromSession.get(UserSessionManager.KEY_ID);
        user_connecte.setId(idFromSession);

        /*--------autres variables --------*/
        final ArrayList<String> listePrefUser = user_connecte.getListeNomCategoriesPreferees();

        /*Nota :  Affichage des données :
            - depuis l'objet user_connecté :
                #pseudo
                #email
                #mot de passe
                #liste des categories préférées
            - depuis la requete vers la base de données :
                #liste de toutes les catégories.
         */

        etPseudo.setText(user_connecte.getPseudo());
        tv_aff_email.setText(user_connecte.getEmail());
        tv_aff_mdp.setText(user_connecte.getMotDePasse());

/*--- listener de la réponse de connexion ---*/
        //connexion à RecupInfo_pourModifProfil.php pour acces à la bdd
        //-----------------------------------------------------------
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //'response' qui est en argument est la réponse de register.php
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    //si le traitement est un succes, alors on va aller sur la page Login
                    if(success){


                        //liste de toutes les categories
                        liste_categ_tab = new ArrayList<>();
                        JSONArray jsonArrayCateg = jsonResponse.getJSONArray("cat_nom");
                        for (int i = 0; i<jsonArrayCateg.length(); i++){
                            liste_categ_tab.add(jsonArrayCateg.getString(i));
                        }

                        /*----- gestion du checked ou non suivant la bdd ------*/
                        chk_cat_1 = findViewById(R.id.ckb_categorie_1);
                        //String txt_ckb_cat_1 = chk_cat_1.getText().toString();
                        String txt_ckb_cat_1 = chk_cat_1.getText().toString();
                        chk_cat_2 = findViewById(R.id.ckb_categorie_2);
                        //String txt_ckb_cat_2 = chk_cat_2.getText().toString();
                        String txt_ckb_cat_2 = chk_cat_2.getText().toString();
                        chk_cat_3 = findViewById(R.id.ckb_categorie_3);
                        //String txt_ckb_cat_3 = chk_cat_3.getText().toString();
                        String txt_ckb_cat_3 = chk_cat_3.getText().toString();
                        chk_cat_4 = findViewById(R.id.ckb_categorie_4);
                        //String txt_ckb_cat_4 = chk_cat_4.getText().toString();
                        String txt_ckb_cat_4 = chk_cat_4.getText().toString();

                        for (int u = 0; u < listePrefUser.size(); u++) {
                            switch (listePrefUser.get(u)){
                                case "Jeux" :
                                    chk_cat_1.setChecked(true);
                                    break;
                                case "Art et culture" :
                                    chk_cat_2.setChecked(true);
                                    break;
                                case "Bonnes affaires" :
                                    chk_cat_3.setChecked(true);
                                    break;
                                case "Sport" :
                                    chk_cat_4.setChecked(true);
                                    break;
                            }
                        }//end for (listePrefUser)


                    }else{
                        //sinon on affiche une alert qui propose de réessayer
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilModifActivity.this);
                        builder.setMessage("oups")
                                .setNegativeButton("retry", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }//end onresponse
        }; //end listener

/*--------- envoie de la requête ------*/

        RecupInfo_ModifProfilRequest recupInfo_modifProfilRequest = new RecupInfo_ModifProfilRequest(idFromSession, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ProfilModifActivity.this);
        queue.add(recupInfo_modifProfilRequest);

    /*--------- évènement sur le bouton modif mdp ---------*/
        Button btn_modif_mdp = (Button) findViewById(R.id.btn_modif_mdp);
        btn_modif_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilModifActivity.this, PopupModifMdp.class);
                user_connecte.setId(idFromSession);
                intent.putExtra("user_connecte", (Serializable) user_connecte);
                startActivity(intent);

                //startActivity(new Intent(ProfilModifActivity.this, PopupModifMdp.class));
            }
        });


    }
    /*------------ fin onCreate -------*/




    /*------- Valid modif prodil -------*/
    public void ValidModifProfil(View view) {
        /*----variables ----- */
        final int user_id = user_connecte.getId();
        final String user_pseudo = user_connecte.getPseudo();
        et_new_pseudo = (EditText) findViewById(R.id.et_modif_pseudo);
        final String user_new_pseudo = et_new_pseudo.getText().toString();
        final Integer user_pref_1;
        final Integer user_pref_2;
        final Integer user_pref_3;
        final Integer user_pref_4;


        //récup de l'état des chekboxes
        listPreferencesId.clear();
        if (chk_cat_1.isChecked()) {
            listPreferencesId.add(0, 31);
        } else {
            listPreferencesId.add(0, 0);
        }

        if (chk_cat_2.isChecked()) {
            listPreferencesId.add(1, 32);
        } else {
            listPreferencesId.add(1, 0);
        }
        if (chk_cat_3.isChecked()) {
            listPreferencesId.add(2, 33);
        } else {
            listPreferencesId.add(2, 0);
        }
        if (chk_cat_4.isChecked()) {
            listPreferencesId.add(3, 35);
        } else {
            listPreferencesId.add(3, 0);
        }
        user_pref_1 = listPreferencesId.get(0);
        user_pref_2 = listPreferencesId.get(1);
        user_pref_3 = listPreferencesId.get(2);
        user_pref_4 = listPreferencesId.get(3);

        /*-------vérif qu'au moins 1 categorie est cochée ------*/
        if (listPreferencesId.get(0) == 0 && listPreferencesId.get(1) == 0 && listPreferencesId.get(2) == 0 && listPreferencesId.get(3) == 0) {
            Toast.makeText(getApplicationContext(), R.string.txt_info_au_moins_1_categ, Toast.LENGTH_LONG).show();
        }//end if (les 3 categ sont décochées)
        else if (!user_new_pseudo.equals(user_pseudo)) {

        /*-----verif que le new pseudo n'est pas déjà dans la bdd------*/
            final Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //'response' qui est en argument est la réponse de register.php
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        //si le traitement est un succes, alors on va aller sur la page Login
                        if (success) {
                            //pseudo déjà pris
                            Toast.makeText(getApplicationContext(), R.string.txt_info_pseudo_deja_pris, Toast.LENGTH_LONG).show();

                        } else {
                            //pseudo pas pris :
                         /*----envoi à la bdd ----*/
                            final Response.Listener<String> responseListener_modifProfil = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //'response' qui est en argument est la réponse de register.php
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        //si le traitement est un succes, alors on va aller sur la page Login
                                        if (success) {
                                            Toast.makeText(ProfilModifActivity.this, R.string.txt_info_profil_modifie, Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(), ProfilActivity.class));

                                        } else {
                                            //sinon on affiche une alert qui propose de réessayer
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfilModifActivity.this);
                                            builder.setMessage("oups")
                                                    .setNegativeButton("retry", null)
                                                    .create()
                                                    .show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }//end onresponse
                            }; //end listener

/*--------- envoie de la requête ------*/
                            UpdateProfilRequest updateProfilRequest = new UpdateProfilRequest(user_id, user_pseudo, user_new_pseudo, user_pref_1, user_pref_2, user_pref_3,user_pref_4, responseListener_modifProfil);
                            RequestQueue queue2 = Volley.newRequestQueue(ProfilModifActivity.this);
                            queue2.add(updateProfilRequest);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };

            //création du loginRequest
            RecupPseudoRequest recupPseudoRequest = new RecupPseudoRequest(user_new_pseudo, responseListener);
            //add to the queue
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(recupPseudoRequest);
        }//end if(les 2 pseudo sont =/=)
        else{
            //au moins 1 categ est cochée et les 2 pseudos sont les même -> envoie à la bdd
            /*----envoi à la bdd ----*/
            final Response.Listener<String> responseListener_modifProfil = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //'response' qui est en argument est la réponse de register.php
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        //si le traitement est un succes, alors on va aller sur la page Login
                        if (success) {
                            Toast.makeText(ProfilModifActivity.this, R.string.txt_info_profil_modifie, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), ProfilActivity.class));

                        } else {
                            //sinon on affiche une alert qui propose de réessayer
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfilModifActivity.this);
                            builder.setMessage("oups")
                                    .setNegativeButton("retry", null)
                                    .create()
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }//end onresponse
            }; //end listener

/*--------- envoie de la requête ------*/
            UpdateProfilRequest updateProfilRequest = new UpdateProfilRequest(user_id, user_pseudo, user_new_pseudo, user_pref_1, user_pref_2, user_pref_3,user_pref_4, responseListener_modifProfil);
            RequestQueue queue2 = Volley.newRequestQueue(ProfilModifActivity.this);
            queue2.add(updateProfilRequest);
        }





        /*Affichage pour débug :*/
        //String message = "user_id : "+user_id  ;
///
        //message += "\nuser_pseudo : "+user_pseudo+"\nuser_new_pseudo : " + user_new_pseudo;
//
        //for (int i = 0; i<listPreferencesId.size();i++){
        //   message += "\n Etat categ " + i + " : " + listPreferencesId.get(i);
        //}
        //Toast.makeText(ProfilModifActivity.this, message, Toast.LENGTH_LONG).show();
    }//end valid profil
    /* -------- fin valid profil -------*/

    /*--------annul modif -------*/
    public void annulModif(View view) {
        /*Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);*/
        finish();
    }
    /*--------fin annul profil -------*/

    /*---------- Menu dans toolbar ------*/
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.go_accueil:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.go_contact:
                intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);
                break;
            case R.id.go_connexion:
                intent = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home: //gestion de la flèche de retour dans le menu. ramène à l'accueil en fermant l'activity en cours
                finish();
        }

        return super.onOptionsItemSelected(item);
    }



    /*--------fin toolbar--------*/

    /*-----changement d'état des checkboxes générées dynamiquement ---- */
    View.OnClickListener modifEtatCheckbox(final CheckBox checkBox) {
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String message = "Chekbox checked = "+ checkBox.isChecked();

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                int checkBox_id = checkBox.getId();

            }
        };
    }
}
