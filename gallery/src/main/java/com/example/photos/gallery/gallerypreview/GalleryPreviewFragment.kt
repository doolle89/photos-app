package com.example.photos.gallery.gallerypreview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.photos.gallery.GalleryViewModel
import com.example.photos.gallery.R
import com.example.photos.gallery.photopreview.PhotoPreviewImageView.OnZoomActiveListener
import com.example.photos.gallery.databinding.FragmentGalleryPreviewBinding


class GalleryPreviewFragment : Fragment(), OnZoomActiveListener {

    private val viewModel: GalleryPreviewViewModel by viewModels()
    private val galleryViewModel: GalleryViewModel by activityViewModels()

    private var _binding: FragmentGalleryPreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransitions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        setupViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onZoomActiveStateChange(isActive: Boolean) {
        binding.viewPager.isUserInputEnabled = !isActive
    }

    private fun setupViewPager() {
        val adapter = GalleryPreviewAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                galleryViewModel.selectedGalleryPhoto = adapter.getItem(position)
            }
        })
        viewModel.galleryPhotosLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            val galleryPhoto = galleryViewModel.selectedGalleryPhoto ?: return@observe
            val position = viewModel.getPositionInGalleryPhotos(galleryPhoto.id)
            binding.viewPager.setCurrentItem(position, false)
        }
    }

    private fun setupTransitions() {
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_photo)

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String?>, sharedElements: MutableMap<String?, View?>) {
                val item = galleryViewModel.selectedGalleryPhoto
                val view = binding.viewPager.findViewWithTag<ImageView>(item?.id) ?: return
                sharedElements[names[0]] = view
            }
        })
    }
}