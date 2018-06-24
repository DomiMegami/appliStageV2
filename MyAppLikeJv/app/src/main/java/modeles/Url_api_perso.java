package modeles;

/**
 * Created by Dominique DURI on 23/02/2018.
 * //va reccueillir la nouvelle url de requête aurpès de l'API, afin que cette donnée ai une portée plus grande que simplement le responseListener.
 */

public class Url_api_perso {
    private String url_str;

    public Url_api_perso() {

    }

    public Url_api_perso(String url_str) {
        this.url_str = url_str;
    }

    public String getUrl_str() {
        return url_str;
    }

    public void setUrl_str(String url_str) {
        this.url_str = url_str;
    }
}
