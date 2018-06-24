package utilitaires;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapters.Article_ArrayAdapter;
import modeles.Article;
import modeles.Categorie;
import modeles.Contact;
import modeles.MotCle;

/**
 * Created by Dominique DURI on 16/02/2018.
 */

public class Utility {
    //Methode qui permet d'afficher les listView les une en dessous des autres.
    public static void setDynamicHeight(ListView mListView) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null) {
            // when adapter is null
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }


    // isValidEmailAddress: Check the email address is OK
    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    // isValidPassword: Check the password is OK
    public static boolean isValidPassword(String password) {
        String passwordRegEx;
        Pattern pattern;
        // Regex for a valid email address - Minimum 8 and maximum 20 characters, at least one letter and one number, special characters allowed
        passwordRegEx = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,20}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(passwordRegEx);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    //formater les dates (String) sous forme : jj-mm-aaaa depuis aaaa-mm-jj hh:mm:ss
    public String formaterDateStr(String dateAformater){
        String date_formatage_intermediaire= dateAformater.substring(0,10);
        String date_annee_str = date_formatage_intermediaire.substring(0,4);
        String date_mois_str = date_formatage_intermediaire.substring(5,7);
        String[] tab_mois = {"janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre", "décembre"};
        for(int i=0; i<tab_mois.length; i++){
            Integer mois_int = Integer.parseInt(date_mois_str)-1;
            if(mois_int == i ){
                date_mois_str = tab_mois[i];
                break;
            }
        }
        String date_jour_str = date_formatage_intermediaire.substring(8);
        String date_formatee = date_jour_str + " " + date_mois_str+" "+date_annee_str;
        return date_formatee;
    }

    public static void convertirJSONEnArrayList(JSONArray jsonArray, String nomJSONArray , ArrayList<Article> listeArticles, Article_ArrayAdapter article_arrayAdapter) {

        Article article;
        String titre; //
        String chapoHtml;
        String chapoTxt;
        String contenuHtml;
        String contenuTxt;
        String date_publication_str;
        String date_debut_str;
        String date_fin_str;
        String horaire_str;
        String prix_str;
        Double prix = 10.01;
        int contact_id_dans_article;
        String url_site_web = null;
        String url_facebook= null;
        String adresse;
        String contact_nom = null;
        String contact_adresse= null;
        String contact_commune= null;
        String url_pdf= null;
        String telephone_str= null;
        String url_youtube= null;
        String url_image= null;
        String url_twitter= null;
        String email_evenement= null;
        int cat_id_dans_article;
        String categorie = null ;
        ArrayList <Categorie> categories_liste = new ArrayList<>();
        ArrayList<Integer> mots_cle_id_X_article_liste = new ArrayList<>();
        HashMap<Integer, Integer> motsCle_id_X_article_id_listehm = new HashMap<Integer, Integer>();
        HashMap<Integer, String> motsCle_X_article_listehm = new HashMap<Integer, String>();
        ArrayList<Contact> contacts_liste = new ArrayList<>();
        ArrayList<MotCle> motCleArrayList = new ArrayList<>();



        listeArticles.clear();
        // réinitialiser la liste
        try {
            JSONArray jArray = new JSONArray(nomJSONArray);
            //Récupération des différents tableaux : contacts, mots_cle, catégories, articles et liste_mots_cles_par_categ
            JSONArray contact_jsonArray = (JSONArray) jArray.get(0);
            JSONArray mots_cle_jsonArray = (JSONArray) jArray.get(1);
            JSONArray categories_jsonArray = (JSONArray) jArray.get(2);
            JSONArray article_jsonArray = (JSONArray) jArray.get(3);
            JSONArray liste_mots_c_par_article_jsonArray = (JSONArray) jArray.get(4);

            //récupération de toutes les  catégories :
            for (int i=0; i<categories_jsonArray.length(); i++){
                Categorie categ  = new Categorie();
                JSONObject jso_categ = categories_jsonArray.getJSONObject(i);
                categ.setCat_id(jso_categ.getInt("cat_id"));
                categ.setCat_nom(jso_categ.getString("cat_nom"));
                // categ.setCat_chk(jso_categ.getBoolean("cat_chk"));
                categories_liste.add(categ);
            }
            //récupération de tous les mots cles
            for (int i=0; i<mots_cle_jsonArray.length(); i++){
                MotCle motCle = new MotCle();
                JSONObject jso_motsCle = mots_cle_jsonArray.getJSONObject(i);
                motCle.setMot_cle_id(jso_motsCle.getInt("mc_id"));
                motCle.setMot_cle_nom(jso_motsCle.getString("mc_nom"));
                motCle.setMot_cle_categorie_id(jso_motsCle.getInt("mc_cat_id"));
                motCleArrayList.add(motCle);
            }

            //récupération des mots cles par article
            for (int ind=0; ind<liste_mots_c_par_article_jsonArray.length(); ind++){
                JSONObject jso_motCleXarticle = liste_mots_c_par_article_jsonArray.getJSONObject(ind);
                String article_id_str = jso_motCleXarticle.getString("article_id");
                Integer article_id = Integer.parseInt(article_id_str);
                String motCle_id_str = jso_motCleXarticle.getString("kw_id");
                Integer motCle_id = Integer.parseInt(motCle_id_str);

                motsCle_id_X_article_id_listehm.put(article_id, motCle_id);

            }


            //récupération des contacts
            for (int i=0; i<contact_jsonArray.length(); i++){
                Contact contact = new Contact();
                JSONObject jso_contact = contact_jsonArray.getJSONObject(i);
                contact.setContact_id(jso_contact.getInt("contact_id"));
                contact.setContact_nom(jso_contact.getString("contact_nom"));
                contact.setContact_adresse(jso_contact.getString("contact_adresse"));
                contact.setContact_commune(jso_contact.getString("contact_commune"));

                String contact_tel = jso_contact.getString("contact_tel");
                if(contact_tel.length()==0 || contact_tel.equals("null")){
                    contact_tel="non connu";
                }
                contact.setContact_tel(contact_tel);

                String contact_mail = jso_contact.getString("contact_mail");
                if(contact_mail.length() == 0 || contact_mail.equals("null")){
                    contact_mail="non connu";
                }
                contact.setContact_mail(contact_mail);

                String contact_FB = jso_contact.getString("contact_FB");
                if(contact_FB.length() == 0 || contact_FB.equals("null")){
                    contact_FB = "non connu";
                }
                contact.setContact_facebook(contact_FB);

                String contact_TW = jso_contact.getString("contact_TW");
                if((contact_TW.length() == 0) || (contact_TW.equals("null"))){
                    contact_TW = "non connu";
                }
                contact.setContact_twitter(contact_TW);

                String contact_site = jso_contact.getString("contact_site");
                if((contact_site.length()==0)||(contact_site.equals("null"))){
                    contact_site = "non_connu";
                }
                contact.setContact_site("contact_site");

                contacts_liste.add(contact);
            }
            for ( int i = 0 ; i<article_jsonArray.length();i++){

                JSONObject jsoArticle = (JSONObject) article_jsonArray.get(i);
                article = new Article();
                //récupération des infos dans l'API :

                //ID :
                article.setId(jsoArticle.getInt("id"));

                //DATE PUBLICATION :
                date_publication_str = jsoArticle.getString("date_pub");
                date_publication_str = date_publication_str.substring(0,10);
                article.setDate_publication(date_publication_str);

                //TITRE :
                titre = jsoArticle.getString("titre") ;
                titre = Html.fromHtml((String) titre).toString();
                article.setTitre(titre);

                //CHAPO ;
                chapoHtml =jsoArticle.getString("chapo");
                if((chapoHtml.length()==0) || (chapoHtml.equals("null"))){
                    chapoTxt = "non connu";
                }else{
                    chapoTxt =chapoHtml.replaceAll("<[^>]*>", "");
                    chapoTxt = Html.fromHtml((String) chapoTxt).toString();
                }
                article.setChapo(chapoTxt);

                //CONTENU :
                contenuHtml = jsoArticle.getString("contenu");
                contenuTxt = contenuHtml.replaceAll("^<BR>$", "\n");
                contenuTxt = Html.fromHtml((String) contenuTxt).toString();
                article.setContenu(contenuTxt);

                //DATE DEBUT DE LA SORTIE :
                date_debut_str=jsoArticle.getString("date_deb");
                article.setDate_debut(date_debut_str);
                //DATE FIN DE LA SORTIE :
                date_fin_str = jsoArticle.getString("date_fin");
                article.setDate_fin(date_fin_str);
                //HORAIRE DE LA SORTIE :
                horaire_str = jsoArticle.getString("horaires");
                article.setHoraire(horaire_str);
                //PRIX

                prix_str = jsoArticle.getString("prix");
                if( prix_str.equals("null")){
                    prix = 9999.99;
                }else{
                    prix = jsoArticle.getDouble("prix");
                }
                article.setPrix(prix);
                //SITE WEB - FACEBOOK - ADRESSE - TELEPHONE - TWITTER - EMAIL EVENT:
                //1) récuperer l'id du contact qui est dans l'article :
                contact_id_dans_article = jsoArticle.getInt("liste_contacts_id");
                //2) parcours de l'arrayList des Catégories :
                for(int cpt =0; cpt<contacts_liste.size(); cpt++){
                    //3) ne prendre que le nom de la catégorie qui correspond à cat_id_dans_article
                    if (contact_id_dans_article == contacts_liste.get(cpt).getContact_id()){
                        //Site web :
                        url_site_web = contacts_liste.get(cpt).getContact_site();
                        //facebook evenement :
                        url_facebook=contacts_liste.get(cpt).getContact_facebook();
                        //adresse :
                        contact_nom = contacts_liste.get(cpt).getContact_nom() ;
                        contact_adresse = contacts_liste.get(cpt).getContact_adresse();
                        contact_commune = contacts_liste.get(cpt).getContact_commune();
                        //téléphone :
                        telephone_str = contacts_liste.get(cpt).getContact_tel();
                        //twitter :
                        url_twitter = contacts_liste.get(cpt).getContact_twitter();
                        //email evenement
                        email_evenement = contacts_liste.get(cpt).getContact_mail();

                        break;
                    }
                }
                //URL site web
                if((url_site_web.length() == 0 ) || (url_site_web.equals("null"))){
                    url_site_web = "non connu";
                }
                article.setUrl_site_web(url_site_web);

                //URL facebook
                if((url_facebook.length() == 0 ) || (url_facebook.equals("null"))){
                    url_facebook = "non connu";
                }
                article.setUrl_facebook(url_facebook);

                //Contact nom
                if((contact_nom.length() == 0 ) || (contact_nom.equals("null"))){
                    contact_nom = "non connu";
                }
                article.setContact_nom(contact_nom);

                //Contact adresse
                if((contact_adresse.length() == 0 ) || (contact_adresse.equals("null"))){
                    contact_adresse = "non connu";
                }
                article.setContact_adresse(contact_adresse);

                //Contact commune
                if((contact_commune.length() == 0 ) || (contact_commune.equals("null"))){
                    contact_commune = "non connu";
                }
                article.setContact_commune(contact_commune);

                //Téléphone
                if((telephone_str.length() == 0 ) || (telephone_str.equals("null"))){
                    telephone_str = "non connu";
                }
                article.setTelephone(telephone_str);

                //url_twitter
                if((url_twitter.length() == 0 ) || (url_twitter.equals("null"))){
                    url_twitter = "non connu";
                }
                article.setUrl_twitter(url_twitter);

                //email_evenement
                if((email_evenement.length() == 0 ) || (email_evenement.equals("null"))){
                    email_evenement = "non connu";
                }
                article.setEmail_evenement(email_evenement);

                //LIEN PDF :
                url_pdf = jsoArticle.getString("lien_pdf");
                if((url_pdf.length()==0)||(url_pdf.equals("null"))){
                    url_pdf = "non connu";
                }
                article.setUrl_pdf(url_pdf);

                //LIEN YOUTUBE
                url_youtube = jsoArticle.getString("lien_yt");
                if((url_youtube.length()==0)||(url_youtube.equals("null"))){
                    url_youtube = "non connu";
                }
                article.setUrl_youtube(url_youtube);

                //CATEGORIE:

                //1) récuperer l'id de la catégorie qui est dans l'article :
                cat_id_dans_article = jsoArticle.getInt("categorie_id");
                //2) parcours de l'arrayList des Catégories :
                for(int c =0; c<categories_liste.size(); c++){
                    //3) ne prendre que le nom de la catégorie qui correspond à cat_id_dans_article
                    if (cat_id_dans_article == categories_liste.get(c).getCat_id()){
                        categorie = categories_liste.get(c).getCat_nom();
                        break;
                    }
                }
                article.setCategorie(categorie);

                //LIEN IMAGE :
                url_image = "http://77.141.122.110/jvback/img/"+categorie+"/" + jsoArticle.getString("lien_image");
                article.setUrl_image(url_image);

                //MOT CLE :
                //1) Dans le JSONArray des liste_mots_c_par_article_jsonArray, récupérer les id des mots clé associés à l'article

                Set<Map.Entry<Integer, Integer>> entrees = motsCle_id_X_article_id_listehm.entrySet(); //'entrees' est un ensemble de paires key-value
                Iterator<Map.Entry<Integer, Integer>> iter = entrees.iterator(); // itérateur sur les paires
                while (iter.hasNext()) {
                    Map.Entry<Integer, Integer> entree = (Map.Entry<Integer, Integer>)iter.next();
                    Integer article_id_K = entree.getKey();
                    Integer motCle_id_V = entree.getValue();
                    if(article_id_K == article.getId()){
                        mots_cle_id_X_article_liste.add(motCle_id_V);
                    }
                }
                //2) Dans la liste des MotCle, récupérer les noms associés aux id

                for(int j = 0 ; j<mots_cle_id_X_article_liste.size(); j++){
                    for (int k = 0; k<motCleArrayList.size(); k++){
                        if(motCleArrayList.get(k).getMot_cle_id() == mots_cle_id_X_article_liste.get(j)){
                            motsCle_X_article_listehm.put(mots_cle_id_X_article_liste.get(j), motCleArrayList.get(k).getMot_cle_nom());
                        }
                    }
                }
                //3) Dans Article, put dans l'hashMap getMots_cle_liste_hm
                article.setMots_cle_liste_hm(motsCle_X_article_listehm);


                //Ajout à la liste des articles :
                article_arrayAdapter.add(article);



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
