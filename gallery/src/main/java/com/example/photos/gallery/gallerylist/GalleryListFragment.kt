package com.example.photos.gallery.gallerylist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.Toast
import androidx.core.app.SharedElementCallback
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.example.photos.gallery.GalleryViewModel
import com.example.photos.gallery.R
import com.example.photos.gallery.databinding.FragmentGalleryListBinding
import com.example.photos.gallery.gallerylist.GalleryListAdapter.GallerySpanSizeLookup
import com.example.photos.gallery.gallerypreview.GalleryPreviewFragment
import com.example.photos.gallery.model.GalleryItem
import com.example.photos.gallery.model.GalleryPhoto
import com.example.photos.gallery.model.StringResource
import kotlin.math.roundToInt

class GalleryListFragment : Fragment() {

    private val viewModel: GalleryListViewModel by viewModels()
    private val galleryViewModel: GalleryViewModel by activityViewModels()

    private var _binding: FragmentGalleryListBinding? = null
    private val binding get() = _binding!!

    private var spanCount = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { spanCount = it.getInt(KEY_SPAN_COUNT, 3) }
        setupTransitions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponePhotoEnterTransition()
        setupUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SPAN_COUNT, spanCount)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUi() {
        val adapter = GalleryListAdapter(
            onPhotoLoadedListener = { item, _ ->
                startPostponedPhotoEnterTransition(item.id)
            },
            onItemClickListener = { view, item ->
                navigateToGalleryPreviewFragment(view, item)
        })
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        layoutManager.spanSizeLookup = adapter.createSpanSizeLookup(layoutManager.spanCount)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setOnTouchListener(object : View.OnTouchListener {

            val scaleGestureDetector = ScaleGestureDetector(context,
                GalleryOnScaleGestureListener(adapter,layoutManager)
            )

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                scaleGestureDetector.onTouchEvent(event)
                return false
            }

        })
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter.submitList(uiState.galleryItems)
            (view?.parent as? ViewGroup)?.doOnPreDraw {
                syncRecyclerViewPosition()
            }
            binding.progressBar.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE
            handleErrorMessage(uiState.errorMessages.firstOrNull())
        }
    }

    private fun setupTransitions() {
        sharedElementReturnTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_photo)

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String?>, sharedElements: MutableMap<String?, View?>) {
                val item = galleryViewModel.selectedGalleryPhoto ?: return
                val selectedViewHolder = binding.recyclerView.findViewHolderForItemId(item.positionId) ?: return
                sharedElements[names[0]] = selectedViewHolder.itemView
            }
        })
    }

    private fun syncRecyclerViewPosition() {
        val selectedGalleryPhoto = galleryViewModel.selectedGalleryPhoto ?: return
        val vh = binding.recyclerView.findViewHolderForItemId(selectedGalleryPhoto.positionId)
        if (vh != null) {
            return
        }
        val position = viewModel.getItemPositionInGalleryItems(selectedGalleryPhoto.id)
        if (position < 0 || position >= binding.recyclerView.adapter!!.itemCount) {
            return
        }
        binding.recyclerView.scrollToPosition(position)
        return
    }

    private fun navigateToGalleryPreviewFragment(itemView: View, item: GalleryItem) {
        if (item !is GalleryPhoto) return
        galleryViewModel.selectedGalleryPhoto = item
        parentFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(itemView, item.id)
            .replace(R.id.galleryContainer, GalleryPreviewFragment())
            .addToBackStack("gallery_fragment_list")
            .commit()
    }

    private fun postponePhotoEnterTransition() {
        if (galleryViewModel.selectedGalleryPhoto != null) {
            postponeEnterTransition()
        }
    }

    private fun startPostponedPhotoEnterTransition(photoId: String) {
        if (photoId == galleryViewModel.selectedGalleryPhoto?.id) {
            startPostponedEnterTransition()
        }
    }

    private fun handleErrorMessage(errorMessage: StringResource?) {
        if (errorMessage == null) return
        Toast.makeText(requireContext(), errorMessage.resolve(requireContext()), Toast.LENGTH_SHORT).show()
        viewModel.setErrorMessageShown(errorMessage)
    }

    private inner class GalleryOnScaleGestureListener(private val adapter: GalleryListAdapter,
                                                      private val layoutManager: GridLayoutManager)
        : SimpleOnScaleGestureListener() {
        val minSpanCount = 2
        val maxSpanCount = 5

        var scale = 3.0f

        @SuppressLint("NotifyDataSetChanged")
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale *= detector.scaleFactor
            scale = minSpanCount.toFloat().coerceAtLeast(scale.coerceAtMost(maxSpanCount.toFloat()))

            spanCount = maxSpanCount + minSpanCount - scale.roundToInt()
            layoutManager.spanCount = spanCount
            layoutManager.gallerySpanSizeLookup.spanCount = spanCount
            adapter.notifyDataSetChanged()
            return true
        }

        private val GridLayoutManager.gallerySpanSizeLookup: GallerySpanSizeLookup get() {
            return this.spanSizeLookup as GallerySpanSizeLookup
        }
    }

    companion object {
        const val KEY_SPAN_COUNT = "span_count"
    }
}