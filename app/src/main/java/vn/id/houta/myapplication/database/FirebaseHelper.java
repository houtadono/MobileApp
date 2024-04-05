package vn.id.houta.myapplication.database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseHelper {

    public void getLessionForUser(String userId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference lessonsRef = db.collection("lessons");
        lessonsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            System.out.println(document.getId() + " => " + document.getData());
                        }
                    } else {
                        System.out.println("No documents found in the collection.");
                    }
                } else {
                    Exception e = task.getException();
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
