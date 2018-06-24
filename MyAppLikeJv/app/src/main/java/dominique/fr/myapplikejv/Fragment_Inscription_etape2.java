package dominique.fr.myapplikejv;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class Fragment_Inscription_etape2 extends Fragment {


    public Fragment_Inscription_etape2() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inscription_etape2, container, false);
    }


    //methodes vides, mais elles doivent être présentes sinon une erreur est générée.
    //Pourtant, la méthode qui sera utilisée est dans "ConnexionActivity.java"
    public void goPreviousFromStep2(View view){
    }

    public void goStep3(View view) {
    }
}