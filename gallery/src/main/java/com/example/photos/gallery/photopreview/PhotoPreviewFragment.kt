package com.example.photos.gallery.photopreview

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.photos.gallery.R
import com.example.photos.gallery.databinding.FragmentPhotoPreviewBinding
import com.example.photos.gallery.model.GalleryPhoto
import com.example.photos.gallery.photopreview.PhotoPreviewImageView.OnZoomActiveListener

class PhotoPreviewFragment : Fragment() {

    private val viewModel: PhotoPreviewViewModel by viewModels()

    private var _binding: FragmentPhotoPreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupZoomView()
        viewModel.setGalleryPhotoId(photoId)
        viewModel.galleryPhotoLiveData.observe(viewLifecycleOwner) { galleryPhoto ->
            setupGalleryPhoto(galleryPhoto)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupGalleryPhoto(galleryPhoto: GalleryPhoto?) {
        if (galleryPhoto == null) {
            parentFragment?.startPostponedEnterTransition()
            return
        }
        ViewCompat.setTransitionName(binding.imageView, galleryPhoto.id)
        binding.imageView.tag = galleryPhoto.id
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                parentFragment?.startPostponedEnterTransition()
                return false
            }
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                parentFragment?.startPostponedEnterTransition()
                return false
            }
        }
        Glide.with(this)
            .load(galleryPhoto.thumbnailUrlLarge)
            .placeholder(R.drawable.placeholder)
            .thumbnail(Glide
                .with(this)
                .load(galleryPhoto.thumbnailUrlSmall)
                .listener(listener))
            .listener(listener)
            .into(binding.imageView)
    }

    private fun setupZoomView() {
        binding.imageView.setOnZoomListener(object : OnZoomActiveListener {
            override fun onZoomActiveStateChange(isActive: Boolean) {
                val fragment = parentFragment
                if (fragment is OnZoomActiveListener) {
                    fragment.onZoomActiveStateChange(isActive)
                }
            }
        })
    }

    private val photoId: String get() {
        val arg = arguments ?: throw IllegalStateException("Arguments are missing")
        return arg.getString(KEY_PHOTO_ID) ?: throw IllegalStateException("Photo id argument is missing")
    }

    companion object {
        private const val KEY_PHOTO_ID = "photo_id"

        fun newInstance(photoUrl: String): PhotoPreviewFragment {
            return PhotoPreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_PHOTO_ID, photoUrl)
                }
            }
        }
    }
}