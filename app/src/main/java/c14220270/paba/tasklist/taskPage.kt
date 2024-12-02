package c14220270.paba.tasklist

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.Calendar

class taskPage : AppCompatActivity() {

    val db = Firebase.firestore
    private var editPage = false
    private var originalTaskName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _btnBack = findViewById<Button>(R.id.backHomeBtn)
        _btnBack.setOnClickListener{
            val intent = Intent(this@taskPage,MainActivity::class.java)
            startActivity(intent)
        }

        val _name = findViewById<EditText>(R.id.taskViewName)
        val _description = findViewById<EditText>(R.id.taskViewDesc)
        val _dateText = findViewById<TextView>(R.id.taskViewDate)
        val _datePickerBtn = findViewById<Button>(R.id.datePicker)

        val _addTaskButton = findViewById<Button>(R.id.AddEditTaskBtn)
        _addTaskButton.setOnClickListener{
            Log.d("ButtonClick", "Add Task button clicked")
            if (editPage){
                UpdateData(db, originalTaskName.toString(), _name.text.toString(), _description.text.toString(), _dateText.text.toString())
            }
            TambahData(db, _name.text.toString(), _description.text.toString(), _dateText.text.toString())
        }

        val intent = intent.getParcelableExtra("task", taskList::class.java)
        if ( intent != null ) {
            editPage = true
            _addTaskButton.text = "Edit Task"
            originalTaskName = intent.name

            _name.setText(intent.name)
            _description.setText(intent.description)
            _dateText.setText(intent.date)
        }

        _datePickerBtn.setOnClickListener{
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our text view.
                    _dateText.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }
    }

    private fun TambahData(db: FirebaseFirestore, Name: String, Description: String, Date: String){
        val dataBaru = taskList(Name, Description, Date, "Start")

        db.collection("tasks")
            .document(dataBaru.name)
            .set(dataBaru)
            .addOnSuccessListener {
                Log.d("Firebase", "Data Berhasil Disimpan")
                val intent = Intent(this@taskPage,MainActivity::class.java)
                startActivity(intent)
                if (editPage){
                    Toast.makeText(this@taskPage, "Task Successfully Updated", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@taskPage, "Task Successfully Added", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
                Toast.makeText(this@taskPage, "Task Failed to be Added", Toast.LENGTH_LONG).show()
            }
    }

    private fun UpdateData(db: FirebaseFirestore, originalTaskName: String, Name: String, Description: String, Date: String){
        db.collection("tasks")
            .document(originalTaskName)
            .delete()
            .addOnSuccessListener {
                TambahData(db, Name, Description, Date)
            }
            .addOnFailureListener{
                Toast.makeText(this@taskPage, "Failed to Update Task", Toast.LENGTH_LONG).show()
            }
    }
}