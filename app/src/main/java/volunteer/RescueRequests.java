package volunteer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.R;
import com.example.android.databinding.ActivityRescueRequestsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import navigationBars.DrawerBaseActivity;

public class RescueRequests extends DrawerBaseActivity {

    public final String KEY_INFORMATION = "Information";
    public final String KEY_TYPE = "Type";
    public final String KEY_NAME = "Name";
    public final String KEY_CONTACT = "Contact";
    public final String KEY_LOCATION = "Location";

    CardView[] cardViews = new CardView[6];
    TextView[] textViews = new TextView[6];
    ImageView[] imageViews = new ImageView[6];
    String[] documentSnapShotIDs = new String[6];

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference helpRequests = db.collection("Rescue Requests");

    ActivityRescueRequestsBinding activityRescueRequestsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRescueRequestsBinding = ActivityRescueRequestsBinding.inflate(getLayoutInflater());
        setContentView(activityRescueRequestsBinding.getRoot());
        allocateActivityTitle("Rescue Requests");

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

                String data = "";

                int cnt = 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (cnt == 6) break;

                    String name = documentSnapshot.getString(KEY_NAME);
                    String contact = documentSnapshot.getString(KEY_CONTACT);
                    String location = documentSnapshot.getString(KEY_LOCATION);

                    documentSnapShotIDs[cnt] = documentSnapshot.getId();

                    data = "Name: " + name + "\n\nContact: " + contact + "\n\nLocation: " + location + "\n\n";

                    addData(data, cnt++);
                }
            }
        });
    }

    public void connectWithIDs() {
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

            DocumentReference documentReference1 = db.collection("Rescue Requests").document(documentName);

            Map<String, Object> info = new HashMap<>();
            String information = '\n' + textViews[idx].getText().toString().trim();

            info.put(KEY_TYPE, "Rescue");
            info.put(KEY_INFORMATION, information);

            documentReference.set(info, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "Thanks for your help.", Toast.LENGTH_SHORT).show();
                    documentReference1.delete();
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
}