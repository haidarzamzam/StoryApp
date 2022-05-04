package com.haidev.storyapp.ui.screen.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ui.IconGenerator
import com.haidev.storyapp.R
import com.haidev.storyapp.data.model.Status
import com.haidev.storyapp.data.model.StoryModel
import com.haidev.storyapp.databinding.FragmentStoryMapBinding
import com.haidev.storyapp.di.prefs
import com.haidev.storyapp.ui.base.BaseFragment
import com.haidev.storyapp.ui.custom.LoadingScreen
import com.haidev.storyapp.ui.screen.login.LoginActivity
import org.koin.android.ext.android.inject

class StoryMapFragment : BaseFragment<FragmentStoryMapBinding, StoryMapViewModel>(),
    StoryMapNavigator, OnMapReadyCallback {

    private val storyMapViewModel: StoryMapViewModel by inject()
    private var _binding: FragmentStoryMapBinding? = null
    private val binding get() = _binding

    private lateinit var mMap: GoogleMap
    private lateinit var markerManager: MarkerManager
    private lateinit var mIconGenerator: IconGenerator
    private var mImageView: ImageView? = null

    override fun onInitialization() {
        super.onInitialization()
        _binding = getViewDataBinding()
        binding?.lifecycleOwner = this
        storyMapViewModel.navigator = this
        initUI()
    }

    private fun initUI() {
        binding?.ivLogout?.setOnClickListener {
            MaterialDialog.Builder(requireContext())
                .title("Logout")
                .content("Are you sure you want to logout the app?")
                .cancelable(false)
                .positiveText("Yes")
                .onPositive { _, _ ->
                    logout()
                }
                .negativeText("No")
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun initMaps() {
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment?.getMapAsync(this)
    }

    override fun setLayout(): Int = R.layout.fragment_story_map

    override fun getViewModels(): StoryMapViewModel = storyMapViewModel

    override fun onReadyAction() {
        storyMapViewModel.getStoryLocation()
        initMaps()
    }

    override fun onObserveAction() {
        storyMapViewModel.responseStory.observe(this) {
            when (it?.status) {
                Status.LOADING ->
                    LoadingScreen.displayLoadingWithText(
                        requireContext(),
                        "Get data location. . .",
                        false
                    )
                Status.SUCCESS -> {
                    LoadingScreen.hideLoading()
                    it.data?.let { data -> setupMarker(data.listStory) }
                }
                Status.ERROR -> {
                    LoadingScreen.hideLoading()
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> LoadingScreen.hideLoading()
            }
        }
    }

    companion object {
        private const val TAG = "StoryMapFragment"
    }

    private fun setupMarker(listLocation: List<StoryModel.Response.Story>) {
        val markerMap: HashMap<Marker, StoryModel.Response.Story> =
            HashMap()

        val markerCollection =
            markerManager.newCollection()
        mIconGenerator = IconGenerator(requireContext())
        mImageView = ImageView(requireContext())
        val mDimension = resources.getDimension(R.dimen.custom_profile_image).toInt()
        mImageView?.layoutParams = ViewGroup.LayoutParams(mDimension, mDimension)
        val padding = resources.getDimension(R.dimen.custom_profile_padding).toInt()
        mImageView?.setPadding(padding, padding, padding, padding)
        mIconGenerator.setContentView(mImageView)

        for (i in listLocation) {
            val marker = markerCollection.addMarker(
                MarkerOptions()
                    .position(LatLng(i.lat!!, i.lon!!))
                    .title(i.name)
                    .snippet(i.description)
            )

            Glide.with(requireContext())
                .asBitmap()
                .load(i.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        try {
                            mIconGenerator.setBackground(
                                ContextCompat.getDrawable(
                                    activity!!,
                                    R.drawable.bg_rounded
                                )
                            )
                            mImageView?.setImageBitmap(resource)
                            mImageView?.buildDrawingCache()
                            val icon: Bitmap = mIconGenerator.makeIcon()
                            marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            markerMap[marker] = i
        }

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    listLocation.first().lat!!,
                    listLocation.first().lon!!
                ), 15f
            )
        )

        markerCollection.setOnMarkerClickListener { marker: Marker? ->
            marker?.showInfoWindow()
            false
        }

        markerCollection.setOnInfoWindowClickListener {
            val markerModel: StoryModel.Response.Story? = markerMap[it]
            val intent = Intent(context, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.EXTRA_DATA_STORY, markerModel)
            startActivity(intent)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        markerManager = MarkerManager(mMap)

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setMapStyle()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_style
                    )
                )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    override fun logout() {
        prefs.prefUserToken = ""
        activity?.finish()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }
}