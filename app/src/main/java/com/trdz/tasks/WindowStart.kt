package com.trdz.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trdz.tasks.databinding.FragmentWindowStartBinding

class WindowStart : Fragment(), View.OnClickListener {

	private var _executors: Leader? = null
	private val executors get() = _executors!!
	private var _binding: FragmentWindowStartBinding? = null
	private val binding get() = _binding!!

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
		_executors = null
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentWindowStartBinding.inflate(inflater, container, false)
		_executors = (requireActivity() as MainActivity)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		buttonBinds()
	}

	private fun buttonBinds() {
		binding.goToContact.setOnClickListener(this)
	}

	companion object {
		fun newInstance() = WindowStart()
	}

	override fun onClick(type: View?) {
		executors.getNavigation().replace(requireActivity().supportFragmentManager, WindowContacts.newInstance())
	}
}