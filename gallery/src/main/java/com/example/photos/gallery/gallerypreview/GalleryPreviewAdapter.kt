package com.example.photos.gallery.gallerypreview

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.photos.gallery.model.GalleryPhoto
import com.example.photos.gallery.photopreview.PhotoPreviewFragment
import com.example.photos.gallery.utils.GalleryItemDiffUtilCallback

class GalleryPreviewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val listDiffer = AsyncListDiffer(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder(GalleryItemDiffUtilCallback).build()
    )

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return PhotoPreviewFragment.newInstance(getItem(position).id)
    }

    fun getItem(position: Int): GalleryPhoto {
        return listDiffer.currentList[position] as GalleryPhoto
    }

    fun submitList(list: List<GalleryPhoto>?) {
        listDiffer.submitList(list)
    }
}