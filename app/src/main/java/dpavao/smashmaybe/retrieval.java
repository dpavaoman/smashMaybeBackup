package dpavao.smashmaybe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import android.view.animation.OvershootInterpolator;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


/**
 * Created by dpava on 5/26/2017.
 */

public class retrieval extends Activity{

    EditText tournament_slug_text;
    TextView responseView;
    ProgressBar progressBar;
    ImageView banner;
    ImageView bannerg1;
    ImageView bannerg2;
    TextView gameName1;
    int game_count;



    private List<Game> games;
    private RecyclerView rv;




    private String TAG = MainActivity.class.getSimpleName();
    HashMap<String, String> tourney_list = new HashMap<>();
    HashMap<String,Object> image_hash = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_cards);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);




        new RetrieveFeedTask().execute();




    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {


        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            //responseView.setText("");



        }

        protected String doInBackground(Void... urls) {
            Intent intent = getIntent();

            String slug = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

            try {
                URL url = new URL("https://api.smash.gg/tournament/"+slug+"?expand[]=event");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    String response = stringBuilder.toString();
                    Bitmap bannerBit;
                    JSONObject raw_json = (JSONObject) new JSONTokener(response).nextValue();
                    JSONObject entities_json = raw_json.getJSONObject("entities");

                    //This is the entities JSON
                    //Contains tournament{}, phase[], groups[], event[], videogame[]

                    JSONObject tourney_json = entities_json.getJSONObject("tournament");

                    //This is the tournament JSON

                    JSONArray vg_json = entities_json.getJSONArray("videogame");

                    //This is the videogame[] JSONObject

                    JSONObject g1_json = vg_json.getJSONObject(0);


                    //THIS IS WHERE WE ACTUALLY PARSE THE JSON FOR THE TOURNEY
                    //ALL FURTHER RETRIEVAL HAPPENS PAST HERE
/*
                    int i = 0;
                    game_count = 1;
                    while (vg_json.getJSONObject(i) != null){
                        tourney_list.put(Integer.toString(i), vg_json.getJSONObject(i).getString("abbrev"));
                        i++;
                        game_count++;
                    }
*/
                    String tourney_name = tourney_json.getString("name");

                    int i = 0;
                    game_count = 0;

                    while (!vg_json.isNull(i)){
                        String gameNameString = Integer.toString(vg_json.getJSONObject(i).getInt("id"));
                        tourney_list.put(Integer.toString(i), gameNameString);
                        i++;
                        game_count++;
                    }


                   // String gameNameString1 = vg_json.getJSONObject(0).getString("abbrev");
                    tourney_list.put("name", tourney_name);


                    return tourney_name;


                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(final Exception e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });


                return null;
            }


        }

        protected void onPostExecute(String result) {
            if(result == null) {
                responseView.setText("THERE WAS AN ERROR");
            }
            progressBar.setVisibility(View.GONE);

            initializeList();
            initializeAdapter();
        }





    }
    private void initializeList() {
        games = new ArrayList<>();
        Game melee = new Game("Super Smash Bros. Melee", R.drawable.melee_banner_white, Color.parseColor("#F44336"));
        Game wiiu = new Game("Super Smash Bros. Wii U", R.drawable.wii_u_banner_white, Color.parseColor("#03A9F4"));
        Game smash = new Game("Super Smash Bros. 64", R.drawable.smash_64_banner_white, Color.parseColor("#FFEB3B"));
        Game projectm = new Game("Super Smash Bros. PM", R.drawable.pm_banner_white, Color.parseColor("#673AB7"));

//TODO make the switch, make it pass on the tourney_list file and retrieve the game id instead of the game name. might have to make that fucker a different array list i dont care get it done
        //TODO also move these variables over

        int game_id;
        for (int i = 0; i< game_count; i++){
            game_id = Integer.parseInt(tourney_list.get(Integer.toString(i)));
            switch (game_id){
                case 1: games.add(melee);
                    break;
                case 2: games.add(projectm);
                    break;
                case 3: games.add(wiiu);
                    break;
                case 4: games.add(smash);
                    break;
            }
        }

/*
        if (tourney_list.get("0").equals("melee")) {
            games.add(melee);
        } else if (tourney_list.get("0").equals("wiiu")) {
            games.add(wiiu);
        } else if (tourney_list.get("0").equals("pm")) {
            games.add(projectm);
        } else {
            games.add(smash);
        }


        if (game_count >= 2) {

            if (tourney_list.get("1").equals("melee")) {
                games.add(melee);
            } else if (tourney_list.get("1").equals("wiiu")) {
                games.add(wiiu);
            } else if (tourney_list.get("1").equals("pm")) {
                games.add(projectm);
            } else {
                games.add(smash);
            }



            if (game_count == 3) {
                    if (tourney_list.get("gameName3").equals("melee")) {
                        games.add(melee);
                    } else if (tourney_list.get("gameName3").equals("wiiu")) {
                        games.add(wiiu);
                    } else if (tourney_list.get("gameName3").equals("64")) {
                        games.add(smash);
                    }
                }

        }
        */
    }






    private void initializeAdapter() {
        MainAdapter adapter = new MainAdapter(this, games);
        rv.setAdapter(new AlphaInAnimationAdapter(adapter));
        //games.add(new Game("Test 1", R.drawable.melee_banner_white, Color.parseColor("#F44336")));
        //games.add(new Game("Super Smash Bros. Wii U", R.drawable.wii_u_banner_white, Color.parseColor("#03A9F4")));

    }
}

