package hr.fvlahov.shows_franko_vlahov.shows

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import hr.fvlahov.shows_franko_vlahov.BuildConfig
import hr.fvlahov.shows_franko_vlahov.R
import hr.fvlahov.shows_franko_vlahov.databinding.DialogProfileBinding
import hr.fvlahov.shows_franko_vlahov.databinding.FragmentShowsBinding
import hr.fvlahov.shows_franko_vlahov.login.REMEMBER_ME_LOGIN
import hr.fvlahov.shows_franko_vlahov.model.Review
import hr.fvlahov.shows_franko_vlahov.model.Show
import hr.fvlahov.shows_franko_vlahov.utils.FileUtil
import hr.fvlahov.shows_franko_vlahov.utils.preparePermissionsContract
import java.io.File
import java.io.IOException

private const val REQUEST_IMAGE_CAPTURE = 2

class ShowsFragment : Fragment() {

    companion object {
        fun buildIntent(context: Activity): Intent {
            return Intent(context, ShowsFragment::class.java)
        }
    }

    private val officeReviews = mutableListOf(
        Review(
            "review1",
            3.7f,
            "This show was a complete masterpiece, I really liked it.",
            "imenko.prezimenovic",
            R.drawable.ic_profile_placeholder
        ),
        Review("review2", 3.5f, "", "branimir.akmadzic", R.drawable.ic_profile_placeholder),
        Review(
            "review3",
            3.7f,
            "It was good. I laughed a lot, it matches my sense of humor perfectly. Loved it!",
            "testamenko.testovic",
            R.drawable.ic_profile_placeholder
        ),
    )

    private val shows = listOf(
        Show(
            "office",
            "The Office",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            R.drawable.ic_office,
            officeReviews
        ),
        Show(
            "strangerThings",
            "Stranger Things",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            R.drawable.ic_stranger_things,
            mutableListOf()
        ),
        Show(
            "bloodAintWater",
            "Krv nije Voda",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
            R.drawable.ic_krv_nije_voda,
            mutableListOf()
        )
    )

    private var showsVisibility = false

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!

    private var adapter: ShowsAdapter? = null

    private val cameraPermission = preparePermissionsContract({ takePhoto() })

    private var profileImage: ShapeableImageView? = null

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    profileImage?.setImageURI(uri)
                }
            }
        }
    private var latestTmpUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShowsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initShowsRecyclerView()
        initShowHideEmptyStateButton()

        //preventBackToLoginIfLoggedIn()

        binding.buttonShowProfile.setOnClickListener { onShowProfileClicked() }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhoto() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }

/*        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = FileUtil.createImageFile(requireContext())
            } catch (ex: IOException) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI_: Uri = FileProvider.getUriForFile(requireContext(), "hr.fvlahov.shows_franko_vlahov.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI_)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }*/
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = FileUtil.createImageFile(requireContext())

        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            tmpFile!!
        )
        //Kako da izbjegnem
    }

    private fun preventBackToLoginIfLoggedIn() {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val shouldNavigateToShows = prefs?.getBoolean(REMEMBER_ME_LOGIN, false) ?: false
        if (shouldNavigateToShows) {
            val navController = findNavController()
            val startDestination = navController.graph.startDestination
            val navOptions = NavOptions.Builder()
                .setPopUpTo(startDestination, true)
                .build()
            navController.navigate(startDestination, null, navOptions)
        }
    }

    private fun onShowProfileClicked() {
        val bottomSheetDialog = BottomSheetDialog(this.requireContext())

        val bottomSheetBinding = DialogProfileBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.buttonLogout.setOnClickListener {
            onButtonLogoutClicked(bottomSheetDialog)
        }

        bottomSheetBinding.buttonChangeProfilePhoto.setOnClickListener {
            onButtonChangeProfilePhotoClicked()
        }

        profileImage = bottomSheetBinding.imageProfile
        bottomSheetDialog.show()
    }

    private fun onButtonChangeProfilePhotoClicked() {
        cameraPermission.launch(arrayOf(android.Manifest.permission.CAMERA))
    }

    private fun onButtonLogoutClicked(bottomSheetDialog: BottomSheetDialog) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext()).apply {
            setCancelable(true)
            setTitle(getString(R.string.are_you_sure))
            setMessage(getString(R.string.are_you_sure_logout))
            setPositiveButton(getString(R.string.confirm)) { alertDialog, which ->
                onConfirmLogoutClicked(
                    bottomSheetDialog
                )
            }
            setNegativeButton(getString(android.R.string.cancel)) { dialog, which -> dialog.dismiss() }
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun onConfirmLogoutClicked(bottomSheetDialog: BottomSheetDialog) {
        bottomSheetDialog.dismiss()
        logout()
    }

    private fun initShowHideEmptyStateButton() {
        binding.buttonShowHideEmptyState.setOnClickListener {
            if (showsVisibility) {
                binding.imageEmptyShows.visibility = View.GONE
                binding.labelEmptyShows.visibility = View.GONE
                binding.recyclerShows.visibility = View.VISIBLE
            } else {
                binding.imageEmptyShows.visibility = View.VISIBLE
                binding.labelEmptyShows.visibility = View.VISIBLE
                binding.recyclerShows.visibility = View.GONE
            }
            showsVisibility = !showsVisibility
        }
    }

    private fun initShowsRecyclerView() {
        binding.recyclerShows.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        adapter = ShowsAdapter(shows) { show ->
            onShowClicked(show)
        }
        binding.recyclerShows.adapter = adapter

        if (adapter?.itemCount ?: 0 < 1) {
            binding.recyclerShows.visibility = View.GONE
        } else {
            binding.imageEmptyShows.visibility = View.GONE
            binding.labelEmptyShows.visibility = View.GONE
        }
    }

    private fun onShowClicked(show: Show) {
        val action = ShowsFragmentDirections.actionShowsToShowDetails(show)
        findNavController().navigate(action)
    }

    private fun logout() {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        with(prefs?.edit()) {
            this?.putBoolean(REMEMBER_ME_LOGIN, false)
            this?.apply()
        }
        findNavController().navigate(R.id.action_shows_to_login)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}