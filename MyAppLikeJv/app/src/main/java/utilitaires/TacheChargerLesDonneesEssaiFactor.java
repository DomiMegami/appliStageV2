package utilitaires;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import adapters.Article_ArrayAdapter;
import modeles.Article;

/**
 * Created by Dominique DURI on 16/02/2018.
 *
 * Nota du 16-02-2018 : factorisation => echec.
 *DD
 */

public class TacheChargerLesDonneesEssaiFactor extends AsyncTask<URL, Void ,JSONArray> {

    String strData;
    private ArrayList<Article> listeArticles;
    private Article_ArrayAdapter article_arrayAdapter;

    public TacheChargerLesDonneesEssaiFactor(ArrayList<Article> listeArticles, Article_ArrayAdapter article_arrayAdapter) {

        this.listeArticles = listeArticles;
        this.article_arrayAdapter = article_arrayAdapter;
    }


    @Override
    protected JSONArray doInBackground(URL... urls) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) urls[0].openConnection();
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                strData = builder.toString();
                return new JSONArray(strData);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return null;
    }


    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        //super.onPostExecute(jsonArray);
        //convertirJSONEnArrayList(jsonArray);
        Utility.convertirJSONEnArrayList(jsonArray, strData, getListeArticles(), getArticle_arrayAdapter());
        getArticle_arrayAdapter().notifyDataSetChanged();

    }


    public ArrayList<Article> getListeArticles() {
        return listeArticles;
    }

    public void setListeArticles(ArrayList<Article> listeArticles) {
        this.listeArticles = listeArticles;
    }

    public Article_ArrayAdapter getArticle_arrayAdapter() {
        return article_arrayAdapter;
    }

    public void setArticle_arrayAdapter(Article_ArrayAdapter article_arrayAdapter) {
        this.article_arrayAdapter = article_arrayAdapter;
    }
}
