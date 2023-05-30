package kalachik.com.mfctasks.admin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kalachik.com.mfctasks.ActivityEnter
import kalachik.com.mfctasks.R
import kalachik.com.mfctasks.admin.ActivitySeeEmp.userHelp.emailEmp
import kalachik.com.mfctasks.admin.ActivitySeeEmp.userHelp.passEmp
import kalachik.com.mfctasks.emploee.MainActivityEmp
import kalachik.com.mfctasks.listTask.AdapterTask
import kalachik.com.mfctasks.listTask.Task

class SeeTaskEmp : AppCompatActivity() { //просмотр записей выбранного сотрудника через администратора
    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskArrayList: ArrayList<Task>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_task_emp)

        auth = Firebase.auth

        val fio = findViewById<TextView>(R.id.textFIO)
        val user = auth.currentUser
        if (user != null) {
            val userUID = user.uid
            dbref = FirebaseDatabase.getInstance().getReference("Users").child(userUID).child("name")
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.getValue()
                    fio.text = name.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        val back = findViewById<ImageButton>(R.id.btnExit)
        back.setOnClickListener{
            auth.signOut()
            auth.signInWithEmailAndPassword("admin@gmail.com", "adminmfc")
            val intent = Intent(this, ActivitySeeEmp::class.java)
            startActivity(intent)
        }

        taskRecyclerView = findViewById(R.id.task_recyclerView)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskRecyclerView.setHasFixedSize(true)
        taskArrayList = arrayListOf<Task>()
        getTaskData()

        val delTask = findViewById<Button>(R.id.btnDelTask) //удаление записей у сотрудника
        delTask.setOnClickListener {
            var user = auth.currentUser
            if (user != null) {
                val userid = user.uid
                dbref = FirebaseDatabase.getInstance().getReference(userid)
                dbref.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (taskSnapshot in snapshot.children) {
                            taskSnapshot.ref.removeValue()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                Toast.makeText(baseContext, "Все удалено!", Toast.LENGTH_SHORT).show()
            }
        }

        val delUser = findViewById<Button>(R.id.btnDelEmp) //удаление сотрудника
        delUser.setOnClickListener {
            Toast.makeText(baseContext, "Для удаления нажмите еще раз", Toast.LENGTH_LONG).show()
            delUser.setOnClickListener {
                val user = auth.currentUser!!
                val userid = user.uid
                if (userid != "kDshbkTWPwMwrfHi2knpveNS7p72" ){
                    user.delete().addOnCompleteListener { // удалить сотрудника из Authentication
                            task ->
                        if (task.isSuccessful){
                            dbref = FirebaseDatabase.getInstance().getReference(userid)//удалить его записи
                            dbref.removeValue()
                            dbref = FirebaseDatabase.getInstance().getReference("Users").child(userid) //удалить его из Users
                            dbref.removeValue()
                            val intent = Intent(this, ActivitySeeEmp::class.java)
                            startActivity(intent)
                        }
                    }
                }
                else{
                    Toast.makeText(baseContext, "Администратор не может удалить себя!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val addTask = findViewById<Button>(R.id.btnAddTask)
        addTask.setOnClickListener {
            val intent = Intent(this, ActivityAddTask::class.java)
            startActivity(intent)
        }

    }

    private fun getTaskData() { //получение данных в recycler

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
    override fun onBackPressed() {
        Toast.makeText(baseContext, "Чтобы вернуться нажмите на стрелочку наверху", Toast.LENGTH_LONG).show()
    }


    override fun onStop() { //  вход по аккаунтом админа при выходе из приложения
        super.onStop()
        auth.signOut()
        auth.signInWithEmailAndPassword("admin@gmail.com", "adminmfc")
    }
}

