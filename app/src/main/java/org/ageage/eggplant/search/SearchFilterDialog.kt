package org.ageage.eggplant.search

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.search_filter_dialog.view.*
import org.ageage.eggplant.R
import org.ageage.eggplant.common.ui.arrayadapter.EnumSpinnerAdapter

private const val DEFAULT_SEARCH_FILTER_OPTION = "default_search_filter_option"

class SearchFilterDialog : DialogFragment() {

    private lateinit var sortType: org.ageage.eggplant.repository.enums.SortType
    private lateinit var searchTarget: org.ageage.eggplant.repository.enums.SearchTarget
    private lateinit var minimumBookmarkCount: org.ageage.eggplant.repository.enums.MinimumBookmarkCount
    private var isSafeSearchEnabled = true

    interface NoticeDialogListener {
        fun onDialogPositiveClick(
            searchFilterOption: org.ageage.eggplant.repository.model.SearchFilterOption
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { b ->
            (b.getSerializable(DEFAULT_SEARCH_FILTER_OPTION) as org.ageage.eggplant.repository.model.SearchFilterOption).let {
                sortType = it.sortType
                searchTarget = it.target
                minimumBookmarkCount = it.minimumBookmarkCount
                isSafeSearchEnabled = it.isSafeSearchEnabled
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setView(inflateAndInitView(it))
                .setTitle(R.string.search_filter_dialog_title)
                .setPositiveButton(R.string.search_filter_dialog_button_apply) { dialog, id ->
                    if (targetFragment is NoticeDialogListener) {
                        (targetFragment as NoticeDialogListener).onDialogPositiveClick(
                            org.ageage.eggplant.repository.model.SearchFilterOption(
                                sortType,
                                searchTarget,
                                minimumBookmarkCount,
                                isSafeSearchEnabled
                            )
                        )
                    }
                }
                .setNegativeButton(R.string.search_filter_dialog_button_cancel) { dialog, id ->
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    @SuppressLint("InflateParams")
    private fun inflateAndInitView(activity: Activity): View {
        return activity.layoutInflater.inflate(R.layout.search_filter_dialog, null, false)
            .also { view ->
                EnumSpinnerAdapter(
                    activity,
                    org.ageage.eggplant.repository.enums.SortType.valuesForDisplayed()
                ).let { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    view.spinnerSort.adapter = adapter
                    view.spinnerSort.setSelection(adapter.getPosition(sortType))
                    view.spinnerSort.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Do nothing.
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                parent?.let {
                                    sortType =
                                        it.getItemAtPosition(position) as org.ageage.eggplant.repository.enums.SortType
                                }
                            }
                        }
                }

                EnumSpinnerAdapter(
                    activity,
                    org.ageage.eggplant.repository.enums.SearchTarget.valuesForDisplayed()
                ).let { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    view.spinnerTarget.adapter = adapter
                    view.spinnerTarget.setSelection(adapter.getPosition(searchTarget))
                    view.spinnerTarget.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Do nothing.
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                searchTarget =
                                    parent?.getItemAtPosition(position) as org.ageage.eggplant.repository.enums.SearchTarget
                            }
                        }
                }

                EnumSpinnerAdapter(
                    activity,
                    org.ageage.eggplant.repository.enums.MinimumBookmarkCount.valuesForDisplayed()
                ).let { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    view.spinnerBookmarkCount.adapter = adapter
                    view.spinnerBookmarkCount.setSelection(adapter.getPosition(minimumBookmarkCount))
                    view.spinnerBookmarkCount.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Do nothing.
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                minimumBookmarkCount =
                                    parent?.getItemAtPosition(position) as org.ageage.eggplant.repository.enums.MinimumBookmarkCount
                            }
                        }
                }

                view.switchSafeSearch.isChecked = isSafeSearchEnabled
                view.switchSafeSearch.setOnCheckedChangeListener { _, enabled ->
                    isSafeSearchEnabled = enabled
                }
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            target: Fragment,
            requestCode: Int,
            defaultSearchFilterOption: org.ageage.eggplant.repository.model.SearchFilterOption
        ) =
            SearchFilterDialog().also { f ->
                f.setTargetFragment(target, requestCode)
                f.arguments = Bundle().also { b ->
                    b.putSerializable(DEFAULT_SEARCH_FILTER_OPTION, defaultSearchFilterOption)
                }
            }
    }
}