package requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dominique DURI on 16/02/2018.
 */

public class RegisterRequest extends StringRequest {
    /*StringRequest permet d'échanger avec le fichier php
    On envoie une requete au server et on reçoit une réponse de type String */

    //définir l'url de register.php
    private static final String REGISTER_REQUEST_URL = "http://sd-67292.dedibox.fr/~dominique.d/Register.php";
    //Créer une Map : elle va servir à recuillir les différentes values qui correspondent aux champs de la bdd.
    private Map<String, String> params;
    //constructeur :    RegisterRequest(user_pseudo, user_email, user_mdp, user_pref_1, user_pref_2, user_pref_3, user_pref_4, responseListener);
    public RegisterRequest(String user_pseudo,String user_email,String user_mdp,Integer user_pref_1,Integer user_pref_2,
                           Integer user_pref_3,Integer user_pref_4,Response.Listener<String> listener)
    {
        //on va communiquer avec les fichiers php en post.
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        //initialisation de params :
        params = new HashMap<>();
        //on 'put' les =/= values dans params. On est dans register, alors il faut 'put' user_pseudo, user_email, user_mdp et age :
        //on put dans params des values de type String
        //params.put("nom du champ", valeur); //valeur est donnée en argument du constructeur
        params.put("user_pseudo", user_pseudo);
        params.put("user_email", user_email);
        params.put("user_mdp", user_mdp);
        params.put("user_pref_1", String.valueOf(user_pref_1));
        params.put("user_pref_2", String.valueOf(user_pref_2));
        params.put("user_pref_3", String.valueOf(user_pref_3));
        params.put("user_pref_4", String.valueOf(user_pref_4));

    }

    //maintenant qu'on a alimenté params, on va créer un getter pour y acceder :
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}