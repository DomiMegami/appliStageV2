package dominique.fr.myapplikejv;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Fragment_Inscription_etape3 extends Fragment {


    public Fragment_Inscription_etape3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inscription_etape3, container, false);
    }

    public void goPreviousFromStep3(View view){
    }

    public void goStep4(View view) {
    }
}
