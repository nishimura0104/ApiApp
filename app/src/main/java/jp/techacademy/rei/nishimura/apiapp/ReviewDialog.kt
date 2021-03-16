package jp.techacademy.rei.nishimura.apiapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar

class ReviewDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        activity?.let {
            val view = it.layoutInflater.inflate(R.layout.review_dialog, null)
            builder.setView(view)
        }

        val show = builder.create()

        show.window?.setGravity(Gravity.TOP)

        return show

    }

    override fun onPause() {
        super.onPause()
        // onPause でダイアログを閉じる場合
        dismiss()
    }

}