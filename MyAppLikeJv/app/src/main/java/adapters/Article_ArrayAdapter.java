package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import dominique.fr.myapplikejv.R;
import modeles.Article;

/**
 * Created by Dominique DURI on 15/02/2018.
 */

public class Article_ArrayAdapter extends ArrayAdapter<Article> {

    //Déclaration des variables :
    private TextView titre_article;    //Recevra l'élément de la liste à afficher

    private ImageView image_article;


    //cache
    private Map<String, Bitmap> bitmaps = new HashMap<String, Bitmap>();


    //Constructeur de l'adapter
    public Article_ArrayAdapter(@NonNull Context context, @NonNull ArrayList<Article> objects) {
        super(context, -1, objects);
    }

    //getView permet de définir dans quelle activity est la listview
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //création de la ligne qui va accueillir l'info
        View row = convertView;

        //récupération du template. s'il n'existe pas, on le créé
        if (row == null){

            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row=inflater.inflate(R.layout.item_liste_articles, parent, false);
            //une fois créer,on va pouvoir utiliser row pour les itérations suivantes
        }

        titre_article = row.findViewById(R.id.titre_article);
        image_article = row.findViewById(R.id.image_article);


        Article aAfficher = getItem(position);
        titre_article.setText(aAfficher.getTitre());

        String url = aAfficher.getUrl_image();
        Picasso.with(getContext()).load(url).into(image_article);


        return row;
    }
}
