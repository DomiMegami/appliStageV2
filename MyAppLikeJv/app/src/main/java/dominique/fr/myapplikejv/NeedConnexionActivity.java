package dominique.fr.myapplikejv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NeedConnexionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_connexion);
        Button btn_retour_splash_screen = findViewById(R.id.btn_retour_splash_screen);
        btn_retour_splash_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SplashScreenActivity.class));
            }
        });

        Button btn_quitter_application = findViewById(R.id.btn_quitter_application);
        btn_quitter_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });
    }
}
