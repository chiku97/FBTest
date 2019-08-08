package com.errorfoundteam.fbtest

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.viewholder.*
import kotlinx.android.synthetic.main.viewholder.view.*
import kotlinx.android.synthetic.main.viewholder.view.textView_course

class MainActivity : AppCompatActivity() {
    var photoUri : Uri? = null
    lateinit var image : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = GroupAdapter<ViewHolder>()
        recyclerView_home.adapter = adapter



        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

        button.setOnClickListener {
            uploadImage()

        }

      getData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            photoUri = data.data



//            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,link)
//            val drable = BitmapDrawable(bitmap)
//            imageView.setBackgroundDrawable(drable)
        }


    }

    fun uploadData(){
        val name = editText.text.toString()
        val course = editText2.text.toString()
        val ref = FirebaseDatabase.getInstance().getReference("Marwari")

        val value = SaveData(name, course,image)
        ref.child("$course/$name").setValue(value).addOnSuccessListener {
            getData()
            Log.d("Save","Successfull ")
            Toast.makeText(this, "saved success", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Log.d("Save","Sorrryyyyyy")
            }
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


    fun uploadImage(){
        val refffff = FirebaseStorage.getInstance().getReference("/BCA/${editText.text}")
        refffff.putFile(photoUri!!)
            .addOnSuccessListener {
                refffff.downloadUrl.addOnSuccessListener {
                    image = it.toString()
                    Log.d("Save",image)
                    uploadData()
                }

                Toast.makeText(this,"Photo Added",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d("Save","Bara wala sorryyy")
            }
    }
}


class Order(val data : SaveData): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.viewholder
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        Log.d("Save","yhaan tk pouncha")
        viewHolder.itemView.textView_name.text = data.name

        Picasso.get().load(data.link).into(viewHolder.itemView.imageView2)
    }

}


