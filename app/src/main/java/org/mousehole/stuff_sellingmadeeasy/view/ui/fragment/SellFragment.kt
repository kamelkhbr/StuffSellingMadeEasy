package org.mousehole.stuff_sellingmadeeasy.view.ui.fragment


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.mousehole.stuff_sellingmadeeasy.R
import org.mousehole.stuff_sellingmadeeasy.model.data.Stuff
import org.mousehole.stuff_sellingmadeeasy.viewmodel.MarketViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.util.*


class SellFragment : Fragment() {


    //Firebase
    private val firebaseAuth = FirebaseAuth.getInstance()

    // photo storage
    private val CAMERA_REQUEST_CODE = 123
    private var photoStoragePath: String?=null





    private lateinit var sellButton: Button
    private lateinit var itemImageView: ImageView
    private lateinit var itemName: EditText
    private lateinit var itemLocation: EditText
    private lateinit var itemDescription: EditText
    private lateinit var itemPrice: EditText

    private var stuffBitmap: Bitmap? = null

    private val marketView: MarketViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_sell, container, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this){
            parentFragment?.childFragmentManager?.popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sellButton = view.findViewById(R.id.upload_sell_btn)
        itemImageView = view.findViewById(R.id.sell_image)
        itemName = view.findViewById(R.id.sell_name)
        itemLocation = view.findViewById(R.id.sell_location)
        itemDescription = view.findViewById(R.id.sell_description)
        itemPrice = view.findViewById(R.id.sell_price)

        itemImageView.setOnClickListener {

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            context?.let { con ->
                if (cameraIntent.resolveActivity(con.packageManager) != null) {

                    try {

                        val tempFile = createTemporaryFile()
                        tempFile?.let { file ->

                            val imageUri = FileProvider.getUriForFile(
                                con, "org.mousehole.stuff_sellingmadeeasy.fileprovider", file
                            )
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                        }
                    } catch (e: Exception) {
                        Log.d("TAG_X", "CAMERA Intent Error ${e.localizedMessage}")

                    }

                }
            }
        }

        sellButton.setOnClickListener {

            stuffBitmap?.let {

                val bOS = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, bOS)
                val imageBytes = bOS.toByteArray()

                val userId = firebaseAuth.currentUser?.uid?:"default"
                val timestamp = Date().time
                val storageReference = FirebaseStorage.getInstance()
                    .reference.child("$userId/$timestamp.jpeg")

                val uploadTask = storageReference.putBytes(imageBytes)

                uploadTask.addOnCompleteListener {

                    if(it.isSuccessful){

                        storageReference
                            .downloadUrl
                            .addOnCompleteListener { dlTask->
                                if (dlTask.isSuccessful)
                                    upload(dlTask.result)
                            }
                    }
                }

            } ?: {Toast.makeText(context, "Must upload a picture", Toast.LENGTH_SHORT).show()
            }()
        }
    }

    private fun createTemporaryFile(): File? {
        val tempName = "${firebaseAuth.currentUser?.uid}_${Date().time}"
        val tempDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val imageFile = File.createTempFile(tempName, ".jpeg", tempDir)
        photoStoragePath = imageFile.absolutePath

        return imageFile
    }

    private fun upload(result: Uri?) {
        //val userEmail = FirebaseAuth.getInstance().currentUser?.email?: "unknown"

        val name = itemName.text.toString().trim()
        val location = itemLocation.text.toString().trim()
        val description = itemDescription.text.toString().trim()
        val price = itemPrice.text.toString().trim()
        val stuff= Stuff().also {
            it.idKey= UUID.randomUUID().toString()
            it.imageUrl = result.toString()
            it.itemName = name
            it.itemPrice = price
            it.itemLocation = location
            it.itemDescription = description
        }
        marketView.uploadStuff(stuff)

        itemImageView.setImageResource(R.drawable.ic_baseline_cloud_upload_24)
        itemName.text.clear()
        itemLocation.text.clear()
        itemDescription.text.clear()
        itemPrice.text.clear()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){

            stuffBitmap = BitmapFactory.decodeFile(photoStoragePath)
            stuffBitmap?.let {
                itemImageView.setImageBitmap(it)
            }
        }
    }
}