package dominique.fr.myapplikejv;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import requests.RecupInfoProfilRequest;
import utilitaires.UserSessionManager;

public class ProfilActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textViewPref;

    /*------- Session -------*/
    UserSessionManager session;

    /*------- Fin session ------*/

    User user_connecte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ciblage des éléments de l'Activity
        final TextView tvPseudo = (TextView) findViewById(R.id.tv_aff_pseudo);
        final TextView tvMdp = (TextView)findViewById(R.id.tv_aff_mdp);
        final TextView tv_aff_email = (TextView) findViewById(R.id.tv_aff_email);
        final LinearLayout tv_aff_preferences = (LinearLayout)findViewById(R.id.tv_aff_preferences);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final Float widthPixels = Float.valueOf(metrics.widthPixels);

        /*-----------Session -------*/
        session = new UserSessionManager(getApplicationContext());

        //vérif que la session existe (et donc que l'utilisateur est connecté)
        //si l'utilisateur n'est pas connecté, alors on le redirige vers la page d'accueil
        if (session.checkLogin()){
            finish();
        }

        //si l'user est connecté, on récupère les données de session :
        HashMap<String, Integer> userFromSession = session.getUserDetails();
        //récup de l'id
        int idFromSession = userFromSession.get(UserSessionManager.KEY_ID);

        /*-------*/

        //connexion à RecupInfoProfil.php pour acces à la bdd
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //'response' qui est en argument est la réponse de register.php
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    //si le traitement est un succes, alors on va aller sur la page Login
                    if(success){

                        user_connecte = new User();
                        //get the detail from the json response and send it to the view
                        //pseudo
                        user_connecte.setPseudo(jsonResponse.getString("user_pseudo"));

                        //email
                        user_connecte.setEmail(jsonResponse.getString("user_email"));

                        //mdp
                        user_connecte.setMotDePasse(jsonResponse.getString("user_mdp"));

                        //categories
                        ArrayList<String> liste_categ_tab = new ArrayList<>();
                        JSONArray jsonArrayCateg = jsonResponse.getJSONArray("cat_nom");
                        for (int i = 0; i<jsonArrayCateg.length(); i++){
                            liste_categ_tab.add(jsonArrayCateg.getString(i));
                        }
                        user_connecte.setListeNomCategoriesPreferees(liste_categ_tab);

                        tvPseudo.setText(user_connecte.getPseudo());
                        tv_aff_email.setText(user_connecte.getEmail());
                        tvMdp.setText(user_connecte.getMotDePasse());
                        for (int j=0; j<user_connecte.getListeNomCategoriesPreferees().size(); j++){
                            textViewPref = new TextView(getApplicationContext());
                            textViewPref.setId(j);
                            if(widthPixels >= 1200){
                                textViewPref.setTextSize(22);
                            }else {
                                textViewPref.setTextSize(18);
                            }
                            textViewPref.setTextColor(getResources().getColor(R.color.noir));
                            textViewPref.setText(user_connecte.getListeNomCategoriesPreferees().get(j));
                            tv_aff_preferences.addView(textViewPref);
                        }


                    }else{
                        //sinon on affiche une alert qui propose de réessayer
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
                        builder.setMessage("Connexion échouée")
                                .setNegativeButton("retenter", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }//end onresponse
        }; //end listener


        //création du loginRequest
        RecupInfoProfilRequest recupInfoProfilRequest = new RecupInfoProfilRequest(idFromSession, responseListener);
        //add to the queue
        RequestQueue queue = Volley.newRequestQueue(ProfilActivity.this);
        queue.add(recupInfoProfilRequest);



        /*-------- Fin session ------*/

    }//end oncreate

    /*----------------
   Menu dans toolbar
    */
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
                intent.putExtra("user_connecte", user_connecte);
                startActivity(intent);
                finish();
                break;
            case R.id.go_contact:
                intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.go_connexion:
                intent = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(intent);
                finish();
                break;
            case android.R.id.home: //gestion de la flèche de retour dans le menu. ramène à l'accueil en fermant l'activity en cours
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                intent2.putExtra("user_connecte", user_connecte);
                startActivity(intent2);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //--------fin toolbar--------

    public void goModifProfil(View view) {

        Intent intent = new Intent(this, ProfilModifActivity.class);
        intent.putExtra("user_connecte",(Serializable) user_connecte);
        startActivity(intent);

    }

    public void logout_from_profil(View view) {
        session.logoutUser();
    }

    /*-----Clic sur bouton contact orga-------*/
    public void contactOrga(View view) {
        String e_mail_contact_organisateurs = getString(R.string.mail_to_orga_asso);

        //ouverture direct de l'appli mail vers l'adresse spéciale organisateurs
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(e_mail_contact_organisateurs));
        intent.putExtra(Intent.EXTRA_SUBJECT, R.string.sujet_mail_orga_asso);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
}
