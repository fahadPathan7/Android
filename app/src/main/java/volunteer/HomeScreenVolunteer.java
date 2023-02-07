package volunteer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.android.R;
import com.example.android.databinding.ActivityHomeScreenVolunteerBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import navigationBars.DrawerBaseActivity;
import user.HomeScreenUser;

public class HomeScreenVolunteer extends DrawerBaseActivity implements View.OnClickListener {

    SwitchCompat switchCompat;

    CardView helpRequestsCardView;

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
                switchCompat.setChecked(true);
                start_HomeScreenUser_activity();
            }
        });


        helpRequestsCardView = findViewById(R.id.helpRequestsCardViewID);
        helpRequestsCardView.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeScreenUser.makeBackPressedCntZero();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.helpRequestsCardViewID) {
            start_HelpRequests_activity();
        }
    }

    public void start_HomeScreenUser_activity() {
        HomeScreenUser.makeBackPressedCntZero();
        Intent intent = new Intent(getApplicationContext(), HomeScreenUser.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void start_HelpRequests_activity() {
        Intent intent = new Intent(getApplicationContext(), HelpRequests.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}