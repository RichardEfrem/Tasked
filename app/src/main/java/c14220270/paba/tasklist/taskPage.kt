package c14220270.paba.tasklist

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

        val _addTaskButton = findViewById<Button>(R.id.AddEditTaskBtn)
        _addTaskButton.setOnClickListener{
            Log.d("ButtonClick", "Add Task button clicked")
            TambahData(db, _name.text.toString(), _description.text.toString(), _dateText.text.toString())
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
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}