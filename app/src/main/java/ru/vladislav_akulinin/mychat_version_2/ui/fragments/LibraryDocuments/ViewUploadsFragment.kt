package ru.vladislav_akulinin.mychat_version_2.ui.fragments.LibraryDocuments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.Upload
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_view_uploads.view.*
import ru.vladislav_akulinin.mychat_version_2.constants.Constants
import kotlin.collections.ArrayList
import android.widget.Toast
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import android.os.Environment.MEDIA_MOUNTED
import android.os.Environment
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.DialogInterface
import android.app.ProgressDialog
import android.os.AsyncTask
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.ContextThemeWrapper
import java.io.File
import java.io.FileOutputStream
import java.lang.System.out
import java.net.HttpURLConnection
import java.net.URL


class ViewUploadsFragment : Fragment() {

    lateinit var mStorage: StorageReference
    var mDatabaseReference: DatabaseReference? = null
    var uploadList: List<Upload>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_uploads, container, false)

        uploadList = ArrayList()
        mStorage = FirebaseStorage.getInstance().reference

        view.listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            val (_, url) = (uploadList as ArrayList<Upload>)[i]

            //todo Opening the upload file in browser using the upload url
            //getting the upload
//
//            val upload: Upload = (uploadList as ArrayList<Upload>)[i]

//            //Opening the upload file in browser using the upload url
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse(url)
//            startActivity(intent)

            //TODO в БД html
            Toast.makeText(context, mStorage.child(Constants().STORAGE_PATH_UPLOADS + (uploadList as ArrayList<Upload>)[i].name)
                    .downloadUrl.toString(), Toast.LENGTH_SHORT).show()

            downloadForUrl()

        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants().DATABASE_PATH_UPLOADS)

        mDatabaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val upload = postSnapshot.getValue(Upload::class.java)
                    if (upload != null) {
                        (uploadList as ArrayList<Upload>).add(upload)
                    }
                }

                val uploads = arrayOfNulls<String>((uploadList as ArrayList<Upload>).size)
                for (i in uploads.indices) {
                    uploads[i] = (uploadList as ArrayList<Upload>)[i].name
                }

                val adapter = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, uploads) }
                view.listView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return view
    }

    fun downloadForUrl() {
        var URL: String = "http://www.codeplayon.com/samples/resume.pdf" //http://www.codeplayon.com/samples/resume.pdf

        context?.let { DownloadTask(it, URL) }
    }

//
//    private fun downloadfile() {
//        val storage: FirebaseStorage = FirebaseStorage.getInstance()
//        val storageReference: StorageReference =
//                storage
//                        .getReferenceFromUrl("gs://mychat-version-1.appspot.com/uploads/")
//                        .child("1574892307174.pdf")
//
//        var file: File? = null
//        try {
//            file = File.createTempFile("1574892307174", ".pdf")
//        } catch (e: IOException) {
//
//        }
//        val uploadTask: UploadTask = storageReference.putFile(Uri.fromFile(file))
//
//        val tasks = storageReference.activeDownloadTasks
//        for (task in tasks) {
//            task.addOnSuccessListener {
//                Log.e("Tuts+", "download successful!")
//            }
//        }
//    }
}

class CheckForSDCard {
    //Check If SD Card is present or not method
    val isSDCardPresent: Boolean
        get() = Environment.getExternalStorageState().equals(MEDIA_MOUNTED)
}

class DownloadTask(private val context: Context, downloadUrl: String) {

    private var downloadUrl = ""
    private var downloadFileName = ""
    private var progressDialog: ProgressDialog? = null

    init {

        this.downloadUrl = downloadUrl


        downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length)//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName)

        //Start Downloading Task
        DownloadingTask().execute()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class DownloadingTask : AsyncTask<Void, Void, Void>() {

        internal var apkStorage: File? = null
        internal var outputFile: File? = null

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Downloading...")
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }

        override fun onPostExecute(result: Void) {
            try {
                if (outputFile != null) {
                    progressDialog!!.dismiss()
                    val ctw = ContextThemeWrapper(context, R.style.Widget_AppCompat_ButtonBar_AlertDialog)
                    val alertDialogBuilder = AlertDialog.Builder(ctw)
                    alertDialogBuilder.setTitle("Document  ")
                    alertDialogBuilder.setMessage("Document Downloaded Successfully ")
                    alertDialogBuilder.setCancelable(false)
                    alertDialogBuilder.setPositiveButton("ok") { dialog, id -> }

                    alertDialogBuilder.setNegativeButton("Open report") { _, _ ->
                        val pdfFile = File(Environment.getExternalStorageDirectory().toString() + "/CodePlayon/" + downloadFileName)  // -> filename = maven.pdf
                        val path = Uri.fromFile(pdfFile)
                        val pdfIntent = Intent(Intent.ACTION_VIEW)
                        pdfIntent.setDataAndType(path, "application/pdf")
                        pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        try {
                            context.startActivity(pdfIntent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show()
                        }
                    }
                    alertDialogBuilder.show()
                    //                    Toast.makeText(context, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
                } else {

                    Handler().postDelayed({ }, 3000)

                    Log.e(TAG, "Download Failed")

                }
            } catch (e: Exception) {
                e.printStackTrace()

                //Change button text if exception occurs

                Handler().postDelayed(Runnable { }, 3000)
                Log.e(TAG, "Download Failed with Exception - " + e.localizedMessage!!)

            }


            super.onPostExecute(result)
        }

        override fun doInBackground(vararg arg0: Void): Void? {
            try {
                val url = URL(downloadUrl)//Create Download URl
                val c = url.openConnection() as HttpURLConnection//Open Url Connection
                c.requestMethod = "GET"//Set Request Method to "GET" since we are grtting data
                c.connect()//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.responseCode !== HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.responseCode
                            + " " + c.responseMessage)

                }


                //Get File if SD card is present
                if (CheckForSDCard().isSDCardPresent) {

                    apkStorage = File(Environment.getExternalStorageDirectory().toString() + "/" + "CodePlayon")
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show()

                //If File is not present create directory
                if (!apkStorage!!.exists()) {
                    apkStorage!!.mkdir()
                    Log.e(TAG, "Directory Created.")
                }

                outputFile = File(apkStorage, downloadFileName)//Create Output file in Main File

                //Create New File if not present
                if (!outputFile!!.exists()) {
                    outputFile!!.createNewFile()
                    Log.e(TAG, "File Created")
                }

                val fos = FileOutputStream(outputFile)//Get OutputStream for NewFile Location

                val is1 = c.inputStream//Get InputStream for connection

                val buffer = ByteArray(1024)//Set buffer type
                var len1 = 0//init length

                var bytesRead:Int =0
                while(is1.read(buffer).also { bytesRead = it } >=0) {
                    out.write(buffer, 0, bytesRead)
                }


//                while ((len1 in is1.read(buffer)) != -1) {
//                    fos.write(buffer, 0, len1)//Write new file
//                }

                //Close all connection after doing task
                fos.close()
                is1.close()

            } catch (e: Exception) {

                //Read exception if something went wrong
                e.printStackTrace()
                outputFile = null
                Log.e(TAG, "Download Error Exception " + e.message)
            }

            return null
        }
    }

    companion object {
        private val TAG = "Download Task"
    }
}