package dominique.fr.myapplikejv;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import modeles.User;
import requests.LoginRequest;
import requests.RecupEmailRequest;
import requests.RecupPseudoRequest;
import requests.RegisterRequest;
import utilitaires.UserSessionManager;
import utilitaires.Utility;

public class ConnexionActivity extends AppCompatActivity {

    //toolbar
    private Toolbar toolbar;



    //préparation des variables pour l'affichage des fragments :
    private Fragment fragment;
    private Fragment fragment2;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    //récupèrera les données de l'utilisateur qui s'inscrit
    private User new_user = new User();
    //récupèrera la liste des checkboxes cochées
    private ArrayList<String> listUserPreferences = new ArrayList<>();
    private ArrayList<Integer> listPreferencesId = new ArrayList<>();

    //Session
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*---------- Session --------------------- */
        session = new UserSessionManager(getApplicationContext());


           /*affichage du fragment de connexion quand on arrive sur cette activity :*/
        //On attribut à fragment le type Fragment_1
        fragment = (Fragment)new FragmentConnexion();
        //on créé le FragmentManager qui va permettre de commencer une transaction
        fragmentManager = getSupportFragmentManager();
        //on créé la transaction
        fragmentTransaction = fragmentManager.beginTransaction();
        //on remplace dans la vue :
        //fragmentTransaction.replace (R.id.idDuTrucARemplacer , fragment défini ici)
        fragmentTransaction.replace(R.id.fragment_place, fragment);
        // /!\ Ne pas oublier de commit la transaction !!! (sinon, ça ne marche pas :p )
        fragmentTransaction.commit();

        //--------Fin Session-----------


    }



    //Méthode qui permet de faire apparaitre le fragment pour se connecter ou pour s'inscrire.
    public void goToFragment(View view) {

        //buttons :
        Button se_connecter = (Button)findViewById(R.id.se_connecter);
        Button s_inscrire = findViewById(R.id.s_inscrire);

        if (view == findViewById(R.id.se_connecter)){
            //clic sur le 'bouton' se connecter
            se_connecter.setTextSize(18);
            se_connecter.setTextColor(getResources().getColor(R.color.bleu_a));
            s_inscrire.setTextSize(13);
            s_inscrire.setTextColor(getResources().getColor(R.color.txt_sur_fond_clair));

            fragment = (Fragment)new FragmentConnexion();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fragment);
            fragmentTransaction.commit();
        }
        else if(view == findViewById(R.id.s_inscrire)){
            //Clic sur le 'bouton' s'inscrire
            s_inscrire.setTextSize(18);
            s_inscrire.setTextColor(getResources().getColor(R.color.bleu_a));
            se_connecter.setTextSize(13);
            se_connecter.setTextColor(getResources().getColor(R.color.txt_sur_fond_clair));
            fragment = (Fragment)new Fragment_Inscription_etape1();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, fragment);
            fragmentTransaction.commit();
        }

    }

    //Methode pour envoyer le pseudo et le mot de passe pour se connecter.
    public void logIn(View view){

        EditText et_pseudo = findViewById(R.id.editText_pseudo);
        EditText et_mdp = findViewById(R.id.editText_mdp);

        final String pseudo_entre = et_pseudo.getText().toString();
        final String mdp_entre = et_mdp.getText().toString();

        /*---------- Session --------------------- */
        session = new UserSessionManager(getApplicationContext());
        //--------Fin Session-----------
        //final Integer test = 1;
        //connexion à login.php pour acces à la bdd
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //'response' qui est en argument est la réponse de register.php
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    //si le traitement est un succes, alors on va aller sur la page Login
                    if(success){

                        User user_connecte = new User();
                        //get the detail from the json response and send it to the view
                        //id
                        Integer user_id = jsonResponse.getInt("user_id");

                        /*---------- Session ----------*/
                        session.createUserLoginSession(user_id);

                        /*---------- Fin session --------*/

                        //envoi vers la page de profil
                        Intent intent = new Intent(ConnexionActivity.this, ProfilActivity.class);
                        ConnexionActivity.this.startActivity(intent);
                    }else{
                        //sinon on affiche une alert qui propose de réessayer
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                        builder.setMessage("connexion échouée")
                                .setNegativeButton("retenter", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        //création du loginRequest
        LoginRequest loginRequest = new LoginRequest(pseudo_entre, mdp_entre, responseListener);
        //add to the queue
        RequestQueue queue = Volley.newRequestQueue(ConnexionActivity.this);
        queue.add(loginRequest);

    }

    //methode pour passer d'un fragment inscription à l'autre
    //methodes NEXT :
    //étape1 vers étape2 (etape 1 = pseudo)
    public void goStep2(View view) {
        //Toast.makeText(this, "Clic depuis connectionActivity", Toast.LENGTH_LONG).show();
        EditText et_new_pseudo = findViewById(R.id.editText_new_Pseudo);
        final String new_pseudo = et_new_pseudo.getText().toString();

        if(new_pseudo.equals("")){ //si pseudo vide
            Toast.makeText(ConnexionActivity.this, "Merci de rentrer un pseudo", Toast.LENGTH_LONG).show();
        }else {
            final Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) { //si success alors le pseudo existe déja en bdd
                            Toast.makeText(ConnexionActivity.this,
                                    R.string.txt_info_pseudo_deja_pris,
                                    Toast.LENGTH_LONG).show();
                        } else { //on attribut le pseudo à new_user et on passe à l'étape suivante
                            new_user.setPseudo(new_pseudo);
                            fragment = (Fragment) new Fragment_Inscription_etape2();
                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_place, fragment);
                            fragmentTransaction.commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //création du loginRequest
            RecupPseudoRequest recupPseudoRequest = new RecupPseudoRequest(new_pseudo, responseListener);
            //add to the queue
            RequestQueue queue = Volley.newRequestQueue(ConnexionActivity.this);
            queue.add(recupPseudoRequest);
        }
    }//end goStep2

    //étape2 vers étape3 (etape 2 = email)
    public void goStep3(View view) {

        EditText et_new_email = findViewById(R.id.editText_new_email);
        EditText et_verif_new_email = findViewById(R.id.editText_verif_new_email);
        final String new_email = et_new_email.getText().toString();
        final String verif_new_mail = et_verif_new_email.getText().toString();

        //vérification de la conformité de l'e-mail : 1) les 2 champs doivent être identique/2) L'email doit être sous format d'email
        if(!new_email.equals(verif_new_mail)){
            Toast.makeText(this, R.string.txt_info_egalite_2_email, Toast.LENGTH_LONG).show();
        }else{
            if (!Utility.isValidEmailAddress(new_email)){
                Toast.makeText(this, R.string.txt_info_demande_verif_email, Toast.LENGTH_LONG).show();
            }else {
                //vérif que l'e-mail n'est pas déjà enregistré dans la bdd :
                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                Toast.makeText(ConnexionActivity.this,
                                        R.string.txt_info_email_existe_se_connecter,
                                        Toast.LENGTH_LONG).show();
                            }else{
                                new_user.setEmail(new_email);
                                fragment = (Fragment)new Fragment_Inscription_etape3();
                                fragmentManager = getSupportFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_place, fragment);
                                fragmentTransaction.commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //création du loginRequest
                RecupEmailRequest recupEmailRequest = new RecupEmailRequest(new_email, responseListener);
                //add to the queue
                RequestQueue queue = Volley.newRequestQueue(ConnexionActivity.this);
                queue.add(recupEmailRequest);
            }//end verif email valid
        }//end verif les 2 mails sont les memes
    }//end goStep3

    //étape3 vers étape4 (etape 3 = mdp)
    public void goStep4(View view) {

        EditText et_new_mdp = findViewById(R.id.editText_new_mdp);
        EditText et_verif_new_mdp = findViewById(R.id.editText_verif_new_mdp);

        String new_mdp = et_new_mdp.getText().toString();
        String verif_new_mdp = et_verif_new_mdp.getText().toString();

        //vérfication de la conformité du mot de passe 1) les 2 champs doivent être identique /2) le mot de passe doit être sous le format décidé.
        if(!new_mdp.equals(verif_new_mdp)){
            Toast.makeText(this, R.string.txt_info_egalite_2_mdp, Toast.LENGTH_LONG).show();
        }else{
            if (!Utility.isValidPassword(new_mdp)){
                Toast.makeText(this, R.string.txt_info_format_mdp, Toast.LENGTH_LONG).show();
            }else{
                new_user.setMotDePasse(new_mdp);
                fragment = (Fragment)new Fragment_Inscription_etape4();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fragment);
                fragmentTransaction.commit();
            }
        }
    }

    //étape 4 vers validation de l'inscription. (redirection vers profil ? vers page accueil ? vers page connection ?)
    public void validateRegister(View view) {

        CheckBox categorie_1 = findViewById(R.id.categ_1);
        if (categorie_1.isChecked()){
            listUserPreferences.add(categorie_1.getText().toString());
            listPreferencesId.add(0,31); // listPreferencesId.get(0) == 31 -> c'est l'id de la catégorie
        }else{
            listPreferencesId.add(0,0); //listPreferencesId.get(0) == 0 -> categ non choisie
        }
        CheckBox categorie_2 = findViewById(R.id.categ_2);
        if (categorie_2.isChecked()){
            listUserPreferences.add(categorie_2.getText().toString());
            listPreferencesId.add(1,32);
        }else{
            listPreferencesId.add(1,0);
        }
        CheckBox categorie_3 = findViewById(R.id.categ_3);
        if (categorie_3.isChecked()){
            listUserPreferences.add(categorie_3.getText().toString());
            listPreferencesId.add(2,33);
        }else{
            listPreferencesId.add(2,0);
        }
        CheckBox categorie_4 = findViewById(R.id.categ_4);
        if (categorie_4.isChecked()){
            listUserPreferences.add(categorie_4.getText().toString());
            listPreferencesId.add(3,35);
        }else{
            listPreferencesId.add(3,0);
        }

         /*-------vérif qu'au moins 1 categorie est cochée ------*/
        if (listPreferencesId.get(0) == 0 && listPreferencesId.get(1) == 0 && listPreferencesId.get(2) == 0 && listPreferencesId.get(3) == 0) {
            Toast.makeText(getApplicationContext(), R.string.txt_info_au_moins_1_categ, Toast.LENGTH_LONG).show();
        }//end if (les 4 categ sont décochées)
        else{
            //Connexion et envoi des données à la bdd :
            //Les données user_pseudo, user_email et user_mdp sont déjà dans new_user
            final String user_pseudo = new_user.getPseudo();
            final String user_email = new_user.getEmail();
            final String user_mdp = new_user.getMotDePasse();
            final Integer user_pref_1 = listPreferencesId.get(0);
            final Integer user_pref_2 = listPreferencesId.get(1);
            final Integer user_pref_3 = listPreferencesId.get(2);
            final Integer user_pref_4 = listPreferencesId.get(3);

            //Listener de réponse :
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                /*'response' qui est en argument est la réponse de register.php.
                On doit convertir cette réponse en objet JSON car dans register.php la réponse est en JSON (ce sera entouré d'un try catch)
                 */
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        //dans register.php, il y a une réponse "success" = true. On va la récupérer
                        boolean success = jsonResponse.getBoolean("success");
                        //si le traitement est un succes, alors on va aller sur la page Login
                        if(success){
                            Intent intent = new Intent(ConnexionActivity.this, ConnexionActivity.class);
                            ConnexionActivity.this.startActivity(intent);
                        }else{
                            //sinon on affiche une alert qui propose de réessayer
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                            builder.setMessage("register failed")
                                    .setNegativeButton("retry", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //creer la request
            RegisterRequest registerRequest = new RegisterRequest(user_pseudo, user_email, user_mdp, user_pref_1, user_pref_2, user_pref_3,user_pref_4, responseListener);
            //on créé une RequestQueue à laquelle on va .add notre registerRequest
            RequestQueue queue = Volley.newRequestQueue(ConnexionActivity.this);
            queue.add(registerRequest);
            // /!\ Comme la bdd est sur le net, on doit ajouter une autorisation dans le manifest
        }
    } // end validateRegister()


    //------- Methodes PREVIOUS ------

    //étape2 vers étape1
    public void goPreviousFromStep2(View view) {

        fragment = (Fragment)new Fragment_Inscription_etape1();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fragment);
        fragmentTransaction.commit();

    }
    //étape3 vers étape2
    public void goPreviousFromStep3(View view) {

        fragment = (Fragment)new Fragment_Inscription_etape2();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fragment);
        fragmentTransaction.commit();


    }
    //étape4 vers étape3
    public void goPreviousFromStep4(View view) {

        fragment = (Fragment)new Fragment_Inscription_etape3();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fragment);
        fragmentTransaction.commit();

    }


    //au clic sur une checkbox :
    View.OnClickListener getOnClickDoSomething(final Button button){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String message = "Chekbox id = "+ button.getId();
                Toast.makeText(ConnexionActivity.this, message, Toast.LENGTH_LONG).show();
            }
        };
    }

    /*----- Menu dans toolbar ---------*/
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
    //--------fin toolbar--------

}
