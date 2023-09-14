package com.example.mvvmtodolist.bookmark

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mvvmtodolist.databinding.BookmarkFragmentBinding
import com.example.mvvmtodolist.main.MainActivity
import com.example.mvvmtodolist.todo.content.TodoContentActivity

class BookmarkFragment : Fragment() {

    companion object {
        fun newInstance() = BookmarkFragment()
    }

    private var _binding: BookmarkFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel : BookmarkViewModel by viewModels()

    private val listAdapter by lazy {
        BookmarkListAdapter { position, item ->
            removeItem(item, position)
            modifyItemAtTodoTab(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BookmarkFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel()
    }

    private fun initModel() = with(viewModel) {
        // viewModel 상 읽기용 list
        list.observe(viewLifecycleOwner) { // Fragment LV : observe(viewLifecycleOwner)
            listAdapter.submitList(it)
        }
    }

    private fun initView() = with(binding) {
        bookmarkList.adapter = listAdapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun removeItem(
        item:BookmarkModel,
        position: Int? = null
    ) {
        viewModel.removeBookmarkItem(item,position)
    }

    fun addItem(item : BookmarkModel) {
        viewModel.addBookmarkItem(item)
    }

    private fun modifyItemAtTodoTab(
        item: BookmarkModel
    ) {
        (activity as? MainActivity)?.modifyTodoItem(item)
    }


}