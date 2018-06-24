package dominique.fr.myapplikejv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import modeles.User;
import requests.RecupInfo_ModifProfilRequest;
import requests.UpdateMdpRequest;
import utilitaires.Utility;

public class PopupModifMdp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_modif_mdp);
        //définition des dimentions de la popUp :

        //DisplayMetrics va contenir les dimentions
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //initialisation de width et height
        int width = dm.widthPixels; // (récup la taille de l'écran)
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .8));

        //on cible les edit text de activity_dialog_Login :
        final EditText et_ancien_mdp = (EditText) findViewById(R.id.et_ancien_mdp);
        final EditText et_new_mdp = (EditText) findViewById(R.id.et_new_mdp);
        final EditText et_verif_new_mdp = (EditText) findViewById(R.id.et_verif_new_mdp);
        Button btn_valid_modif_mdp = (Button) findViewById(R.id.btn_valid_modif_mdp);

        /*-----------intent--------*/
        Intent intent = getIntent();
        final User user_connecte = (User) intent.getSerializableExtra("user_connecte");

        /*------- autres variables ------*/


        //Onclick Listener pour le bouton login
        btn_valid_modif_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String message = "user_connecte : \nid : " + user_connecte.getId() + "\npseudo : " + user_connecte.getPseudo() + "\nemail : " + user_connecte.getEmail() + "\nmdp : " + user_connecte.getMotDePasse();
                   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();*/

                int user_id = user_connecte.getId();
                String user_id_str = String.valueOf(user_id);
                String user_pseudo = user_connecte.getPseudo();
                String user_ancien_mdp = et_ancien_mdp.getText().toString();
                String user_new_mdp = et_new_mdp.getText().toString();
                String user_verif_new_mdp = et_verif_new_mdp.getText().toString();
                //String message = "donnes récup : \nid : " + user_id_str + "\npseudo : " + user_pseudo + "\nancien mdp : " + user_ancien_mdp + "\nnew mdp : " + user_new_mdp+ "\nverif new mdp : " + user_new_mdp;
                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                //Au clic :
                //1) verif que le new mdp et sa confirmation sont les mêmes
                if (!user_new_mdp.equals(user_verif_new_mdp)) {
                    Toast.makeText(getApplicationContext(), R.string.txt_info_egalite_2_mdp, Toast.LENGTH_LONG).show();
                } else {
                    if (!Utility.isValidPassword(user_new_mdp)) {
                        Toast.makeText(getApplicationContext(), R.string.txt_info_format_mdp, Toast.LENGTH_LONG).show();
                    } else {
                        //2) si le new mdp est vérifié et a le bon format :  envoi à la bdd pour vérif si l'ancien mdp est correct, et si oui, l'update.

                        /*--- listener de la réponse de connexion ---*/
                        //connexion à UpdateMdp.php pour acces à la bdd

                        //-----------------------------------------------------------
                        final Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //'response' qui est en argument est la réponse de register.php
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    //si le traitement est un succes, alors on va aller sur la page Login
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), R.string.txt_confirm_mdp_modifié, Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {

                                        //sinon on affiche une alert qui propose de réessayer
                                        AlertDialog.Builder builder = new AlertDialog.Builder(PopupModifMdp.this);
                                        builder.setMessage(R.string.txt_ancien_mdp_incorrect)
                                                .setNegativeButton("retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }//end onresponse
                        }; //end listener

        /*--------- envoie de la requête ------*/

                        UpdateMdpRequest updateMdpRequest = new UpdateMdpRequest(user_id_str, user_pseudo, user_ancien_mdp, user_new_mdp, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(PopupModifMdp.this);
                        queue.add(updateMdpRequest);
                    } //end if (le nouveau mdp a le bon format)

                }//end if(les 2 mots de passe sont les mêmes)
            }//end onclick
        });//end listener
    }

    public void annulModif(View view) {
        finish();
    }
}
