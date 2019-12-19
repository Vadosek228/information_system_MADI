package ru.vladislav_akulinin.mychat_version_2.ui.fragments.LibraryDocuments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vladislav_akulinin.mychat_version_2.R
import android.widget.ProgressBar
import android.widget.EditText
import android.widget.TextView

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ru.vladislav_akulinin.mychat_version_2.constants.Constants
import android.content.Intent
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.content.pm.PackageManager
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.net.Uri
import androidx.core.content.ContextCompat
import android.os.Build
import kotlinx.android.synthetic.main.fragment_library.view.*
import android.widget.Toast
import android.app.Activity.RESULT_OK
import retrofit2.http.Url
import ru.vladislav_akulinin.mychat_version_2.model.Upload
import ru.vladislav_akulinin.mychat_version_2.ui.fragments.LoadFragment
import java.net.URL


class LibraryFragment : Fragment() {

    //this is the pic pdf code used in file chooser
    val PICK_PDF_CODE = 2342

    //these are the views
    var textViewStatus: TextView? = null
    var editTextFilename: EditText? = null
    var progressBar: ProgressBar? = null

    //the firebase objects for storage and database
    var mStorageReference: StorageReference = FirebaseStorage.getInstance().reference
    var mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference(Constants().DATABASE_PATH_UPLOADS)

    lateinit var view_global: View

    companion object {
        fun newInstance(): LoadFragment = LoadFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)
        view_global = view

        //getting firebase objects
//        mStorageReference = FirebaseStorage.getInstance().reference
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants().DATABASE_PATH_UPLOADS)

        //getting the views
        textViewStatus = view.findViewById(R.id.textViewStatus)
        editTextFilename = view.findViewById(R.id.editTextFileName)
        progressBar = view.findViewById(R.id.progressbar)

        view.buttonUploadFile.setOnClickListener{
            getPDF()
        }

        view.textViewUploads.setOnClickListener{
            fragmentManager!!
                    .beginTransaction()
                    .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                    .replace(R.id.container, ViewUploadsFragment())
                    .addToBackStack(null)
                    .commit()
        }

        return view
    }

    //this function will get the pdf from the storage
    private fun getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context?.let {
                    ContextCompat.checkSelfPermission(it, READ_EXTERNAL_STORAGE)
                } != PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" +
                            activity?.packageName ))
            startActivity(intent)
            return
        }

        //creating an intent for file chooser
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            //if a file is selected
            if (data.data != null) {
                //uploading the file
                uploadFile(data.data!!)
            } else {
                Toast.makeText(context, "No file chosen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //this method is uploading the file
    //the code is same as the previous tutorial
    //so we are not explaining it
    private fun uploadFile(data: Uri) {
        Toast.makeText(context, Uri.parse(data.toString()).toString(), Toast.LENGTH_SHORT).show()
        progressBar?.visibility = View.VISIBLE
        val sRef = mStorageReference.child(Constants().STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf")
        sRef.putFile(data)
                .addOnSuccessListener { taskSnapshot ->
                    view_global.progressbar.visibility = View.GONE
                    view_global.textViewStatus.text = "File Uploaded Successfully"

                    val upload = Upload(editTextFilename?.text.toString(), taskSnapshot.storage.toString())
                    mDatabaseReference.child(mDatabaseReference.push().key!!).setValue(upload)
                }
                .addOnFailureListener { exception -> Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show() }
                .addOnProgressListener { taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    textViewStatus?.text = progress.toInt().toString() + "% Uploading..."
                }
    }
}
