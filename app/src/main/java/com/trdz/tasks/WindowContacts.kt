package com.trdz.tasks

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.trdz.tasks.databinding.FragmentWindowContactsBinding
import android.R.id
import android.content.Intent
import android.net.Uri
import android.widget.Toast

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
		executors.getExecutor().showToast(requireContext(),getString(R.string.t_message_deny), Toast.LENGTH_SHORT)
		requireActivity().supportFragmentManager.popBackStack()
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		if (requestCode == REQUEST_CODE) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) getContacts()
			else permissionForbidden()
			}
		else if (requestCode == REQUEST_CALL) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) makeCall()
			}
		}

	private fun getContacts() {
		binding.subTitle.text = getString(R.string.t_subtitle)
		val contentResolver: ContentResolver = requireContext().contentResolver
		val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null,null, ContactsContract.Contacts.DISPLAY_NAME + " ASC")
		cursor?.let {
			for (i in 0 until it.count){
				if(cursor.moveToPosition(i)){
					val columnNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
					val name:String = cursor.getString(columnNameIndex)
					val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
					val number = getNumberFromID(contentResolver,contactId)
					addView(name, number)
				}
			}
			it.close()
		}
	}

	private fun getNumberFromID(cr: ContentResolver, contactId: String) :String {
		val phones = cr.query(
			ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null
		)
		var number = "0"
		phones?.let { cursor ->
			while (cursor.moveToNext()) {
				number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
			}
		}
		return number
	}

	private fun addView(name:String, number:String) {
		binding.containerForContacts.addView(TextView(requireContext(),null,0,R.style.contact).apply {
			text= "$name: $number"
			setOnClickListener {
				executors.getExecutor().showToast(context,context.getString(R.string.t_start_call), Toast.LENGTH_SHORT)
				numberCurrent =  number
				makeCall()
			}
		})
	}

	private var numberCurrent: String = "none"

	private fun makeCall() {
		if(ContextCompat.checkSelfPermission(
				requireContext(),
				Manifest.permission.CALL_PHONE
			) == PackageManager.PERMISSION_GRANTED){
			val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$numberCurrent"))
			startActivity(intent)
		}else{
			requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
		}
	}

	companion object {
		@JvmStatic
		fun newInstance() = WindowContacts()
	}

}
