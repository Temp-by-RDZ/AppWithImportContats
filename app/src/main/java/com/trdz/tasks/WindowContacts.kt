package com.trdz.tasks

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.trdz.tasks.databinding.FragmentWindowContactsBinding

class WindowContacts : Fragment() {

	private var _executors: Leader? = null
	private val executors get() = _executors!!
	private var _binding: FragmentWindowContactsBinding? = null
	private val binding get() = _binding!!

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
		_executors = null
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentWindowContactsBinding.inflate(inflater, container, false)
		_executors = (requireActivity() as MainActivity)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		checkPermission()
	}

	private fun checkPermission() {
		when {ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) ==
			PackageManager.PERMISSION_GRANTED -> { getContacts() }
			shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> { explain() }
			else -> { permissionGranted() }
		}
	}

	private fun explain(){
		AlertDialog.Builder(requireContext())
			.setTitle(getString(R.string.t_permission_title))
			.setMessage(getString(R.string.t_permission_explain))
			.setPositiveButton(getString(R.string.t_permission_yes)) { _, _ -> permissionGranted() }
			.setNegativeButton(getString(R.string.t_permission_no)) { dialog, _ -> dialog.dismiss(); permissionForbidden(); }
			.create()
			.show()
	}

	private fun permissionGranted() {
		requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),REQUEST_CODE)
	}

	private fun permissionForbidden() {
		requireActivity().supportFragmentManager.popBackStack()
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		if(requestCode==REQUEST_CODE){
			for(i in permissions.indices){
				if(permissions[i]==Manifest.permission.READ_CONTACTS&&grantResults[i]==PackageManager.PERMISSION_GRANTED){
					getContacts()
				}else{
					permissionForbidden()
				}
			}
		}
		else{ super.onRequestPermissionsResult(requestCode, permissions, grantResults) }
	}

	private fun getContacts() {
		binding.subTitle.text = getString(R.string.t_subtitle)
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = WindowContacts()
	}

}
