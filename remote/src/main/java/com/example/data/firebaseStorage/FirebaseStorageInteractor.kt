package com.example.data.firebaseStorage

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import javax.inject.Singleton

@Singleton
class FirebaseStorageInteractor {

    private val reference by lazy { Firebase.storage("gs://peach-assessment.appspot.com").reference }

    fun getMedia(storageRef: String): Task<Uri> {
        return reference.child(storageRef).downloadUrl
    }
}