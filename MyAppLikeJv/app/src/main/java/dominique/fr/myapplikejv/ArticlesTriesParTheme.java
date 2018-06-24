package dominique.fr.myapplikejv;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import modeles.Categorie;
import modeles.Contact;
import modeles.MotCle;
import utilitaires.Utility;

public class ArticlesTriesParTheme extends AppCompatActivity {

    /*
    Cette activity va accueillir l'affichage des articles suivant le lien cliqué en page d'accueil :
     - bouton categorie 1
     - bouton categorie 2
     - bouton categorie 3
     - bouton '+ de sorties'
     */

    /*---------------VARIABLES GLOBALES-------------------*/

        Toolbar toolbar;

        //modele : (ce qu'on veut afficher )
        ArrayList<Article> listeArticles = new ArrayList<Article>();
        Article articleCourant;

        //vue : où on va l'afficher :
        ListView listViewArticles;
        //qui va faire l'adaptation :
        Article_ArrayAdapter articleArrayAdapter ;

        String strData = null;
        String urlApi ;

        ArrayList<Categorie> categories_liste;

    /*--------- onCreate -------*/
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_articles_tries_par_theme);

         /*----------Toolbar---------*/
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //gère le retour home
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*-------intent ------*/
            Intent intent = getIntent();
            String titre_page = intent.getStringExtra("titre");
            urlApi = intent.getStringExtra("url");

            TextView tv_titre_page = findViewById(R.id.tvTitrePage);
            tv_titre_page.setText(titre_page);



            //connection directe à l'API :
            //-------------------------------------------------
            //instance de l'objet ListView
            listViewArticles = findViewById(R.id.lvArticles);
            //instancier l'adaptateur
            articleArrayAdapter= new Article_ArrayAdapter(this, listeArticles);
            //associer l'adapter avec l'objet listView
            listViewArticles.setAdapter(articleArrayAdapter);

            try {
                new TacheChargerDonnes().execute(new URL(urlApi)) ;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //ajout de l'évènement lorsqu'on clique sur une ligne de la liste :
            listViewArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //position de l'actu sélectionnée:
                    Object o = listViewArticles.getItemAtPosition(i);
                    articleCourant = (Article) o;

                    //envoi des donnees dans la nouvelle activity :
                    Intent intent = new Intent(getApplicationContext(), DetailArticle.class);

                    intent.putExtra("ActuEnvoyee", (Serializable) articleCourant);

                    startActivity(intent);
                }
            });





        }    /*-------end oncreate------------*/

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
            Utility.convertirJSONEnArrayList(jsonArray,strData , listeArticles, articleArrayAdapter);

            articleArrayAdapter.notifyDataSetChanged();

        }
    } //tachechargedonnees

    /*--------- gestion des données récupérées depuis l'API format : JSON ------*/


/*------fin gestion des données récupérées depuis l'API format : JSON -----*/
}
