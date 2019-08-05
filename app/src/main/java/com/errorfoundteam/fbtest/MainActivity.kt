package com.errorfoundteam.fbtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.viewholder.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = GroupAdapter<ViewHolder>()
        recyclerView_home.adapter = adapter



        button.setOnClickListener {
            getData()

            val name = editText.text.toString()
            val course = editText2.text.toString()
            val ref = FirebaseDatabase.getInstance().getReference("Marwari")

            val value = SaveData(name, course)
            ref.child("$course/$name").setValue(value).addOnSuccessListener {
                Log.d("Save","Successfull ")
                Toast.makeText(this, "saved success", Toast.LENGTH_SHORT).show()
            }
        }

      getData()
    }
    fun getData(){
        val ref = FirebaseDatabase.getInstance().getReference("Marwari/bca")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    val marwari = it.getValue(SaveData::class.java)
                    Log.d("Save",it.toString())
                    if (marwari != null)

                        adapter.add(Order(marwari))
                    Log.d("Save","yhin to hai hi")

                }
                recyclerView_home.adapter = adapter
            }

        })
    }
}


class Order(val data : SaveData): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.viewholder
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        Log.d("Save","yhaan tk pouncha")
        viewHolder.itemView.textView_name.text = data.name
        viewHolder.itemView.textView_course.text = data.course

    }

}


