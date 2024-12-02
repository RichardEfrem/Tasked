package c14220270.paba.tasklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore
    var DataTaskList= ArrayList<taskList>()
    private lateinit var taskAdapter: adapterTaskList
    private lateinit var _rvTaskList : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        _rvTaskList = findViewById<RecyclerView>(R.id.cardTaskList)
        _rvTaskList.layoutManager = LinearLayoutManager(this)
        taskAdapter = adapterTaskList(DataTaskList)
        _rvTaskList.adapter = taskAdapter

        readData(db)

        val _addTaskBtn = findViewById<Button>(R.id.addTaskBtn)
        _addTaskBtn.setOnClickListener{
            val intent = Intent(this@MainActivity,taskPage::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        readData(db)
    }

    private fun readData(db: FirebaseFirestore) {
        db.collection("tasks").get()
            .addOnSuccessListener {
                result ->
                DataTaskList.clear()
                for (document in result){
                    val readData = taskList(
                        document.data.get("name").toString(),
                        document.data.get("description").toString(),
                        document.data.get("date").toString(),
                        document.data.get("status").toString()
                    )
                    DataTaskList.add(readData)
                }
                taskAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}