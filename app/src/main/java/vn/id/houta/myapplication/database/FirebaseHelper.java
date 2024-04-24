package vn.id.houta.myapplication.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.authentication.LoginFragment;
import vn.id.houta.myapplication.model.Lesson;
import vn.id.houta.myapplication.model.Quiz;
import vn.id.houta.myapplication.model.Ranking;
import vn.id.houta.myapplication.model.User;
import vn.id.houta.myapplication.model.UserLesson;

public class FirebaseHelper {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public FirebaseHelper() {
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }


    public interface GetLessonCallback {
        void onGetLessonCallback(Lesson lesson);
    }

    public interface InfoLessonCallback {
        void onListenHeartCountCallback(int heartCount);
    }
    public interface GetTimeLearnTodayCallback {
        void onListenTimeLearnTodayCallback(int heartCount);
    }

    public interface GetQuizCallback {
        void onGetQuizCallback(Quiz quiz);
    }

    public interface GetRankCallback{
        void onGetRankUserCallback(Ranking ranking);
    }
    public interface GetTimeLearnDailyCallback{
        void onGetTimeLearnDaily( List<BarEntry> lbe, List<String> days);
    }

    public void getLessonsForUser(final GetLessonCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onGetLessonCallback(null);
            return;
        }

        CollectionReference lessonsRef = db.collection("lessons");
        CollectionReference userLessonRef = db.collection("users")
                .document(Objects.requireNonNull(user.getEmail()))
                .collection("userlessons");

        Map<String, UserLesson> mapUserLessons = new HashMap<>();
        CompletableFuture<Void> userLessonFuture = new CompletableFuture<>();
        userLessonRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String lessonId = document.getId();
                        UserLesson userLesson = document.toObject(UserLesson.class);
                        System.out.println(userLesson);
                        mapUserLessons.put(lessonId, userLesson);
                    }
                }
            }
            userLessonFuture.complete(null);
        });

        userLessonFuture.thenRun(() -> {
            lessonsRef.orderBy("stt", Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String lessonId = document.getId();
                            Lesson lesson = document.toObject(Lesson.class);
                            lesson.afterObjectCreation(lessonId);
                            UserLesson ul = mapUserLessons.get(lessonId);
                            if (ul != null) {
                                lesson.setUserLesson(ul);
                            }
                            callback.onGetLessonCallback(lesson);
                        }
                    } else {
                        callback.onGetLessonCallback(null);
                    }
                } else {
                    handleTaskError(task);
                    callback.onGetLessonCallback(null);
                }
            });
        });
    }

    public void saveTimeStudyLessonForUser(String lessonID, int timesStudy) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        String email = user.getEmail();
        DocumentReference userLessonsRef = db.collection("users").document(email).collection("userlessons").document(lessonID);
        Map<String, Integer> data = new HashMap<>();
        data.put("timesStudy", timesStudy);
        userLessonsRef.set(data, SetOptions.merge());
    }

    public void setLearnedLessonForUser(String lessonID, boolean isLearned) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        Map<String, Object> data = new HashMap<>();
        data.put("learned", isLearned);

        db.collection("users")
                .document(Objects.requireNonNull(firebaseUser.getEmail()))
                .collection("userlessons")
                .document(lessonID)
                .set(data, SetOptions.merge());
    }

    public void setHeartLessonUser(String lessonID, boolean isLiked) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        Map<String, Object> data = new HashMap<>();
        data.put("liked", isLiked);

        db.collection("users")
                .document(Objects.requireNonNull(firebaseUser.getEmail()))
                .collection("userlessons")
                .document(lessonID)
                .set(data, SetOptions.merge());

        DocumentReference lessonRef = db.collection("lessons").document(lessonID);
        lessonRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Long currentValue = snapshot.getLong("heartCount");
                    int newValue = (currentValue != null ? currentValue.intValue() : 0) + (isLiked ? 1 : -1);
                    Map<String, Object> newData = new HashMap<>();
                    newData.put("heartCount", newValue);
                    lessonRef.set(newData, SetOptions.merge());
                }
            }
        });
    }

    public void getHeartCountLesson(InfoLessonCallback callback, String lessonID){
        db.collection("lessons").document(lessonID)
                .addSnapshotListener((snapshot, e) -> {
                    int data = 0;
                    if (snapshot != null && snapshot.exists()) {
                        Long a = snapshot.getLong("heartCount");
                        data = a==null? 0 : a.intValue();
                    }
                    callback.onListenHeartCountCallback(data);
                });
    }

    public void getTimeLearnToday(GetTimeLearnTodayCallback callback){
        FirebaseUser firebaseUser = auth.getCurrentUser();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        String str_today = formatter.format(date);
        db.collection("users").document(firebaseUser.getEmail())
                .collection("statistics").document(str_today)
                .addSnapshotListener((snapshot, e) -> {
                    int seconds = 0;
                    if (snapshot != null && snapshot.exists()) {
                        Long a = snapshot.getLong("timeStudy");
                        seconds = a==null? 0 : a.intValue();
                    }
                    callback.onListenTimeLearnTodayCallback(seconds);
                });
    }
    public void increaseTimeLearnToday(int timeStudy) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        String str_today = formatter.format(date);
        DocumentReference userLessonsRef = db.collection("users").document(firebaseUser.getEmail())
                .collection("statistics").document(str_today);
        final int[] timeLearned = {0};
        userLessonsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    timeLearned[0] = document.getLong("timeStudy").intValue();
                }
            }
        } );

        Map<String, Integer> data = new HashMap<>();
        data.put("timeStudy", timeStudy + timeLearned[0]);
        userLessonsRef.set(data, SetOptions.merge());
    }

    public void getTimeLearnDaily(GetTimeLearnDailyCallback callback){
        FirebaseUser firebaseUser = auth.getCurrentUser();
        List<String> days = new ArrayList<String>();
        List<BarEntry> l = new ArrayList<>();
        db.collection("users").document(firebaseUser.getEmail())
                .collection("statistics")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        int c = 0;
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                long seconds = document.getLong("timeStudy");
                                days.add(document.getId());
                                l.add(new BarEntry(c, (float) seconds /3600));
                                c+=1;
//        , Float.parseFloat(document.getId(
                            }
                            callback.onGetTimeLearnDaily(l,days);
                        }
                    } else {
                        handleTaskError(task);
                    }
                });
    }
    public void getQuizs(GetQuizCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
//        if (user == null) {
//            callback.onGetLessonCallback(null);
//            return;
//        }

//        CollectionReference quizsRef = db.collection("quizs");
//        CollectionReference userLessonRef = db.collection("users")
//                .document(Objects.requireNonNull(user.getEmail()))
//                .collection("userquizs");

//        Map<String, UserLesson> mapUserLessons = new HashMap<>();
//        CompletableFuture<Void> userLessonFuture = new CompletableFuture<>();
//        userLessonRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                QuerySnapshot querySnapshot = task.getResult();
//                if (querySnapshot != null && !querySnapshot.isEmpty()) {
//                    for (QueryDocumentSnapshot document : querySnapshot) {
//                        String lessonId = document.getId();
//                        UserLesson userLesson = document.toObject(UserLesson.class);
//                        mapUserLessons.put(lessonId, userLesson);
//                    }
//                }
//            }
//            userLessonFuture.complete(null);
//        });
        CollectionReference quizsRef = db.collection("quizs");
        quizsRef.orderBy(FieldPath.documentId(), Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String quizID = document.getId();
                        Quiz quiz = document.toObject(Quiz.class);
                        quiz.setQuizId(quizID);
                        callback.onGetQuizCallback(quiz);
                    }
                } else {
                    callback.onGetQuizCallback(null);
                }
            } else {
                handleTaskError(task);
                callback.onGetQuizCallback(null);
            }
        });
    }

    public void registerUser(final Context context, final String email, final String password,
                             User user, final FragmentManager fragmentManager) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser == null) return;

                        String userid = firebaseUser.getUid();
                        int id_image = user.getGender() == 1 ? R.drawable.img_gender_boy : R.drawable.img_gender_girl;
                        Drawable drawable = context.getDrawable(id_image);
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference imageRef = storageRef.child("avatarUser/"+email);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = imageRef.putBytes(data);
                        uploadTask.addOnSuccessListener(taskSnapshot -> {
                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageURL = uri.toString();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(Uri.parse(imageURL)).build();
                                firebaseUser.updateProfile(profileUpdates);
                                UserProfileChangeRequest displayNameUpdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(user.getName()).build();
                                firebaseUser.updateProfile(displayNameUpdate);
                                FirebaseAuth.getInstance().signOut();
                            });
                        });

                        CollectionReference usersRef = db.collection("users");
                        user.setEmail(email);
                        user.setUserid(userid);

                        usersRef.document(email).set(user)
                                .addOnCompleteListener(innerTask -> {
                                    if (innerTask.isSuccessful()) {
                                        fragmentManager.beginTransaction()
                                                .setCustomAnimations(
                                                        R.anim.slide_in_bottom, R.anim.slide_out_left
                                                )
                                                .replace(R.id.frame_layout, new LoginFragment())
                                                .commit();
                                        Toast.makeText(context, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(context, "Bạn không thể đăng ký bằng email hoặc mật khẩu này", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getNameCurrentUser() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getDisplayName() : "unknown";
    }

    public void getRankings(GetRankCallback callback){
        FirebaseUser firebaseUser = auth.getCurrentUser();
        CollectionReference leaderboardRef = db.collection("leaderboard4");
        leaderboardRef.orderBy("score", Query.Direction.DESCENDING).orderBy("time", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Ranking ranking = document.toObject(Ranking.class);
                        String id = document.getId();
                        ranking.setId(id);
                        boolean isCurrent = id.equals(firebaseUser.getEmail());
                        if(isCurrent) ranking.setName("Bạn");
                        callback.onGetRankUserCallback(ranking);
                    }
                } else {
                    callback.onGetRankUserCallback(null);
                }
            } else {
                handleTaskError(task);
                callback.onGetRankUserCallback(null);
            }
        });
    }

    public void updateRankUser(int score, int totalTime) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String email = firebaseUser.getEmail();
        Ranking new_rank = new Ranking(email,firebaseUser.getDisplayName(), score, totalTime);
        CollectionReference leaderboardRef = db.collection("leaderboard4");
        leaderboardRef.document(email).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Ranking old_rank = documentSnapshot.toObject(Ranking.class);
                        int old_time = (int) old_rank.getTime();
                        int old_score = (int) old_rank.getScore();
                        if(score < old_score || (score == old_score && old_time < totalTime)){
                            return;
                        }
                    }
                    leaderboardRef.document(email).set(new_rank);
                });
    }

    public void loadAvatarFromEmail(Context context, ImageView imageView, String email) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("avatarUser/"+email);
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .load(uri)
                    .circleCrop()
                    .placeholder(R.drawable.img_gender_boy)
//                    .error(R.drawable.error_avatar)
                    .into(imageView);
        });
    }

    private void handleTaskError(Task<?> task) {
        Exception e = task.getException();
        if (e != null) {
            e.printStackTrace();
        }
    }
}
