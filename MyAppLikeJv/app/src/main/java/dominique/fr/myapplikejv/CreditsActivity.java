package dominique.fr.myapplikejv;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class CreditsActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageButton imageButton_youtube = findViewById(R.id.imageButton_youtube);
        ImageButton imageButton_pdf = findViewById(R.id.imageButton_pdf);
        ImageButton imageButton_www = findViewById(R.id.imageButton_www);
        ImageButton imageButton_facebook = findViewById(R.id.imageButton_facebook);
        ImageButton imageButton_voiture = findViewById(R.id.imageButton_voiture);

        //liens vers les sites des icones :
        imageButton_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.onlinewebfonts.com/icon"));
                startActivity(browserIntent);
            }
        });
        imageButton_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.freepik.com"));
                startActivity(browserIntent);
            }
        });
        imageButton_www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.freepik.com"));
                startActivity(browserIntent);
            }
        });
        imageButton_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/authors/icomoon"));
                startActivity(browserIntent);
            }
        });
        imageButton_voiture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.onlinewebfonts.com/icon"));
                startActivity(browserIntent);
            }
        });
    }
}
