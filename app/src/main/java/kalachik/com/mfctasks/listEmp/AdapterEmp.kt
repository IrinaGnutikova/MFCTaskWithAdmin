package kalachik.com.mfctasks.listEmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kalachik.com.mfctasks.R

class AdapterEmp (private val empList: ArrayList<Emploee> ): RecyclerView.Adapter<AdapterEmp.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_list_emploee, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return empList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = empList[position]

        holder.tvfio.text = currentitem.name
        holder.tvemail.text = currentitem.email
        holder.tvpas.text = currentitem.password
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvfio : TextView = itemView.findViewById(R.id.fio)
        val tvemail : TextView = itemView.findViewById(R.id.email)
        val tvpas : TextView = itemView.findViewById(R.id.pass)
    }
}

