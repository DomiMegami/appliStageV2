package dominique.fr.myapplikejv;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import adapters.Article_ArrayAdapter;
import modeles.Article;
import modeles.Url_api_perso;
import requests.RecupInfo_ModifProfilRequest;
import utilitaires.UserSessionManager;
import utilitaires.Utility;

public class AccueilUserConnectedActivity extends AppCompatActivity {


    /*---------------VARIABLES GLOBALES-------------------*/

        Toolbar toolbar;

        //session
        UserSessionManager session ;

        //Article qui sera sélectionné dans l'une ou l'autre des listes :
        Article articleCourant;

        //var pour liste des sorties du jour
        //la liste
        ArrayList<Article> listeSortiesDuJour = new ArrayList<Article>();
        //la listView
        ListView lV_sortiesDuJour;
        //l'adapter
        Article_ArrayAdapter list_sortiesDuJour_arrayAdapter; //adapter pour type Article

        //strData
        String strData = null;

        //liste des catégories reçue depuis l'api
        ArrayList<Integer> liste_id_categ_tab;
        ArrayList<String> liste_categ_tab;
        int idFromSession;
        //récup du pseudo
        String pseudo;
        TextView titre_de_la_page;

        //pour l'url de l'api :
        Url_api_perso url_api;
        String debut_url_api = "http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters";
        String categ_x_user_url_api = "";
        String fin_url_api = ".txt";
        String url_api_str = "";
        String bout_url_date_1 = "_0";
        String bout_url_date_2 = "_1";
        String bout_url_date_3 = "_2";

        String keyHashMapPourDebug;
        String liste_categ_pour_debug="";



/*-----------------FIN VARIABLES GLOBALES--------------*/

    /*------------------ OnCreate ----------------*/
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_accueil_user_connected);

    /*----------Toolbar------------*/
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //gère le retour home
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*-----------Session -------*/
        session = new UserSessionManager(getApplicationContext());
        //vérif que la session existe (et donc que l'utilisateur est connecté)
        //si l'utilisateur n'est pas connecté, alors on le redirige vers la page d'accueil
        if (session.checkLogin()){
            finish();
        }
        //si l'user est connecté, on récupère les données de session :
        HashMap<String, Integer> userFromSession = session.getUserDetails();

        titre_de_la_page = findViewById(R.id.titre_de_la_page);

            //Parcours de la hashMap :
		/*une table est comme un ensemble de paires Key-Value. */
		String contenuHashMap="";
            Set <Map.Entry<String, Integer>> entrees = userFromSession.entrySet(); //'entrees' est un ensemble de paires key-value
            Iterator <Map.Entry<String, Integer>> iter = entrees.iterator(); // itérateur sur les paires
            while (iter.hasNext()) {
                Map.Entry<String, Integer> entree = (Map.Entry<String, Integer>)iter.next();
                String cle = entree.getKey();
                Integer val = entree.getValue();
               contenuHashMap += "cle : " + cle + " | val : "+ val + "\n";
                idFromSession = val;
                keyHashMapPourDebug = cle;
            }

            //Je vais utiliser la requête "RecupInfo_ModifProfilRequest" car elle renvoie la liste des id des catégories (entre autre)
    /*--- listener de la réponse de connexion ---*/
            //connexion à RecupInfo_pourModifProfil.php pour acces à la bdd
            //-----------------------------------------------------------
            /*final Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //'response' qui est en argument est la réponse de register.php
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        //si le traitement est un succes, alors on va aller sur la page Login
                        if(success){


                            //liste de toutes les categories
                            liste_id_categ_tab = new ArrayList<>();
                            JSONArray jsonArrayCateg = jsonResponse.getJSONArray("cat_pref_id");
                            for (int i = 0; i<jsonArrayCateg.length(); i++){
                                liste_id_categ_tab.add(jsonArrayCateg.getInt(i));
                            }

                            liste_categ_tab = new ArrayList<>();
                            JSONArray jsonArrayCateg_nom = jsonResponse.getJSONArray("cat_nom");
                            for (int i = 0; i<jsonArrayCateg_nom.length(); i++){
                                liste_categ_tab.add(jsonArrayCateg_nom.getString(i));
                            }

                            pseudo = jsonResponse.getString("user_pseudo");
                            titre_de_la_page.setText("Bienvenue " + pseudo + " ! ");

                            *//*----------------liste des actus -------------------*//*
                            //1) récupération des infos de l'user connecté depuis l profil
                            if (liste_id_categ_tab !=null){
                                if(liste_id_categ_tab.size()>0 ){
                                    for (int j = 0 ; j < liste_id_categ_tab.size(); j++){
                                        //Version avec connexion à l'API
                                        //categ_x_user_url_api += "_" + liste_id_categ_tab.get(j);

                                        //Version pour la connexion aux fichiers Json.txt que j'ai fait
                                        switch(liste_id_categ_tab.get(j)){
                                            case 31 :
                                                categ_x_user_url_api += "_31" ;
                                                break;
                                            case 32:
                                                categ_x_user_url_api += "_32" ;
                                                break;
                                            case 33:
                                                categ_x_user_url_api += "_33" ;
                                                break;
                                            case 35 :
                                                categ_x_user_url_api += "_35" ;
                                                break;
                                        }
                                    }
                                    url_api_str = debut_url_api + categ_x_user_url_api + fin_url_api;
                                }
                            }else{
                                url_api_str = debut_url_api + fin_url_api;
                            }




                            String url_api_test = "http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters.txt";

                            //connexion à l'API :
                            try {
                                new AccueilUserConnectedActivity.TacheChargerDonnes().execute(new URL(url_api_str)) ;

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }


                            //appel de l'adapter + initialisation
                            list_sortiesDuJour_arrayAdapter = new Article_ArrayAdapter(getApplicationContext(), listeSortiesDuJour);
                            //ciblage de la listView dans activity_main.xml
                            lV_sortiesDuJour = findViewById(R.id.liste1);
                            //attribution de l'arrayAdapter
                            lV_sortiesDuJour.setAdapter(list_sortiesDuJour_arrayAdapter);
                            //Appel de la methode de Utility qui permet d'afficher les listeView l'une en dessous de l'autre :
                            Utility.setDynamicHeight(lV_sortiesDuJour);

                            //ajout de l'évènement lorsqu'on clique sur une ligne de la liste :
                            lV_sortiesDuJour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //position de l'actu sélectionnée:
                                    Object o = lV_sortiesDuJour.getItemAtPosition(i);
                                    articleCourant = (Article) o;

                                    //envoi des donnees dans la nouvelle activity :
                                    Intent intent = new Intent(getApplicationContext(), DetailArticle.class);

                                    intent.putExtra("ActuEnvoyee", (Serializable) articleCourant);

                                    startActivity(intent);
                                }
                            });
            *//*--------------fin liste 1-----------------*//*


                        }else{
                            //aff_listeCteg_debug.setText(":(");
                            //sinon on affiche une alert qui propose de réessayer
                            AlertDialog.Builder builder = new AlertDialog.Builder(AccueilUserConnectedActivity.this);
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

*//*--------- envoie de la requête ------*//*

            RecupInfo_ModifProfilRequest recupInfo_modifProfilRequest = new RecupInfo_ModifProfilRequest(idFromSession, responseListener);
            RequestQueue queue = Volley.newRequestQueue(AccueilUserConnectedActivity.this);
            queue.add(recupInfo_modifProfilRequest);*/

            final CheckBox cb_tri_date_1 = findViewById(R.id.cb_tri_date_1);
            final CheckBox cb_tri_date_2 = findViewById(R.id.cb_tri_date_2);
            final CheckBox cb_tri_date_3 = findViewById(R.id.cb_tri_date_3);

            final Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            liste_id_categ_tab = new ArrayList<>();
                            JSONArray jsonArrayCateg = jsonResponse.getJSONArray("cat_pref_id");
                            for (int i = 0; i<jsonArrayCateg.length(); i++){
                                liste_id_categ_tab.add(jsonArrayCateg.getInt(i));
                            }
                            liste_categ_tab = new ArrayList<>();
                            JSONArray jsonArrayCateg_nom = jsonResponse.getJSONArray("cat_nom");
                            for (int i = 0; i<jsonArrayCateg_nom.length(); i++){
                                liste_categ_tab.add(jsonArrayCateg_nom.getString(i));
                            }
                            pseudo = jsonResponse.getString("user_pseudo");
                            titre_de_la_page.setText("Bienvenue " + pseudo + " ! ");
                            //getSupportActionBar().setTitle("Bienvenue, " + pseudo + " !");
                            if (liste_id_categ_tab !=null){
                                if(liste_id_categ_tab.size()>0 ){
                                    for (int j = 0 ; j < liste_id_categ_tab.size(); j++){
                                        switch(liste_id_categ_tab.get(j)){
                                            case 31 :
                                                categ_x_user_url_api += "_31" ;
                                                break;
                                            case 32:
                                                categ_x_user_url_api += "_32" ;
                                                break;
                                            case 33:
                                                categ_x_user_url_api += "_33" ;
                                                break;
                                            case 35 :
                                                categ_x_user_url_api += "_35" ;
                                                break;
                                        }
                                    }
                                    url_api_str = debut_url_api +bout_url_date_1 + categ_x_user_url_api + fin_url_api;
                                }
                            }else{
                                url_api_str = debut_url_api + bout_url_date_1;
                            }
                            try {
                                new AccueilUserConnectedActivity.TacheChargerDonnes().execute(new URL(url_api_str)) ;

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            list_sortiesDuJour_arrayAdapter = new Article_ArrayAdapter(getApplicationContext(), listeSortiesDuJour);
                            lV_sortiesDuJour = findViewById(R.id.liste1);
                            lV_sortiesDuJour.setAdapter(list_sortiesDuJour_arrayAdapter);
                            lV_sortiesDuJour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Object o = lV_sortiesDuJour.getItemAtPosition(i);
                                    articleCourant = (Article) o;
                                    Intent intent = new Intent(getApplicationContext(), DetailArticle.class);
                                    intent.putExtra("ActuEnvoyee", (Serializable) articleCourant);
                                    startActivity(intent);
                                }
                            });
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(AccueilUserConnectedActivity.this);
                            builder.setMessage("oups")
                                    .setNegativeButton("retry", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//end onresponse
            };
            RecupInfo_ModifProfilRequest recupInfo_modifProfilRequest = new RecupInfo_ModifProfilRequest(idFromSession, responseListener);
            RequestQueue queue = Volley.newRequestQueue(AccueilUserConnectedActivity.this);
            queue.add(recupInfo_modifProfilRequest);

            cb_tri_date_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (cb_tri_date_1.isChecked()){
                        cb_tri_date_2.setChecked(false);
                        cb_tri_date_3.setChecked(false);

                        final Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if(success){
                                        liste_id_categ_tab = new ArrayList<>();
                                        JSONArray jsonArrayCateg = jsonResponse.getJSONArray("cat_pref_id");
                                        for (int i = 0; i<jsonArrayCateg.length(); i++){
                                            liste_id_categ_tab.add(jsonArrayCateg.getInt(i));
                                        }
                                        liste_categ_tab = new ArrayList<>();
                                        JSONArray jsonArrayCateg_nom = jsonResponse.getJSONArray("cat_nom");
                                        for (int i = 0; i<jsonArrayCateg_nom.length(); i++){
                                            liste_categ_tab.add(jsonArrayCateg_nom.getString(i));
                                        }
                                        pseudo = jsonResponse.getString("user_pseudo");
                                        titre_de_la_page.setText("Bienvenue " + pseudo + " ! ");
                                        categ_x_user_url_api = "";
                                        url_api_str="";

                                        if (liste_id_categ_tab !=null){
                                            if(liste_id_categ_tab.size()>0 ){
                                                for (int j = 0 ; j < liste_id_categ_tab.size(); j++){
                                                    switch(liste_id_categ_tab.get(j)){
                                                        case 31 :
                                                            categ_x_user_url_api += "_31" ;
                                                            break;
                                                        case 32:
                                                            categ_x_user_url_api += "_32" ;
                                                            break;
                                                        case 33:
                                                            categ_x_user_url_api += "_33" ;
                                                            break;
                                                        case 35 :
                                                            categ_x_user_url_api += "_35" ;
                                                            break;
                                                    }
                                                }
                                                url_api_str = debut_url_api +bout_url_date_1 + categ_x_user_url_api + fin_url_api;
                                            }
                                        }else{
                                            url_api_str = debut_url_api + bout_url_date_1;
                                        }
                                        try {
                                            new AccueilUserConnectedActivity.TacheChargerDonnes().execute(new URL(url_api_str)) ;

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        list_sortiesDuJour_arrayAdapter = new Article_ArrayAdapter(getApplicationContext(), listeSortiesDuJour);
                                        lV_sortiesDuJour = findViewById(R.id.liste1);
                                        lV_sortiesDuJour.setAdapter(list_sortiesDuJour_arrayAdapter);
                                        lV_sortiesDuJour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Object o = lV_sortiesDuJour.getItemAtPosition(i);
                                                articleCourant = (Article) o;
                                                Intent intent = new Intent(getApplicationContext(), DetailArticle.class);
                                                intent.putExtra("ActuEnvoyee", (Serializable) articleCourant);
                                                startActivity(intent);
                                            }
                                        });
                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AccueilUserConnectedActivity.this);
                                        builder.setMessage("oups")
                                                .setNegativeButton("retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }//end onresponse
                        };
                        RecupInfo_ModifProfilRequest recupInfo_modifProfilRequest = new RecupInfo_ModifProfilRequest(idFromSession, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AccueilUserConnectedActivity.this);
                        queue.add(recupInfo_modifProfilRequest);
                    }
                }
            });

            cb_tri_date_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (cb_tri_date_2.isChecked()){
                        cb_tri_date_1.setChecked(false);
                        cb_tri_date_3.setChecked(false);
                        final Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if(success){
                                        liste_id_categ_tab = new ArrayList<>();
                                        JSONArray jsonArrayCateg = jsonResponse.getJSONArray("cat_pref_id");
                                        for (int i = 0; i<jsonArrayCateg.length(); i++){
                                            liste_id_categ_tab.add(jsonArrayCateg.getInt(i));
                                        }
                                        liste_categ_tab = new ArrayList<>();
                                        JSONArray jsonArrayCateg_nom = jsonResponse.getJSONArray("cat_nom");
                                        for (int i = 0; i<jsonArrayCateg_nom.length(); i++){
                                            liste_categ_tab.add(jsonArrayCateg_nom.getString(i));
                                        }
                                        pseudo = jsonResponse.getString("user_pseudo");
                                        titre_de_la_page.setText("Bienvenue " + pseudo + " ! ");

                                        categ_x_user_url_api = "";
                                        url_api_str="";
                                        if (liste_id_categ_tab !=null){
                                            if(liste_id_categ_tab.size()>0 ){
                                                for (int j = 0 ; j < liste_id_categ_tab.size(); j++){
                                                    switch(liste_id_categ_tab.get(j)){
                                                        case 31 :
                                                            categ_x_user_url_api += "_31" ;
                                                            break;
                                                        case 32:
                                                            categ_x_user_url_api += "_32" ;
                                                            break;
                                                        case 33:
                                                            categ_x_user_url_api += "_33" ;
                                                            break;
                                                        case 35 :
                                                            categ_x_user_url_api += "_35" ;
                                                            break;
                                                    }
                                                }
                                                url_api_str = debut_url_api +bout_url_date_2 + categ_x_user_url_api+fin_url_api;

                                            }
                                        }else{
                                            url_api_str = debut_url_api + bout_url_date_2;
                                        }
                                        try {
                                            new AccueilUserConnectedActivity.TacheChargerDonnes().execute(new URL(url_api_str)) ;

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        list_sortiesDuJour_arrayAdapter = new Article_ArrayAdapter(getApplicationContext(), listeSortiesDuJour);
                                        lV_sortiesDuJour = findViewById(R.id.liste1);
                                        lV_sortiesDuJour.setAdapter(list_sortiesDuJour_arrayAdapter);
                                        lV_sortiesDuJour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Object o = lV_sortiesDuJour.getItemAtPosition(i);
                                                articleCourant = (Article) o;
                                                Intent intent = new Intent(getApplicationContext(), DetailArticle.class);
                                                intent.putExtra("ActuEnvoyee", (Serializable) articleCourant);
                                                startActivity(intent);
                                            }
                                        });
                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AccueilUserConnectedActivity.this);
                                        builder.setMessage("oups")
                                                .setNegativeButton("retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }//end onresponse
                        };
                        RecupInfo_ModifProfilRequest recupInfo_modifProfilRequest = new RecupInfo_ModifProfilRequest(idFromSession, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AccueilUserConnectedActivity.this);
                        queue.add(recupInfo_modifProfilRequest);
                    }
                }
            });

            cb_tri_date_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (cb_tri_date_3.isChecked()){
                        cb_tri_date_1.setChecked(false);
                        cb_tri_date_2.setChecked(false);

                        final Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if(success){
                                        liste_id_categ_tab = new ArrayList<>();
                                        JSONArray jsonArrayCateg = jsonResponse.getJSONArray("cat_pref_id");
                                        for (int i = 0; i<jsonArrayCateg.length(); i++){
                                            liste_id_categ_tab.add(jsonArrayCateg.getInt(i));
                                        }
                                        liste_categ_tab = new ArrayList<>();
                                        JSONArray jsonArrayCateg_nom = jsonResponse.getJSONArray("cat_nom");
                                        for (int i = 0; i<jsonArrayCateg_nom.length(); i++){
                                            liste_categ_tab.add(jsonArrayCateg_nom.getString(i));
                                        }
                                        pseudo = jsonResponse.getString("user_pseudo");
                                        titre_de_la_page.setText("Bienvenue " + pseudo + " ! ");
                                        categ_x_user_url_api = "";
                                        url_api_str="";
                                        if (liste_id_categ_tab !=null){
                                            if(liste_id_categ_tab.size()>0 ){
                                                for (int j = 0 ; j < liste_id_categ_tab.size(); j++){
                                                    switch(liste_id_categ_tab.get(j)){
                                                        case 31 :
                                                            categ_x_user_url_api += "_31" ;
                                                            break;
                                                        case 32:
                                                            categ_x_user_url_api += "_32" ;
                                                            break;
                                                        case 33:
                                                            categ_x_user_url_api += "_33" ;
                                                            break;
                                                        case 35 :
                                                            categ_x_user_url_api += "_35" ;
                                                            break;
                                                    }
                                                }
                                                url_api_str = debut_url_api +bout_url_date_3 + categ_x_user_url_api+fin_url_api;
                                            }
                                        }else{
                                            url_api_str = debut_url_api + bout_url_date_3;
                                        }
                                        try {
                                            new AccueilUserConnectedActivity.TacheChargerDonnes().execute(new URL(url_api_str)) ;

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        list_sortiesDuJour_arrayAdapter = new Article_ArrayAdapter(getApplicationContext(), listeSortiesDuJour);
                                        lV_sortiesDuJour = findViewById(R.id.liste1);
                                        lV_sortiesDuJour.setAdapter(list_sortiesDuJour_arrayAdapter);
                                        lV_sortiesDuJour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Object o = lV_sortiesDuJour.getItemAtPosition(i);
                                                articleCourant = (Article) o;
                                                Intent intent = new Intent(getApplicationContext(), DetailArticle.class);
                                                intent.putExtra("ActuEnvoyee", (Serializable) articleCourant);
                                                startActivity(intent);
                                            }
                                        });
                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AccueilUserConnectedActivity.this);
                                        builder.setMessage("oups")
                                                .setNegativeButton("retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }//end onresponse
                        };
                        RecupInfo_ModifProfilRequest recupInfo_modifProfilRequest = new RecupInfo_ModifProfilRequest(idFromSession, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AccueilUserConnectedActivity.this);
                        queue.add(recupInfo_modifProfilRequest);
                    }
                }
            });





        }
/*------------end onCreate--------------*/

    /*----------Menu dans toolbar -------------*/
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
                //intent = new Intent(getApplicationContext(), ConnexionActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
/*---------------fin menu dans toolbar -----------*/




    /*---------------appel du traitement des données venant de l'API------------------*/
    private class TacheChargerDonnes extends AsyncTask<URL, Void ,JSONArray> {

        @Override
        protected JSONArray doInBackground(URL... urls) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK){
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    strData = builder.toString();
                    return new JSONArray(strData);
                }else{
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                connection.disconnect();
            }

            return null;
        }



        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            //super.onPostExecute(jsonArray);
            Utility.convertirJSONEnArrayList(jsonArray, strData, listeSortiesDuJour, list_sortiesDuJour_arrayAdapter);
            list_sortiesDuJour_arrayAdapter.notifyDataSetChanged();

        }
    } //tachechargedonnees

    /*--------- gestion des données récupérées depuis l'API format : JSON ------*/


/*------fin gestion des données récupérées depuis l'API format : JSON -----*/
}
