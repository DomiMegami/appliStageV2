package dominique.fr.myapplikejv;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import requests.LoginRequest;


public class FragmentConnexion extends Fragment {


    public FragmentConnexion() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connexion, container, false);
    }



    //Methode pour envoyer le pseudo et le mot de passe pour se connecter. Cette méthode doit être déclarée dans "connexionActivity.java"
    public void logIn(View view){

    }

}
