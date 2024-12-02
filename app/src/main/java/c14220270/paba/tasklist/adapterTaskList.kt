package c14220270.paba.tasklist

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class adapterTaskList (private val listTask: ArrayList<taskList>) : RecyclerView
    .Adapter<adapterTaskList.ListViewHolder> ()
{
    val db = Firebase.firestore

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _namaTask = itemView.findViewById<TextView>(R.id.TaskName)
        var _deskripsiTask = itemView.findViewById<TextView>(R.id.TaskDesc)
        var _tanggalTask = itemView.findViewById<TextView>(R.id.TaskDate)
        var _stateBtn = itemView.findViewById<Button>(R.id.stateBtn)
        var _editBtn = itemView.findViewById<Button>(R.id.editBtn)
        var _deleteBtn = itemView.findViewById<Button>(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.task_card, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       var task = listTask[position]

        holder._namaTask.setText(task.name)
        holder._deskripsiTask.setText(task.description)
        holder._tanggalTask.setText(task.date)

        when (task.status){
            "Start" -> {
                holder._stateBtn.text = "Start"
                holder._stateBtn.setBackgroundColor(Color.parseColor("#0096FF"))
            }
            "Ongoing" -> {
                holder._stateBtn.text = "Ongoing"
                holder._stateBtn.setBackgroundColor(Color.parseColor("#FFC000"))
            }
            else -> {
                holder._stateBtn.text = "Completed"
                holder._stateBtn.setBackgroundColor(Color.parseColor("#50C878"))
            }
        }

//        holder._stateBtn.text = when (task.status) {
//            "Start" -> "Start"
//            "Ongoing" -> "Ongoing"
//            else -> "Completed"
//        }

        holder._stateBtn.setOnClickListener{

            if (task.status == "Completed"){
                return@setOnClickListener
            }
            val newStatus = when (task.status) {
                "Start" -> "Ongoing"
                else -> "Completed"
            }
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Confirm Status Change")
                .setMessage("Are you sure you want to change the task status to $newStatus?")
                .setPositiveButton("Yes") { dialog, _->
                    db.collection("tasks")
                        .whereEqualTo("name", task.name)
                        .whereEqualTo("description", task.description)
                        .whereEqualTo("date", task.date)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (!documents.isEmpty) {
                                // Assuming only one document matches the query
                                val documentId = documents.documents[0].id
                                db.collection("tasks")
                                    .document(documentId)
                                    .update("status", newStatus)
                                    .addOnSuccessListener {
                                        task.status = newStatus
                                        notifyItemChanged(position)
                                        Log.d("Firebase", "Status updated to $newStatus")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.d("Firebase", "Error updating document: ${e.message}")
                                    }
                            } else {
                                Log.d("Firebase", "No matching document found")
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.d("Firebase", "Error finding document: ${e.message}")
                        }
                }
                .setNegativeButton("No") { dialog, _->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        holder._editBtn.setOnClickListener{
            val intent = Intent(holder.itemView.context, taskPage::class.java).apply {
                putExtra("task", task)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder._deleteBtn.setOnClickListener{
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this task ?")
                .setPositiveButton("Yes") { dialog, _ ->
                    db.collection("tasks")
                        .whereEqualTo("name", task.name)
                        .whereEqualTo("description", task.description)
                        .whereEqualTo("date", task.date)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (!documents.isEmpty) {
                                val documentId = documents.documents[0].id
                                db.collection("tasks")
                                    .document(documentId)
                                    .delete()
                                    .addOnSuccessListener {
                                        listTask.removeAt(position)
                                        notifyItemRemoved(position)
                                        notifyItemRangeChanged(position, listTask.size)
                                        Log.d("Firebase", "Task deleted successfully")
                                        Toast.makeText(holder.itemView.context, "Task Successfully Deleted", Toast.LENGTH_LONG).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.d("Firebase", "Error deleting document: ${e.message}")
                                        Toast.makeText(holder.itemView.context, "Task Failed to be Deleted", Toast.LENGTH_LONG).show()
                                    }
                            } else {
                                Log.d("Firebase", "No matching document found for deletion")
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.d("Firebase", "Error finding document: ${e.message}")
                        }
                }.setNegativeButton("No") {dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }
}