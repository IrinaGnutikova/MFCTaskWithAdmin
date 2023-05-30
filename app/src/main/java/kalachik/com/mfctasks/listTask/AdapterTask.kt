package kalachik.com.mfctasks.listTask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kalachik.com.mfctasks.R

class AdapterTask (private val taskList: ArrayList<Task>): RecyclerView.Adapter<AdapterTask.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_task, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = taskList[position]
        holder.tvtask.text = currentitem.taskName

    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvtask : TextView = itemView.findViewById(R.id.task)

    }
}
