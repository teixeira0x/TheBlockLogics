package com.raredev.theblocklogics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raredev.theblocklogics.databinding.FragmentBlankBinding

class BlankFragment: Fragment() {

  private var _binding: FragmentBlankBinding? = null
  private val binding: FragmentBlankBinding
    get() = checkNotNull(_binding)

  companion object {

    fun newInstance(): BlankFragment {
      return BlankFragment()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentBlankBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
