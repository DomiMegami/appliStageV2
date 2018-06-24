package dominique.fr.myapplikejv;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import adapters.Article_ArrayAdapter;
import modeles.Article;
import modeles.Categorie;
import utilitaires.UserSessionManager;
import utilitaires.Utility;

public class MainActivity extends AppCompatActivity {

    /*---------------VARIABLES GLOBALES-------------------*/

    Toolbar toolbar;

    //session
    UserSessionManager session ;

    //préparation des variables pour l'affichage des fragments :
    private Fragment fragment;
    private Fragment fragment2;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    //Article qui sera sélectionné dans l'une ou l'autre des listes :
    Article articleCourant;

    //var pour liste des sorties du jour
    //la liste
    private ArrayList<Article> listeSortiesDuJour = new ArrayList<Article>();
    //la listView
    private ListView lV_sortiesDuJour;
    //l'adapter
    private Article_ArrayAdapter list_sortiesDuJour_arrayAdapter; //adapter pour type Article
    //strData -> nom du tableau json qu'on récupère depuis l'API. Ici il n'a pas de nom => null
    private String strData = null;

    //var pour la gestion des boutons aujourd'hui, Dans la semaine et plus tard
    private LinearLayout ll_ce_jour;
    private LinearLayout ll_dans_la_semaine;
    private LinearLayout ll_plus_tard;
    private Button btn_ce_jour;
    private Button btn_dans_la_semaine;
    private Button btn_plus_tard;
    private Button btn_ligne;
    private Button btn_ligne_gauche;
    private Button btn_ligne_milieu;
    private Button btn_ligne_droite;

    private String url_api = "";
    private String btn_ligne_x_str;
    private String btn_ligne_y_str;
    private Float btn_ligne_x;
    private Float btn_ligne_y;
   //booleen pour boutons sélectionné
    private Boolean aujourdhui_checked = false;
    private Boolean semaine_checked = false;
    private Boolean plusTard_checked = false;
/*-----------------FIN VARIABLES GLOBALES--------------*/



    /*------------------ OnCreate ----------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*-----------Session -------*/
        session = new UserSessionManager(getApplicationContext());

        /*----------Toolbar---------*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //vérif que la session existe (et donc que l'utilisateur est connecté)
        //si l'utilisateur n'est pas connecté, alors on le redirige vers la page d'accueil
        if (session.isUserLoggedIn()){
            startActivity(new Intent(getApplicationContext(), AccueilUserConnectedActivity.class));
            //Toast.makeText(getApplicationContext(), "utilisateur non connecté", Toast.LENGTH_LONG).show();
        }

        /*affichage du fragment de connexion quand on arrive sur cette activity :*/
        //NE FONCTIONNE PAS - DD - 20/02/2018
        /*//On attribut à fragment le type Fragment_1
        fragment = (Fragment)new Fragment_sortiesDuJour();
        //on créé le FragmentManager qui va permettre de commencer une transaction
        fragmentManager = getSupportFragmentManager();
        //on créé la transaction
        fragmentTransaction = fragmentManager.beginTransaction();
        //on remplace dans la vue :
        //fragmentTransaction.replace (R.id.idDuTrucARemplacer , fragment défini ici)
        fragmentTransaction.replace(R.id.place_liste_sorties_du_jour, fragment);
        // /!\ Ne pas oublier de commit la transaction !!! (sinon, ça ne marche pas :p )
        fragmentTransaction.commit();*/


/*-------------boutons filtres par période --------------*/
        //vérification de la taille de l'écran en dpi :
        //Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final Float widthPixels = Float.valueOf(metrics.widthPixels);
        final String widthPixels_str = String.valueOf(widthPixels);

        //ciblage des boutons
        btn_ce_jour = findViewById(R.id.btn_ce_jour);
        btn_dans_la_semaine = findViewById(R.id.btn_dans_la_semaine);
        btn_plus_tard = findViewById(R.id.btn_plus_tard);
        btn_ligne_gauche = findViewById(R.id.btn_ligne_gauche);
        btn_ligne_milieu = findViewById(R.id.btn_ligne_milieu);
        btn_ligne_droite = findViewById(R.id.btn_ligne_droite);

        /*essai d'animation translate d'une ligne sous les boutons
        //Ciblage du bouton qui contient la ligne, et attribution de la taille 1/3 de l'écran.
        btn_ligne = findViewById(R.id.btn_ligne);
        int width = getResources().getDisplayMetrics().widthPixels/3;
        int hei=8;
        btn_ligne.setLayoutParams(new LinearLayout.LayoutParams(width,hei));


        //définition des 3 "x" de l'écran : 0 ; 1/3 de l'écran ; 2/3 de l'écran
        final float x_start = 0;
        final float x_middle = widthPixels/3;
        final float x_end = 2*widthPixels/3;
*/




        btn_ce_jour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(widthPixels >= 1200){
                    //Toast.makeText(MainActivity.this, "écran supérieur à 600 dpi" + widthPixels_str, Toast.LENGTH_SHORT).show();
                    btn_dans_la_semaine.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_dans_la_semaine.setTextColor(getResources().getColor(R.color.noir));
                    btn_dans_la_semaine.setTextSize(15);

                    btn_plus_tard.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_plus_tard.setTextColor(getResources().getColor(R.color.noir));
                    btn_plus_tard.setTextSize(15);

                    btn_ce_jour.setBackgroundColor(getResources().getColor(R.color.blanc));
                    btn_ce_jour.setTextColor(getResources().getColor(R.color.bleu_a));
                    btn_ce_jour.setTextSize(22);
                }else{
                    //Toast.makeText(MainActivity.this, widthPixels_str , Toast.LENGTH_SHORT).show();
                    btn_dans_la_semaine.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_dans_la_semaine.setTextColor(getResources().getColor(R.color.noir));
                    btn_dans_la_semaine.setTextSize(10);

                    btn_plus_tard.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_plus_tard.setTextColor(getResources().getColor(R.color.noir));
                    btn_plus_tard.setTextSize(10);

                    btn_ce_jour.setBackgroundColor(getResources().getColor(R.color.blanc));
                    btn_ce_jour.setTextColor(getResources().getColor(R.color.bleu_a));
                    btn_ce_jour.setTextSize(15);

                }

                btn_ligne_gauche = findViewById(R.id.btn_ligne_gauche);
                btn_ligne_milieu = findViewById(R.id.btn_ligne_milieu);
                btn_ligne_droite = findViewById(R.id.btn_ligne_droite);
                btn_ligne_gauche.setVisibility(View.VISIBLE);
                btn_ligne_milieu.setVisibility(View.INVISIBLE);
                btn_ligne_droite.setVisibility(View.INVISIBLE);


                /*-------animation de la ligne  btn_ce_jour-------*/
                /*
                //btn_ligne_x = btn_ligne.getX();
                btn_ligne_y = btn_ligne.getY();
                if (semaine_checked){
                    //btn_ligne.setX(x_middle);
                    translate_btn_ligne(btn_ligne, x_middle, x_start, btn_ligne_y, btn_ligne_y);
                    semaine_checked=false;
                }else if(plusTard_checked){
                    //btn_ligne.setX(x_end);
                    translate_btn_ligne(btn_ligne, x_end, x_start, btn_ligne_y, btn_ligne_y);
                    plusTard_checked = false;
                }
                aujourdhui_checked=true;


                Toast.makeText(getApplicationContext(), "aujourd'hui - x avant translate : "+String.valueOf(btn_ligne.getX()), Toast.LENGTH_LONG).show();
                */
               /* //si sous btn_semaine
                if(btn_ligne_x.equals(x_middle)){
                    translate_btn_ligne(btn_ligne, x_middle, x_start, btn_ligne_y, btn_ligne_y);
                    Toast.makeText(getApplicationContext(), "x_middle -> x_start, new_x : "+String.valueOf(btn_ligne.getX()), Toast.LENGTH_LONG).show();
                //si sous btn_+tard
                }else if(btn_ligne_x.equals(x_end)){
                    translate_btn_ligne(btn_ligne, x_end, x_start, btn_ligne_y, btn_ligne_y);
                    Toast.makeText(getApplicationContext(), "x_end -> x_start, new_x : "+String.valueOf(btn_ligne.getX()), Toast.LENGTH_LONG).show();
                }*/



                /*--------fin animation de la ligne -------*/

                //connexion à l'API :
                try {
                    new TacheChargerDonnees().execute(new URL("http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters_0.txt")) ;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                //appel de l'adapter + initialisation
                list_sortiesDuJour_arrayAdapter = new Article_ArrayAdapter(getApplicationContext(), listeSortiesDuJour);
                //ciblage de la listView dans activity_main.xml
                lV_sortiesDuJour = findViewById(R.id.liste1);

                //Appel de la methode de Utility qui permet d'afficher les listeView l'une en dessous de l'autre :
                Utility.setDynamicHeight(lV_sortiesDuJour);
                //attribution de l'arrayAdapter
                lV_sortiesDuJour.setAdapter(list_sortiesDuJour_arrayAdapter);


                //ajout de l'évènement lorsqu'on clique sur une ligne de la liste :
                lV_sortiesDuJour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //position de l'actu sélectionnée:
                        Object o = lV_sortiesDuJour.getItemAtPosition(i);
                        articleCourant = (Article) o;

                        //envoi des donnees dans la nouvelle activity :
                        Intent intent = new Intent(MainActivity.this, DetailArticle.class);

                        intent.putExtra("ActuEnvoyee", (Serializable) articleCourant);

                        startActivity(intent);
                    }
                });

            }
        });

        btn_dans_la_semaine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(widthPixels >= 1200){
                    //Toast.makeText(MainActivity.this, "écran supérieur à 600 dpi" + widthPixels_str, Toast.LENGTH_SHORT).show();
                    btn_ce_jour.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_ce_jour.setTextColor(getResources().getColor(R.color.noir));
                    btn_ce_jour.setTextSize(15);

                    btn_plus_tard.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_plus_tard.setTextColor(getResources().getColor(R.color.noir));
                    btn_plus_tard.setTextSize(15);

                    btn_dans_la_semaine.setBackgroundColor(getResources().getColor(R.color.blanc));
                    btn_dans_la_semaine.setTextColor(getResources().getColor(R.color.bleu_a));
                    btn_dans_la_semaine.setTextSize(22);
                }else{
                    //Toast.makeText(MainActivity.this, widthPixels_str , Toast.LENGTH_SHORT).show();
                    btn_ce_jour.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_ce_jour.setTextColor(getResources().getColor(R.color.noir));
                    btn_ce_jour.setTextSize(10);

                    btn_plus_tard.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_plus_tard.setTextColor(getResources().getColor(R.color.noir));
                    btn_plus_tard.setTextSize(10);

                    btn_dans_la_semaine.setBackgroundColor(getResources().getColor(R.color.blanc));
                    btn_dans_la_semaine.setTextColor(getResources().getColor(R.color.bleu_a));
                    btn_dans_la_semaine.setTextSize(15);

                }

                /*-------animation de la ligne btn_semaine-------*/
                btn_ligne_gauche = findViewById(R.id.btn_ligne_gauche);
                btn_ligne_milieu = findViewById(R.id.btn_ligne_milieu);
                btn_ligne_droite = findViewById(R.id.btn_ligne_droite);
                btn_ligne_gauche.setVisibility(View.INVISIBLE);
                btn_ligne_milieu.setVisibility(View.VISIBLE);
                btn_ligne_droite.setVisibility(View.INVISIBLE);
                /*
                //btn_ligne_x = btn_ligne.getX();
                btn_ligne_y = btn_ligne.getY();
                if (aujourdhui_checked){
                    //btn_ligne.setX(x_middle);
                    translate_btn_ligne(btn_ligne, x_middle, x_start, btn_ligne_y, btn_ligne_y);
                    aujourdhui_checked=false;
                }else if(plusTard_checked){
                    //btn_ligne.setX(x_end);
                    translate_btn_ligne(btn_ligne, x_end, x_start, btn_ligne_y, btn_ligne_y);
                    plusTard_checked = false;
                }
                semaine_checked=true;

                Toast.makeText(getApplicationContext(), "semaine - x avant translate : "+String.valueOf(btn_ligne.getX()), Toast.LENGTH_LONG).show();
                */
                /*//si sous btn_aujourdhui
                if(btn_ligne_x.equals(x_start)){
                   translate_btn_ligne(btn_ligne, x_start, x_middle, btn_ligne_y, btn_ligne_y);
                    Toast.makeText(getApplicationContext(), "x_start -> x_middle, new_x : "+String.valueOf(btn_ligne.getX()), Toast.LENGTH_LONG).show();
                //si sous btn_+tard
                }else if(btn_ligne_x.equals(x_end)){
                    translate_btn_ligne(btn_ligne, x_end, x_middle, btn_ligne_y, btn_ligne_y);
                    Toast.makeText(getApplicationContext(), "x_end -> x_middle, new_x : "+String.valueOf(btn_ligne.getX()), Toast.LENGTH_LONG).show();
                }*/

                /*--------fin animation de la ligne -------*/



                //connexion à l'API :
                try {

                    //new TacheChargerDonnees().execute(new URL("http://77.141.122.110/api/public/eventsbyfilters/31/32/33/35")) ;
                    new TacheChargerDonnees().execute(new URL("http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters_1.txt")) ;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                //appel de l'adapter + initialisation
                list_sortiesDuJour_arrayAdapter = new Article_ArrayAdapter(getApplicationContext(), listeSortiesDuJour);
                //ciblage de la listView dans activity_main.xml
                lV_sortiesDuJour = findViewById(R.id.liste1);

                //Appel de la methode de Utility qui permet d'afficher les listeView l'une en dessous de l'autre :
                Utility.setDynamicHeight(lV_sortiesDuJour);
                //attribution de l'arrayAdapter
                lV_sortiesDuJour.setAdapter(list_sortiesDuJour_arrayAdapter);


                //ajout de l'évènement lorsqu'on clique sur une ligne de la liste :
                lV_sortiesDuJour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //position de l'actu sélectionnée:
                        Object o = lV_sortiesDuJour.getItemAtPosition(i);
                        articleCourant = (Article) o;

                        //envoi des donnees dans la nouvelle activity :
                        Intent intent = new Intent(MainActivity.this, DetailArticle.class);

                        intent.putExtra("ActuEnvoyee", (Serializable) articleCourant);

                        startActivity(intent);
                    }
                });
            }
        });

        btn_plus_tard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(widthPixels >= 1200){
                    //Toast.makeText(MainActivity.this, "écran supérieur à 600 dpi" + widthPixels_str, Toast.LENGTH_SHORT).show();
                    btn_ce_jour.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_ce_jour.setTextColor(getResources().getColor(R.color.noir));
                    btn_ce_jour.setTextSize(15);

                    btn_dans_la_semaine.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_dans_la_semaine.setTextColor(getResources().getColor(R.color.noir));
                    btn_dans_la_semaine.setTextSize(15);

                    btn_plus_tard.setBackgroundColor(getResources().getColor(R.color.blanc));
                    btn_plus_tard.setTextColor(getResources().getColor(R.color.bleu_a));
                    btn_plus_tard.setTextSize(22);
                }else{
                    //Toast.makeText(MainActivity.this, widthPixels_str , Toast.LENGTH_SHORT).show();
                    btn_ce_jour.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_ce_jour.setTextColor(getResources().getColor(R.color.noir));
                    btn_ce_jour.setTextSize(10);

                    btn_dans_la_semaine.setBackgroundColor(getResources().getColor(R.color.bkg_btn_unselect));
                    btn_dans_la_semaine.setTextColor(getResources().getColor(R.color.noir));
                    btn_dans_la_semaine.setTextSize(10);

                    btn_plus_tard.setBackgroundColor(getResources().getColor(R.color.blanc));
                    btn_plus_tard.setTextColor(getResources().getColor(R.color.bleu_a));
                    btn_plus_tard.setTextSize(15);

                }

                /*-------animation de la ligne  btn_plustard-------*/
                btn_ligne_gauche = findViewById(R.id.btn_ligne_gauche);
                btn_ligne_milieu = findViewById(R.id.btn_ligne_milieu);
                btn_ligne_droite = findViewById(R.id.btn_ligne_droite);
                btn_ligne_gauche.setVisibility(View.INVISIBLE);
                btn_ligne_milieu.setVisibility(View.INVISIBLE);
                btn_ligne_droite.setVisibility(View.VISIBLE);
                /*
                //btn_ligne_x = btn_ligne.getX();
                btn_ligne_y = btn_ligne.getY();
                if (semaine_checked){
                    //btn_ligne.setX(x_middle);
                    translate_btn_ligne(btn_ligne, x_middle, x_start, btn_ligne_y, btn_ligne_y);
                    semaine_checked=false;
                }else if(aujourdhui_checked){
                    //btn_ligne.setX(x_end);
                    translate_btn_ligne(btn_ligne, x_end, x_start, btn_ligne_y, btn_ligne_y);
                    aujourdhui_checked = false;
                }
                plusTard_checked=true;
                Toast.makeText(getApplicationContext(), "+ tard - x avant translate : "+String.valueOf(btn_ligne.getX()), Toast.LENGTH_LONG).show();
                */
                /*if(btn_ligne_x.equals(x_start)){
                    //btn_ligne.setX(translate_btn_ligne(btn_ligne, 0, x_end, btn_ligne_y, btn_ligne_y));
                    Animation translate = new TranslateAnimation(x_start,x_end,btn_ligne_y,btn_ligne_y);
                    translate.setDuration(5000);
                    translate.setFillAfter(true);
                    btn_ligne.startAnimation(translate);
                    btn_ligne.setVisibility(View.VISIBLE);

                    btn_ligne.setX(x_end);
                    Toast.makeText(getApplicationContext(), "x_start -> x_end, new_x : "+String.valueOf(btn_ligne.getX()), Toast.LENGTH_LONG).show();
                }else if(btn_ligne_x.equals(x_middle)){
                    translate_btn_ligne(btn_ligne, x_middle, x_end, btn_ligne_y, btn_ligne_y);
                    Toast.makeText(getApplicationContext(), "x_middle -> x_end, new_x : "+String.valueOf(btn_ligne.getX()), Toast.LENGTH_LONG).show();
                }*/

                /*--------fin animation de la ligne -------*/
                //connexion à l'API :
                try {

                    //new TacheChargerDonnees().execute(new URL("http://77.141.122.110/api/public/eventsbyfilters/31/32/33/35")) ;
                    new TacheChargerDonnees().execute(new URL("http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters_2.txt")) ;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                //appel de l'adapter + initialisation
                list_sortiesDuJour_arrayAdapter = new Article_ArrayAdapter(getApplicationContext(), listeSortiesDuJour);
                //ciblage de la listView dans activity_main.xml
                lV_sortiesDuJour = findViewById(R.id.liste1);

                //Appel de la methode de Utility qui permet d'afficher les listeView l'une en dessous de l'autre :
                Utility.setDynamicHeight(lV_sortiesDuJour);
                //attribution de l'arrayAdapter
                lV_sortiesDuJour.setAdapter(list_sortiesDuJour_arrayAdapter);


                //ajout de l'évènement lorsqu'on clique sur une ligne de la liste :
                lV_sortiesDuJour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //position de l'actu sélectionnée:
                        Object o = lV_sortiesDuJour.getItemAtPosition(i);
                        articleCourant = (Article) o;

                        //envoi des donnees dans la nouvelle activity :
                        Intent intent = new Intent(MainActivity.this, DetailArticle.class);

                        intent.putExtra("ActuEnvoyee", (Serializable) articleCourant);

                        startActivity(intent);
                    }
                });
            }
        });



/*---------liste 1 : sorties du jour-----------*/

        //connexion à l'API :
        try {

            //new TacheChargerDonnees().execute(new URL("http://77.141.122.110/api/public/eventsbyfilters/31/32/33/35")) ;
            new TacheChargerDonnees().execute(new URL("http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters_0.txt")) ;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        //appel de l'adapter + initialisation
        list_sortiesDuJour_arrayAdapter = new Article_ArrayAdapter(this, listeSortiesDuJour);
        //ciblage de la listView dans activity_main.xml
        lV_sortiesDuJour = findViewById(R.id.liste1);

        //Appel de la methode de Utility qui permet d'afficher les listeView l'une en dessous de l'autre :
        Utility.setDynamicHeight(lV_sortiesDuJour);
        //attribution de l'arrayAdapter
        lV_sortiesDuJour.setAdapter(list_sortiesDuJour_arrayAdapter);


        //ajout de l'évènement lorsqu'on clique sur une ligne de la liste :
        lV_sortiesDuJour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //position de l'actu sélectionnée:
                Object o = lV_sortiesDuJour.getItemAtPosition(i);
                articleCourant = (Article) o;

                //envoi des donnees dans la nouvelle activity :
                Intent intent = new Intent(MainActivity.this, DetailArticle.class);

                intent.putExtra("ActuEnvoyee", (Serializable) articleCourant);

                startActivity(intent);
            }
        });




/*--------------fin liste 1-----------------*/



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

    /*--------- Clic sur une des catégories en heut de page ou sur '+ de sorties' -------------*/
    public void goToSortiesParCateg(View view) {

        if (view == findViewById(R.id.imgBtn_cat1)){

            Intent intent = new Intent(getApplicationContext(), ArticlesTriesParTheme.class);
            intent.putExtra("titre", "jeux");
            intent.putExtra("url", "http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters_31.txt");
            startActivity(intent);
        }
        else if(view == findViewById(R.id.imgBtn_cat2)){
            Intent intent = new Intent(getApplicationContext(), ArticlesTriesParTheme.class);
            intent.putExtra("titre","Art et culture");
            intent.putExtra("url", "http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters_32.txt");
            startActivity(intent);
        }
        else if(view == findViewById(R.id.imgBtn_cat3)){
            Intent intent = new Intent(getApplicationContext(), ArticlesTriesParTheme.class);
            intent.putExtra("titre", "Bonnes affaires");
            intent.putExtra("url", "http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters_33.txt");
            startActivity(intent);
        }else if(view == findViewById(R.id.imgBtn_cat4)){
            Intent intent = new Intent(getApplicationContext(), ArticlesTriesParTheme.class);
            intent.putExtra("titre", "Sport");
            intent.putExtra("url", "http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters_35.txt");
            startActivity(intent);
        }
        /*else if (view == findViewById(R.id.btn_plus_de_sorties)){
            Intent intent = new Intent(getApplicationContext(), ArticlesTriesParTheme.class);
            intent.putExtra("titre", "Liste des sorties à venir");
            intent.putExtra("url", "http://sd-67292.dedibox.fr/~dominique.d/eventsbyfilters_1_2.txt");
            startActivity(intent);
        }*/

    }

    /*public void goToAujourdhui(View view) {
        Toast.makeText(getApplicationContext(), "test aujourd'hui", Toast.LENGTH_LONG).show();

    }*/

    public void goToSemaine(View view) {
        Toast.makeText(getApplicationContext(), "test semaine", Toast.LENGTH_LONG).show();

    }

    public void goToPlusTard(View view) {
        Toast.makeText(getApplicationContext(), "test plus tard", Toast.LENGTH_LONG).show();
    }

    /*---------------appel du traitement des données venant de l'API------------------*/
    private class TacheChargerDonnees extends AsyncTask<URL, Void ,JSONArray> {

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
            //convertirJSONEnArrayList(jsonArray);
            Utility.convertirJSONEnArrayList(jsonArray, strData, listeSortiesDuJour, list_sortiesDuJour_arrayAdapter);
            list_sortiesDuJour_arrayAdapter.notifyDataSetChanged();

        }
    } //tachechargedonnees

    /*--------- gestion des données récupérées depuis l'API format : JSON ------*/

/*------fin gestion des données récupérées depuis l'API format : JSON -----*/

    /*-------ANIMATION DE LA BARRE ----*/
    protected void translate_btn_ligne (Button btn_ligne, float x_depart, float x_arrivee, float y_depart, float y_arrivee){
        //paramétrage des animations :
        //float btn_ligne_new_x;
        Animation translate = new TranslateAnimation(x_depart,x_arrivee,y_depart,y_arrivee);
        translate.setDuration(5000);
        translate.setFillAfter(true);
        btn_ligne.startAnimation(translate);
        btn_ligne.setVisibility(View.VISIBLE);
        //btn_ligne_new_x = x_arrivee;
        //return btn_ligne_new_x;
    }
}
