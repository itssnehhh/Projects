package com.example.fastfoodapp.Activity.OfferList

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.fastfoodapp.Model.Offer
import com.example.fastfoodapp.databinding.ActivityOfferAddBinding
import com.example.fastfoodapp.databinding.LayoutImageBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException

class OfferAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOfferAddBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var storageRef: StorageReference

    private var offer: Offer? = null
    private var imageBitmap: Bitmap? = null
    private var imageUri: Uri? = null
    private var existingUri: String? = null

    //From Gallery
    private var galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            imageUri = it
            imageBitmap = getBitmapFromUri(it)
            imageBitmap?.let {
                binding.ivFood.setImageBitmap(it)
            }
        } else {
            Log.d("PICK MEDIA", "No media selected")
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            var inputStream = contentResolver.openInputStream(uri)
            var bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    //For Camera
    private var cameraContract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            binding.ivFood.setImageURI(imageUri)
        } else {
            imageUri = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseRef = Firebase.database.reference
        storageRef = Firebase.storage.reference

        offer = intent.getParcelableExtra("OFFER")

        if (offer!=null){
            //Update
            Picasso.get().load(offer!!.image).into(binding.ivFood)
            binding.etName.setText(offer!!.foodName)
            binding.etRestaurantName.setText(offer!!.restaurantName)
            binding.etDesc.setText(offer!!.description)
            binding.etCategory.setText(offer!!.category)
            binding.etCalorie.setText(offer!!.calaries)
            binding.etPrice.setText(offer!!.price!!.toInt().toString())
            binding.etDiscount.setText(offer!!.discount)
            binding.rating.setText(offer!!.rating.toString())
        }

        binding.ivFood.setOnClickListener {
            imageUri = createImageUri()
            showBottomSheet()
        }

        binding.btnAdd.setOnClickListener {
            var name = binding.etName.text.toString().trim()
            var category = binding.etCategory.text.toString().trim()
            var type = binding.etType.text.toString().trim()
            var discount = binding.etDiscount.text.toString().trim()
            var price = binding.etPrice.text.toString().trim()
            var restaurantName = binding.etRestaurantName.text.toString().trim()
            var rating = binding.rating.text.toString().trim()
            var calaries = binding.etCalorie.text.toString().trim()
            var description = binding.etDesc.text.toString().trim()

            getDownloadUrl(imageUri) { imageUrl ->

                if (imageUri!=null){
                createOffer(name, category, type, discount, price, restaurantName, rating, calaries, description, imageUrl!!)
                }else{
                    Toast.makeText(this, "Please Select image first", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showBottomSheet() {
        var bind = LayoutImageBottomSheetBinding.inflate(layoutInflater)
        var dialog = BottomSheetDialog(this)
        dialog.setContentView(bind.root)
        dialog.show()

        bind.layoutCamera.setOnClickListener {
                cameraContract.launch(imageUri)
                dialog.dismiss()
        }

        bind.layoutGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
            dialog.dismiss()
        }

    }

    private fun createImageUri(): Uri? {
        var fileName = "${System.currentTimeMillis()}.jpg"
        var imageFile = File(filesDir,fileName)

        return FileProvider.getUriForFile(this,"com.example.fastfoodapp",imageFile)
    }

    private fun getDownloadUrl(imageUri: Uri?, onComplete: (String?) -> Unit) {
        if (imageUri != null) {
            var dirName = "Offer Images"
            var fileName = "${System.currentTimeMillis()}.jpg"
            var uploadTask = storageRef.child(dirName).child(fileName).putFile(imageUri)

            uploadTask.continueWithTask {
                if (!it.isSuccessful) {
                    it.exception?.let {
                        onComplete(null)
                    }
                }
                storageRef.child(dirName).child(fileName).downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    onComplete(it.result.toString())
                } else {
                    onComplete(null)
                }
            }
        }else{
            onComplete(existingUri)
        }

    }

    private fun createOffer(name: String, category: String, type: String, discount: String, price: String, restaurantName: String, rating: String, calaries: String, description: String, image: String,
    ) {
        var id = if (offer != null) {
            //update
            offer!!.id
        } else {
            //create
            databaseRef.push().key
        }

        id?.let {
            var offer = Offer(it,name, image, category, type, discount.toInt(),price.toInt(), disPrice = price.toInt() - discount.toInt() * price.toInt() / 100, restaurantName, rating.toFloat(), calaries, description)

            databaseRef.child("offer-node").child(it).setValue(offer).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Offer Added", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }
    }
}