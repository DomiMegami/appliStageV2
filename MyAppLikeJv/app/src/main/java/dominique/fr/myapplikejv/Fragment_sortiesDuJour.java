package dominique.fr.myapplikejv;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Fragment_sortiesDuJour extends Fragment {


    public Fragment_sortiesDuJour() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sorties_du_jour, container, false);
    }
    public void goToSortiesParCateg(View v){

    }
}
