package org.ageage.eggplant.common.ui.arrayadapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class EnumSpinnerAdapter(
    context: Context,
    objects: Array<SpinnerOption>
) : ArrayAdapter<EnumSpinnerAdapter.SpinnerOption>(
    context,
    android.R.layout.simple_spinner_item,
    objects
) {

    interface SpinnerOption {
        val textResId: Int
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: super.getView(position, convertView, parent)
        return view.findViewById<TextView>(android.R.id.text1).also { textView ->
            val item = getItem(position)
            if (item != null) {
                textView.setText(item.textResId)
            } else {
                textView.text = ""
            }
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: super.getDropDownView(position, convertView, parent)
        return view.findViewById<TextView>(android.R.id.text1).also { textView ->
            val item = getItem(position)
            if (item != null) {
                textView.setText(item.textResId)
            } else {
                textView.text = ""
            }
        }
    }
}