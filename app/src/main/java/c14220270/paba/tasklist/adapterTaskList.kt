package c14220270.paba.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class adapterTaskList (private val listTask: ArrayList<taskList>) : RecyclerView
    .Adapter<adapterTaskList.ListViewHolder> ()
{
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

        holder._stateBtn.text = when (task.status) {
            "Start" -> "Start"
            "Ongoing" -> "Ongoing"
            else -> "Completed"
        }

        holder._stateBtn.setOnClickListener{

        }

        holder._editBtn.setOnClickListener{

        }

        holder._deleteBtn.setOnClickListener{

        }

    }
}