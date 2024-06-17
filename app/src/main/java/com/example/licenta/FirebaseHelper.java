package com.example.licenta;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.ArrayList;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QuerySnapshot;


import com.example.licenta.model.Users;
import com.example.licenta.model.Messages;
import com.example.licenta.model.ChatRoom;
import com.example.licenta.model.CalendarEvent;
import com.example.licenta.model.Notification;
import com.example.licenta.model.Attendance;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.Arrays;
import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {
    private static final String UsersCollection = "users";
    private  static final String StudentsCollection = "students";
    private static final String ProfessorsCollection = "professors";
    private static final String ChatRoomsCollection = "chat_rooms";
    private static final String ChatMessagesCollection = "chat_messages";
    private static final String CalendarCollection = "calendar";
    private static final String AttendancesCollection = "attendances";
    private static final String NotificationsCollection = "notifications";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection(UsersCollection);
    private CollectionReference studentsRef = db.collection(StudentsCollection);
    private CollectionReference profsRef = db.collection(ProfessorsCollection);
    private CollectionReference chatRoomsRef = db.collection(ChatRoomsCollection);
    private CollectionReference chatMessagesRef = db.collection(ChatMessagesCollection);
    private  CollectionReference calendarRef = db.collection(CalendarCollection);
    private  CollectionReference attendancesRef = db.collection(AttendancesCollection);
    private CollectionReference notificationsRef = db.collection(NotificationsCollection);

    public void insertUser(String email, String status, String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("status", status);
        user.put("password", password);
        db.collection(UsersCollection).document(email).set(user);

    }

    public void insertStudentData(String lastName, String firstName, String email, String idNumber) {
        Map<String, Object> student = new HashMap<>();
        student.put("last_name", lastName);
        student.put("first_name", firstName);
        student.put("email", email);
        student.put("id_number", idNumber);
        db.collection(StudentsCollection).document(email).set(student);
    }
    public void insertProfData(String lastName, String firstName, String email, String identificationNumber) {
        Map<String, Object> prof = new HashMap<>();
        prof.put("last_name", lastName);
        prof.put("first_name", firstName);
        prof.put("email", email);
        prof.put("identification_number", identificationNumber);
        db.collection(ProfessorsCollection).document(email).set(prof);
    }

    public void insertChatRoom(String user1Email, String user2Email) {
        Map<String, Object> chatRoom = new HashMap<>();
        chatRoom.put("user1_email", user1Email);
        chatRoom.put("user2_email", user2Email);
        chatRoom.put("created_at", FieldValue.serverTimestamp());
        db.collection(ChatRoomsCollection).add(chatRoom);
    }

    public void insertChatMessage(String chatId, String senderEmail, String messageContent)
{
    Map<String, Object> chatMessage = new HashMap<>();
    chatMessage.put("chat_id", chatId);
    chatMessage.put("sender_email", senderEmail);
    chatMessage.put("message_content", messageContent);
    chatMessage.put("sent_at", FieldValue.serverTimestamp());
    db.collection(ChatMessagesCollection).add(chatMessage);
}
public void addEvent(String name, String date, String startTime, String endTime, String createdBy, String room)
{
    Map<String, Object> event = new HashMap<>();
    event.put("name", name);
    event.put("date", date);
    event.put("start_time", startTime);
    event.put("end_time", endTime);
    event.put("created_by", createdBy);
    event.put("room", room);
    db.collection(CalendarCollection).add(event);
}
public void insertStudentAttendance(String lastName, String firstName, String idNumber, String classType,String date)
{
    Map<String, Object> attendance = new HashMap<>();
    attendance.put("last_name", lastName);
    attendance.put("first_name", firstName);
    attendance.put("id_number", idNumber);
    attendance.put("class_type", classType);
    attendance.put("date", date);
    db.collection(AttendancesCollection).add(attendance);
}
public void insertNotification(String content, String createdBy, String notificationType)
{
    Map<String, Object> notification = new HashMap<>();
    notification.put("content", content);
    notification.put("created_by", createdBy);
    notification.put("created_at", FieldValue.serverTimestamp());
    notification.put("type", notificationType);
    db.collection(NotificationsCollection).add(notification);
}
    private void updateFieldInCollection(String collectionName, String fieldName, String fieldValue, String updateFieldName, String updateFieldValue) {
        CollectionReference collectionRef = db.collection(collectionName);
        Query query = collectionRef.whereEqualTo(fieldName, fieldValue);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                for (DocumentSnapshot document : documents) {
                    String documentId = document.getId();
                    collectionRef.document(documentId).update(updateFieldName, updateFieldValue);
                }
            }
        });
    }

    public void updateLastName(String lastname, String email) {
        updateFieldInCollection(StudentsCollection, "email", email, "last_name", lastname);
        updateFieldInCollection(ProfessorsCollection, "email", email, "last_name", lastname);
        updateFieldInCollection(AttendancesCollection, "email", email, "last_name", lastname);
    }

    public void updateFirstName(String firstname, String email) {
        updateFieldInCollection(StudentsCollection, "email", email, "first_name", firstname);
        updateFieldInCollection(ProfessorsCollection, "email", email, "first_name", firstname);
        updateFieldInCollection(AttendancesCollection, "email", email, "first_name", firstname);
    }

    public void updateEmail(String newEmail, String oldEmail) {
        updateFieldInCollection(StudentsCollection, "email", oldEmail, "email", newEmail);
        updateFieldInCollection(ProfessorsCollection, "email", oldEmail, "email", newEmail);
        updateFieldInCollection(ChatRoomsCollection, "user1_email", oldEmail, "user1_email", newEmail);
        updateFieldInCollection(ChatRoomsCollection, "user2_email", oldEmail, "user2_email", newEmail);
        updateFieldInCollection(ChatMessagesCollection, "sender_email", oldEmail, "sender_email", newEmail);
        updateFieldInCollection(UsersCollection, "email", oldEmail, "email", newEmail);
        updateFieldInCollection(CalendarCollection, "created_by", oldEmail, "created_by", newEmail);
        updateFieldInCollection(NotificationsCollection, "created_by", oldEmail, "created_by", newEmail);
    }


    public Task<Boolean> idNumberExists(String idNumber) {
        Query query = studentsRef.whereEqualTo("id_number", idNumber).limit(1);

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                return !querySnapshot.isEmpty();
            } else {
                Exception e = task.getException();
                if (e != null) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }


    public Task<Boolean> chatRoomExists(String user1Email, String user2Email) {
        Query query1 = chatRoomsRef.whereEqualTo("user1_email", user1Email)
                .whereEqualTo("user2_email", user2Email).limit(1);
        Query query2 = chatRoomsRef.whereEqualTo("user1_email", user2Email)
                .whereEqualTo("user2_email", user1Email).limit(1);

        Task<QuerySnapshot> query1Task = query1.get();
        Task<QuerySnapshot> query2Task = query2.get();

        return Tasks.whenAllSuccess(query1Task, query2Task).continueWith(task -> {
            if (task.isSuccessful()) {
                List<Object> results = task.getResult();
                for (Object result : results) {
                    QuerySnapshot querySnapshot = (QuerySnapshot) result;
                    if (!querySnapshot.isEmpty()) {
                        return true;
                    }
                }
            } else {
                Exception e = task.getException();
                if (e != null) {
                    e.printStackTrace();
                }
            }
            return false;
        });
    }


    public Task<Void> deleteAccount(String email) {
        List<Task<Void>> tasks = new ArrayList<>();

        Task<Void> deleteUserTask = usersRef.document(email).delete();
        tasks.add(deleteUserTask);

        Task<Void> deleteStudentTask = studentsRef.document(email).delete();
        tasks.add(deleteStudentTask);

        Task<Void> deleteProfessorTask = profsRef.document(email).delete();
        tasks.add(deleteProfessorTask);

        Task<Void> deleteAttendanceTask = attendancesRef.document(email).delete();
        tasks.add(deleteAttendanceTask);

        Task<Void> deleteCalendarTask = calendarRef.document(email).delete();
        tasks.add(deleteCalendarTask);

        return Tasks.whenAll(tasks);
    }
    public Task<String[]> retrieveDataWithEmail(String email) {
        Task<String[]> studentDataTask = studentsRef.document(email).get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String lastName = document.getString("last_name");
                            String firstName = document.getString("first_name");
                            String status = "student";
                            return new String[]{lastName, firstName, status};
                        }
                    }
                    return null;
                });

        Task<String[]> professorDataTask = profsRef.document(email).get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String lastName = document.getString("last_name");
                            String firstName = document.getString("first_name");
                            String status = "profesor";
                            return new String[]{lastName, firstName, status};
                        }
                    }
                    return null;
                });

        return Tasks.whenAllSuccess(studentDataTask, professorDataTask)
                .continueWith(task -> {
                    List<Object> results = task.getResult();
                    String[] mergedResult = new String[3];

                    if (results != null && !results.isEmpty()) {
                        if (results.get(0) != null) {
                            String[] studentResult = (String[]) results.get(0);
                            mergedResult[0] = studentResult[0];
                            mergedResult[1] = studentResult[1];
                            mergedResult[2] = studentResult[2];
                        } else if (results.get(1) != null) {
                            String[] professorResult = (String[]) results.get(1);
                            mergedResult[0] = professorResult[0];
                            mergedResult[1] = professorResult[1];
                            mergedResult[2] = professorResult[2];
                        } else {
                            mergedResult = null;
                        }
                    } else {
                        mergedResult = null;
                    }

                    return mergedResult;
                });
    }


    public Task<String> extractPasswordUsingEmail(String email) {
        DocumentReference userRef = db.collection("users").document(email);

        return userRef.get().continueWith(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    return documentSnapshot.getString("password");
                }
            }
           return null;
        });
    }


    public Task<Users> extractUsername(String pattern) {
        Users users = new Users();

        Query studentQuery = studentsRef.orderBy("last_name");
        Query professorQuery = profsRef.orderBy("last_name");

        return studentQuery.get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot studentSnapshot = task.getResult();
                        for (DocumentSnapshot document : studentSnapshot.getDocuments()) {
                            String lastName = document.getString("last_name");
                            String firstName = document.getString("first_name");
                            String email = document.getString("email");
                            users.addUsername(lastName + " " + firstName);
                            users.addEmail(email);
                        }
                    }
                    return professorQuery.get();
                })
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot professorSnapshot = task.getResult();
                        for (DocumentSnapshot document : professorSnapshot.getDocuments()) {
                            String lastName = document.getString("last_name");
                            String firstName = document.getString("first_name");
                            String email = document.getString("email");
                            users.addUsername(lastName + " " + firstName);
                            users.addEmail(email);
                        }
                    }
                    return users;
                });
    }


    public Task<String> retrieveChatRoomId(String user1Email, String user2Email) {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();

        Query query1 = chatRoomsRef.whereEqualTo("user1_email", user1Email).whereEqualTo("user2_email", user2Email);
        Query query2 = chatRoomsRef.whereEqualTo("user1_email", user2Email).whereEqualTo("user2_email", user1Email);

        Task<QuerySnapshot> query1Task = query1.get();
        Task<QuerySnapshot> query2Task = query2.get();

        Tasks.whenAllComplete(query1Task, query2Task).addOnCompleteListener(task -> {
            if (query1Task.isSuccessful() && !query1Task.getResult().isEmpty()) {
                taskCompletionSource.setResult(query1Task.getResult().getDocuments().get(0).getId());
            } else if (query2Task.isSuccessful() && !query2Task.getResult().isEmpty()) {
                taskCompletionSource.setResult(query2Task.getResult().getDocuments().get(0).getId());
            } else {
                taskCompletionSource.setResult(null);
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<Messages> extractMessages(String chatRoomId) {
        Messages messages = new Messages();
        Query query = chatMessagesRef.whereEqualTo("chat_id", chatRoomId).orderBy("sent_at", Query.Direction.ASCENDING);

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String messageContent = document.getString("message_content");
                    String senderEmail = document.getString("sender_email");
                    Timestamp sentAt = document.getTimestamp("sent_at");
                    messages.addMessage(messageContent, senderEmail, sentAt);
                }
            }
            return messages;
        });
    }

    public Task<ChatRoom> extractChatRoom(String email) {
        ChatRoom chatRoom = new ChatRoom();
        Query query1 = chatRoomsRef.whereEqualTo("user1_email", email);
        Query query2 = chatRoomsRef.whereEqualTo("user2_email", email);

        List<Query> queries = Arrays.asList(query1, query2);
        TaskCompletionSource<ChatRoom> taskCompletionSource = new TaskCompletionSource<>();

        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (Query query : queries) {
            tasks.add(query.get());
        }

        Tasks.whenAllSuccess(tasks).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (Task<QuerySnapshot> t : tasks) {
                    QuerySnapshot querySnapshot = t.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String user1Email = document.getString("user1_email");
                            String user2Email = document.getString("user2_email");
                            String id = document.getId();
                            chatRoom.addChatRoom(user1Email, user2Email, id);
                        }
                    }
                }
                taskCompletionSource.setResult(chatRoom);
            } else {
                taskCompletionSource.setException(task.getException());
            }
        });

        return taskCompletionSource.getTask();
    }


    public Task<String[]> getRecentMessage(String chatRoomId) {
        Query query = chatMessagesRef.whereEqualTo("chat_id", chatRoomId)
                .orderBy("sent_at", Query.Direction.DESCENDING)
                .limit(1);
        return query.get().continueWith(task -> {
            String[] recentMessage = new String[2];
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    recentMessage[0] = document.getString("message_content");
                    Timestamp sentAt = document.getTimestamp("sent_at");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    recentMessage[1] = dateFormat.format(sentAt.toDate());
                }
            }
            return recentMessage;
        });
    }

    public Task<Boolean> timeIsAvailable(String date, String room, String startTime, String endTime) {
        Log.d("AddCalendarEvent", "Checking time availability for date: " + date + ", room: " + room + ", startTime: " + startTime + ", endTime: " + endTime);

        return calendarRef.whereEqualTo("date", date)
                .whereEqualTo("room", room)
                .whereLessThan("start_time", endTime)
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("AddCalendarEvent", "Error checking time availability: ", task.getException());
                        throw task.getException();
                    }

                    QuerySnapshot result = task.getResult();
                    if (result != null && !result.isEmpty()) {
                        for (QueryDocumentSnapshot document : result) {
                            String existingEndTime = document.getString("end_time");
                            if (existingEndTime != null && existingEndTime.compareTo(startTime) > 0) {
                                Log.d("AddCalendarEvent", "Time interval not available: " + startTime + " - " + existingEndTime);
                                return false;
                            }
                        }
                    }
                    Log.d("AddCalendarEvent", "Time interval is available");
                    return true;
                });
    }
    public Task<Boolean> timeIsAvailableForModify(String date, String room, String startTime, String endTime, String id) {
        Query query = calendarRef.whereEqualTo("date", date)
                .whereEqualTo("room", room)
                .whereLessThan("start_time", endTime);
        return query.get().continueWith(task -> {
            if (!task.isSuccessful()) {
                Log.e("AddCalendarEvent", "Error checking time availability: ", task.getException());
                throw task.getException();
            }

            QuerySnapshot result = task.getResult();
            if (result != null && !result.isEmpty()) {
                for (QueryDocumentSnapshot document : result) {
                    if (document.getId().equals(id)) {
                        continue;
                    }

                    String existingEndTime = document.getString("end_time");
                    if (existingEndTime != null && existingEndTime.compareTo(startTime) > 0) {
                        Log.d("AddCalendarEvent", "Time interval not available: " + startTime + " - " + existingEndTime);
                        return false;
                    }
                }
            }

            return true;
        });
    }


    public Task<CalendarEvent> extractCalendarEvents(String day) {
        CollectionReference calendarRef = db.collection(CalendarCollection);
        CalendarEvent calendarEvent = new CalendarEvent();
        Query query = calendarRef.whereEqualTo("date", day);

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    String date = document.getString("date");
                    String start = document.getString("start_time");
                    String end = document.getString("end_time");
                    String room = document.getString("room");
                    String time = start + " - " + end;
                    calendarEvent.addEvent(name, date, time, room);
                }
            }
            return calendarEvent;
        });
    }

    public Task<Void> deleteEvent(String name, String date, String startHour, String endHour, String room) {
        Query query = calendarRef.whereEqualTo("name", name)
                .whereEqualTo("date", date)
                .whereEqualTo("start_time", startHour)
                .whereEqualTo("end_time", endHour)
                .whereEqualTo("room", room);

        return query.get().continueWithTask(task -> {
            if (task.isSuccessful()) {
                List<Task<Void>> deleteTasks = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    deleteTasks.add(document.getReference().delete());
                }
                return Tasks.whenAll(deleteTasks);
            } else {
                throw task.getException();
            }
        });
    }

    public void modifyEvent(String id, String newName, String newDate, String newStartTime, String newEndTime, String newRoom) {
        calendarRef.document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    calendarRef.document(id).update("name", newName);
                    calendarRef.document(id).update("date", newDate);
                    calendarRef.document(id).update("start_time", newStartTime);
                    calendarRef.document(id).update("end_time", newEndTime);
                    calendarRef.document(id).update("room", newRoom);
                }
            }
        });
    }

    public Task<String> extractEventId(String event, String date, String startTime, String endTime, String room, String email) {
        Log.d("EditCalendarEvent", "Extracting event ID for event: " + event + ", date: " + date + ", startTime: " + startTime + ", endTime: " + endTime + ", room: " + room + ", email: " + email);
        Query query = calendarRef.whereEqualTo("created_by", email)
                .whereEqualTo("date", date)
                .whereEqualTo("name", event)
                .whereEqualTo("start_time", startTime)
                .whereEqualTo("end_time", endTime)
                .whereEqualTo("room", room)
                .limit(1);
        return query.get().continueWith(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                return document.getId();
            } else {
                throw new Exception("Document not found");
            }
        });
    }

    public Task<Boolean> userIsStudent(String userEmail) {
        Query query = usersRef.whereEqualTo("email", userEmail).whereEqualTo("status", "student").limit(1);
        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                return !querySnapshot.isEmpty();
            }
            return false;
        });
    }


    public Task<String> retrieveIdNumber(String email) {
        Query query = studentsRef.whereEqualTo("email", email);

        return query.get().continueWith(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                return document.getString("id_number");
            } else {
                throw new Exception("Document not found");
            }
        });
    }

    public Task<String> retrieveLastName(String email) {
        Query query = studentsRef.whereEqualTo("email", email);

        return query.get().continueWith(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                return document.getString("last_name");
            } else {
                throw new Exception("Document not found");
            }
        });
    }

    public Task<String> retrieveFirstName(String email) {
        Query query = studentsRef.whereEqualTo("email", email);

        return query.get().continueWith(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                return document.getString("first_name");
            } else {
                throw new Exception("Document not found");
            }
        });
    }

    public Task<Attendance> retrieveAttendances(String idNumber) {
        Query query = attendancesRef.whereEqualTo("id_number", idNumber);

        return query.get().continueWith(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                Attendance attendance = new Attendance();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String classType = document.getString("class_type");
                    String date = document.getString("date");
                    attendance.addAttendance(classType, date);
                }
                return attendance;
            } else {
                throw new Exception("No attendance records found");
            }
        });
    }
    public Task<Attendance> retrieveStudentAttendances() {
        Query query = attendancesRef.orderBy("id_number", Query.Direction.ASCENDING);

        return query.get().continueWith(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                Attendance attendance = new Attendance();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String firstName = document.getString("first_name");
                    String lastName = document.getString("last_name");
                    String name = lastName + " "+ firstName;
                    String idNumber = document.getString("id_number");
                    String classType = document.getString("class_type");
                    if(classType.equals("curs") || classType.equals("seminar"))
                    {
                        attendance.addStudentAttendance(name, idNumber, classType);
                    }
                }
                return attendance;
            } else {
                throw new Exception("No attendance records found");
            }
        });
    }
    public Task<Attendance> retrieveAllStudentAttendances() {
        Query query = attendancesRef.orderBy("id_number", Query.Direction.ASCENDING);

        return query.get().continueWith(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                Attendance attendance = new Attendance();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String firstName = document.getString("first_name");
                    String lastName = document.getString("last_name");
                    String name = lastName + " "+ firstName;
                    String idNumber = document.getString("id_number");
                    String classType = document.getString("class_type");
                    String date = document.getString("date");
                    attendance.addStudentFullAttendance(name, idNumber, classType, date);

                }
                return attendance;
            } else {
                throw new Exception("No attendance records found");
            }
        });
    }
    public Task<Attendance> retrieveAllAttendances(String idNumber) {
        Query query = attendancesRef
                .whereEqualTo("id_number", idNumber)
                .orderBy("id_number", Query.Direction.ASCENDING);

        return query.get().continueWith(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                Attendance attendance = new Attendance();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String firstName = document.getString("first_name");
                    String lastName = document.getString("last_name");
                    String name = lastName + " "+ firstName;
                    String id = document.getString("id_number");
                    String classType = document.getString("class_type");
                    String date = document.getString("date");
                    attendance.addStudentFullAttendance(name, id, classType, date);

                }
                return attendance;
            } else {
                throw new Exception("No attendance records found");
            }
        });
    }


    public Task<Boolean> userAddedEvent(String userEmail, String name, String date, String time) {
        String[] parts = time.split("-", 2);
        String startTime = parts[0].trim();
        String endTime = parts[1].trim();

        Query query = calendarRef
                .whereEqualTo("created_by", userEmail)
                .whereEqualTo("name", name)
                .whereEqualTo("date", date)
                .whereEqualTo("start_time", startTime)
                .whereEqualTo("end_time", endTime);

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                return !querySnapshot.isEmpty();
            }
            return false;
        });
    }
    public Task<Notification> retrieveNotifications() {
        Notification notification = new Notification();
        Query query = notificationsRef.orderBy("created_at", Query.Direction.DESCENDING);

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        String createdBy = document.getString("created_by");
                        String content = document.getString("content");
                        Timestamp createdAt = document.getTimestamp("created_at");
                        String type = document.getString("type");
                        notification.addNotification(content, createdAt.toDate().toString(), type);


                    }
                }
            } else {
                throw task.getException();
            }
            return notification;
        });
    }

    public Task<Boolean> studentWasPresent(String idNumber, String date, String classType) {
        Query query = attendancesRef.whereEqualTo("id_number", idNumber)
                .whereEqualTo("date", date)
                .whereEqualTo("class_type", classType)
                .limit(1);

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                return !querySnapshot.isEmpty();
            }
            return false;
        });
    }
    public Task<Boolean> emailExists(String email) {
        Query query = usersRef.whereEqualTo("email", email).limit(1);

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                return !querySnapshot.isEmpty();
            }
                return false;
        });
    }
    public static StorageReference getProfilePicture(String email)
    {
        return FirebaseStorage.getInstance().getReference().child("profile_picture")
                .child(email);
    }

}
