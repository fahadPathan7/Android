package volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.android.R;
import com.example.android.databinding.ActivityHelpRequestsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import commonClasses.AboutUs;
import navigationBars.DrawerBaseActivity;
import commonClasses.Helpline;

public class HelpRequests extends DrawerBaseActivity implements View.OnClickListener {

    public final String KEY_INFORMATION = "Information";

    BottomNavigationItemView home;
    BottomNavigationItemView helpline;
    BottomNavigationItemView aboutUs;

    CardView[] cardViews = new CardView[6];
    TextView[] textViews = new TextView[6];
    ImageView[] imageViews = new ImageView[6];
    String[] documentSnapShotIDs = new String[6];

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference helpRequests = db.collection("Help Requests");

    ActivityHelpRequestsBinding activityHelpRequestsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHelpRequestsBinding = ActivityHelpRequestsBinding.inflate(getLayoutInflater());
        setContentView(activityHelpRequestsBinding.getRoot());
        allocateActivityTitle("Help Requests");

        home=findViewById(R.id.homeMenuID);
        helpline=findViewById(R.id.helplineMenuID);
        aboutUs=findViewById(R.id.aboutUsMenuID);

        home.setOnClickListener(this);
        helpline.setOnClickListener(this);
        aboutUs.setOnClickListener(this);


        connectWithIDs();
        showRequests();
    }

    public void showRequests() {

        makeViewsInvisible();

        helpRequests.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                int cnt = 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (cnt == 6) break;

                    String information = documentSnapshot.getString("Information");

                    documentSnapShotIDs[cnt] = documentSnapshot.getId();

                    String data = information;

                    addData(data, cnt++);
                }
            }
        });
    }

    private void connectWithIDs() {
        cardViews[0] = findViewById(R.id.cardView0ID);
        cardViews[1] = findViewById(R.id.cardView1ID);
        cardViews[2] = findViewById(R.id.cardView2ID);
        cardViews[3] = findViewById(R.id.cardView3ID);
        cardViews[4] = findViewById(R.id.cardView4ID);
        cardViews[5] = findViewById(R.id.cardView5ID);

        textViews[0] = findViewById(R.id.textView0ID);
        textViews[1] = findViewById(R.id.textView1ID);
        textViews[2] = findViewById(R.id.textView2ID);
        textViews[3] = findViewById(R.id.textView3ID);
        textViews[4] = findViewById(R.id.textView4ID);
        textViews[5] = findViewById(R.id.textView5ID);

        imageViews[0] = findViewById(R.id.imageView0ID);
        imageViews[1] = findViewById(R.id.imageView1ID);
        imageViews[2] = findViewById(R.id.imageView2ID);
        imageViews[3] = findViewById(R.id.imageView3ID);
        imageViews[4] = findViewById(R.id.imageView4ID);
        imageViews[5] = findViewById(R.id.imageView5ID);
    }
    public void makeViewsInvisible() {
        for (int i = 0; i < 6; i++) {
            cardViews[i].setVisibility(View.GONE);
        }
    }

    public void addData(String data, int pos) {
        textViews[pos].setText(data);
        cardViews[pos].setVisibility(View.VISIBLE);
        imageViews[pos].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeOnYourGoals(pos);
            }
        });
    }

    public void writeOnYourGoals(int idx) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        try {
            String uid = user.getUid();
            String collectionName = "Your Goals: " + uid;
            String documentName = documentSnapShotIDs[idx];
            DocumentReference documentReference = db.collection(collectionName).document(documentName);

            DocumentReference documentReference1 = db.collection("Help Requests").document(documentName);

            Map<String, Object> info = new HashMap<>();
            String information = "Help\n\n" + textViews[idx].getText().toString().trim();

            info.put(KEY_INFORMATION, information);

            documentReference.set(info, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //cardViews[idx].setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Thanks for your help.", Toast.LENGTH_SHORT).show();
                    documentReference1.delete();

                    showRequests();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Something wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "YourGoals write fault", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.homeMenuID)
        {
            start_HomeScreenVolunteer_activity();
        }
        else if(v.getId()==R.id.helplineMenuID)
        {
            start_Helpline_activity();
        }
        else if(v.getId()==R.id.aboutUsMenuID)
        {
            start_AboutUs_activity();
        }

    }
    public void start_HomeScreenVolunteer_activity() {
        Intent intent = new Intent(getApplicationContext(), HomeScreenVolunteer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void start_Helpline_activity()
    {
        Intent intent = new Intent(this, Helpline.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
    public void start_AboutUs_activity()
    {
        Intent intent = new Intent(this, AboutUs.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}