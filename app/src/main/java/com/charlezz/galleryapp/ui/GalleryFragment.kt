package com.charlezz.galleryapp.ui

import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.charlezz.galleryapp.databinding.FragmentGalleryBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GalleryFragment : Fragment() {


    val TAG = GalleryFragment::class.java.simpleName

    companion object {
        fun newInstance() = GalleryFragment()
    }

    lateinit var binding: FragmentGalleryBinding
    val adapter = GalleryAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
    }

    override fun onResume() {
        super.onResume()
        adapter.clearItems()
        getItemsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.addItem(it, true)
                }
    }

    fun getItemsObservable(): Observable<GalleryData> {
        val cursor = context?.contentResolver?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA
                ),
                null,
                null,
                null)

        cursor?.let {
            return Observable.fromIterable(RxCursorIterable.from(cursor))
                    .subscribeOn(Schedulers.io())
                    .doAfterNext {
                        if (it.isLast) {
                            it.close()
                        }
                    }
                    .map { c ->
                        val name = c.getString(c.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                        val path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA))
                        GalleryData(name, path)
                    }

        }
        return Observable.empty<GalleryData>()
    }
}