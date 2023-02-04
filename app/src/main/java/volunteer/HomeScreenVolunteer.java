package volunteer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.example.android.R;
import com.example.android.databinding.ActivityHomeScreenVolunteerBinding;

import navigationBars.DrawerBaseActivity;
import user.HomeScreenUser;

public class HomeScreenVolunteer extends DrawerBaseActivity {

    SwitchCompat switchCompat;


    ActivityHomeScreenVolunteerBinding activityHomeScreenVolunteerBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeScreenVolunteerBinding = ActivityHomeScreenVolunteerBinding.inflate(getLayoutInflater());
        setContentView(activityHomeScreenVolunteerBinding.getRoot());
        allocateActivityTitle("Home");


        switchCompat = findViewById(R.id.switchID);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                start_HomeScreenUser_activity();
            }
        });
    }

    public void start_HomeScreenUser_activity() {
        finish();
        Intent intent = new Intent(this, HomeScreenUser.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);

//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
//        Intent intent = new Intent(this, HomeScreenUser.class);
//        startActivity(intent, options.toBundle());
    }

}