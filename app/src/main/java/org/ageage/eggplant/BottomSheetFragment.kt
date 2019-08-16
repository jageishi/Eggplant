package org.ageage.eggplant


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

private const val CATEGORY = "category"

class BottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var category: Category

    private var listener: OnNavigationItemClickListener? = null

    interface OnNavigationItemClickListener {
        fun onClick(category: Category)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnNavigationItemClickListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getSerializable(CATEGORY) as Category
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedItem = when (category) {
            Category.OVERALL -> textViewOverall
            Category.GENERAL -> textViewGeneral
            Category.SOCIAL -> textViewSocial
            Category.ECONOMICS -> textViewEconomics
            Category.LIFE -> textViewLife
            Category.KNOWLEDGE -> textViewKnowledge
            Category.IT -> textViewTechnology
            Category.FUN -> textViewFun
            Category.ENTERTAINMENT -> textViewEntertainment
            Category.GAME -> textViewAnimeAndGame
        }

        context?.let {
            selectedItem.setTextColor(ContextCompat.getColor(it, R.color.colorPrimary))
            selectedItem.setBackgroundColor(ContextCompat.getColor(it, R.color.colorPrimaryOverlay))
        }
        initTextViews()
    }

    override fun onClick(view: View?) {
        val clickedCategory =
            when (view?.id) {
                R.id.textViewOverall -> Category.OVERALL
                R.id.textViewGeneral -> Category.GENERAL
                R.id.textViewSocial -> Category.SOCIAL
                R.id.textViewEconomics -> Category.ECONOMICS
                R.id.textViewLife -> Category.LIFE
                R.id.textViewKnowledge -> Category.KNOWLEDGE
                R.id.textViewTechnology -> Category.IT
                R.id.textViewFun -> Category.FUN
                R.id.textViewEntertainment -> Category.ENTERTAINMENT
                R.id.textViewAnimeAndGame -> Category.GAME
                else -> Category.OVERALL
            }
        listener?.onClick(clickedCategory)
        dismiss()
    }

    private fun initTextViews() {
        textViewOverall.setOnClickListener(this)
        textViewGeneral.setOnClickListener(this)
        textViewSocial.setOnClickListener(this)
        textViewEconomics.setOnClickListener(this)
        textViewLife.setOnClickListener(this)
        textViewKnowledge.setOnClickListener(this)
        textViewTechnology.setOnClickListener(this)
        textViewFun.setOnClickListener(this)
        textViewEntertainment.setOnClickListener(this)
        textViewAnimeAndGame.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(category: Category) =
            BottomSheetFragment().also { f ->
                f.arguments = Bundle().also { b ->
                    b.putSerializable(CATEGORY, category)
                }
            }
    }
}
