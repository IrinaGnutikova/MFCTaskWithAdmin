package kalachik.com.mfctasks.emploee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kalachik.com.mfctasks.ActivityEnter
import kalachik.com.mfctasks.R
import kalachik.com.mfctasks.listTask.AdapterTask
import kalachik.com.mfctasks.listTask.Task

class MainActivityEmp : AppCompatActivity() { //главное окно сотрудника
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskArrayList: ArrayList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_emp)

        auth = Firebase.auth

        val logout = findViewById<ImageButton>(R.id.btnExit)
        logout.setOnClickListener {

            auth.signOut()
            Toast.makeText(
                baseContext, "Вы вышли",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, ActivityEnter::class.java)
            startActivity(intent)

        }

        val user = auth.currentUser
        if (user != null) {
            val userUID = user.uid
            dbref = FirebaseDatabase.getInstance().getReference("Users").child(userUID).child("name")
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.getValue()
                    if (name != "Имя не установлено"){
                        val userInfoTextView = findViewById<TextView>(R.id.textFIO)
                        userInfoTextView.text = "Добро пожаловать, $name\n"
                    }
                    else{
                        Toast.makeText(
                            baseContext, "Имя пользователя не установленно",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
        })
        }

        taskRecyclerView = findViewById(R.id.task_recyclerView)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskRecyclerView.setHasFixedSize(true)
        taskArrayList = arrayListOf<Task>()

        getTaskData()

    }

    override fun onBackPressed() {
        Toast.makeText(baseContext, "Чтобы выйти из аккаунта нажмите на кнопку внизу", Toast.LENGTH_LONG).show()

    }

    private fun getTaskData() {
        val user = auth.currentUser
        if (user != null) {
            taskArrayList.clear()
            val uid = user.uid
            dbref = FirebaseDatabase.getInstance().getReference(uid)
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    taskArrayList.clear()
                    if (snapshot.exists()) {

                        for (taskSnapshot in snapshot.children) {

                            val task = taskSnapshot.getValue(Task::class.java)
                            taskArrayList.add(task!!)

                        }

                        taskRecyclerView.adapter = AdapterTask(taskArrayList)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(baseContext, error.message, Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}
