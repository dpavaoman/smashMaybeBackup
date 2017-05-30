package dpavao.smashmaybe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class activity_test extends Activity {


    ImageView event_banner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        Intent i = getIntent();
        Game gameInfo = (Game) i.getSerializableExtra("gameInfo");
        event_banner = (ImageView) findViewById(R.id.event_banner);
        event_banner.setImageResource(gameInfo.getBanner_id());
        event_banner.setBackgroundColor(gameInfo.getBackgroundColor());



    }


}
