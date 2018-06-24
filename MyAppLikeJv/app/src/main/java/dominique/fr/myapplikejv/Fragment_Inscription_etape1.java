package dominique.fr.myapplikejv;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class Fragment_Inscription_etape1 extends Fragment {


    public Fragment_Inscription_etape1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inscription_etape1, container, false);
    }


    //Il a fallu que je créé cette méthode ici en premier pour qu'aucune erreur ne soit générée.
    //Mais en fin de compte elle ne sert à rien, c'est la méthode dans ConnexionActivity qui est prise en compte...
    public void goStep2(View view) {
        Toast.makeText(getContext(), "Clic depuis le fragment Inscription etape 1", Toast.LENGTH_LONG).show();
    }
}
