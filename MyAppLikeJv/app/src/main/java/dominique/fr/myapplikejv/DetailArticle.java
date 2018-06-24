package dominique.fr.myapplikejv;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import geoloc.GeoActivity;
import modeles.Article;
import utilitaires.Utility;

public class DetailArticle extends AppCompatActivity {

    Toolbar toolbar;
    String message;
    Intent browserIntent;
    //Boolean favori_bool = false;
    //int favori_compteur = 0;
    TextView tv_aff_mot_cle;

    //récupération des données reçues :
    int id;
    String titre;
    String date_publication;
    String date_debut;
    String date_fin;
    String horaire;
    double prix;
    String chapo;
    String contenu;
    String url_site_web;
    String url_facebook;
    String adresse; // = adresse_nom_salle + "\n" + adresse_rue + "\n" + adresse_commune
    String adresse_nom_salle;
    String adresse_rue;
    String adresse_commune;
    String url_pdf;
    String telephone_event;
    String url_youtube;
    String url_image;
    String categorie;
    HashMap<Integer, String> mots_cle_listehm = new HashMap<Integer, String>();
    String url_twitter;
    String email_evenement;

    Article actu_recue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        actu_recue = (Article)intent.getSerializableExtra("ActuEnvoyee");

        String phrase_date_a_afficher ="";
        String non_connu = "non connu";


        //ciblage des zones d'affichage :

        //titre :
        TextView tv_titre = (TextView)findViewById(R.id.tv_titre);
        //Place des mots cle :
        LinearLayout ll_place_mots_cle = findViewById(R.id.ll_place_mots_cle);
        //affichage des dates :
        TextView tv_aff_dates = findViewById(R.id.tv_aff_dates);
        //affichage des horaires :
        TextView tv_aff_horaires = findViewById(R.id.tv_aff_horaires);
        //affichage du prix :
        TextView tv_aff_prix = findViewById(R.id.tv_aff_prix);
        //affichage du chapo :
        TextView tv_aff_chapo = findViewById(R.id.tv_aff_chapo);
        //affichage du contenu :
        TextView tv_aff_contenu = findViewById(R.id.tv_aff_contenu);
        //adresse
        TextView tv_aff_adresse = findViewById(R.id.tv_aff_adresse);
        //email de l'evenement
        //TextView tv_aff_email = findViewById(R.id.tv_aff_email);

        //ciblage image de l'article
        ImageView img_article = findViewById(R.id.img_article);

        //boutons
        ImageButton imgBtn_appel = findViewById(R.id.imgBtn_appel);
        ImageButton imgBtn_facebook = findViewById(R.id.imgBtn_facebook_event);
        ImageButton imgBtn_twitter = findViewById(R.id.imgBtn_twitter_event);
        ImageButton imgBtn_site_web = findViewById(R.id.imgBtn_site_web_event);
        ImageButton imgBtn_pdf = findViewById(R.id.imgBtn_pdf);
        ImageButton imgBtn_youtube = findViewById(R.id.imgBtn_youtube);
        ImageButton imgBtn_mail = findViewById(R.id.imgBtn_mail);



        //Affichage des donnees :
        if (actu_recue != null) {
            //récupération des données reçues :
            id = actu_recue.getId();
            titre = actu_recue.getTitre();
            date_publication = actu_recue.getDate_publication();
            date_debut = actu_recue.getDate_debut();
            date_fin = actu_recue.getDate_fin();
            horaire = actu_recue.getHoraire();
            prix = actu_recue.getPrix();
            chapo = actu_recue.getChapo();
            contenu = actu_recue.getContenu();
            url_site_web = actu_recue.getUrl_site_web();
            url_facebook = actu_recue.getUrl_facebook();
            adresse_nom_salle = actu_recue.getContact_nom();
            adresse_rue = actu_recue.getContact_adresse();
            adresse_commune = actu_recue.getContact_commune();
            url_pdf = actu_recue.getUrl_pdf();
            telephone_event = actu_recue.getTelephone();
            url_youtube = actu_recue.getUrl_youtube();
            url_image = actu_recue.getUrl_image();
            categorie = actu_recue.getCategorie();
            url_twitter = actu_recue.getUrl_twitter();
            email_evenement = actu_recue.getEmail_evenement();
            //titre
            tv_titre.setText(titre);

            //mots cles - seront affichés dynamique suivant le nombre de mots clés associés à l'article
            mots_cle_listehm = actu_recue.getMots_cle_liste_hm();
            Set<Map.Entry<Integer, String>> entrees = mots_cle_listehm.entrySet(); //'entrees' est un ensemble de paires key-value
            Iterator<Map.Entry<Integer, String>> iter = entrees.iterator(); // itérateur sur les paires
            while (iter.hasNext()) {
                Map.Entry<Integer, String> entree = (Map.Entry<Integer, String>)iter.next();
                final Integer cle = entree.getKey(); //mc_id
                final String val = entree.getValue(); //mc_nom
                tv_aff_mot_cle = new TextView(this);
                tv_aff_mot_cle.setId(cle);
                tv_aff_mot_cle.setText(val);
                //si possible, rajouter un margin
                tv_aff_mot_cle.setPadding(10, 2,10,2);
//                tv_aff_mot_cle.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getApplicationContext(), ArticlesTriesParTheme.class);
//                        intent.putExtra("id", cle);
//                        intent.putExtra("nom", val);
//                        startActivity(intent);
//                    }
//                });
                ll_place_mots_cle.addView(tv_aff_mot_cle);
            }



            //dates de l'évenement. Si date_debut == date_fin, alors on affiche "Le + date" et sinon "Du + date_debut + au + date_fin"
            //prépa du bon format de la date :
            date_debut = new Utility().formaterDateStr(date_debut);
            date_fin = new Utility().formaterDateStr(date_fin);

            if (date_fin.equals(date_debut)){
                phrase_date_a_afficher = "Le "+date_debut;
            }else{
                phrase_date_a_afficher = "Du "+date_debut + " au " + date_fin;
            }
            tv_aff_dates.setText(phrase_date_a_afficher);

            //horaire
            tv_aff_horaires.setText("A " + horaire);

            //prix
            if(prix == 9999.99){
                tv_aff_prix.setVisibility(View.GONE);
            }else{
                tv_aff_prix.setText("Prix : " + String.valueOf(prix) + " €");
            }


            //chapo
            if(chapo.equals(non_connu)){
                tv_aff_chapo.setVisibility(View.GONE);
            }else{
                tv_aff_chapo.setText(chapo);
            }


            //contenu
            tv_aff_contenu.setText(contenu);

            //adresse
            adresse = adresse_nom_salle + "\n" + adresse_rue + "\n" + adresse_commune;
            tv_aff_adresse.setText(adresse);

            //email

            if(email_evenement.equals(non_connu)){
                imgBtn_mail.setImageResource(R.drawable.ic_email_grey_24dp);

            }else{
                imgBtn_mail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse(email_evenement));
                        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sujet_demande_renseignements));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
            }
            //img

            Glide.with(getApplicationContext()).load(url_image).into(img_article);
            // Picasso.with(getApplicationContext()).load(url_image).into(img_article);

            //téléphone
            if(telephone_event.equals(non_connu)){
                imgBtn_appel.setImageResource(R.drawable.ic_phone_grey_30dp);

            }else{
                imgBtn_appel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri number = Uri.parse("tel:"+telephone_event);
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        startActivity(callIntent);
                    }
                });
            }
            //facebook
            if(url_facebook.equals(non_connu)){
                imgBtn_facebook.setImageResource(R.drawable.ic_facebook_grey);
            }else{
                imgBtn_facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent facebookIntent;
                        try {
                            getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                            facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href="+url_facebook));
                        } catch (PackageManager.NameNotFoundException e) {
                            facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_facebook));
                        }
                        startActivity(facebookIntent);
                    }
                });
            }
            //twitter
            if(url_twitter.equals(non_connu)){
                imgBtn_twitter.setImageResource(R.drawable.ic_twitter_grey_30dp);
            }else{
                imgBtn_twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name="+url_twitter));
                            startActivity(intent);
                        } catch (Exception e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/"+url_twitter)));
                        }
                    }
                });
            }
            //site web
            if(url_site_web.equals(non_connu)){
                imgBtn_site_web.setImageResource(R.drawable.ic_icon_grey_www);
            }else{
                imgBtn_site_web.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_site_web));
                        startActivity(browserIntent);
                    }
                });
            }
            //pdf
            if(url_pdf.equals(non_connu)){
                imgBtn_pdf.setVisibility(View.GONE);
            }
            //youtube
            if(url_youtube.equals(non_connu)){
                imgBtn_youtube.setVisibility(View.GONE);
            }

        } //end if

    }

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



    public void clicSurPartager(View view) {
        message = "Bouton partager";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void clicSurYoutube(View view) {
        //message = "Bouton youtube : \n" + url_youtube;
        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_youtube));
        startActivity(browserIntent);
    }

    public void clicSurPdf(View view) {
        //message = "Bouton PDF : \n" + url_pdf;
        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_pdf));
        startActivity(browserIntent);
    }

    public void clicSurMap(View view) {
        Intent intent = new Intent(getApplicationContext(), GeoActivity.class);

        intent.putExtra("ARTICLE2",actu_recue);
        startActivity(intent);
    }

}
