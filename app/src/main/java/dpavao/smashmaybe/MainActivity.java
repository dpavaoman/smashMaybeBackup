package dpavao.smashmaybe;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.transition.Transition;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Button;
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
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.HashMap;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends Activity {
    public static final String EXTRA_MESSAGE = "dpavao.smashMaybe.MESSAGE";
    EditText tournament_slug_text;
    /*
    TextView responseView;
    ProgressBar progressBar;
    ImageView banner;
    ImageView bannerg1;
    ImageView bannerg2;
    TextView gameName1;
    //TextView testView;

    private String TAG = MainActivity.class.getSimpleName();
    HashMap<String,String> tourney_list = new HashMap<>();
    HashMap<String,Object> image_hash = new HashMap<>();
    */
    private Transition.TransitionListener mEnterTransitionListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            ImageView logo = (ImageView) findViewById(R.id.splashLogo);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
            logo.startAnimation(fadeIn);


        }
    }



    public void retrieval(View view) {
        Intent intent = new Intent(this, retrieval.class);
        tournament_slug_text = (EditText) findViewById(R.id.tournament_slug_text);
        String message = tournament_slug_text.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
}

        /*
        responseView= (TextView) findViewById(R.id.tourney_name);
        tournament_slug_text = (EditText) findViewById(R.id.tournament_slug_text);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        gameName1 = (TextView) findViewById(R.id.gameName1);
        Button queryButton = (Button) findViewById(R.id.queryButton);
        Button continueButton = (Button) findViewById(R.id.continueButton);
        banner = (ImageView) findViewById(R.id.banner);
        bannerg1 = (ImageView) findViewById(R.id.bannerG1);
        bannerg2 = (ImageView) findViewById(R.id.bannerG2);


        queryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                responseView.setText("");

            }
        });

    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, Bitmap> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");


        }

        protected Bitmap doInBackground(Void... urls) {
            String slug = tournament_slug_text.getText().toString();
            // Do some validation here

            try {
                URL url = new URL("https://api.smash.gg/tournament/"+slug+"?expand[]=event&expand[]=phase&expand[]=groups&expand[]=stations");
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
                    JSONArray g1_image = g1_json.getJSONArray("images");
                    JSONObject g1Image = g1_image.getJSONObject(0);
                    String game1 = g1Image.getString("url");

                    //This gets the URL from the vg[] object

                    InputStream gImage1 = new java.net.URL(game1).openStream();
                    image_hash.put("g1image", BitmapFactory.decodeStream(gImage1));
                    //Builds the BITMAP from the url and puts it in the hash


                    if (vg_json.getJSONObject(1)!=null) {
                        JSONObject g2_json = vg_json.getJSONObject(0);
                        JSONArray g2_image = g2_json.getJSONArray("images");
                        JSONObject g2Image = g2_image.getJSONObject(0);
                        String game2 = g2Image.getString("url");
                        //TODO make this actually build a bitmap and put it in the hash

                    }
                    //Checks if there is another game and makes the new bitmap and puts in the hash




                    //THIS IS WHERE WE ACTUALLY PARSE THE JSON FOR THE TOURNEY
                    //ALL FURTHER RETRIEVAL HAPPENS PAST HERE



                    String tourney_name = tourney_json.getString("name");
                    JSONArray photos = tourney_json.getJSONArray("images");
                    JSONObject banner_json = photos.getJSONObject(1);
                    String gameNameString1 = g1_json.getString("name");


                    String banner_url = banner_json.getString("url");
                    InputStream in = new java.net.URL(banner_url).openStream();
                    bannerBit = BitmapFactory.decodeStream(in);


                    image_hash.put("banner",bannerBit);
                    tourney_list.put("name", tourney_name);
                    tourney_list.put("gameName1", gameNameString1);


                    return bannerBit;


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

        protected void onPostExecute(Bitmap Result) {
            if(Result == null) {
                responseView.setText("THERE WAS AN ERROR");
            }



                responseView.setText(tourney_list.get("name"));
                //reddit_link.setText(tourney_list.get("reddit"));
                banner.setImageBitmap((Bitmap) image_hash.get("banner"));
                bannerg1.setImageBitmap((Bitmap) image_hash.get("g1image"));
                progressBar.setVisibility(View.GONE);




             /*catch (final JSONException e) {
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

                } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        }
    }

*/
