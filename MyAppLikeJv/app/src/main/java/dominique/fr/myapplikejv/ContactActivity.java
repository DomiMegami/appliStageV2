package dominique.fr.myapplikejv;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import utilitaires.UserSessionManager;

public class ContactActivity extends AppCompatActivity {

    Toolbar toolbar;

    /*------- Session -------*/
    UserSessionManager session;

    /*------- Fin session ------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*-----------Session -------*/
        session = new UserSessionManager(getApplicationContext());
    }

    public void PhoneToAssociation(View view) {

        Uri number = Uri.parse("tel:3699");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);

    }

    public void SendEmailToAssociation(View view) {

        /*e-mails à générer (si possible) dynamiquement à partir du back office.
        A défaut il seront en dur dans strings.xml
        /!\ à respecter la syntaxe "mailto:adresse@email.xx"
         */
        String e_mail_contact = getString(R.string.mailto_mail_asso);
        String e_mail_contact_organisateurs = getString(R.string.mail_to_orga_asso);
        Intent intent;


        if (view == findViewById(R.id.e_mail_contact)){
            intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(e_mail_contact));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sujet_mail_asso));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else if(view == findViewById(R.id.e_mail_orga)){
            intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(e_mail_contact_organisateurs));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sujet_mail_orga_asso));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

    }

    /*----------------
    Menu dans toolbar
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.go_accueil:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.go_contact:
                intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);
                break;
            case R.id.go_connexion:
                intent = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home: //gestion de la flèche de retour dans le menu. ramène à l'accueil en fermant l'activity en cours
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //--------fin toolbar--------

    //lien vers CGU
    public void goCGU(View view) {
        startActivity(new Intent(getApplicationContext(), CguActivity.class));
    }
    //lien vers les mentions légales
    public void goMentionsLegales(View view) {
        startActivity(new Intent(getApplicationContext(), MentionsLegalesActivity.class));
    }
    //lien vers les crédits
    public void goCredit(View view) {
        startActivity(new Intent(getApplicationContext(), CreditsActivity.class));
    }
}
